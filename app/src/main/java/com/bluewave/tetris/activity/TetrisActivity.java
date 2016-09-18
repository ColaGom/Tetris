package com.bluewave.tetris.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bluewave.tetris.R;
import com.bluewave.tetris.base.BaseActivity;
import com.bluewave.tetris.common.Const;
import com.bluewave.tetris.common.UserPref;
import com.bluewave.tetris.tetris.BlockUnit;
import com.bluewave.tetris.tetris.TetrisBlock;
import com.bluewave.tetris.view.ShowNextBlockView;
import com.bluewave.tetris.view.TetrisView;

import java.util.TimerTask;

public class TetrisActivity extends BaseActivity
{
    private Button btnLeft;
    private Button btnRight;
    private Button btnDown;
    private Button btnShiftDown;
    private Button btnRotate;
    private Button btnPause;
    private Button btnBomb;
    private Button btnPreview;

    private TextView tvScore;
    private TextView tvMaxScore;
    private TextView tvLevel;
    private TextView tvSpeed;

    private int score;
    private int maxScore;
    private int level;
    private int speed;

    private int checkedSpeed;

    public TetrisView tetrisView;

    public ShowNextBlockView nextBlockView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);

        tetrisView = (TetrisView) findViewById(R.id.tetrisView);
        btnLeft = (Button) findViewById(R.id.btn_left);
        btnRight = (Button) findViewById(R.id.btn_right);
        btnRotate = (Button) findViewById(R.id.btn_rotate);
        btnDown = (Button) findViewById(R.id.btn_down);
        btnShiftDown = (Button) findViewById(R.id.btn_shift_down);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnBomb = (Button) findViewById(R.id.btn_bomb);
        btnPreview = (Button) findViewById(R.id.btn_preview);

        nextBlockView = (ShowNextBlockView) findViewById(R.id.nextBlockView);
        nextBlockView.invalidate();
        tvScore = (TextView) findViewById(R.id.tv_score);
        tvMaxScore = (TextView) findViewById(R.id.tv_maxScore);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tvSpeed = (TextView) findViewById(R.id.tv_speed);

        maxScore = UserPref.getMaxScore();
        score = 0;
        level = 1;
        speed = 1;

        setScore(score);
        setLevel(level);
        setSpeed(speed);
        setMaxScore(maxScore);

        btnPreview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(tetrisView.switchPreviewMode())
                    btnPreview.setText("눈금자 끄기");
                else
                    btnPreview.setText("눈금자 켜기");

            }
        });

        btnBomb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(nextBlockView.nextBlock.isBomb()) return;
                //다음블록을 폭탄으로 변경한다.
                nextBlockView.nextBlock.changeBoom();
                nextBlockView.invalidate();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!tetrisView.isPause() && tetrisView.canMove)
                {
                    tetrisView.getFallingBlock().move(-1);
                    tetrisView.invalidate();
                }
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!tetrisView.isPause() && tetrisView.canMove)
                {
                    tetrisView.getFallingBlock().move(1);
                    tetrisView.invalidate();
                }
            }
        });
        btnRotate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!tetrisView.isPause() && tetrisView.canMove)
                {
                    TetrisBlock copyOfFallingBlock = tetrisView.getFallingBlock().clone();
                    copyOfFallingBlock.rotate();
                    if (copyOfFallingBlock.canRotate())
                    {
                        TetrisBlock fallinBlock = tetrisView.getFallingBlock();
                        fallinBlock.rotate();
                    }

                    tetrisView.invalidate();
                }
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!tetrisView.isPause() && tetrisView.canMove)
                {
                    tetrisView.getFallingBlock().setY(tetrisView.getFallingBlock().getY() + BlockUnit.UNITSIZE);
                    tetrisView.invalidate();
                }
            }
        });

        btnShiftDown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!tetrisView.isPause() && tetrisView.canMove)
                {
                    tetrisView.shiftDown();
                }
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (tetrisView.isPause())
                {
                    startBGM();
                    btnPause.setText("PAUSE");
                    tetrisView.resume();
                } else
                {
                    pauseBGM();
                    btnPause.setText("RESUME");
                    tetrisView.pause();
                }
            }
        });

        tetrisView.setTetrisActivity(this);
        tetrisView.invalidate();

        showSettingDialog();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        tetrisView.pause();
    }

    public void addScore(int value)
    {
        setScore(score + value);
    }

    public void addMaxScore(int value)
    {
        setMaxScore(maxScore + value);
    }

    public void addLevel(int value)
    {
        setLevel(level + value);
    }

    public void addSpeed(int value)
    {
        setSpeed(speed + value);
    }

    public void setScore(int value)
    {
        score = value;
        setTextView(tvScore, Const.SCORE_PREFIX + score);
    }

    public void setMaxScore(int value)
    {
        maxScore = value;
        setTextView(tvMaxScore, Const.MAX_SCORE_PREFIX + maxScore);
    }

    public void setLevel(int value)
    {
        level = value;
        setTextView(tvLevel, Const.LEVEL_PREFIX + level);
    }

    public void setSpeed(int value)
    {
        speed = value;
        setTextView(tvSpeed, Const.SPEED_PREFIX + speed);
    }

    /**
     * Main Thread가 아닌 Tetris 진행 Thread에서 setSpped, setLevel 함수들의 호출이 이뤄지기 때문에
     * runOnUiThread를 활용하여 UI를 변경한다.
     * @param tv
     * @param text
     */
    private void setTextView(final TextView tv, final String text)
    {
        runOnUiThread(new TimerTask()
        {
            @Override
            public void run()
            {
                tv.setText(text);
            }
        });
    }


    public int getScore()
    {
        return score;
    }

    public int getMaxScore()
    {
        return maxScore;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getLevel()
    {
        return level;
    }

    private void showSettingDialog()
    {
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_setting, null);

        final RadioGroup rg = (RadioGroup) rootView.findViewById(R.id.rg_speed);
        final RadioGroup rgMode = (RadioGroup) rootView.findViewById(R.id.rg_mode);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("속도 설정");
        builder.setView(rootView);
        builder.setCancelable(false);
        builder.setPositiveButton("시작", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                int checked = rg.getCheckedRadioButtonId();

                switch (checked)
                {
                    case R.id.rb_low:
                        checkedSpeed = Const.SPEED_LOW;
                        break;

                    case R.id.rb_middle:
                        checkedSpeed = Const.SPEED_MIDDLE;
                        break;

                    case R.id.rb_high:
                        checkedSpeed = Const.SPEED_HIGH;
                        break;
                }

                int checkedMode = rgMode.getCheckedRadioButtonId();

                switch (checkedMode)
                {
                    case R.id.rb_normal :
                        TetrisBlock.TYPESUM = 7;
                        break;
                    case R.id.rb_hard:
                        TetrisBlock.TYPESUM = 14;
                        break;
                }

                startBGM();
                tetrisView.init(checkedSpeed);
            }
        });

        builder.create().show();
    }

    public void gameOver()
    {
        pauseBGM();
        finish();
        UserPref.putRank(score);
        startRankActivity();
    }
}


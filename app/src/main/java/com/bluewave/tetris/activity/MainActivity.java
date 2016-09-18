package com.bluewave.tetris.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bluewave.tetris.R;
import com.bluewave.tetris.base.BaseActivity;

public class MainActivity extends BaseActivity
{
    Button btnStart;
    Button btnRank;
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        startBGM();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        pauseBGM();
    }

    private void bindView()
    {
        btnStart = (Button)findViewById(R.id.btn_down);
        btnRank = (Button)findViewById(R.id.btn_rank);
        btnExit = (Button)findViewById(R.id.btn_exit);
    }

    public void onClick(View view)
    {
        int id = view.getId();

        switch (id)
        {
            case R.id.btn_down:
                startTetrisActivity();
                break;
            case R.id.btn_rank:
                startRankActivity();
                break;
            case R.id.btn_exit:
                finish();
                break;
        }
    }
}

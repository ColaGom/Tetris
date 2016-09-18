package com.bluewave.tetris.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluewave.tetris.R;
import com.bluewave.tetris.base.BaseActivity;
import com.bluewave.tetris.common.Const;
import com.bluewave.tetris.common.UserPref;
import com.bluewave.tetris.model.RankData;

public class RankActivity extends BaseActivity
{
    private LinearLayout container;
    private TextView tvRank;
    private Button btnExit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        bindView();
        tvRank.setText(makeRankString());

        btnExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private String makeRankString()
    {
        RankData[] arrRank = UserPref.getRank();

        StringBuilder sb = new StringBuilder("순위표\n");
        for(int i = 0 ; i < arrRank.length ; ++i)
        {
            int score = arrRank[i].getScore();
            String date = arrRank[i].getDate();

            if(score != 0)
                sb.append(String.format("%d 위 %d 점 %s\n", i+1, score, date));
            else
                sb.append(String.format("%d 위 %s\n", i+1, Const.TEXT_EMPTY_RANK));
        }
        return sb.toString();
    }

    private void bindView()
    {
        container = (LinearLayout)findViewById(R.id.container);
        tvRank = (TextView)findViewById(R.id.tv_rank);
        btnExit = (Button)findViewById(R.id.btn_exit);
    }
}

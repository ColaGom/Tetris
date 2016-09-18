package com.bluewave.tetris.base;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

import com.bluewave.tetris.R;
import com.bluewave.tetris.activity.RankActivity;
import com.bluewave.tetris.activity.TetrisActivity;

/**
 * Created by Developer on 2016-08-29.
 */
public class BaseActivity extends AppCompatActivity
{
    private static MediaPlayer bgm;

    protected void startTetrisActivity()
    {
        Intent intent = new Intent(this, TetrisActivity.class);
        startActivity(intent);
    }

    protected void startRankActivity()
    {
        Intent intent = new Intent(this, RankActivity.class);
        startActivity(intent);
    }

    /***
     *  BGM 재생
    */
    protected void startBGM()
    {
        if(bgm == null)
        {
            bgm = MediaPlayer.create(this, R.raw.tetris_background_music);
            bgm.setLooping(true);
        }
        if(!bgm.isPlaying())
            bgm.start();
    }

    /**
     * BGM 일시정지
     */
    protected void pauseBGM()
    {
        bgm.pause();
    }
}

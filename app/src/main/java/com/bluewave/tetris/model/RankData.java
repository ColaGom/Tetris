package com.bluewave.tetris.model;

import android.text.TextUtils;
import android.util.Log;

import com.bluewave.tetris.common.Const;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Developer on 2016-09-01.
 * 랭크 저장용 클래스 (DTO)
 */
public class RankData
{
    private int score;
    private String date;

    /**
     * 점수
     * @param score
     */
    public RankData(int score)
    {
        this.score = score;
        date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
    }

    /**
     * 기본 생성자
     */
    public RankData()
    {
        this.score = 0;
        date = "";
    }

    /**
     * 저장된 String 값 class로 변환.
     * deserialize
     * @param savedString
     */
    public RankData(String savedString)
    {

        String[] arrStr = savedString.split("\\|");

        if(arrStr.length < 2)
        {
            score = 0;
            return;
        }
        if(!TextUtils.isEmpty(arrStr[0]))
            score = Integer.parseInt(arrStr[0]);

        if(!TextUtils.isEmpty(arrStr[1]))
            date = arrStr[1];

        Log.d(Const.TAG, "[Rankdata]" + savedString + "//" + score + "//" + date);
        Log.d(Const.TAG, "[Rankdata]" + arrStr.length + "//" + arrStr[0]+ "//" + arrStr[1]);
    }

    public int getScore()
    {
        return score;
    }

    public String getDate()
    {
        return date;
    }

    /**
     * class String으로 변환.
     * serialize
     * @return
     */
    @Override
    public String toString()
    {
        return score + "|" + date;
    }
}

package com.bluewave.tetris.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.bluewave.tetris.model.RankData;

import java.util.StringTokenizer;

/**
 * Created by Developer on 2016-08-29.
 */
public class UserPref
{
    private static final String PREF_NAME = "bluewave.tetris";
    private static  SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public static void init(Context context)
    {
        prefs = context.getSharedPreferences(PREF_NAME, 0);
        editor = prefs.edit();
    }

    private static RankData[] arrRank;
    private static String KEY_RANK = "key.rank";

    /**
     * 랭크 입력
     * @param score 점수
     */
    public static void putRank(int score)
    {
        if(arrRank == null)
        {
            loadRank();
        }

        for(int i = 0 ; i < arrRank.length ; ++i)
        {
            // 기존 점수가 없는경우
            if(arrRank[i].getScore() == 0)
            {
                arrRank[i] = new RankData(score);
                break;
            }
            // 기존 점수가 있고 점수가 10위안에 들어간 경우
            else if(score > arrRank[i].getScore())
            {
                //i ~ 마지막 순위까지의 순위를 한칸씩 뒤로 밀어준다.
                for(int j = arrRank.length - 1 ; j > i; --j)
                {
                    if(arrRank[j-1].getScore() != 0)
                    {
                        arrRank[j] = arrRank[j-1];
                    }
                }

                //이후 랭크 삽입.
                arrRank[i] = new RankData(score);
                break;
            }
        }

        saveRank();
    }

    public static int getMaxScore()
    {
        return getRank()[0].getScore();
    }

    public static RankData[] getRank()
    {
        if(arrRank == null)
        {
            loadRank();
        }

        return arrRank;
    }

    /**
     * preference 에 저장된 점수 배열을 불러온다
     */
    private static void loadRank()
    {
        String savedString = prefs.getString(KEY_RANK, "");
        String [] arrStr = savedString.split(",");

        arrRank = new RankData[10];

        for(int i = 0 ; i < arrRank.length; ++i)
        {
            if(i < arrStr.length && !TextUtils.isEmpty(arrStr[i]))
            {
                arrRank[i] = new RankData(arrStr[i]);
            }
            else
            {
                arrRank[i] = new RankData();
            }
        }
    }

    /**
     * preference 에 저장된 점수 배열을 저장한다
     */
    private static void saveRank()
    {
        StringBuilder sb  = new StringBuilder();
        for(int i = 0 ; i < arrRank.length ; ++i)
        {
            sb.append(arrRank[i].toString()).append(",");
        }

        Log.d(Const.TAG, "[saveRank]" + sb.toString());
        editor.putString(KEY_RANK, sb.toString());
        editor.apply();
    }

}

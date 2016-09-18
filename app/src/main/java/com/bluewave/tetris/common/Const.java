package com.bluewave.tetris.common;

import android.graphics.Color;

/**
 * Created by Developer on 2016-08-29.
 * Constant Class
 */
public class Const
{
    public static final String SCORE_PREFIX = "점수 : ";
    public static final String MAX_SCORE_PREFIX = "최고점 : ";
    public static final String LEVEL_PREFIX = "레벨 : ";
    public static final String SPEED_PREFIX = "속도 : ";

    public static final int COLUMN_SIZE = 10;

    public static final int SPEED_LOW = 100;
    public static final int SPEED_MIDDLE = 300;
    public static final int SPEED_HIGH = 500;
    public static final int DEFAULT_INTERVAL = 100;

    public static final int BLOCK_COLORS [] = {Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN, Color.GRAY, Color.CYAN, Color.MAGENTA};
    public static final String TEXT_EMPTY_RANK = "저장된 순위가 없습니다.";
    public static final int ALPHA_PREVIEW = 100;
    public static final String TAG = "TETRIS";
}

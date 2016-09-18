package com.bluewave.tetris.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.bluewave.tetris.tetris.BlockUnit;
import com.bluewave.tetris.tetris.TetrisBlock;

public class ShowNextBlockView extends View {

    public TetrisBlock nextBlock = null;

    public TetrisBlock createNextBlock(TetrisView tetrisView){
        nextBlock = new TetrisBlock(TetrisView.beginX,TetrisView.beginPoint, tetrisView);
        return  nextBlock;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        RectF rect;
        float size = BlockUnit.UNITSIZE;


        if(nextBlock!=null) {

            paint.setColor(nextBlock.getColor());
            for (BlockUnit u : nextBlock.getUnits()) {

                paint.setStyle(Paint.Style.FILL);
                paint.setColor(nextBlock.getColor());


                float tx = u.getX() - BlockUnit.UNITSIZE;
                float ty = u.getY() + BlockUnit.UNITSIZE * 2;

                rect = new RectF(tx, ty, tx + size, ty + size);
                canvas.drawRoundRect(rect, 8, 8, paint);

                paint.setColor(Color.LTGRAY);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                canvas.drawRoundRect(rect, 8, 8, paint);
            }
        }
    }
    public ShowNextBlockView(Context context) {

        super(context);
    }
    public ShowNextBlockView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }
}


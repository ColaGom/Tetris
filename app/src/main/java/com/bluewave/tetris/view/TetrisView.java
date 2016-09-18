package com.bluewave.tetris.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bluewave.tetris.activity.TetrisActivity;
import com.bluewave.tetris.common.Const;
import com.bluewave.tetris.tetris.BlockUnit;
import com.bluewave.tetris.tetris.TetrisBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TetrisView extends View
{
    private final Object syncObj = new Object();

    boolean isPreviewMode = false;

    boolean isPause;

    boolean flag;

    public static int beginPoint = 10;

    public static int max_x, max_y;

    public static float beginX;

    public int dropSpeed = 300;

    public int currentSpeed = 300;

    public boolean isRun = true;

    public boolean canMove = false;

    public Thread dropThread;

    private int[] map = new int[100];

    private TetrisActivity tetrisActivity;

    private TetrisBlock fallingBlock;

    private Thread thread = new Thread();

    private float x1, y1, x2, y2;

    private ArrayList<TetrisBlock> blocks = new ArrayList<>();

    private float h, w;

    private TetrisTask mTask;
    private PreviewTask mPreviewTask;

    public void clear()
    {

        isRun = false;
        blocks.clear();
        thread = new Thread();
        fallingBlock = null;
    }

    // Tetris View  터치 이벤트 제거
//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN)
//        {
//
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if (event.getAction() == MotionEvent.ACTION_UP)
//        {
//
//            if (canMove == false)
//                return false;
//
//            x2 = event.getX();
//            y2 = event.getY();
//
//            float tx = fallingBlock.getX();
//            float ty = fallingBlock.getY();
//
//            if (x1 - x2 > BlockUnit.UNITSIZE)
//            {
//
//                fallingBlock.move(-1);
//
//
//                tetrisActivity.runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        TetrisView.this.invalidate();
//                    }
//                });
//            } else if (x2 - x1 > BlockUnit.UNITSIZE)
//            {
//
//                fallingBlock.move(1);
//
//
//                tetrisActivity.runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        TetrisView.this.invalidate();
//                    }
//                });
//            } else if (y1 - y2 > BlockUnit.UNITSIZE)
//            {
//
//                fallingBlock.rotate();
//
//                tetrisActivity.runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        TetrisView.this.invalidate();
//                    }
//                });
//            }
//        }
//        return true;
//    }

    public boolean isPause()
    {
        return isPause;
    }

    public void pause()
    {
        if(mTask != null){
            mTask.pause();
            isPause = true;
        }
    }

    public void resume()
    {
        if(mTask != null)
        {
            mTask.resume();
            isPause = false;
        }
    }

    public void shiftDown()
    {
        mTask.shiftDown();
    }

    public void init(int speed)
    {
        BlockUnit.UNITSIZE = (getWidth() - beginPoint) / Const.COLUMN_SIZE;

        dropSpeed = 300;

        currentSpeed = speed;

        Arrays.fill(map, 0);

        flag = true;

        isRun = true;

        mPreviewTask = new PreviewTask();
        mTask = new TetrisTask();
        dropThread = new Thread(mTask);
        dropThread.start();
    }


    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if (BlockUnit.UNITSIZE == -1) return;

        max_x = getWidth();
        max_y = getHeight();
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        RectF rel;
        float size = BlockUnit.UNITSIZE;

        if (!getBlocks().isEmpty())
        {

            for (TetrisBlock block : getBlocks())
            {
                paint.setColor(block.getColor());
                for (BlockUnit u : block.getUnits())
                {
                    float tx = u.getX();
                    float ty = u.getY();
                    rel = new RectF(tx, ty, tx + size, ty + size);
                    canvas.drawRoundRect(rel, 8, 8, paint);
                }
            }
        }
        if (fallingBlock != null)
        {
            paint.setColor(fallingBlock.getColor());
            for (BlockUnit u : fallingBlock.getUnits())
            {
                float tx = u.getX();
                float ty = u.getY();
                rel = new RectF(tx, ty, tx + size, ty + size);
                canvas.drawRoundRect(rel, 8, 8, paint);
            }
        }

        // 미리보기 모드인경우
        if(isPreviewMode)
        {
            synchronized (syncObj)
            {
                // 미리보기 태스크가 초기화 안된경우에만 초기화 진행
                if(mPreviewTask == null) mPreviewTask = new PreviewTask();

                mPreviewTask.run();

                TetrisBlock previewBlock = mPreviewTask.getPreviewBlock();
                if (previewBlock != null)
                {
                    paint.setColor(previewBlock.getColor());
                    paint.setAlpha(Const.ALPHA_PREVIEW);

                    for (BlockUnit u : previewBlock.getUnits())
                    {
                        float tx = u.getX();
                        float ty = u.getY();
                        rel = new RectF(tx, ty, tx + size, ty + size);
                        canvas.drawRoundRect(rel, 8, 8, paint);
                    }
                }
            }
        }

        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        for (int i = beginPoint; i < max_x - BlockUnit.UNITSIZE; i += BlockUnit.UNITSIZE)
        {
            for (int j = beginPoint; j < max_y - BlockUnit.UNITSIZE; j += BlockUnit.UNITSIZE)
            {
                rel = new RectF(i, j, i + BlockUnit.UNITSIZE, j + BlockUnit.UNITSIZE);
                canvas.drawRoundRect(rel, 8, 8, paint);
            }
        }

    }

    public TetrisBlock getFallingBlock()
    {
        return fallingBlock;
    }

    public float getH()
    {
        return h;
    }

    public boolean switchPreviewMode()
    {
        isPreviewMode = !isPreviewMode;
        return isPreviewMode;
    }

    public void setH(float h)
    {
        this.h = h;
    }

    public void setFallingBlock(TetrisBlock fallingBlock)
    {
        this.fallingBlock = fallingBlock;
    }

    public Activity getTetrisActivity()
    {
        return tetrisActivity;
    }

    public void setTetrisActivity(TetrisActivity tetrisActivity)
    {
        this.tetrisActivity = tetrisActivity;
    }

    public TetrisView(Context context)
    {
        super(context);
    }

    public TetrisView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    class PreviewTask implements Runnable
    {
        private  TetrisBlock previewBlock;

        public TetrisBlock getPreviewBlock()
        {
            return previewBlock;
        }

        @Override
        public void run()
        {
            if(!canMove) return;
            // fallingBlock(현재 떨어지는중인) 복제 (cloen)한다
            // 이렇게 하지않을경우 call by reference에 인해 현재 떨어지고있는 블록 자체가 변하기때문에 주의해야한다.
            previewBlock = fallingBlock.clone();

            float ty;
            float dropCount = previewBlock.getY();

            // shift down 로직과 동일한 방식으로 생성된 블록을 최하단까지 이동시킨다
            while (dropCount - previewBlock.getY() <= 2 * BlockUnit.UNITSIZE)
            {
                try
                {
                    ty = previewBlock.getY();
                    ty = ty + BlockUnit.UNITSIZE;
                    dropCount += BlockUnit.UNITSIZE;
                    previewBlock.setY(ty);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    class FallingTask implements Runnable
    {
        private boolean isShift = false;
        private Object mLock;
        private boolean mPaused = false;

        public FallingTask()
        {
            mLock = new Object();
        }

        public void shiftDown()
        {
            isShift = true;

            float ty;
            float dropCount = fallingBlock.getY();

            while (dropCount - fallingBlock.getY() <= 2 * BlockUnit.UNITSIZE)
            {
                try
                {
                    ty = fallingBlock.getY();
                    ty = ty + BlockUnit.UNITSIZE;
                    dropCount += BlockUnit.UNITSIZE;
                    fallingBlock.setY(ty);

                    tetrisActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TetrisView.this.invalidate();
                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            checkBlocks();
        }

        @Override
        public void run()
        {
            h = getHeight();
            w = getWidth();

            fallingBlock = tetrisActivity.nextBlockView.nextBlock;

            tetrisActivity.nextBlockView.createNextBlock(TetrisView.this);

            tetrisActivity.runOnUiThread(new Runnable()
            {
                //更新ui
                @Override
                public void run()
                {
                    tetrisActivity.nextBlockView.invalidate();
                }
            });

            float ty;
            float dropCount = fallingBlock.getY();

            canMove = true;

            while (dropCount - fallingBlock.getY() <= 2 * BlockUnit.UNITSIZE)
            {
                try
                {
                    if(isShift) return;

                    Thread.sleep(currentSpeed);

                    ty = fallingBlock.getY();
                    ty = ty + BlockUnit.UNITSIZE;
                    dropCount += BlockUnit.UNITSIZE;
                    fallingBlock.setY(ty);

                    tetrisActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            TetrisView.this.invalidate();
                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

                synchronized (mLock)
                {
                    while(mPaused)
                    {
                        try
                        {
                            mLock.wait();
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if(isShift) return;
            checkBlocks();


        }

        public void pause()
        {
            synchronized (mLock)
            {
                mPaused = true;
            }
        }

        public void resume()
        {
            synchronized (mLock)
            {
                mPaused = false;
                mLock.notifyAll();
            }
        }

        private void checkBlocks()
        {
            if (!canMove) return;

            boolean pFlag = false;

            if(isPreviewMode)
            {
                pFlag = true;
                isPreviewMode = false;
            }

            int end = (int) ((h - BlockUnit.UNITSIZE - beginPoint) / BlockUnit.UNITSIZE);

            canMove = false;

            if (fallingBlock.isBomb())
            {
                ArrayList<Integer> yList = new ArrayList<>();

                for (BlockUnit b:fallingBlock.getUnits())
                {
                    int y = (int) ((b.getY() - beginPoint) / BlockUnit.UNITSIZE);
                    if(!yList.contains(y))
                    {
                        yList.add(y);
                    }
                }

                // 폭탄
                Collections.sort(yList);

                for(Integer y:yList)
                {
                    Log.d("Test", "bomb y : " + y);
                    completeBlocks(y);
                }
            }
            else
            {
                addBlock(fallingBlock);

                for (TetrisBlock b : getBlocks())
                {
                    if (b.getY() <= BlockUnit.UNITSIZE)
                    {
                        isRun = false;
                        if(pFlag)
                        {
                            isPreviewMode = true;
                        }
                        return;
                    }
                }

//                TetrisBlock temp = fallingBlock;
//
//                for (BlockUnit u : temp.getUnits())
//                {
//                    int index = (int) ((u.getY() - beginPoint) / BlockUnit.UNITSIZE);
//                    if (index < 0) break;
//                    map[index]++;
//                }

                int full = (int) ((w - BlockUnit.UNITSIZE - beginPoint) / BlockUnit.UNITSIZE) + 1;

                // 기존에는 떨어진 배열 map에 떨어진(fallingBlock)의 블록값들만 추가시킨뒤 비교하는 방식을 취함
                // 간혈적으로 map값에 제대로 현재 떨어진 블록의 count가 입력되지않아서 블록이 삭제되지 않는 현상 발생.
                // 수정 후에는 블럭이 떨어질때마다 현재 존재하는 모든블록의 complete를 체크한다.

                for(int i = 100 ; i-->0;)
                    map[i] = 0;

                for(TetrisBlock tb : getBlocks())
                    for(BlockUnit u : tb.getUnits())
                    {
                        int index = (int) ((u.getY() - beginPoint) / BlockUnit.UNITSIZE);
                        if (index < 0) break;
                        map[index]++;
                    }

                for (int i = 0; i <= end; i++)
                {
                    if (map[i] >= full)
                    {
                        completeBlocks(i);
                    }
                }
            }

            if(pFlag)
            {
                isPreviewMode = true;
            }
        }

        private void completeBlocks(int y)
        {
            tetrisActivity.addScore(100);

            if (tetrisActivity.getScore() > 1000)
            {
                tetrisActivity.addSpeed(1);
                tetrisActivity.addLevel(1);
            }
            if (tetrisActivity.getScore() > tetrisActivity.getMaxScore())
            {
                tetrisActivity.setMaxScore(tetrisActivity.getScore());
            }

//            map[y] = 0;
//            for (int j = y; j > 0; j--)
//                map[j] = map[j - 1];
//            map[0] = 0;

            removeBlock(y);

            tetrisActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    TetrisView.this.invalidate();
                }
            });
        }
    }

    public ArrayList<TetrisBlock> getBlocks()
    {
        synchronized (syncObj)
        {
            return blocks;
        }
    }

    private void removeBlock(int y)
    {
        synchronized (syncObj)
        {
            for (TetrisBlock b : blocks)
                b.remove(y);

            for (int j = blocks.size() - 1; j >= 0; j--)
            {
                if (blocks.get(j).getUnits().isEmpty())
                {
                    blocks.remove(j);
                    continue;
                }

                for (BlockUnit u : blocks.get(j).getUnits())
                {
                    if ((int) ((u.getY() - beginPoint) / BlockUnit.UNITSIZE) < y)
                        u.setY(u.getY() + BlockUnit.UNITSIZE);
                }
            }

        }
    }

    private void addBlock(TetrisBlock block)
    {
        synchronized (syncObj)
        {
            blocks.add(block);
        }
    }

    class TetrisTask implements Runnable
    {
        private FallingTask mCurrentFallingTask;
        private Object mLock;
        private boolean mPaused = false;

        public TetrisTask()
        {
            mLock = new Object();
        }

        public void shiftDown()
        {
            mCurrentFallingTask.shiftDown();
        }

        public void pause()
        {
            mCurrentFallingTask.pause();
        }

        public void resume()
        {
            mCurrentFallingTask.resume();
        }

        @Override
        public void run()
        {
            while (isRun)
            {
                try
                {
                    Thread.sleep(Const.DEFAULT_INTERVAL);

                    if(!isRun) break;

                    h = getHeight();
                    w = getWidth();

                    beginX = (int) BlockUnit.UNITSIZE * Const.COLUMN_SIZE / 2 + beginPoint;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (flag)
                {
                    tetrisActivity.nextBlockView.createNextBlock(TetrisView.this);

                    tetrisActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            tetrisActivity.nextBlockView.invalidate();
                        }
                    });
                    flag = false;
                }
                if (thread.getState() == Thread.State.TERMINATED || thread.getState() == Thread.State.NEW)
                {
                    mCurrentFallingTask = new FallingTask();
                    thread = new Thread(mCurrentFallingTask);
                    thread.start();
                }

            }

            if (isRun == false)
            {
                tetrisActivity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        tetrisActivity.gameOver();
                    }
                });
            }

            synchronized (mLock)
            {
                while(mPaused)
                {
                    try
                    {
                        mLock.wait();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
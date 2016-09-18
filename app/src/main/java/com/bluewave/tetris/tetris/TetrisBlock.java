package com.bluewave.tetris.tetris;

import android.graphics.Color;

import com.bluewave.tetris.common.Const;
import com.bluewave.tetris.view.TetrisView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Developer on 2016-08-29.
 */
public class TetrisBlock implements Cloneable
{
    private TetrisView tetrisView;
    public static int TYPESUM = 7;

    public final static int DIRECTIONSUM = 4;

    private int blockType, blockDirection;

    private int color;

    private float x, y;

    // 폭탄 인지 확인을위한 필드값 선언
    private boolean isBomb = false;

    private ArrayList<BlockUnit> units = new ArrayList<>();

//    private ArrayList<TetrisBlock> blocks = new ArrayList<>();

    public void remove(int j)
    {
        for (int i = units.size() - 1; i >= 0; i--)
        {
            if ((int) ((units.get(i).getY() - TetrisView.beginPoint) / BlockUnit.UNITSIZE) == j)
                units.remove(i);
        }
    }

    public boolean canRotate()
    {

        for (TetrisBlock b : getBlocks())
        {

            if (canRotate(b) == false)
            {
                return false;
            }
        }
        return true;
    }

    public boolean canRotate(TetrisBlock other)
    {

        for (BlockUnit i : units)
        {

            for (BlockUnit j : other.getUnits())
            {
                if (i.canRotate(j) == false)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void move(int x)
    {

        if (checkCollision_X() < 0 && x < 0 || checkCollision_X() > 0 && x > 0)
            return;


        if (x > 0)
            setX(getX() + BlockUnit.UNITSIZE);
        else
            setX(getX() - BlockUnit.UNITSIZE);
    }

    public boolean checkCollision_Y()
    {
        for (BlockUnit u : units)
        {
            if (u.checkOutOfBoundary_Y())
                return true;
        }

        Iterator<TetrisBlock> iterator = getBlocks().iterator();

        while (iterator.hasNext())
        {
            TetrisBlock block = iterator.next();

            if (this == block)
            {
                continue;
            }

            if (checkCollision_Y(block))
                return true;
        }

        return false;
    }

    public int checkCollision_X()
    {

        for (BlockUnit u : units)
        {

            if (u.checkOutOfBoundary_X() != 0)
                return u.checkOutOfBoundary_X();
        }
        for (TetrisBlock block : getBlocks())
        {
            if (this == block)
                continue;

            if (checkCollision_X(block) != 0)
                return checkCollision_X(block);
        }
        return 0;
    }

    public boolean checkCollision_Y(TetrisBlock other)
    {

        for (BlockUnit i : units)
        {

            for (BlockUnit j : other.units)
            {
                if (i == j)
                {
                    continue;
                }

                if (i.checkVerticalCollision(j))
                    return true;
            }
        }
        return false;
    }

    public int checkCollision_X(TetrisBlock other)
    {

        for (BlockUnit i : units)
        {

            for (BlockUnit j : other.units)
            {
                if (i == j)
                    continue;


                if (i.checkHorizontalCollision(j) != 0)
                    return i.checkHorizontalCollision(j);
            }
        }
        return 0;
    }

    public void changeBoom()
    {
        isBomb = true;
        color = Color.BLACK;
    }

    /**
     * 폭탄인지 확인을 위한 함수
     *
     * @return true=폭탄 , false=일반
     */
    public boolean isBomb()
    {
        return isBomb;
    }

    public TetrisBlock(float x, float y, TetrisView tetrisView)
    {
        this.tetrisView = tetrisView;
        this.x = x;
        this.y = y;

        blockType = (int) (Math.random() * TYPESUM) + 1;
        blockDirection = 1;

        color = Const.BLOCK_COLORS[(blockType - 1) % Const.BLOCK_COLORS.length];

        switch (blockType)
        {
            case 1:
                for (int i = 0; i < 4; i++)
                {
                    units.add(new BlockUnit(x + (-2 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 2:
                units.add(new BlockUnit(x + (-1 + 1) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                for (int i = 0; i < 3; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 3:
                for (int i = 0; i < 2; i++)
                {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 4:
                units.add(new BlockUnit(x + (-1 + 0) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                for (int i = 0; i < 3; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 5:
                units.add(new BlockUnit(x + (-1 + 2) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                for (int i = 0; i < 3; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 6:
                for (int i = 0; i < 2; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + i * BlockUnit.UNITSIZE, y));
                }
                break;
            case 7:
                for (int i = 0; i < 2; i++)
                {
                    units.add(new BlockUnit(x + i * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 8: // 십자형
                for (int i = 0; i < 3; i++)
                {
                    if (i == 1)
                    {
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                    }
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 9: // ㄱ형
                if(new Random().nextInt(2) == 1)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        if (i == 1)
                        {
                            units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                        }
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                    }
                }
                else
                {
                    for (int i = 0; i < 2; i++)
                    {
                        if (i == 0)
                        {
                            units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                        }
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                    }
                }
                break;
            case 10: // H 형
                for (int i = 0; i < 3; i++)
                {
                    if (i == 0 || i == 2)
                    {
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                    }
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 11: // T형
                for (int i = 0; i < 3; i++)
                {
                    if (i == 1)
                    {
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                    }
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                }
                break;
            case 12: // 변칙 T
                for (int i = 0; i < 3; i++)
                {
                    if (i == 1)
                    {
                        units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                    }
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }
                break;
            case 13: // Q형
                for (int i = 0; i < 2; i++)
                {
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + (i - 1) * BlockUnit.UNITSIZE, y));
                }

                if(new Random().nextInt(2) == 1)
                {
                    units.add(new BlockUnit(x - BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                }
                else
                {
                    units.add(new BlockUnit(x, y - BlockUnit.UNITSIZE));
                }
                break;
            case 14: // B 형
                for (int i = 0; i < 3; i++)
                {
                    units.add(new BlockUnit(x + (-1 + i) * BlockUnit.UNITSIZE, y));
                }

                if(new Random().nextInt(2) == 1)
                {
                    units.add(new BlockUnit(x - BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                }
                else
                {
                    units.add(new BlockUnit(x - BlockUnit.UNITSIZE, y + BlockUnit.UNITSIZE));
                    units.add(new BlockUnit(x + BlockUnit.UNITSIZE, y - BlockUnit.UNITSIZE));
                }
                break;
        }

    }

    public void setX(float x)
    {

        float dif_x = x - this.x;

        for (BlockUnit u : units)
        {

            u.setX(u.getX() + dif_x);
        }
        this.x = x;
    }

    public void setY(float y)
    {
        if (checkCollision_Y())
            return;
        float dif_y = y - this.y;
        for (BlockUnit u : units)
        {
            u.setY(u.getY() + dif_y);
        }
        this.y = y;

    }

    public TetrisBlock(TetrisBlock other)
    {
        tetrisView = other.getTetrisView();
        x = other.x;
        y = other.y;
        color = other.color;
        blockDirection = other.blockDirection;
        blockType = other.blockType;
    }

    @Override
    public TetrisBlock clone()
    {

        TetrisBlock block = new TetrisBlock(this);

        for (BlockUnit u : getUnits())
        {
            block.units.add(u.clone());
        }
        return block;
    }

    public ArrayList<TetrisBlock> getBlocks()
    {
        return tetrisView.getBlocks();
    }

    public ArrayList<BlockUnit> getUnits()
    {
        return units;
    }

    public void setUnits(ArrayList<BlockUnit> units)
    {
        this.units = units;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public int getBlockDirection()
    {
        return blockDirection;
    }

    public void setBlockDirection(int blockDirection)
    {
        this.blockDirection = blockDirection;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public TetrisView getTetrisView()
    {
        return tetrisView;
    }

    public void rotate()
    {
        if (checkCollision_X() != 0 && checkCollision_Y() || blockType == 3)
            return;

        for (BlockUnit u : units)
        {
            float tx = u.getX();
            float ty = u.getY();
            u.setX(-(ty - y) + x);
            u.setY(tx - x + y);
        }
    }

}


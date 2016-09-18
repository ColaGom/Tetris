package com.bluewave.tetris.tetris;

import com.bluewave.tetris.view.TetrisView;

/**
 * Created by Developer on 2016-08-29.
 */
public class BlockUnit implements Cloneable {

    public static float UNITSIZE = -1;

    public static float max_x, max_y;

    private float x,y;

    public BlockUnit(float x,float y){

        this.x = x;
        this.y = y;
    }

    public boolean canRotate(BlockUnit other){

        if(x< TetrisView.beginPoint/2 || x>= TetrisView.max_x - UNITSIZE||y >= TetrisView.max_y - UNITSIZE)
            return false;

        if(Math.abs(x-other.x)<=UNITSIZE/2 && Math.abs(y-other.y)<=UNITSIZE/2)
            return false;
        return true;
    }

    public boolean checkOutOfBoundary_Y(){

        if(y >= TetrisView.max_y - UNITSIZE * 2 )
            return true;
        else if( y - TetrisView.max_y - UNITSIZE * 2 <= 1e-5 && y - TetrisView.max_y - UNITSIZE * 2  >= -1e-5)
            return true;
        else
            return false;
    }

    public int checkOutOfBoundary_X()
    {

        if(x<= UNITSIZE)
            return -1;
        else if(x >= TetrisView.max_x - UNITSIZE * 2)
            return 1;
        else
            return 0;
    }
    public boolean checkVerticalCollision(BlockUnit other){
        if(y >= TetrisView.max_y - UNITSIZE  )
            return true;
        else if( y - TetrisView.max_y - UNITSIZE  >= 1e-5 && y - TetrisView.max_y - UNITSIZE  <= -1e-5)
            return true;
        if(Math.abs(x - other.x) >= UNITSIZE)
            return false;
        else{
            if(Math.abs(y - other.y) > UNITSIZE)
                return false;
            else if( y- other.y - UNITSIZE < 1e-5 && y- other.y - UNITSIZE > -1e-5)
                return true;
            return true;
        }
    }
    public int checkHorizontalCollision(BlockUnit other){

        if(x <= UNITSIZE || x > TetrisView.max_x - UNITSIZE * 2)
            return checkOutOfBoundary_X();
        if(Math.abs(y - other.y )>= UNITSIZE)
            return 0;
        else{
            if(Math.abs(x - other.x) > UNITSIZE)
                return 0;
            else if(x - other.x - UNITSIZE <= 1e-5 && x - other.x - UNITSIZE >= -1e-5)
                return -1;
            else if(other.x - x - UNITSIZE <= 1e-5 && other.x - x - UNITSIZE >= -1e-5)
                return 1;

        }
        return 0;
    }

    @Override
    public BlockUnit clone(){
        return new BlockUnit(getX(),getY());
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}


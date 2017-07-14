package com.peter.zensleepfree.Model;

import android.graphics.Point;
import android.util.Size;

/**
 * Created by peter on 6/17/16.
 */
public class WheelPiece {

    public int index;
    public boolean isSelected = false;
    public Point point;
    public Size size;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

}

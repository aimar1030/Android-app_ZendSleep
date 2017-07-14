package com.peter.zensleepfree.Model;


import android.graphics.Point;

import com.peter.zensleepfree.UtilsClass.Utils;

/**
 * Created by peter on 6/16/16.
 */
public class Dot {

    public float radius;
    public Point speed;
    public float angle;
    public Point point;
    public Point viewSize;
    public int alpha;

    public float getRadius() {
        return radius;
    }

    public float getAngle() {
        return angle;
    }

    public Point getSpeed() {
        return speed;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setSpeed(Point speed) {
        this.speed = speed;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void initWithSize (int width, int height) {
        this.viewSize = new Point(width, height);
        refresh();
    }

    public void refresh() {
        if (this.point == null || needRefresh()) {
            this.point = new Point(Math.round(Utils.randomFloat() * viewSize.x), Math.round(Utils.randomFloat() * viewSize.y));
        }
        this.speed = new Point(Math.round(viewSize.x * 0.002f * Utils.randomFloatBetween( -1, 1)), Math.round(viewSize.y * 0.002f * Utils.randomFloatBetween( -1, 1)));
        if (speed.x == 0 && speed.y == 0) {
            this.speed = new Point(1, 1);
        }

        if (Utils.randomInt(4) == 0) {
            radius = viewSize.x * 0.1f * Utils.randomFloat();
            alpha = 4;
        } else {
            radius = viewSize.x * 0.005f * Utils.randomFloat();
            alpha = 18 * Utils.randomInt(9);
        }
        refreshMoving();
    }

    public void refreshMoving() {
        this.point = new Point(this.point.x + this.speed.x, this.point.y + this.speed.y);
    }

    public boolean needRefresh() {
        if (this.point.x > viewSize.x + radius * 2 || this.point.y > viewSize.y + radius * 2 || this.point.x < -radius || this.point.y < -radius) {
            return true;
        }
        return false;
    }
}
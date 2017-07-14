package com.peter.zensleepfree.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.peter.zensleepfree.Model.Dot;

import java.util.ArrayList;

/**
 * Created by peter on 6/16/16.
 */
public class BubblingView extends View {

    private static final String TAG = "BubbleView";
    private Paint paint = new Paint();
    private ArrayList<Dot> dots = new ArrayList<>();
    private static int NUMBER_OF_DOT = 70;

    public BubblingView(Context context) {
        super(context);
//        init();
    }

    public BubblingView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        init();
    }

    public BubblingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        init();
    }

    public void init(int width, int height) {
        for (int i = 0; i < NUMBER_OF_DOT; i++) {
            Dot dot = new Dot();
            dot.initWithSize(width, height);
            dots.add(dot);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < dots.size(); i++) {
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(Color.WHITE);
            paint.setAlpha(dots.get(i).alpha);
            canvas.drawCircle(dots.get(i).point.x, dots.get(i).point.y, dots.get(i).getRadius(), paint);
        }
        updateDots();
    }

    private void updateDots() {
        for (int i = 0; i < dots.size(); i++) {
            if (dots.get(i).needRefresh()) {
                dots.get(i).refresh();
            } else {
                dots.get(i).refreshMoving();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w, h);
    }
}

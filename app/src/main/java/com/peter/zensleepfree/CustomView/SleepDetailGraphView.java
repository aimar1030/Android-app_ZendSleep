package com.peter.zensleepfree.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by peter on 6/28/16.
 */
public class SleepDetailGraphView extends View {

    private float line1Y, line2Y;
    private int width, height;
    private int graphWidth;

    private float[] statuses;

    private Paint paint = new Paint();

    public SleepDetailGraphView(Context context) {
        super(context);
    }

    public SleepDetailGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SleepDetailGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(int width, int height) {
        line1Y = height * 0.33f;
        line2Y = height * 0.66f;
        graphWidth = (width - 150)/100 - 3;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw 2 lines
        paint.setStrokeWidth(2);
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(0.0f, line1Y, (float) width, line1Y, paint);
        canvas.drawLine(0.0f, line2Y, (float) width, line2Y, paint);
        paint.setStrokeWidth(graphWidth);
        for (int i = 0; i < statuses.length; i++) {
            int barHeight = 0;
            if (statuses[i] > 0.65f) {
                paint.setColor(Color.parseColor("#F9B614"));
                barHeight = (int) (statuses[i] * height);
                if (barHeight <= (int)(height * 0.67f)) {
                    barHeight = (int) (height * 0.67f) + 5;
                }
            } else if (statuses[i] > 0.3f) {
                paint.setColor(Color.parseColor("#3F51B5"));
                barHeight = (int) (statuses[i] * 0.67f/0.65f * height);
                if (barHeight >= (int)(height * 0.67f)) {
                    barHeight = (int) (height * 0.67f) - 5;
                } else if (barHeight <= (int)(height * 0.34f)) {
                    barHeight = (int)(height * 0.34f + 5);
                }
            } else {
                paint.setColor(Color.parseColor("#771661"));
                barHeight = (int) (height * 0.34f/0.3f * statuses[i]);
                if (barHeight >= (int)(height * 0.34f)) {
                    barHeight = (int) (height * 0.34f) - 5;
                }
            }
            canvas.drawLine(90 + (graphWidth + 3) * i, height, 90 + (graphWidth + 3) * i, height - barHeight, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        init(w, h);
    }

    public void setFloatArrays(float[] statusValues) {
        statuses = new float[statusValues.length];
        statuses = statusValues;
        invalidate();
    }
}

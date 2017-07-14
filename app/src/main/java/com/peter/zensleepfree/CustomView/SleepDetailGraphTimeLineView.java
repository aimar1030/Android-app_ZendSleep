package com.peter.zensleepfree.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by peter on 7/8/16.
 */
public class SleepDetailGraphTimeLineView extends View {

    private static int NUMBER_OF_GRAPHS = 100;
    private float line1Y, line2Y;
    private int width, height;
    private int graphWidth;

    private float[] textXPos;
    private String[] timeTexts;

    private float textWidth;

    private Paint paint = new Paint();
    private Path path = new Path();

    private Context ctx;

    public SleepDetailGraphTimeLineView(Context context) {
        super(context);
        ctx = context;
    }

    public SleepDetailGraphTimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    public SleepDetailGraphTimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
    }

    private void init(int width, int height) {
        this.width = width;
        this.height = height;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(3);
        paint.setColor(Color.BLUE);

        for (int i = 0; i < timeTexts.length; i++) {
            canvas.drawCircle(90 + (width - 90 * 2) * textXPos[i], 3, 4, paint);
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(36);
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "Gotham-Book.ttf");
        paint.setTypeface(typeface);
        paint.setColor(Color.BLACK);
        for (int i = 0; i < timeTexts.length; i++) {
            textWidth = paint.measureText(timeTexts[i]);
            canvas.drawText(timeTexts[i], 90 + (width - 90 * 2) * textXPos[i] - textWidth/2, 48, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w, h);
    }

    public void setTimes(ArrayList<String> timeTexts, ArrayList<Float> textX) {
        this.timeTexts = new String[timeTexts.size()];
        textXPos = new float[textX.size()];
        for (int i = 0; i < timeTexts.size(); i++) {
            this.timeTexts[i] = timeTexts.get(i);
            textXPos[i] = textX.get(i);
        }
        invalidate();
    }
}

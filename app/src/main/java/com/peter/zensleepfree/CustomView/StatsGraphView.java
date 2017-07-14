package com.peter.zensleepfree.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by peter on 7/7/16.
 */
public class StatsGraphView extends View {

//    private static int NUMBER_OF_GRAPHS = 100;
    private float line1Y, line2Y, line3Y, line4Y;
    private int width, height;
    private int graphWidth;

    private float[] statuses;

    private Paint paint = new Paint();
    private Path path = new Path();
    float textWidth, oneDayWidth, stepWidth;
    private String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
    private double[] durations, efficiencies;
//    private float[] durations = {3.5f * 3600, 5.7f * 3600, 7.2f * 3600, 4.8f * 3600, 5.4f * 3600, 9.4f * 3600, 7 * 3600};
//    private float[] efficiencies = {0.2f, 0.8f, 0.6f, 0.9f, 0.1f, 0.7f, 0.45f};

    private Context ctx;

    public StatsGraphView(Context context) {
        super(context);
        ctx = context;
    }

    public StatsGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
    }

    public StatsGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ctx = context;
    }

    private void init(int width, int height){
        line1Y = (height - 144) * 0.1f;
        line2Y = (height - 144) * 0.4f;
        line3Y = (height - 144) * 0.7f;
        line4Y = (height - 144);

        oneDayWidth = width/9;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw 4 lines
        paint.setStrokeWidth(2);
//        paint.setPathEffect(null);
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(0.0f, line1Y, (float) width, line1Y, paint);
        canvas.drawLine(0.0f, line2Y, (float) width, line2Y, paint);
        canvas.drawLine(0.0f, line3Y, (float) width, line3Y, paint);
        canvas.drawLine(0.0f, line4Y, (float) width, line4Y, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(32);
        Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "Gotham-Light.ttf");
        paint.setTypeface(typeface);
        textWidth = (int)paint.measureText("100%");
        canvas.drawText("4h", 10, line4Y - 10, paint);
        canvas.drawText("6h", 10, line3Y - 10, paint);
        canvas.drawText("8h", 10, line2Y - 10, paint);
        canvas.drawText("10h", 10, line1Y - 10, paint);

        canvas.drawText("40%", width - textWidth - 10, line4Y - 10, paint);
        canvas.drawText("60%", width - textWidth - 10, line3Y - 10, paint);
        canvas.drawText("80%", width - textWidth - 10, line2Y - 10, paint);
        canvas.drawText("100%", width - textWidth - 10, line1Y - 10, paint);

        paint.setTextSize(42);
        for (int i = 0; i < days.length; i++) {
            textWidth = paint.measureText(days[i]);
            stepWidth = oneDayWidth - textWidth;
            canvas.drawText(days[i], oneDayWidth * (i + 1) + stepWidth/2, line4Y + 64, paint);
        }

        // Draw graph
        graphWidth = (int) (textWidth/4);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(graphWidth);
        paint.setColor(Color.parseColor("#8F8567"));

        if (durations != null) {
            for (int i = 0; i < 7; i++) {
                textWidth = paint.measureText(days[i]);
                stepWidth = oneDayWidth - textWidth;
                int barHeight = 0;
                if (durations[i] == 0) {
                    barHeight = 0;
                } else if ((durations[i] - 4 * 3600) < 0) {
                    barHeight = 4;
                } else if ((durations[i] - 4 * 3600) > 6 * 3600) {
                    barHeight = (int) (height - 144 - (height - 144) * 0.1f);
                } else {
                    double zoom = (durations[i] - 4 * 3600) / (6 * 3600);
                    barHeight = (int) (zoom * (height - 144 - (height - 144) * 0.1f));
                }
                if (i == 1 || i == 3) {
                    canvas.drawLine(oneDayWidth * (i + 1) + stepWidth + graphWidth, height - 144, oneDayWidth * (i + 1) + stepWidth + graphWidth, height - 144 - barHeight, paint);
                } else if (i == 5) {
                    canvas.drawLine(oneDayWidth * (i + 1) + stepWidth / 2 + graphWidth / 2, height - 144, oneDayWidth * (i + 1) + stepWidth / 2 + graphWidth / 2, height - 144 - barHeight, paint);
                } else {
                    canvas.drawLine(oneDayWidth * (i + 1) + stepWidth + graphWidth / 2, height - 144, oneDayWidth * (i + 1) + stepWidth + graphWidth / 2, height - 144 - barHeight, paint);
                }
            }

            paint.setColor(Color.parseColor("#8F8567"));

            for (int i = 0; i < 7; i++) {
                textWidth = paint.measureText(days[i]);
                stepWidth = oneDayWidth - textWidth;
                int barHeight = 0;
                if (efficiencies[i] > 0.8f) {
                    paint.setColor(Color.parseColor("#20B199"));
                } else if (efficiencies[i] > 0.6f) {
                    paint.setColor(Color.parseColor("#FDB914"));
                } else {
                    paint.setColor(Color.parseColor("#F04B5F"));
                }

                if (efficiencies[i] == 0) {
                    barHeight = 0;
                } else if (efficiencies[i] < 0.4f) {
                    barHeight = 4;
                } else if (efficiencies[i] > 1) {
                    barHeight = (int) (height - 144 - (height - 144) * 0.1f);
                } else {
                    double zoom = (efficiencies[i] - 0.4f) / 0.6f;
                    barHeight = (int) (zoom * (height - 144 - (height - 144) * 0.1f));
                }
                if (i == 1 || i == 3) {
                    canvas.drawLine(oneDayWidth * (i + 1) - stepWidth + oneDayWidth - graphWidth, height - 144, oneDayWidth * (i + 1) - stepWidth + oneDayWidth - graphWidth, height - 144 - barHeight, paint);
                } else if (i == 5) {
                    canvas.drawLine(oneDayWidth * (i + 1) - stepWidth / 2 + oneDayWidth - graphWidth / 2, height - 144, oneDayWidth * (i + 1) - stepWidth / 2 + oneDayWidth - graphWidth / 2, height - 144 - barHeight, paint);
                } else {
                    canvas.drawLine(oneDayWidth * (i + 1) - stepWidth + oneDayWidth - graphWidth / 2, height - 144, oneDayWidth * (i + 1) - stepWidth + oneDayWidth - graphWidth / 2, height - 144 - barHeight, paint);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        init(w, h);
    }

    public void setValues(double[] duration, double[] efficiency) {
        durations = new double[duration.length];
        durations = duration;
        efficiencies = new double[efficiency.length];
        efficiencies = efficiency;

        invalidate();
    }
}

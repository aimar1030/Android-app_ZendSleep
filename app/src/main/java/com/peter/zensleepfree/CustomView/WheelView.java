package com.peter.zensleepfree.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.ZenSleep;

/**
 * Created by peter on 6/17/16.
 */
public class WheelView extends View {

//    private ArrayList<WheelPiece> wheelPieces = new ArrayList<>();
    private Paint paint = new Paint();
    private Path path = new Path();
    private boolean[] selected = {false, false, false, false, false, false};
    private float smallRadius, radius, center_x, center_y, imgCenterX, imgCenterY;
    float textWidth;

    public WheelView(Context context) {
        super(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = (float)getWidth();
        float height = (float)getHeight();
        center_x = width/2;
        center_y = height/2;

        if (width > height) {
            radius = height * 0.4f;
            smallRadius = height * 0.15f;
        } else {
            radius = width * 0.4f;
            smallRadius = width * 0.15f;
        }

        paint.setStrokeWidth(3);
//        paint.setPathEffect(null);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);

//         3rd piece
        if (!selected[2]) {
            path.moveTo((float) (center_x + 6 + radius * Math.sin(Math.PI / 6)), (float) (center_y - 6 - radius * Math.cos(Math.PI / 6)));
            // Big circle
            for (double i = Math.PI / 6; i < Math.PI / 2; i += 0.01) {
                float x = (float) (center_x + 6 + radius * Math.sin(i));
                float y = (float) (center_y - 6 - radius * Math.cos(i));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.lineTo((float) (center_x + 6 + smallRadius * Math.sin(Math.PI / 2)), (float) (center_y - 6 - smallRadius * Math.cos(Math.PI / 2)));
            path.moveTo((float) (center_x + 6 + smallRadius * Math.sin(Math.PI / 6)), (float) (center_y - 6 - smallRadius * Math.cos(Math.PI / 6)));
            // Small circle
            for (double i = Math.PI / 6; i < Math.PI / 2; i += 0.01) {
                float x = (float) (center_x + 6 + smallRadius * Math.sin(i));
                float y = (float) (center_y - 6 - smallRadius * Math.cos(i));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.moveTo((float) (center_x + 6 + smallRadius * Math.sin(Math.PI / 6)), (float) (center_y - 6 - smallRadius * Math.cos(Math.PI / 6)));
            path.lineTo((float) (center_x + 6 + radius * Math.sin(Math.PI / 6)), (float) (center_y - 6 - radius * Math.cos(Math.PI / 6)));

            imgCenterX = (float) (center_x + smallRadius + (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y - (radius - (radius - smallRadius) * 0.3)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_3);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            textWidth = paint.measureText("electronics");
            canvas.drawText("electronics", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            textWidth = paint.measureText("in bed");
            canvas.drawText("in bed", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4) + 32, paint);

            paint.setStyle(Paint.Style.STROKE);
        } else {
            for (double i = Math.PI / 6; i < Math.PI / 2; i += 0.007) {
                float bx = (float) (center_x + 6 + radius * Math.sin(i));
                float by = (float) (center_y - 6 - radius * Math.cos(i));
                float sx = (float) (center_x + 6 + smallRadius * Math.sin(i));
                float sy = (float) (center_y - 6 - smallRadius * Math.cos(i));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            imgCenterX = (float) (center_x + smallRadius + (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y - (radius - (radius - smallRadius) * 0.3)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_3_on);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            paint.setColor(Color.parseColor("#144B5E"));
            textWidth = paint.measureText("electronics");
            canvas.drawText("electronics", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            textWidth = paint.measureText("in bed");
            canvas.drawText("in bed", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4) + 32, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
        }

        // 4th piece
        if (!selected[3]) {
            path.moveTo((float) (center_x + 6 + radius * Math.cos(0)), (float) (center_y + 6 - radius * Math.sin(0)));
            // Big circle
            for (double i = Math.PI / 2; i < Math.PI / 6 * 5; i += 0.01) {
                float x = (float) (center_x + 6 + radius * Math.cos(i - Math.PI / 2));
                float y = (float) (center_y + 6 + radius * Math.sin(i - Math.PI / 2));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.lineTo((float) (center_x + 6 + smallRadius * Math.cos(Math.PI / 6 * 5 - Math.PI / 2)), (float) (center_y + 6 + smallRadius * Math.sin(Math.PI / 6 * 5 - Math.PI / 2)));
            path.moveTo((float) (center_x + 6 + smallRadius * Math.cos(0)), (float) (center_y + 6 - smallRadius * Math.sin(0)));
            // Small circle
            for (double i = Math.PI / 2; i < Math.PI / 6 * 5; i += 0.01) {
                float x = (float) (center_x + 6 + smallRadius * Math.cos(i - Math.PI / 2));
                float y = (float) (center_y + 6 + smallRadius * Math.sin(i - Math.PI / 2));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.moveTo((float) (center_x + 6 + smallRadius * Math.cos(0)), (float) (center_y + 6 - smallRadius * Math.sin(0)));
            path.lineTo((float) (center_x + 6 + radius * Math.cos(0)), (float) (center_y + 6 - radius * Math.sin(0)));

            imgCenterX = (float) (center_x + smallRadius + (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y + (radius - (radius - smallRadius) * 0.6)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_4);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            textWidth = paint.measureText("coffeine");
            canvas.drawText("coffeine", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);

        } else {
            for (double i = Math.PI / 2; i < Math.PI / 6 * 5; i += 0.007) {
                float bx = (float) (center_x + 6 + radius * Math.cos(i - Math.PI / 2));
                float by = (float) (center_y + 6 + radius * Math.sin(i - Math.PI / 2));
                float sx = (float) (center_x + 6 + smallRadius * Math.cos(i - Math.PI / 2));
                float sy = (float) (center_y + 6 + smallRadius * Math.sin(i - Math.PI / 2));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            imgCenterX = (float) (center_x + smallRadius + (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y + (radius - (radius - smallRadius) * 0.6)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_4_on);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            paint.setColor(Color.parseColor("#144B5E"));
            textWidth = paint.measureText("coffeine");
            canvas.drawText("coffeine", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
        }

        // 6th piece
        if (!selected[5]) {
            path.moveTo((float) (center_x - 6 - radius * Math.sin(Math.PI / 6)), (float) (center_y + 6 + radius * Math.cos(Math.PI / 6)));
            // Big circle
            for (double i = Math.PI / 6 * 7; i < Math.PI / 6 * 9; i += 0.01) {
                float x = (float) (center_x - 6 - radius * Math.sin(i - Math.PI));
                float y = (float) (center_y + 6 + radius * Math.cos(i - Math.PI));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.lineTo((float) (center_x - 6 - smallRadius * Math.sin(Math.PI / 6 * 9 - Math.PI)), (float) (center_y + 6 + smallRadius * Math.cos(Math.PI / 6 * 9 - Math.PI)));
            path.moveTo((float) (center_x - 6 - smallRadius * Math.sin(Math.PI / 6 * 7 - Math.PI)), (float) (center_y + 6 + smallRadius * Math.cos(Math.PI / 6 * 7 - Math.PI)));
            // Small circle
            for (double i = Math.PI / 6 * 7; i < Math.PI / 6 * 9; i += 0.01) {
                float x = (float) (center_x - 6 - smallRadius * Math.sin(i - Math.PI));
                float y = (float) (center_y + 6 + smallRadius * Math.cos(i - Math.PI));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.moveTo((float) (center_x - 6 - smallRadius * Math.sin(Math.PI / 6 * 7 - Math.PI)), (float) (center_y + 6 + smallRadius * Math.cos(Math.PI / 6 * 7 - Math.PI)));
            path.lineTo((float) (center_x - 6 - radius * Math.sin(Math.PI / 6 * 7 - Math.PI)), (float) (center_y + 6 + radius * Math.cos(Math.PI / 6 * 7 - Math.PI)));

            imgCenterX = (float) (center_x - smallRadius - (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y + (radius - (radius - smallRadius) * 0.6)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_6);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            textWidth = paint.measureText("alcohol");
            canvas.drawText("alcohol", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
        } else {
            for (double i = Math.PI / 6 * 7; i < Math.PI / 6 * 9; i += 0.007) {
                float bx = (float) (center_x - 6 - radius * Math.sin(i - Math.PI));
                float by = (float) (center_y + 6 + radius * Math.cos(i - Math.PI));
                float sx = (float) (center_x - 6 - smallRadius * Math.sin(i - Math.PI));
                float sy = (float) (center_y + 6 + smallRadius * Math.cos(i - Math.PI));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            imgCenterX = (float) (center_x - smallRadius - (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y + (radius - (radius - smallRadius) * 0.6)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_6_on);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            paint.setColor(Color.parseColor("#144B5E"));
            textWidth = paint.measureText("alcohol");
            canvas.drawText("alcohol", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
        }

        // 1st piece
        if (!selected[0]) {
            path.moveTo((float) (center_x - 6 - radius * Math.cos(0)), (float) (center_y - 6 - radius * Math.sin(0)));
            // Big circle
            for (double i = Math.PI / 6 * 9; i < Math.PI / 6 * 11; i += 0.01) {
                float x = (float) (center_x - 6 - radius * Math.cos(i - Math.PI / 2 * 3));
                float y = (float) (center_y - 6 - radius * Math.sin(i - Math.PI / 2 * 3));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.lineTo((float) (center_x - 6 - smallRadius * Math.cos(Math.PI / 6 * 11 - Math.PI / 2 * 3)), (float) (center_y - 6 - smallRadius * Math.sin(Math.PI / 6 * 11 - Math.PI / 2 * 3)));
            path.moveTo((float) (center_x - 6 - smallRadius * Math.cos(Math.PI / 6 * 9 - Math.PI / 2 * 3)), (float) (center_y - 6 - smallRadius * Math.sin(Math.PI / 6 * 9 - Math.PI / 2 * 3)));
            // Small circle
            for (double i = Math.PI / 6 * 9; i < Math.PI / 6 * 11; i += 0.01) {
                float x = (float) (center_x - 6 - smallRadius * Math.cos(i - Math.PI / 2 * 3));
                float y = (float) (center_y - 6 - smallRadius * Math.sin(i - Math.PI / 2 * 3));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.moveTo((float) (center_x - 6 - smallRadius * Math.cos(Math.PI / 6 * 9 - Math.PI / 2 * 3)), (float) (center_y - 6 - smallRadius * Math.sin(Math.PI / 6 * 9 - Math.PI / 2 * 3)));
            path.lineTo((float) (center_x - 6 - radius * Math.cos(Math.PI / 6 * 9 - Math.PI / 2 * 3)), (float) (center_y - 6 - radius * Math.sin(Math.PI / 6 * 9 - Math.PI / 2 * 3)));
            imgCenterX = (float) (center_x - smallRadius - (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y - (radius - (radius - smallRadius) * 0.3)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_1);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            textWidth = paint.measureText("worked out");
            canvas.drawText("worked out", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
        } else {
            for (double i = Math.PI / 6 * 9; i < Math.PI / 6 * 11; i += 0.007) {
                float bx = (float) (center_x - 6 - radius * Math.cos(i - Math.PI / 2 * 3));
                float by = (float) (center_y - 6 - radius * Math.sin(i - Math.PI / 2 * 3));
                float sx = (float) (center_x - 6 - smallRadius * Math.cos(i - Math.PI / 2 * 3));
                float sy = (float) (center_y - 6 - smallRadius * Math.sin(i - Math.PI / 2 * 3));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            imgCenterX = (float) (center_x - smallRadius - (radius - smallRadius) * 0.4);
            imgCenterY = (float) (center_y - (radius - (radius - smallRadius) * 0.3)/2);
            Drawable d = getResources().getDrawable(R.drawable.status_1_on);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            paint.setColor(Color.parseColor("#144B5E"));
            textWidth = paint.measureText("worked out");
            canvas.drawText("worked out", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
        }
        // 2nd piece
        if (!selected[1]) {
            path.moveTo((float) (center_x - radius * Math.cos(Math.PI / 6 * 11 - Math.PI / 2 * 3)), (float) (center_y - 12 - radius * Math.sin(Math.PI / 6 * 11 - Math.PI / 2 * 3)));
            // Big circle
            for (double i = Math.PI / 6 * 11; i < Math.PI * 2; i += 0.01) {
                float x = (float) (center_x - radius * Math.cos(i - Math.PI / 2 * 3));
                float y = (float) (center_y - 12 - radius * Math.sin(i - Math.PI / 2 * 3));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            for (double i = 0; i < Math.PI / 6; i += 0.01) {
                float x = (float) (center_x + radius * Math.sin(i));
                float y = (float) (center_y - 12 - radius * Math.cos(i));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.lineTo((float) (center_x + smallRadius * Math.sin(Math.PI / 6)), (float) (center_y - 12 - smallRadius * Math.cos(Math.PI / 6)));
            path.moveTo((float) (center_x - smallRadius * Math.cos(Math.PI / 6 * 11 - Math.PI / 2 * 3)), (float) (center_y - 12 - smallRadius * Math.sin(Math.PI / 6 * 11 - Math.PI / 2 * 3)));
            // Small circle
            for (double i = Math.PI / 6 * 11; i < Math.PI * 2; i += 0.01) {
                float x = (float) (center_x - smallRadius * Math.cos(i - Math.PI / 2 * 3));
                float y = (float) (center_y - 12 - smallRadius * Math.sin(i - Math.PI / 2 * 3));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            for (double i = 0; i < Math.PI / 6; i += 0.01) {
                float x = (float) (center_x + smallRadius * Math.sin(i));
                float y = (float) (center_y - 12 - smallRadius * Math.cos(i));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.moveTo((float) (center_x - smallRadius * Math.cos(Math.PI / 6 * 11 - Math.PI / 2 * 3)), (float) (center_y - 12 - smallRadius * Math.sin(Math.PI / 6 * 11 - Math.PI / 2 * 3)));
            path.lineTo((float) (center_x - radius * Math.cos(Math.PI / 6 * 11 - Math.PI / 2 * 3)), (float) (center_y - 12 - radius * Math.sin(Math.PI / 6 * 11 - Math.PI / 2 * 3)));

            imgCenterX = center_x;
            imgCenterY = center_y - smallRadius * 2;
            Drawable d = getResources().getDrawable(R.drawable.status_2);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            textWidth = paint.measureText("stress");
            canvas.drawText("stress", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
        } else {
            for (double i = Math.PI / 6 * 11; i < Math.PI * 2; i += 0.007) {
                float bx = (float) (center_x - radius * Math.cos(i - Math.PI / 2 * 3));
                float by = (float) (center_y - 12 - radius * Math.sin(i - Math.PI / 2 * 3));
                float sx = (float) (center_x - smallRadius * Math.cos(i - Math.PI / 2 * 3));
                float sy = (float) (center_y - 12 - smallRadius * Math.sin(i - Math.PI / 2 * 3));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            for (double i = 0; i < Math.PI / 6; i += 0.007) {
                float bx = (float) (center_x + radius * Math.sin(i));
                float by = (float) (center_y - 12 - radius * Math.cos(i));
                float sx = (float) (center_x + smallRadius * Math.sin(i));
                float sy = (float) (center_y - 12 - smallRadius * Math.cos(i));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            imgCenterX = center_x;
            imgCenterY = center_y - smallRadius * 2;
            Drawable d = getResources().getDrawable(R.drawable.status_2_on);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            paint.setColor(Color.parseColor("#144B5E"));
            textWidth = paint.measureText("stress");
            canvas.drawText("stress", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
        }
        // 5th piece
        if (!selected[4]) {
            path.moveTo((float) (center_x + radius * Math.cos(Math.PI / 6 * 5 - Math.PI / 2)), (float) (center_y + 12 + radius * Math.sin(Math.PI / 6 * 5 - Math.PI / 2)));
            // Big circle
            for (double i = Math.PI / 6 * 5; i < Math.PI; i += 0.01) {
                float x = (float) (center_x + radius * Math.cos(i - Math.PI / 2));
                float y = (float) (center_y + 12 + radius * Math.sin(i - Math.PI / 2));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }

            for (double i = Math.PI; i < Math.PI / 6 * 7; i += 0.01) {
                float x = (float) (center_x - radius * Math.sin(i - Math.PI));
                float y = (float) (center_y + 12 + radius * Math.cos(i - Math.PI));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.lineTo((float) (center_x - smallRadius * Math.sin(Math.PI / 6 * 7 - Math.PI)), (float) (center_y + 12 + smallRadius * Math.cos(Math.PI / 6 * 7 - Math.PI)));
            path.moveTo((float) (center_x + smallRadius * Math.cos(Math.PI / 6 * 5 - Math.PI / 2)), (float) (center_y + 12 + smallRadius * Math.sin(Math.PI / 6 * 5 - Math.PI / 2)));
            // Small circle
            for (double i = Math.PI / 6 * 5; i < Math.PI; i += 0.01) {
                float x = (float) (center_x + smallRadius * Math.cos(i - Math.PI / 2));
                float y = (float) (center_y + 12 + smallRadius * Math.sin(i - Math.PI / 2));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }

            for (double i = Math.PI; i < Math.PI / 6 * 7; i += 0.01) {
                float x = (float) (center_x - smallRadius * Math.sin(i - Math.PI));
                float y = (float) (center_y + 12 + smallRadius * Math.cos(i - Math.PI));
                path.lineTo(x, y);
                path.moveTo(x, y);
            }
            path.moveTo((float) (center_x + smallRadius * Math.cos(Math.PI / 6 * 5 - Math.PI / 2)), (float) (center_y + 12 + smallRadius * Math.sin(Math.PI / 6 * 5 - Math.PI / 2)));
            path.lineTo((float) (center_x + radius * Math.cos(Math.PI / 6 * 5 - Math.PI / 2)), (float) (center_y + 12 + radius * Math.sin(Math.PI / 6 * 5 - Math.PI / 2)));

            imgCenterX = center_x;
            imgCenterY = center_y + smallRadius * 1.8f;
            Drawable d = getResources().getDrawable(R.drawable.status_5);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            textWidth = paint.measureText("missed meal");
            canvas.drawText("missed meal", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
        } else {
            for (double i = Math.PI / 6 * 5; i < Math.PI; i += 0.007) {
                float bx = (float) (center_x + radius * Math.cos(i - Math.PI / 2));
                float by = (float) (center_y + 12 + radius * Math.sin(i - Math.PI / 2));
                float sx = (float) (center_x + smallRadius * Math.cos(i - Math.PI / 2));
                float sy = (float) (center_y + 12 + smallRadius * Math.sin(i - Math.PI / 2));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            for (double i = Math.PI; i < Math.PI / 6 * 7; i += 0.007) {
                float bx = (float) (center_x - radius * Math.sin(i - Math.PI));
                float by = (float) (center_y + 12 + radius * Math.cos(i - Math.PI));
                float sx = (float) (center_x - smallRadius * Math.sin(i - Math.PI));
                float sy = (float) (center_y + 12 + smallRadius * Math.cos(i - Math.PI));
                canvas.drawLine(bx, by, sx, sy, paint);
            }
            imgCenterX = center_x;
            imgCenterY = center_y + smallRadius * 1.8f;
            Drawable d = getResources().getDrawable(R.drawable.status_5_on);
            if (d != null) {
                d.setBounds((int) (imgCenterX - (radius - smallRadius) * 0.25),
                        (int) (imgCenterY - (radius - smallRadius) * 0.25),
                        (int) (imgCenterX + (radius - smallRadius) * 0.25),
                        (int) (imgCenterY + (radius - smallRadius) * 0.25));
                d.draw(canvas);
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(36);
            paint.setColor(Color.parseColor("#144B5E"));
            textWidth = paint.measureText("missed meal");
            canvas.drawText("missed meal", imgCenterX - textWidth/2, (int) (imgCenterY + (radius - smallRadius) * 0.4), paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.WHITE);
        }
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (touchX > center_x + smallRadius && touchX < center_x + radius && touchY > center_y - radius * Math.tan(Math.PI/6) && touchY < center_y) {
                    selected[2] = !selected[2];
                    path.reset();
                    if (selected[2]) {
                        ZenSleep.sleepConditions = ZenSleep.sleepConditions + Constant.SLEEP_STATUS_NOTMYBED;
                    } else {
                        if (ZenSleep.sleepConditions >= Constant.SLEEP_STATUS_NOTMYBED) {
                            ZenSleep.sleepConditions = ZenSleep.sleepConditions - Constant.SLEEP_STATUS_NOTMYBED;
                        }
                    }
                    invalidate();
                } else if (touchX > center_x + smallRadius && touchX < center_x + radius && touchY < center_y + radius * Math.tan(Math.PI/6) && touchY > center_y) {
                    selected[3] = !selected[3];
                    path.reset();
                    if (selected[3]) {
                        ZenSleep.sleepConditions = ZenSleep.sleepConditions + Constant.SLEEP_STATUS_CAFFEINE;
                    } else {
                        if (ZenSleep.sleepConditions >= Constant.SLEEP_STATUS_CAFFEINE) {
                            ZenSleep.sleepConditions = ZenSleep.sleepConditions - Constant.SLEEP_STATUS_CAFFEINE;
                        }
                    }
                    invalidate();
                } else if (touchX > center_x - radius && touchX < center_x - smallRadius && touchY < center_y + radius * Math.tan(Math.PI/6) && touchY > center_y) {
                    selected[5] = !selected[5];
                    path.reset();
                    if (selected[5]) {
                        ZenSleep.sleepConditions = ZenSleep.sleepConditions + Constant.SLEEP_STATUS_ALCOHOL;
                    } else {
                        if (ZenSleep.sleepConditions >= Constant.SLEEP_STATUS_ALCOHOL) {
                            ZenSleep.sleepConditions = ZenSleep.sleepConditions - Constant.SLEEP_STATUS_ALCOHOL;
                        }
                    }
                    invalidate();
                } else if (touchX > center_x - radius && touchX < center_x - smallRadius && touchY > center_y - radius * Math.tan(Math.PI/6) && touchY < center_y) {
                    selected[0] = !selected[0];
                    path.reset();
                    if (selected[0]) {
                        ZenSleep.sleepConditions = ZenSleep.sleepConditions + Constant.SLEEP_STATUS_WORKED_OUT;
                    } else {
                        if (ZenSleep.sleepConditions >= Constant.SLEEP_STATUS_WORKED_OUT) {
                            ZenSleep.sleepConditions = ZenSleep.sleepConditions - Constant.SLEEP_STATUS_WORKED_OUT;
                        }
                    }
                    invalidate();
                } else if (touchX > center_x - smallRadius/2 && touchX < center_x + smallRadius/2 && touchY > center_y - radius && touchY < center_y - smallRadius) {
                    selected[1] = !selected[1];
                    path.reset();
                    if (selected[1]) {
                        ZenSleep.sleepConditions = ZenSleep.sleepConditions + Constant.SLEEP_STATUS_STRESS;
                    } else {
                        if (ZenSleep.sleepConditions >= Constant.SLEEP_STATUS_STRESS) {
                            ZenSleep.sleepConditions = ZenSleep.sleepConditions - Constant.SLEEP_STATUS_STRESS;
                        }
                    }
                    invalidate();
                } else if (touchX > center_x - smallRadius/2 && touchX < center_x + smallRadius/2 && touchY > center_y + smallRadius && touchY < center_y + radius) {
                    selected[4] = !selected[4];
                    path.reset();
                    if (selected[4]) {
                        ZenSleep.sleepConditions = ZenSleep.sleepConditions + Constant.SLEEP_STATUS_MISSEDMEAL;
                    } else {
                        if (ZenSleep.sleepConditions >= Constant.SLEEP_STATUS_MISSEDMEAL) {
                            ZenSleep.sleepConditions = ZenSleep.sleepConditions - Constant.SLEEP_STATUS_MISSEDMEAL;
                        }
                    }
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}

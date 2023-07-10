package com.service.keylessrn.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.service.keylessrn.utility.Utility;

public class AutoBrightnessIcon extends View {

    Paint paint;
    Canvas canvas;
    Context context;
    int width;
    int height;

    public AutoBrightnessIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        drawBrightnessImage();
        width = getWidth();
        height = getHeight();
    }

    private void drawBrightnessImage() {

        paint.reset();
        int width = getHeight();
        int left = width / 4 + 45;
        int top = width / 4 + 45;
        int right = (3 * width) / 4 - 45;
        int bottom = (3 * width) / 4 - 45;

        paint.setColor(Color.WHITE);
        canvas.drawArc(0, 0, width, width, 0, 360, true, paint);


        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(left, top, right, bottom, 0, 360, true, paint);

        float angle = 360f / 8f;
        for (int i = 1; i <= 8; i++) {
            float positionAngle = i * angle;
            double radians = Math.toRadians(positionAngle);

            int startX = (int) ((width / 2) + 25 * Math.cos(radians));
            int startY = (int) ((width / 2) + 25 * Math.sin(radians));

            int endX = (int) ((width / 2) + 33 * Math.cos(radians));
            int endY = (int) ((width / 2) + 33 * Math.sin(radians));

            if (i == 1) {
                Paint numberPaint = new Paint();
                numberPaint.setColor(Color.BLACK);
                numberPaint.setTextSize(30);
                numberPaint.setFakeBoldText(true);

                Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
                float yOffset = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
                endY += yOffset;

                canvas.drawText("A", endX - 5, endY - 5, numberPaint);
            } else {
                canvas.drawLine(startX, startY, endX, endY, paint);
            }
        }

        paint.reset();
    }
}

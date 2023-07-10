package com.service.keylessrn.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Clock extends View {
    Paint paint;
    Canvas canvas;
    int centerX;
    int centerY;
    RectF rectangle;
    Path path;

    public Clock(Context context) {
        super(context);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        rectangle = new RectF();
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        setCoordinates();
        drawBaseCircle();
        drawLines();
        drawCenterPoint();
        drawHourHand();
        drawMinuteHand();
        // drawStar();
    }

    private void drawStar() {
        int x = centerX + 400;
        int y = centerY - 200;

        paint.setColor(Color.GREEN);
        canvas.drawCircle(x, y, 150, paint);

        paint.setColor(Color.RED);
        canvas.drawPoint(x - 45, y - 100, paint);

        paint.setColor(Color.BLUE);
        canvas.drawPoint(x - 200, y + 25, paint);
        paint.setColor(Color.BLACK);

        Path starPath = new Path();
        starPath.moveTo(x, y);
        starPath.lineTo(x - 50, y - 100);
        starPath.lineTo(x - 200, y + 25);

        starPath.close();

        canvas.drawPath(starPath, paint);

    }

    private void drawBaseCircle() {
        int left = centerX - 300;
        int top = centerY - 300;
        int right = centerX + 300;
        int bottom = centerY + 300;

        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(left, top, right, bottom, 0, 360, true, paint);
    }

    private void drawLines() {
        float angle = 360f / 12f;
        for (int i = -2; i < 10; i++) {
            float positionAngle = i * angle;
            double radians = Math.toRadians(positionAngle);

            int startX = (int) (centerX + 260 * Math.cos(radians));
            int startY = (int) (centerY + 260 * Math.sin(radians));

            int endX = (int) (centerX + 300 * Math.cos(radians));
            int endY = (int) (centerY + 300 * Math.sin(radians));

            canvas.drawLine(startX, startY, endX, endY, paint);

            int x = (int) (centerX + 220 * Math.cos(radians));
            int y = (int) (centerY + 220 * Math.sin(radians));

            Paint numberPaint = new Paint();
            numberPaint.setColor(Color.BLACK);
            numberPaint.setTextSize(40);
            numberPaint.setFakeBoldText(true);

            Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
            float yOffset = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            y += yOffset;

            x -= numberPaint.measureText(String.valueOf(i + 3)) / 2;

            canvas.drawText(String.valueOf(i + 3), x, y, numberPaint);
        }
    }

    private void drawCenterPoint() {
        paint.reset();
        paint.setColor(Color.BLACK);
        int left = centerX - 20;
        int top = centerY - 20;
        int right = centerX + 20;
        int bottom = centerY + 20;

        canvas.drawArc(left, top, right, bottom, 0, 360, false, paint);
    }

    private void drawHourHand() {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        int endX = (int) (centerX + 80 * Math.cos(300));
        int endY = (int) (centerY + 80 * Math.sin(300));

        canvas.drawLine(centerX, centerY, endX, endY, paint);

        Path trianglePath = new Path();
        trianglePath.moveTo(endX - 30, endY);
        trianglePath.lineTo(endX + 30, endY);
        trianglePath.lineTo(endX, endY - 50);
        trianglePath.close();

        canvas.drawPath(trianglePath, paint);
    }

    private void drawMinuteHand() {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        int endX = (int) (centerX + 150 * Math.cos(280));
        int endY = (int) (centerY + 150 * Math.sin(280));

        canvas.drawLine(centerX, centerY, endX, endY, paint);

        Path trianglePath = new Path();
        trianglePath.moveTo(endX - 30, endY);
        trianglePath.lineTo(endX + 30, endY);
        trianglePath.lineTo(endX, endY - 50);

        canvas.rotate(290, endX, endY);
        canvas.drawPath(trianglePath, paint);
    }

    private void setCoordinates() {
        this.centerX = getWidth() / 2;
        this.centerY = getHeight() / 2;
    }

}

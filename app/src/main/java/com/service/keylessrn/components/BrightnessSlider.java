package com.service.keylessrn.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.service.keylessrn.utility.Utility;

public class BrightnessSlider extends View {
    Paint paint;
    Canvas canvas;
    Context context;
    RectF sliderBounds;
    int minSliderWidth;
    int maxSliderWidth;
    int sliderWidth;

    public BrightnessSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        sliderBounds = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        setCoordinates();
        drawSlider();
        drawBrightnessImage();
    }

    private void drawBrightnessImage() {
        int left = minSliderWidth / 4 + 50;
        int top = minSliderWidth / 4 + 50;
        int right = (3 * minSliderWidth) / 4 - 50;
        int bottom = (3 * minSliderWidth) / 4 - 50;
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(left, top, right, bottom, 0, 360, true, paint);

        canvas.rotate((int) (sliderWidth / 2), (int) (minSliderWidth / 2), (int) (minSliderWidth / 2));
        float angle = 360f / 8f;
        for (int i = 1; i <= 8; i++) {
            float positionAngle = i * angle;
            double radians = Math.toRadians(positionAngle);

            int startX = (int) ((minSliderWidth / 2) + 25 * Math.cos(radians));
            int startY = (int) ((minSliderWidth / 2) + 25 * Math.sin(radians));

            int endX = (int) ((minSliderWidth / 2) + 33 * Math.cos(radians));
            int endY = (int) ((minSliderWidth / 2) + 33 * Math.sin(radians));

            canvas.drawLine(startX, startY, endX, endY, paint);
        }

        paint.reset();
    }

    private void setCoordinates() {
        if (sliderWidth == 0) {
            sliderWidth = getHeight();
        }
        if (minSliderWidth == 0) {
            minSliderWidth = getHeight();
        }
        if (maxSliderWidth == 0) {
            maxSliderWidth = getWidth();
        }
        sliderBounds.set(0, 0, getWidth(), getHeight());
    }

    private void drawSlider() {
        int left = 0;
        int top = 0;
        int right = sliderWidth;
        int bottom = getHeight();
        int borderRadius = Utility.dpToPx(context, 50);

        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(left, top, right, bottom, borderRadius, borderRadius, paint);
    }

    float initialTouchX = 0;
    float initialWidth = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                initialTouchX = x;
                initialWidth = sliderWidth;
                return sliderBounds.contains(x, y);
            case MotionEvent.ACTION_MOVE:
                sliderWidth = (int) Math.min(Math.max((initialWidth + (x - initialTouchX)), minSliderWidth), maxSliderWidth);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                return true;
        }
        return true;
    }
}

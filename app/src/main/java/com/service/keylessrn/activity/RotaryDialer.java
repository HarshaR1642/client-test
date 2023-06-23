package com.service.keylessrn.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

interface OnPasscodeEnterListener {
    void onPasscodeEnter(String passcode);
}

public class RotaryDialer extends View {
    private float width;
    private float height;
    private float centerX;
    private float centerY;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private Canvas canvas;
    private final Paint paint;
    private final Paint numberPaint;
    private final Path clipPath;
    private float progressAngle = 45;
    String number = "";
    public OnPasscodeEnterListener onPasscodeEnterListener;

    public RotaryDialer(Context context, AttributeSet attributeSet) {
        super(context);

        paint = new Paint();
        numberPaint = new Paint();
        clipPath = new Path();
    }

    public void setOnPasscodeEnterListener(OnPasscodeEnterListener listener) {
        this.onPasscodeEnterListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        setWidthAndHeight(getWidth(), getHeight());
        setCoordinates();
        drawDialer();
    }

    private void setWidthAndHeight(float width, float height) {
        this.width = width;
        this.height = height;
    }

    private void setCoordinates() {
        centerX = width / 2;
        centerY = height / 2;

        float radius = 500;
        left = centerX - radius;
        top = centerY - radius;
        right = centerX + radius;
        bottom = centerY + radius;
    }

    private void drawDialer() {
        drawBaseCircle();
        drawRotaryCircle();
        drawNumbers();
    }

    private void drawBaseCircle() {
        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(100);

        clipPath.addArc(centerX - 280, centerY - 280, centerX + 280, centerY + 280, 0, 360);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(clipPath);
        }

        canvas.drawArc(left, top, right, bottom, 0, 360, true, paint);
    }

    private void drawRotaryCircle() {
        paint.reset();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(250);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawArc(left + 85, top + 85, right - 85, bottom - 85, progressAngle, 270, false, paint);
    }

    private void drawNumbers() {
        float angle = 360f / 12f;
        float startAngle = 45f;

        paint.reset();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        numberPaint.setColor(Color.WHITE);
        numberPaint.setTextSize(90);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setFakeBoldText(true);
        int modifiedRadius = 410;

        for (int i = 0; i < 12; i++) {
            float numberAngle = startAngle + i * angle;
            double radians = Math.toRadians(numberAngle);

            if (i < 10) {
                int x = (int) (centerX + modifiedRadius * Math.cos(radians));
                int y = (int) (centerY + modifiedRadius * Math.sin(radians));

                int c_x = (int) (centerX + modifiedRadius * Math.cos(Math.toRadians(progressAngle + i * angle)));
                int c_y = (int) (centerY + modifiedRadius * Math.sin(Math.toRadians(progressAngle + i * angle)));

                canvas.drawArc(c_x - 75, c_y - 75, c_x + 75, c_y + 75, 0, 360, true, paint);

                Paint.FontMetrics fontMetrics = numberPaint.getFontMetrics();
                float yOffset = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
                y += yOffset;

                canvas.drawText(String.valueOf(i == 0 ? 0 : 10 - i), x, y, numberPaint);
            } else if (i == 11) {
                int x = (int) (centerX + modifiedRadius * Math.cos(radians));
                int y = (int) (centerY + modifiedRadius * Math.sin(radians));

                paint.setColor(Color.WHITE);
                canvas.drawCircle(x, y, 35, paint);
            }
        }
    }

    private void animateBack() {
        ValueAnimator animator = ValueAnimator.ofFloat(progressAngle < 45 ? 180 : progressAngle, 45);
        animator.addUpdateListener(valueAnimator -> {
            progressAngle = (float) animator.getAnimatedValue();
            invalidate();
        });

        animator.start();
    }

    float initialTouchAngle = 0;
    boolean circled = false;
    String touchedNumber = "";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float angle = 360f / 12f;
                float _angle = (float) Math.toDegrees(Math.atan2(touchY - centerY, touchX - centerX));
                _angle = (_angle + 360) % 360;
                initialTouchAngle = _angle;

                for (int i = 0; i < 10; i++) {
                    final double radians = Math.toRadians(_angle + i * angle);
                    int c_x = (int) (centerX + 410 * Math.cos(radians));
                    int c_y = (int) (centerY + 410 * Math.sin(radians));

                    Rect bounds = new Rect(c_x - 75, c_y - 75, c_x + 75, c_y + 75);
                    if (bounds.contains((int) touchX, (int) touchY)) {
                        int _n = (int) (11 - Math.floor(_angle/360 * 12));
                        if(_n > 0 && _n < 11){
                            if(_n == 10){
                                _n = 0;
                            }
                            touchedNumber = String.valueOf(_n);
                            return true;
                        }
                    }
                }
                return false;

            case MotionEvent.ACTION_MOVE:
                float dx = touchX - centerX;
                float dy = touchY - centerY;
                float touchAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
                touchAngle = (touchAngle + 360) % 360;

                progressAngle = touchAngle - initialTouchAngle + 45;
                if(touchAngle >= 350){
                    circled = true;
                }

                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                animateBack();
                if(circled) {
                    number += touchedNumber;
                }
                if(number.length() == 4){
                    // this.onPasscodeEnterListener.onPasscodeEnter(number);
                    number = "";
                }
                touchedNumber = "";
                circled = false;
                initialTouchAngle = 0;
                return true;
        }

        return super.onTouchEvent(event);
    }
}

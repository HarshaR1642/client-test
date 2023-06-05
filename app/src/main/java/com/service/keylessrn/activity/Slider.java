package com.service.keylessrn.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.service.keylessrn.utility.Utility;

public class Slider extends View {
    private float width;
    private float height;
    private float centerX;
    private float centerY;
    private final float radius = 800;
    private final float startAngle = 180;
    private final float sweepAngle = 180;
    private float c_progress = 50;
    private float h_progress = 53;
    private float maxProgress = 100;
    private final Paint arcPaint;
    private final Paint thumbPaint;
    private final RectF arcRect;
    private final Rect coolIconBounds;
    private final Rect heatIconBounds;
    private boolean coolIconTouched = false;
    private boolean heatIconTouched = false;
    Canvas canvas;

    public Slider(Context context, AttributeSet attributeSet) {
        super(context);

        // String customAttribute = attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        arcPaint = new Paint();
        arcPaint.setColor(Color.WHITE);
        arcPaint.setStrokeWidth(0);
        arcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arcPaint.setAntiAlias(true);

        thumbPaint = new Paint();
        thumbPaint.setColor(Color.WHITE);
        thumbPaint.setTextSize(48);

        arcRect = new RectF();
        heatIconBounds = new Rect();
        coolIconBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        setWidthAndHeight(getWidth(), getHeight());
        setCenter();

        drawArc();
        setCool();
        setHeat();
    }

    private void setWidthAndHeight(float width, float height) {
        this.width = width;
        this.height = height;
    }

    private void setCenter() {
        centerX = width / 2;
        centerY = height + 500;
    }

    private void drawArc() {
        float left = centerX - radius;
        float top = centerY - radius;
        float right = centerX + radius;
        float bottom = centerY + radius;

        arcRect.set(left, top, right, bottom);
        this.canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint);
    }

    private void setCool() {
        float angle = startAngle + (c_progress / maxProgress) * sweepAngle;

        float x = (float) (centerX + radius * Math.cos(Math.toRadians(angle)));
        float y = (float) (centerY + radius * Math.sin(Math.toRadians(angle)));

        Drawable thumb = ContextCompat.getDrawable(getContext(), R.drawable.cool_point);
        int iconWidth = Utility.dpToPx(getContext(), 75);
        int iconHeight = Utility.dpToPx(getContext(), 75);
        int left = (int) (x - iconWidth / 2f);
        int top = (int) (y - iconHeight);
        int right = left + iconWidth;
        int bottom = top + iconHeight;

        if (thumb != null) {
            coolIconBounds.set(left, top, right, bottom);
            thumb.setBounds(left, top, right, bottom);
            thumb.draw(canvas);

            int _x = (int) (left + right - 90) / 2;
            int _y = (int) (top + bottom + 30) / 2;
            canvas.drawText(""+(int) c_progress+"°F", _x, _y, thumbPaint);
        }
    }

    private void setHeat() {
        float angle = startAngle + (h_progress / maxProgress) * sweepAngle;

        float x = (float) (centerX + radius * Math.cos(Math.toRadians(angle)));
        float y = (float) (centerY + radius * Math.sin(Math.toRadians(angle)));

        int iconWidth = Utility.dpToPx(getContext(), 75);
        int iconHeight = Utility.dpToPx(getContext(), 75);

        Drawable thumb = ContextCompat.getDrawable(getContext(), R.drawable.heat_point);
        int left = (int) (x - iconWidth / 2f);
        int top = (int) (y - iconHeight);
        int right = left + iconWidth;
        int bottom = top + iconHeight;

        if (thumb != null) {
            thumb.setBounds(left, top, right, bottom);
            heatIconBounds.set(left, top, right, bottom);
            thumb.draw(canvas);

            int _x = (int) (left + right - 90) / 2;
            int _y = (int) (top + bottom + 30) / 2;
            canvas.drawText(""+(int) h_progress+"°F", _x, _y, thumbPaint);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (heatIconBounds.contains((int) touchX, (int) touchY)) {
                    heatIconTouched = true;
                } else if (coolIconBounds.contains((int) touchX, (int) touchY)) {
                    coolIconTouched = true;
                }

                return (coolIconTouched || heatIconTouched);

            case MotionEvent.ACTION_MOVE:
                float dx = touchX - centerX;
                float dy = touchY - centerY;
                float touchAngle = (float) Math.toDegrees(Math.atan2(dy, dx));
                touchAngle = (touchAngle + 360) % 360;

                if (touchAngle < 225) {
                    touchAngle = 225;
                }

                if (touchAngle > 315) {
                    touchAngle = 315;
                }

                float normalizedAngle = (touchAngle - startAngle + 360) % 360;
                float progressAngle = (normalizedAngle / sweepAngle) * maxProgress;
                if (coolIconTouched) {
                    c_progress = progressAngle;
                } else if (heatIconTouched) {
                    h_progress = progressAngle;
                }

                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                coolIconTouched = false;
                heatIconTouched = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

    public void setProgress(float progress) {
        this.c_progress = progress;
        this.h_progress = progress;
        invalidate();
    }

    public float getProgress() {
        return c_progress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    public float getMaxProgress() {
        return maxProgress;
    }
}

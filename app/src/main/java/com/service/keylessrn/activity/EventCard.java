package com.service.keylessrn.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class EventCard extends View {
    Paint paint;
    Canvas canvas;
    int centerX;
    int centerY;
    RectF rectangle;
    Path path;

    public EventCard(Context context) {
        super(context);
    }

    public EventCard(Context context, @Nullable AttributeSet attrs) {
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
        drawCard();
        drawYellowArc();
        drawBlueArc();
    }

    private void setCoordinates() {
        this.centerX = getWidth() / 2;
        this.centerY = getHeight() / 2;
    }

    private void drawCard() {
        int left = centerX - 450;
        int top = centerY - 300;
        int right = centerX + 450;
        int bottom = centerY + 300;

        rectangle.set(left, top, right, bottom);

        paint.setARGB(1000, 255, 127, 80);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectangle, 10, 10, paint);
    }

    private void drawYellowArc() {

        int l = -450;
        int b = -300;
        int left = centerX - 200 + l;
        int top = centerY - 200 + b;
        int right = centerX + 200 + l;
        int bottom = centerY + 200 + b;

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);
        paint.setColor(Color.YELLOW);

        canvas.drawArc(left, top, right, bottom, 0, 90, false, paint);
    }

    private void drawBlueArc() {

        int l = -450;
        int b = -300;
        int left = centerX - 350 + l+50;
        int top = centerY - 350 + b;
        int right = centerX + 350 + l-50;
        int bottom = centerY + 350 + b;

        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(100);
        paint.setARGB(1000, 135, 206, 235);

        canvas.drawArc(left, top, right, bottom, 0, 90, false, paint);
    }

}

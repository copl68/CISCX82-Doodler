package com.example.doodler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Stack;

public class DoodleView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private float X;
    private float Y;
    private Path path;
    private Paint paint;
    private ArrayList<Stroke> paths = new ArrayList<Stroke>();
    private int color;
    private int size;
    private int opacity;
    private ArrayList<Stroke> undo_stack = new ArrayList<Stroke>();
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint bitmapPaint = new Paint();

    public DoodleView(Context context) {
        this(context, null);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAlpha(255);
        paint.setStrokeWidth(10);
    }

    public void init(int height, int width) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        color = Color.BLACK;
        size = 10;
        opacity = 255;
    }

    public void setColor(int c) {
        color = c;
    }

    public void setSize(int s) {
        size = s;
    }

    public void setOpacity(int o){
        opacity = o;
    }

    public void clear(){
        paths.clear();
        invalidate();
    }

    public void undo(){
        if(paths.size() > 0) {
            Stroke s = paths.remove(paths.size() - 1);
            undo_stack.add(s);
            invalidate();
        }
    }

    public void redo(){
        if(undo_stack.size() > 0) {
            Stroke s = undo_stack.remove(undo_stack.size() - 1);
            paths.add(s);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas c) {
        // save the current state of the canvas before,
        // to draw the background of the canvas
        c.save();

        canvas.drawColor(Color.WHITE);

        // now, we iterate over the list of paths
        // and draw each path on the canvas
        for (Stroke s : paths) {
            paint.setColor(s.color);
            paint.setStrokeWidth(s.size);
            paint.setAlpha(s.opacity);
            canvas.drawPath(s.path, paint);
        }
        c.drawBitmap(bitmap, 0, 0, bitmapPaint);
        c.restore();
    }

    // the below methods manages the touch
    // response of the user on the screen
    // firstly, we create a new Stroke
    // and add it to the paths list
    private void touchStart(float x_loc, float y_loc) {
        path = new Path();
        Stroke s = new Stroke(color, size, opacity, path);
        paths.add(s);

        // finally remove any curve
        // or line from the path
        path.reset();

        // this methods sets the starting
        // point of the line being drawn
        path.moveTo(x_loc, y_loc);

        // we save the current
        // coordinates of the finger
        X = x_loc;
        Y = y_loc;
    }

    // in this method we check
    // if the move of finger on the
    // screen is greater than the
    // Tolerance we have previously defined,
    // then we call the quadTo() method which
    // actually smooths the turns we create,
    // by calculating the mean position between
    // the previous position and current position
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - X);
        float dy = Math.abs(y - Y);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(X, Y, (x + X) / 2, (y + Y) / 2);
            X = x;
            Y = y;
        }
    }

    // at the end, we call the lineTo method
    // which simply draws the line until
    // the end position
    private void touchUp() {
        path.lineTo(X, Y);
        undo_stack.clear();
    }

    // the onTouchEvent() method provides us with
    // the information about the type of motion
    // which has been taken place, and according
    // to that we call our desired methods
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
        }
        return true;
    }
}

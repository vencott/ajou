package com.ajou.android.lab5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private int selectedColor;
    private Path mPath;
    private ArrayList<ColoredPath> mColoredPathList;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

        selectedColor = Color.BLACK;
        mPath = new Path();
        mColoredPathList = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    public void clearCanvas() {
        mPath.reset();
        if(!mColoredPathList.isEmpty())
            mColoredPathList.clear();

        mCanvas.drawColor(Color.WHITE);

        invalidate();
        Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show();
    }

    public void setPenColor(int color) {
        mPaint.setColor(color);
        selectedColor = color;
    }

    public void undo() {
        if(!mColoredPathList.isEmpty())
            mColoredPathList.remove(mColoredPathList.size()-1);

        mCanvas.drawColor(Color.WHITE);
        for(ColoredPath coloredPath : mColoredPathList) {
            mPaint.setColor(coloredPath.getColor());
            mCanvas.drawPath(coloredPath.getPath(), mPaint);
        }

        mPaint.setColor(selectedColor);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private void startTouch(float x, float y) {
        mX = x;
        mY = y;
        mPath.moveTo(mX, mY);
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mX = x;
            mY = y;
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
        }
    }

    private void upTouch() {
        ColoredPath coloredPath = new ColoredPath(mPath, mPaint.getColor());
        mColoredPathList.add(coloredPath);
        mPath = new Path();
    }
}
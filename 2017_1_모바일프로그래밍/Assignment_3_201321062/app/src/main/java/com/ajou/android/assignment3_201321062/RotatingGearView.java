package com.ajou.android.assignment3_201321062;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class RotatingGearView extends View {

    public static final int ROTATE_VALUE = 10;
    public static final int DELAY = 1000;

    private Bitmap originalGear1 = BitmapFactory.decodeResource(getResources(), R.drawable.gear1);
    private Bitmap originalGear2 = BitmapFactory.decodeResource(getResources(), R.drawable.gear2);
    private List<Bitmap> gear1Bitmaps = new ArrayList<>();
    private List<Bitmap> gear2Bitmaps = new ArrayList<>();

    private ValueAnimator mValueAnimator = ValueAnimator.ofInt(0, 35);

    private Matrix mMatrix = new Matrix();

    private boolean ccw = false;
    private int gear1_Index = 0;
    private int gear2_Index = 0;
    private int gear1_width = originalGear1.getWidth();
    private int gear1_height = originalGear1.getHeight();
    private int gear2_width = originalGear2.getWidth();
    private int gear2_height = originalGear2.getHeight();

    public RotatingGearView(Context context) {
        super(context);
        createBitmaps();
    }

    public RotatingGearView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createBitmaps();
    }

    public RotatingGearView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createBitmaps();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int gear1_current_width = this.getWidth() / 2 - gear1Bitmaps.get(gear1_Index).getWidth() / 2;
        int gear1_current_height = gear1Bitmaps.get(0).getHeight() / 2 - gear1Bitmaps.get(gear1_Index).getHeight() / 2;
        int gear2_current_width = this.getWidth() / 2 - gear2Bitmaps.get(gear2_Index).getWidth() / 2;
        int gear2_current_height = gear1_height - 50 + gear2Bitmaps.get(0).getHeight() / 2 - gear2Bitmaps.get(gear2_Index).getHeight() / 2;

        canvas.drawBitmap(gear1Bitmaps.get(gear1_Index), gear1_current_width, gear1_current_height, null);
        canvas.drawBitmap(gear2Bitmaps.get(gear2_Index), gear2_current_width, gear2_current_height, null);
    }

    private void createBitmaps() {
        for (int i = 0; i < 36; i++) {
            gear1Bitmaps.add(rotate(originalGear1, ROTATE_VALUE * i, gear1_width, gear1_height));
            gear2Bitmaps.add(rotate(originalGear2, ROTATE_VALUE * i, gear2_width, gear2_height));
        }
    }

    private Bitmap rotate(Bitmap originBitmap, int nRotate, int width, int height) {
        mMatrix.setRotate(nRotate, originBitmap.getWidth() / 2, originBitmap.getHeight() / 2);

        Bitmap rotatedBitmap = Bitmap.createBitmap(originBitmap, 0, 0, width, height, mMatrix, true);
        return rotatedBitmap;
    }

    public void startAnimation() {
        mValueAnimator.setDuration(DELAY);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ObjectAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (ccw) {
                    gear1_Index = (int) animation.getAnimatedValue();
                    gear2_Index = 35 - gear1_Index;
                } else {
                    gear2_Index = (int) animation.getAnimatedValue();
                    gear1_Index = 35 - gear2_Index;
                }
                invalidate();
            }
        });
        mValueAnimator.start();
    }

    public void setCCW(Boolean ccw) {
        this.ccw = ccw;
    }
}
package com.existing.boilerx.common.widgets;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.animation.LinearInterpolator;

/**
 * Created by「 The Khaeng 」on 26 Mar 2018 :)
 */

public class LoaderController implements ValueAnimator.AnimatorUpdateListener {

    public final static int COLOR_DEFAULT_GREY = Color.rgb(215, 215, 215);
    public final static int COLOR_DARKER_GREY = Color.rgb(180, 180, 180);
    public final static int COLOR_DEFAULT_GRADIENT = Color.rgb(245, 245, 245);
    public final static float MIN_WEIGHT = 0.0f;
    public final static float MAX_WEIGHT = 1.0f;
    public final static int CORNER_DEFAULT = 0;
    public final static boolean USE_GRADIENT_DEFAULT = false;
    public final static int MAX_COLOR_CONSTANT_VALUE = 255;
    public final static int ANIMATION_CYCLE_DURATION = 500; //milis
    public final static int ANIMATION_STOP_DURATION = 0; //milis


    public interface LoaderView {
        void setRectColor( Paint rectPaint );

        void invalidate();

        boolean valueSet();
    }

    private LoaderView loaderView;
    private Paint rectPaint;
    private LinearGradient linearGradient;
    private float progress;
    private ValueAnimator valueAnimator;
    private float widthWeight = MAX_WEIGHT;
    private float heightWeight = MAX_WEIGHT;
    private boolean useGradient = USE_GRADIENT_DEFAULT;
    private int corners = CORNER_DEFAULT;
    private boolean isEnabled = true;

    public LoaderController( LoaderView view) {
        loaderView = view;
        init();
    }

    public LoaderController( LoaderView view, boolean isEnabled) {
        this.loaderView = view;
        this.isEnabled = isEnabled;
        init();
    }

    private void init() {
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        loaderView.setRectColor(rectPaint);
        setValueAnimator(0.20f, 0.25f, ObjectAnimator.INFINITE, ANIMATION_CYCLE_DURATION);
    }

    public void onDraw(Canvas canvas) {
        onDraw(canvas, 0, 0, 0, 0);
    }

    public void onDraw(Canvas canvas, float leftPad, float topPad, float rightPad, float bottomPad) {
        if (isEnabled) {
            float marginHeight = canvas.getHeight() * (1 - heightWeight) / 2;
            rectPaint.setAlpha((int) (progress * MAX_COLOR_CONSTANT_VALUE));
            if (useGradient) {
                prepareGradient(canvas.getWidth() * widthWeight);
            }
            canvas.drawRoundRect(new RectF(0 + leftPad,
                            marginHeight + topPad + ((canvas.getHeight() - marginHeight - bottomPad) * 0.125f),
                            canvas.getWidth() * widthWeight - rightPad,
                            (canvas.getHeight() - marginHeight - bottomPad) * 1f),
                    corners, corners,
                    rectPaint);
        }
    }

    public void onSizeChanged() {
        if (isEnabled) {
            linearGradient = null;
            startLoading();
        }
    }

    private void prepareGradient(float width) {
        if (linearGradient == null) {
            linearGradient = new LinearGradient(0, 0, width, 0, rectPaint.getColor(),
                    COLOR_DEFAULT_GRADIENT, Shader.TileMode.MIRROR);
        }
        rectPaint.setShader(linearGradient);
    }

    public void startLoading() {
        if (isEnabled) {
            if (valueAnimator != null && !loaderView.valueSet()) {
                valueAnimator.cancel();
                init();
                valueAnimator.start();
            }
        }
    }

    public void setHeightWeight(float heightWeight) {
        this.heightWeight = validateWeight(heightWeight);
    }

    public void setWidthWeight(float widthWeight) {
        this.widthWeight = validateWeight(widthWeight);
    }

    public void setUseGradient(boolean useGradient) {
        this.useGradient = useGradient;
    }

    public void setCorners(int corners) {
        this.corners = corners;
    }

    private float validateWeight(float weight) {
        if (weight > MAX_WEIGHT)
            return MAX_WEIGHT;
        if (weight < MIN_WEIGHT)
            return MIN_WEIGHT;
        return weight;
    }

    public void stopLoading() {
        if (isEnabled) {
            if (valueAnimator != null) {
                valueAnimator.cancel();
                setValueAnimator(progress, 0, 0,ANIMATION_STOP_DURATION);
                valueAnimator.start();
            }
        }
    }


    private void setValueAnimator(float begin, float end, int repeatCount, int duration) {
        valueAnimator = ValueAnimator.ofFloat(begin, end);
        valueAnimator.setRepeatCount(repeatCount);
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(this);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        progress = (float) valueAnimator.getAnimatedValue();
        loaderView.invalidate();
    }

    public void removeAnimatorUpdateListener() {
        if (valueAnimator != null) {
            valueAnimator.removeUpdateListener(this);
            valueAnimator.cancel();
        }
    }
}

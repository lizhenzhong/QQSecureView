package org.lzz.qqsecureview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class QQSecureView extends View {
    private Paint mGradientPaint;
    private Paint mArcPaint;
    private Paint mBackPaint;
    private Paint mTrianglePaint;
    private Paint mLinePaint;
    private Paint mBitmapPaint;
    private Paint mLightPaint;

    private float mRadius;
    private float mBackRadius;

    private int width, height;

    private int mGradientStrokeWidth = 50;
    private int mArcStrokeWidth = mGradientStrokeWidth / 2;

    private SweepGradient sweepGradient;//扫描渲染
    private LinearGradient linearGradient;//线性渲染
    private RadialGradient radialGradient;//环形渲染

    private RectF mGradientRectF;
    private RectF mBorderRectF;
    private Rect mBitmapRect;

    private Path mPath;
    private Path mLinePath1;
    private Path mLinePath2;
    private Path mLinePath3;

    private Bitmap bitmap;
    private ValueAnimator valueAnimator;

    public QQSecureView(Context context) {
        super(context);
        init();
    }

    public QQSecureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QQSecureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGradientPaint = new Paint();
        mGradientPaint.setDither(true);
        mGradientPaint.setAntiAlias(true);
        mGradientPaint.setStyle(Paint.Style.FILL);
        mGradientPaint.setStrokeWidth(mGradientStrokeWidth);

        mArcPaint = new Paint();
        mArcPaint.setDither(true);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.argb(160, 255, 255, 255));
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcStrokeWidth);

        mBackPaint = new Paint();
        mBackPaint.setDither(true);
        mBackPaint.setAntiAlias(true);
        mBackPaint.setColor(Color.argb(25, 0, 0, 0));
        mBackPaint.setStyle(Paint.Style.FILL);

        mTrianglePaint = new Paint();
        mTrianglePaint.setDither(true);
        mTrianglePaint.setAntiAlias(true);
        mTrianglePaint.setStrokeWidth(1);
        mTrianglePaint.setColor(Color.argb(80, 255, 255, 255));
        mTrianglePaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint();
        mLinePaint.setDither(true);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.argb(80, 255, 255, 255));
        mLinePaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{10, 5, 10, 5}, 1);
        mLinePaint.setPathEffect(effects);

        mBitmapPaint = new Paint();
        mBitmapPaint.setDither(true);
        mBitmapPaint.setAntiAlias(true);

        mLightPaint = new Paint();
        mLightPaint.setDither(true);
        mLightPaint.setAntiAlias(true);
        mLightPaint.setStrokeWidth(2);
        mLightPaint.setColor(Color.WHITE);
        mLightPaint.setStyle(Paint.Style.STROKE);
        mLightPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        mLinePath1 = new Path();
        mLinePath2 = new Path();
        mLinePath3 = new Path();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.u1);

        valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1000);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (isBreath) {
                    ++step;
                    if (step == 2) {
                        isBreath = false;
                    }
                } else {
                    step = 0;
                    isBreath = true;
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        mRadius = width / 2f;
        mBackRadius = mRadius - mGradientStrokeWidth / 4f - mArcStrokeWidth * 2;

        //环形渲染
        if (radialGradient == null) {
            radialGradient = new RadialGradient(width / 2, height / 2, mRadius, new int[]{Color.TRANSPARENT, Color.argb(20, 255, 255, 255), Color.argb(255, 255, 255, 255), Color.argb(20, 255, 255, 255)},
                    new float[]{0f, 1 - (mGradientStrokeWidth / mRadius), 1 - (mGradientStrokeWidth / (2 * mRadius)), 1f}, Shader.TileMode.CLAMP);
            mGradientPaint.setShader(radialGradient);
        }

        if (mGradientRectF == null) {
            mGradientRectF = new RectF(0, 0, width, height);
        }

        if (mBorderRectF == null) {
            mBorderRectF = new RectF(mGradientStrokeWidth / 4f + mArcStrokeWidth / 2f, mGradientStrokeWidth / 4f + mArcStrokeWidth / 2f, width - mGradientStrokeWidth / 4f - mArcStrokeWidth / 2f, height - mGradientStrokeWidth / 4f - mArcStrokeWidth / 2f);
        }

        sweepAngle = (360 - 3 * gapAngle) / 3f;
        gradientSweepAngle = (360 - 3 * gradientGapAngle) / 3f;

        mPath.moveTo(width / 2, height / 2 - mBackRadius);
        mPath.lineTo((float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
        mPath.lineTo((float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
        mPath.close();

        mLinePath1.moveTo(width / 2, height / 2);
        mLinePath1.lineTo(width / 2, height / 2 - mBackRadius);

        mLinePath2.moveTo(width / 2, height / 2);
        mLinePath2.lineTo((float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);

        mLinePath3.moveTo(width / 2, height / 2);
        mLinePath3.lineTo((float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);

        if (mBitmapRect == null) {
            float rectX1 = (float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius / 2);
            float rectX2 = (float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius / 2);
            float rectY2 = height / 2 + mBackRadius / 2f;
            float rectY1 = rectY2 - (rectX2 - rectX1) * 322 / 284f;
            mBitmapRect = new Rect((int) rectX1, (int) rectY1, (int) rectX2, (int) rectY2);
        }
    }

    private float sweepAngle;
    private float gapAngle = 3f;

    private float gradientSweepAngle;
    private float gradientGapAngle = 0.1f;

    private int step;
    private int lightLen = 10;

    private float currentValue;
    private boolean isBreath;

    @Override
    protected void onDraw(Canvas canvas) {
        valueAnimator.setRepeatMode(isBreath ? ValueAnimator.REVERSE : ValueAnimator.RESTART);

        if (isBreath) {
            canvas.save();
            int aph = (int) (255 * currentValue);
            mGradientPaint.setAlpha(aph);//通过改变透明度来实现渐变环呼吸的动画效果
            for (int i = 0; i < 3; i++) {
                canvas.drawArc(mGradientRectF, gradientGapAngle / 2 - 90 + (gradientSweepAngle + gradientGapAngle) * i, gradientSweepAngle, true, mGradientPaint);
            }
            canvas.restore();
        }

        for (int i = 0; i < 3; i++) {
            canvas.drawArc(mBorderRectF, gapAngle / 2 - 90 + (sweepAngle + gapAngle) * i, sweepAngle, false, mArcPaint);
        }

        canvas.drawCircle(width / 2, height / 2, mBackRadius, mBackPaint);
        canvas.drawPath(mPath, mTrianglePaint);

        canvas.drawPath(mLinePath1, mLinePaint);
        canvas.drawPath(mLinePath2, mLinePaint);
        canvas.drawPath(mLinePath3, mLinePaint);

        if (!isBreath) {
            canvas.drawLine(width / 2, height / 2 - mBackRadius * currentValue - lightLen, width / 2, height / 2 - mBackRadius * currentValue, mLightPaint);

            canvas.save();
            canvas.rotate(120, width / 2, height / 2);
            canvas.drawLine(width / 2, height / 2 - mBackRadius * currentValue - lightLen, width / 2, height / 2 - mBackRadius * currentValue, mLightPaint);
            canvas.restore();

            canvas.save();
            canvas.rotate(240, width / 2, height / 2);
            canvas.drawLine(width / 2, height / 2 - mBackRadius * currentValue - lightLen, width / 2, height / 2 - mBackRadius * currentValue, mLightPaint);
            canvas.restore();

            canvas.drawLine((float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * currentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * currentValue) - lightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.drawLine((float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * currentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * currentValue) + lightLen, height / 2 + mBackRadius / 2f, mLightPaint);

            canvas.save();
            canvas.rotate(-60, (float) (width / 2 - Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
            canvas.drawLine((float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * currentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * currentValue) - lightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.drawLine((float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * currentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * currentValue) + lightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.restore();

            canvas.save();
            canvas.rotate(60, (float) (width / 2 + Math.cos(Math.toRadians(30)) * mBackRadius), height / 2 + mBackRadius / 2f);
            canvas.drawLine((float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * currentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 - mBackRadius * Math.sin(Math.toRadians(60)) * currentValue) - lightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.drawLine((float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * currentValue), height / 2 + mBackRadius / 2f, (float) (width / 2 + mBackRadius * Math.sin(Math.toRadians(60)) * currentValue) + lightLen, height / 2 + mBackRadius / 2f, mLightPaint);
            canvas.restore();
        }

        canvas.drawBitmap(bitmap, null, mBitmapRect, mBitmapPaint);

        startAnimator();
    }

    @Override
    protected void onDetachedFromWindow() {
        valueAnimator.cancel();
        super.onDetachedFromWindow();
    }

    private void startAnimator() {
        if (!valueAnimator.isStarted()) {
            valueAnimator.start();
        }
    }
}

package com.weilongzhang.coder.zhangwl.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by apolunor on 16/4/18.
 * @deprecated 可以在外面使用属性动画0.0f-1.0f然后根据返回的value值去出来百分比,然后设置值,方法内会重画
 */
public class ProgressCircleView2 extends View {

    private float mRimWidth = 1.5f;
    private float mBarWidth = 4f;

    private int mCenterX;
    private int mCenterY;

    private Paint mRimPaint;
    private Paint mBarPaint;

    private RectF mBounds;
    private int mRadius;

    private int mRimColor = 0xFF00AAEF;
    private int mBarColor = 0xFF00AAEF;

    private int mAccuracy = 40;

    public ProgressCircleView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mRimWidth = dip2px(context, mRimWidth);
        mBarWidth = dip2px(context, mBarWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setupBounds(w, h);
        setupPaints();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.rotate(-135, mCenterX, mCenterY);
        canvas.drawArc(mBounds, -90, 270, false, mRimPaint);
        canvas.drawArc(mBounds, -90, 270 * mAccuracy / 100.0f, false, mBarPaint);
        canvas.restore();
    }

    private void setupBounds(int width, int height) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int minValue = Math.min(width - paddingLeft - paddingRight,
                height - paddingBottom - paddingTop);

        int adjust = (int) Math.max(mRimWidth, mBarWidth) >> 1;
        mBounds = new RectF(paddingLeft + adjust, paddingTop + adjust,
                minValue - paddingRight - adjust, minValue - paddingBottom - adjust);

        int diameter = minValue - paddingRight - paddingLeft - adjust * 2;
        mRadius = diameter >> 1;
        mCenterX = paddingTop + adjust + mRadius;
        mCenterY = paddingLeft + adjust + mRadius;
    }

    private void setupPaints() {
        mRimPaint = new Paint();
        mRimPaint.setColor(mRimColor);
        mRimPaint.setAntiAlias(true);
        mRimPaint.setStyle(Paint.Style.STROKE);
        mRimPaint.setStrokeWidth(mRimWidth);
        mRimPaint.setStrokeCap(Paint.Cap.ROUND);

        mBarPaint = new Paint();
        mBarPaint.setColor(mBarColor);
        mBarPaint.setAntiAlias(true);
        mBarPaint.setStyle(Paint.Style.STROKE);
        mBarPaint.setStrokeWidth(mBarWidth);
        mBarPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setAccuracy(int accuracy) {
        mAccuracy = accuracy;

        invalidate();
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}

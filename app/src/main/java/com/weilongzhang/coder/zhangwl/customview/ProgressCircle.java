package com.weilongzhang.coder.zhangwl.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.weilongzhang.coder.zhangwl.R;

/**
 * Created by weilongzhang on 16/3/24.
 * 带有圆形进度条的圆
 */
public class ProgressCircle extends View {

    //画笔对象的引用
    private Paint bgCirclePaint;
    private Paint ftCirclePaint;
    private Paint textPaint;
    private Paint ftCirclePaintArrow;
    //圆环的颜色
    private int roundColor;
    //圆环进度的颜色
    private int roundProgressColor;
    //中间进度百分比的字符串的颜色
    private int textColor;
    //中间进度百分比的字符串的字体
    private float textSize;
    //圆环的宽度
    private float backgroundRoundWidth;
    private float frontRoundWidth;
    //最大进度
    private int max;
    //当前进度
    private int progress;
    //是否显示中间的进度
    private boolean textIsDisplayable;
    //开始的角度和转过多少角度
    private float startAngle;
    private float sweepAngle;
    //进度的风格，实心或者空心
    private int style;
    public static final int STROKE = 0;
    public static final int FILL = 1;
    //圆环或者扇形
    private int shape;
    public static final int CIRCLE = 1;
    public static final int ARC = 0;

    private String percentValue;
    private int startValue=0;

    private boolean canAnimation;
    private int defaultProgress;

    private OnProgressChangeListener onProgressChangeListener;

    public ProgressCircle(Context context) {
        this(context, null);
    }

    public ProgressCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        backgroundRoundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_backgroundRoundWidth, 6);
        frontRoundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_frontRoundWidth,10);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max,100 );
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
        startAngle = mTypedArray.getFloat(R.styleable.RoundProgressBar_startAngle,90f);
        sweepAngle = mTypedArray.getFloat(R.styleable.RoundProgressBar_sweepAngle,360f);
        shape = mTypedArray.getInt(R.styleable.RoundProgressBar_shape,0);
        canAnimation = mTypedArray.getBoolean(R.styleable.RoundProgressBar_animation,false);

        bgCirclePaint = new Paint();
        ftCirclePaint = new Paint();
        textPaint = new Paint();
        ftCirclePaintArrow = new Paint();
        //文字画笔
        textPaint.setStrokeWidth(0);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
        //背景圆画笔
        bgCirclePaint.setColor(roundColor); //设置圆环的颜色
        bgCirclePaint.setStyle(Paint.Style.STROKE); //设置空心
        bgCirclePaint.setStrokeWidth(backgroundRoundWidth); //设置圆环的宽度
        bgCirclePaint.setAntiAlias(true);  //消除锯齿
        //进度圆画笔
        ftCirclePaint.setStrokeWidth(frontRoundWidth); //设置圆环的宽度
        ftCirclePaint.setColor(roundProgressColor);  //设置进度的颜色
        ftCirclePaint.setAntiAlias(true);

        ftCirclePaintArrow.setAntiAlias(true);
        ftCirclePaintArrow.setColor(roundProgressColor);
        ftCirclePaintArrow.setStyle(Paint.Style.FILL);
        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canAnimation){
            progress = startValue;
        }
        //画最外层的大圆环
        int center = getWidth() / 2; //获取圆心的x坐标
        int backgroundRadius = (int) (center - frontRoundWidth / 2); //圆环的半径,-2：进度圆环比底部圆环大一些
        if (shape == CIRCLE) {
            canvas.drawCircle(center, center, backgroundRadius, bgCirclePaint); //画出圆环
        } else if (shape == ARC) {
            RectF rectF = new RectF();
            rectF.set(frontRoundWidth / 2, frontRoundWidth / 2, getWidth() - frontRoundWidth / 2, getHeight() - frontRoundWidth / 2);
            canvas.drawArc(rectF, startAngle, sweepAngle, false, bgCirclePaint);
        }
        //画中间的文字
        int percent = (int) (((float) progress / (float) max) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        if (textIsDisplayable) {
            percentValue = percent + "%";
            if (onProgressChangeListener!=null){
                if(!TextUtils.isEmpty(onProgressChangeListener.modifyValue(percentValue))){
                    percentValue = onProgressChangeListener.modifyValue(percentValue);
                }
            }
            canvas.drawText(percentValue, (getWidth() - textPaint.measureText(percentValue)) / 2.0f, (getHeight() - (textPaint.descent() + textPaint.ascent())) / 2.0f, textPaint);
            if (canAnimation) {
                if (onProgressChangeListener != null) {
                    onProgressChangeListener.progressChanging(percentValue);
                }
            }

        }
        //画进度
        RectF oval = new RectF(frontRoundWidth / 2,frontRoundWidth / 2,getWidth()-frontRoundWidth/2,getHeight()-frontRoundWidth/2);  //用于定义的圆弧的形状和大小的界限
        switch (style) {
            case STROKE: {
                ftCirclePaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, startAngle, sweepAngle* progress / max, false, ftCirclePaint);  //根据进度画圆弧
                break;
            }
            case FILL: {
                ftCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, startAngle,sweepAngle * progress / max, true, ftCirclePaint);  //根据进度画圆弧
                break;
            }
        }
        //进度条最前面和最后面的圆形图（sin和cos里面是π值，需要转化）
        int angle = (int) (startAngle + sweepAngle * progress/max);
        int frontSmallCircleY = (int) ((center - frontRoundWidth/2)*Math.sin(Math.PI/180 * angle));
        int frontSmallCircleX = (int) ((center - frontRoundWidth/2)*Math.cos(Math.PI/180 * angle));
        int backSmallCircleX = (int) ((center - frontRoundWidth/2)*Math.cos(Math.PI/180 * startAngle));
        int backSmallCircleY = (int) ((center - frontRoundWidth/2)*Math.sin(Math.PI/180 * startAngle));
        //画布移动，然后画进度条最前面的圆和最后面的圆，画完之后恢复画布
        canvas.translate(center,center);
        canvas.drawCircle(frontSmallCircleX,frontSmallCircleY,frontRoundWidth/2,ftCirclePaintArrow);
        canvas.drawCircle(backSmallCircleX,backSmallCircleY,frontRoundWidth/2,ftCirclePaintArrow);
        canvas.translate(-center,-center);

        if (canAnimation){
            if (startValue >= defaultProgress){
                if (onProgressChangeListener!=null){
                    onProgressChangeListener.onEndProgressChange(percentValue);
                }
            }else {
                startValue++;
                postInvalidateDelayed(10);
            }
        }
    }

    public synchronized int getMax() {
        return max;
    }
    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }
    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }
    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * <p/>
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.defaultProgress = progress;
            this.progress = progress;
            this.startValue = 0;
            postInvalidate();
        }
    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getBackgroundRoundWidth() {
        return backgroundRoundWidth;
    }

    public void setBackgroundRoundWidth(float backgroundRoundWidth) {
        this.backgroundRoundWidth = backgroundRoundWidth;
    }


    public static abstract class OnProgressChangeListener{
        public abstract void progressChanging(String percent);
        public  void onStartProgressChange(){

        }
        public abstract void onEndProgressChange(String endValue);
        public String modifyValue(String value){
            return value;
        }
    }
    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener){
        this.onProgressChangeListener = onProgressChangeListener;
    }
    public void setCanAnimation(boolean canAnimation){
        this.canAnimation = canAnimation;
        postInvalidate();
    }

}

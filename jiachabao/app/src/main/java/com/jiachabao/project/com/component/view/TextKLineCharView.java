package com.jiachabao.project.com.component.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.jiachabao.project.com.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loki on 2017/7/10.
 */

public class TextKLineCharView extends ScrollAndScaleView {
    public TextKLineCharView(Context context) {
        super(context);
        init();
    }

    public TextKLineCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextKLineCharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //x轴的偏移量
    protected float mTranslateX = Float.MIN_VALUE;
    private float mOverScrollRange = 0;
    //显示区域中X开始点在数组的位置
    protected int mStartIndex = 0;
    //显示区域中X结束点在数组的位置
    protected int mStopIndex = 0;
    private List<Float> mXs=new ArrayList<Float>();
    private int mItemCount=100;
    private int mWidth=0;
    private float columnSpace=20;
    private float mDataLen;
    //长按之后选择的点的序号
    protected int mSelectedIndex;

    private void init() {
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mOverScrollRange= DisplayUtil.dip2px(getContext(),100);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth=w;
        if (mItemCount != 0) {
            mXs.clear();
            for (int i = 0; i <= mItemCount; i++) {
                float x=columnSpace * i;
                mXs.add(x);
            }
            mDataLen = (mItemCount - 1) * columnSpace;
        }
        checkAndFixScrollX();
        mTranslateX = mScrollX + (-mDataLen + mWidth / mScaleX - columnSpace / 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mGridPaint.setColor(Color.RED);
        Log.d("测试K","*----------------:"+xToTranslateX(0)+"-----------:"+xToTranslateX(mWidth));
        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(mWidth));

        canvas.save();
        canvas.translate(mTranslateX * mScaleX, 0);
        canvas.scale(mScaleX, 1);

        Log.d("测试K","----------------mStartIndex:"+mStartIndex+"-----------mStopIndex:"+mStopIndex);
        for (int i = mStartIndex; i <= mStopIndex; i++) {
            float currentPointX = getX(i);
            float lastX = i == 0 ? currentPointX : getX(i - 1);
            canvas.drawLine(lastX, 45, lastX, 50, mGridPaint);
            canvas.drawText(String.valueOf(i), lastX, 70, mGridPaint);
        }
        canvas.restore();
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        int lastIndex = mSelectedIndex;
        Log.d("测试K","----------------e.getX():"+e.getX());
        calculateSelectedX(e.getX());
        if (lastIndex != mSelectedIndex) {
            //onSelectedChanged(this, getItem(mSelectedIndex), mSelectedIndex);
        }
        invalidate();
    }

    private void calculateSelectedX(float x) {
        mSelectedIndex = indexOfTranslateX(xToTranslateX(x));
        if (mSelectedIndex < mStartIndex) {
            mSelectedIndex = mStartIndex;
        }
        if (mSelectedIndex > mStopIndex) {
            mSelectedIndex = mStopIndex;
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mTranslateX = mScrollX + (-mDataLen + mWidth / mScaleX - columnSpace / 2);
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        checkAndFixScrollX();
        mTranslateX = mScrollX + (-mDataLen + mWidth / mScaleX - columnSpace / 2);
        super.onScaleChanged(scale, oldScale);
    }

    public int indexOfTranslateX(float translateX) {
        return indexOfTranslateX(translateX, 0, mItemCount - 1);
    }

    public float xToTranslateX(float x) {
        Log.d("测试K",mScrollX+":----------------mScaleX:"+mScaleX);
        //return -mTranslateX + x / mScrollX;
        return -mTranslateX + x / mScaleX;
    }

    /**
     * 二分查找当前值的index
     * @param translateX
     * @return
     */
    public int indexOfTranslateX(float translateX, int start, int end) {
        if (end == start) {
            return start;
        }
        if (end - start == 1) {
            float startValue = getX(start);
            float endValue = getX(end);
            return Math.abs(translateX - startValue) < Math.abs(translateX - endValue) ? start : end;
        }
        int mid = start + (end - start) / 2;
        float midValue = getX(mid);
        if (translateX < midValue) {
            return indexOfTranslateX(translateX, start, mid);
        } else if (translateX > midValue) {
            return indexOfTranslateX(translateX, mid, end);
        } else {
            return mid;
        }
    }

    public float getX(int position) {
        return mXs.get(position);
    }


    @Override
    public void onLeftSide() {
        //Log.d("测试K","--------------------onLeftSide:");
    }

    @Override
    public void onRightSide() {
        //Log.d("测试K","--------------------onRightSide:");
    }

    @Override
    public int getMinScrollX() {
        //Log.d("测试K","--------------------getMinScrollX:"+mScaleX);
        return -20;
    }

    @Override
    public int getMaxScrollX() {
        //Log.d("测试K","--------------------getMaxScrollX:"+mScaleX);
        return  Math.round((columnSpace / 2) - (-mDataLen + mWidth / mScaleX - columnSpace / 2));
    }



}

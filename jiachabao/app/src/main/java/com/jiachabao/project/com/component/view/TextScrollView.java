package com.jiachabao.project.com.component.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.util.DisplayUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loki on 2017/6/6.
 */

public class TextScrollView extends RelativeLayout implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {

    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector mScaleDetector ;
    protected OverScroller mScroller;
    //是否在触摸中
    protected boolean touch = false;
    //是否是长按事件
    protected boolean isLongPress = false;
    //是否多指触碰
    private boolean mMultipleTouch=false;
    private ArrayList<Float> arrMinutes,arrCurVolume;


    public TextScrollView(Context context) {
        super(context);
        init();
    }

    public TextScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void  init(){
        Log.d("测试","--------------init:");
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mScroller = new OverScroller(getContext());
//        mRowList.add("08:00");
//        mRowList.add("13:00");
//        mRowList.add("18:00");
//        mRowList.add("23:00");
//        mRowList.add("05:00");
    }

    //表格画笔
    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //边框画笔
    protected Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //字体画笔
    protected Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //最高点和最高比率画笔
    protected Paint mMaxTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mMinTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //线画笔
    protected Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private List<String> mRowList;
    private float mMaxCurp=0,mMinCurp=0,mMaxBar;
    private String mMaxUpDown,mMinUpDown;

    private int mMainHeight=0,mGridHeight=0,mMainWidth=0,barMainHeight=0;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("测试",mMainWidth+"--------onDraw-------*"+mGridHeight);
        //绘制view背景颜色
        canvas.save();
        canvas.drawColor(Color.WHITE);
        drawGird(canvas);

        drawValue(canvas);
        canvas.restore();

    }

    private void drawGird(Canvas canvas){
        //绘制横虚线
        float line= DisplayUtil.px2dip(getContext(),5);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setColor(Color.parseColor("#999999"));
        mGridPaint.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(new float[] {line, line, line, line}, 1);
        mGridPaint.setPathEffect(effects);
        Path path = new Path();
        path.moveTo(1, 20);
        path.lineTo(mMainWidth,20);
        canvas.drawPath(path,mGridPaint);
        float rowSpace = mGridHeight / 2;
        Path path1 = new Path();
        path1.moveTo(1, rowSpace);
        path1.lineTo(mMainWidth,rowSpace);
        canvas.drawPath(path1,mGridPaint);

        Path path2 = new Path();
        path2.moveTo(1, mGridHeight-20);
        path2.lineTo(mMainWidth,mGridHeight-20);
        canvas.drawPath(path2,mGridPaint);


        //绘制边框线
        mBorderPaint.setColor(Color.parseColor("#999999"));//fa6268
        mBorderPaint.setStrokeWidth(1f);
        canvas.drawLine(0,1,mMainWidth,1,mBorderPaint);
        canvas.drawLine(0,mGridHeight-1,mMainWidth,mGridHeight-1,mBorderPaint);
        canvas.drawLine(1,1,1,mGridHeight-2,mBorderPaint);
        canvas.drawLine(mMainWidth-1,1,mMainWidth-1,mGridHeight-2,mBorderPaint);

        canvas.drawLine(0,mGridHeight+25,mMainWidth,mGridHeight+25,mBorderPaint);
        canvas.drawLine(0,mMainHeight-1,mMainWidth,mMainHeight-1,mBorderPaint);
        canvas.drawLine(1,(mMainHeight-barMainHeight+25+1),1,mMainHeight-2,mBorderPaint);
        canvas.drawLine(mMainWidth-1,(mMainHeight-barMainHeight+25+1),mMainWidth-1,mMainHeight-2,mBorderPaint);


        if(mRowList!=null&&mRowList.size()>0) {
            //绘制树虚线
            float rowSpace1 = (mMainWidth-2) / (mRowList.size()-1);
            for(int i=0;i<mRowList.size()-1;i++){
                Path path3 = new Path();
                path3.moveTo(rowSpace1*i,0);
                path3.lineTo(rowSpace1*i,mGridHeight-2);
                canvas.drawPath(path3,mGridPaint);

                Path path4 = new Path();
                path4.moveTo(rowSpace1*i,(mMainHeight-barMainHeight+25));
                path4.lineTo(rowSpace1*i,mMainHeight-2);
                canvas.drawPath(path4,mGridPaint);

            }


            //底部时间段
            mTextPaint.setColor(Color.parseColor("#999999"));
            for (int i = 0; i < mRowList.size(); i++) {
                String tab = mRowList.get(i);
                float w;
                if (i == 0) {
                    w = 0;
                } else if (i == mRowList.size() - 1) {
                    w = rowSpace1 * i - mTextPaint.measureText(tab);
                } else {
                    w = rowSpace1 * i - (mTextPaint.measureText(tab) / 2);
                }
                canvas.drawText(tab, w, mGridHeight + 15, mTextPaint);
            }
        }
        mMaxTextPaint.setColor(Color.RED);
        mMaxTextPaint.setTextSize(12f);
        Rect rect = new Rect();
        if(mMaxCurp!=0) {
            mMaxTextPaint.getTextBounds(String.valueOf(mMaxCurp), 0, 1, rect);
            canvas.drawText(String.valueOf(mMaxCurp), 5, 22 + rect.height(), mMaxTextPaint);
        }
        if(mMaxUpDown!=null){
            mMaxTextPaint.getTextBounds(mMaxUpDown, 0, 1, rect);
            canvas.drawText(mMaxUpDown,mMainWidth-mMaxTextPaint.measureText(mMaxUpDown)-5, 22+rect.height(), mMaxTextPaint);
        }
        mMinTextPaint.setColor(Color.GREEN);
        mMinTextPaint.setTextSize(12f);
        canvas.drawText(String.valueOf(mMinCurp), 5, mGridHeight - 22, mMinTextPaint);
        if(mMinUpDown!=null) {
            canvas.drawText(mMinUpDown, mMainWidth - mMinTextPaint.measureText(mMinUpDown) - 5, mGridHeight - 22, mMinTextPaint);
        }
        mMinTextPaint.setColor(Color.parseColor("#999999"));
        mMinTextPaint.setTextSize(12f);
        mMaxTextPaint.getTextBounds(String.valueOf(mMaxBar), 0, 1, rect);
        canvas.drawText(String.valueOf(mMaxBar), 5, mGridHeight+25+rect.height()+5, mMinTextPaint);
        mMinTextPaint.setAlpha(200);
        canvas.drawText("手", 5, mMainHeight-5, mMinTextPaint);

    }

    private void  drawValue(Canvas canvas){
        if(arrMinutes!=null){
            float startX=0,startValue=0;
            List<Float> xs=new ArrayList<>();
            List<Float> ys=new ArrayList<>();
            mLinePaint.setColor(Color.BLUE);
            float a=mMaxCurp/Float.valueOf(mGridHeight-40);
            float h=mGridHeight-40;
            for (int i=0;i<arrMinutes.size();i++){

                float y2=(mMaxCurp-mMinCurp)/(h-0);
                float y3=(h-((arrMinutes.get(i)-mMinCurp)/y2))+20;

                float x=(Float.valueOf(mMainWidth)/Float.valueOf(1260))*i;
                if(i!=0){
                    startX=xs.get(i-1);
                    startValue=ys.get(i-1);
                }else {
                    startValue=y3;
                }

                canvas.drawLine(startX,y3, x, startValue, mLinePaint);
                xs.add(x);
                ys.add(y3);
            }
        }

        if(arrCurVolume!=null){
            List<Float> xsCur=new ArrayList<>();
            float startX=0,startValue=0;
            float a=mMaxBar/Float.valueOf(barMainHeight);
            for (int i=0;i<arrCurVolume.size();i++){
                if(i%2==0){
                    mLinePaint.setColor(Color.RED);
                }else {
                    mLinePaint.setColor(Color.GREEN);
                }
                float x=(Float.valueOf(mMainWidth)/Float.valueOf(1260))*i;
                //float y=mMaxBar/ barMainHeight;
                if(i!=0){
                    startX=xsCur.get(i-1);
                }

                float y2=(mMaxBar-0)/(barMainHeight-0);
                float y3=barMainHeight-((arrCurVolume.get(i)-0)/y2);

                float f;
                if(arrCurVolume.get(i)<=0){
                    f =mMainHeight-1;
                }else {
                     f = y3 + mGridHeight + 25;
                    //f =(barMainHeight-v)+ (mMainHeight-barMainHeight+25);
                }
                Log.d("测试","   Y:"+f+"   Y3:"+y3+"   V:"+arrCurVolume.get(i));
                mLinePaint.setAlpha(200);
                canvas.drawLine(startX,f, x, mMainHeight-0.5f, mLinePaint);
                xsCur.add(x);
            }



        }


    }


    public void setBarData(ArrayList<Float> list){
        this.arrCurVolume=list;
        invalidate();
    }

    public void setData(ArrayList<Float> list){
        this.arrMinutes=list;
        invalidate();
    }


    public void setLabels(List<String> tabs){
        this.mRowList=tabs;
        //invalidate();
    }

    public void setMaxCurp(float maxCurp,float minCurp,String maxUpDown,String minUpDown,float barMax,float preClose){
        if(maxCurp==minCurp&&maxCurp==preClose){
            if(maxCurp==0){
                this.mMaxCurp=0.0001f;
                this.mMinCurp=-0.0001f;
            }else {
                this.mMaxCurp=maxCurp*2;
                this.mMinCurp=-this.mMaxCurp/3;
            }
        }else {
            if(Math.abs(maxCurp-preClose)>=Math.abs(minCurp-preClose)){
                this.mMaxCurp=maxCurp;
                this.mMinCurp=preClose*2-maxCurp;
            }else {
                this.mMinCurp=minCurp;
                this.mMaxCurp=preClose*2-minCurp;
            }

        }

//        this.mMaxCurp=maxCurp;
//        this.mMinCurp=minCurp;
        this.mMaxUpDown=maxUpDown;
        this.mMinUpDown=minUpDown;
        this.mMaxBar=barMax;
        //invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mMainHeight=h;
        this.mGridHeight=h-120;
        this.barMainHeight=120;
        this.mMainWidth=w;
        Log.d("测试","---------------w:"+w+"---h:"+h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d("测试","onTouchEvent--------------ACTION_DOWN:");
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("测试","onTouchEvent--------------ACTION_MOVE:");
                if (event.getPointerCount() == 1) {
                    //长按之后移动
                    if (isLongPress) {
                        onLongPress(event);
                    }
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("测试","onTouchEvent--------------ACTION_POINTER_UP:");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("测试","onTouchEvent--------------ACTION_UP:");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("测试","onTouchEvent--------------ACTION_CANCEL:");
                break;
        }
        mMultipleTouch=event.getPointerCount()>1;
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);

        return true; //super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("测试","按下--------------onDown:"+e.getAction());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("测试","按住--------------onShowPress:"+e.getAction());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("测试","抬起--------------onSingleTapUp:"+e.getAction());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("测试","滚动--------------onScroll-----distanceX:"+distanceX+"---------distanceY:"+distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("测试","长按--------------onLongPress:"+e.getAction());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("测试","抛掷--------------onFling------velocityX:"+velocityX+"-----velocityY:"+velocityY);
        return true;
    }


    //
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        Log.d("测试","--------------onScale:"+detector.getCurrentSpan());
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d("测试","--------------onScaleBegin:"+detector.getCurrentSpan());
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d("测试","--------------onScaleEnd:"+detector.getCurrentSpan());
    }


}

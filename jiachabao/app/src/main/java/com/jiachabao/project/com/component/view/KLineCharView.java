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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.RelativeLayout;

import com.jiachabao.project.com.R;
import com.jiachabao.project.com.data.KLine;
import com.jiachabao.project.com.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loki on 2017/6/13.
 */

public class KLineCharView extends RelativeLayout implements View.OnClickListener, GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    private  Context mContext;
    public KLineCharView(Context context) {
        super(context);
        mContext=context;
        init();
    }

    public KLineCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init();
    }

    public KLineCharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init();
    }

    private float itemClearance=2;
    //每个Item宽度
    private float itemWidth=0,itemClearanceWidth=0;
    //一个格子显示多少个Item
    private int itemCount=18;
    //控件宽高
    private  float viewWidth=0,viewHeight=0;
    private float barViewHeight=200,labelViewHeight=40,clearanceViewHeight=20,tabViewHeight=50;

    //{barHeight:底部区域高度，labelHeight：Tab区域高度，lineHeight：K线区域高度,clearanceHeight:K线上下虚线间隔高度}
    private float barHeight=0,labelHeight=0,lineHeight=0,clearanceHeight=0,tabHeight=0;
    //网格画笔
    protected Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int gridColor=Color.parseColor("#999999");//Color.RED; //
    //虚线画笔
    protected Paint dottedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //文本画笔
    protected Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //K线画笔
    protected Paint kLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private View viewTab;
    private int selectTabIndex=1;
    private float maxPoint=0,minPoint=0,maxBar=0;
    private ArrayList<KLine> arrList;
    private List<Float> listX=new ArrayList<>();
    private List<Float> listY=new ArrayList<>();
    protected GestureDetectorCompat mDetector;
    protected ScaleGestureDetector mScaleDetector;
    private OverScroller mScroller;


    private void init(){
        setWillNotDraw(false);
        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);
        mScroller = new OverScroller(getContext());


        viewTab = LayoutInflater.from(getContext()).inflate(R.layout.layout_stock_tab, null, false);
        viewTab.findViewById(R.id.tv_vol_tab).setOnClickListener(this);
        viewTab.findViewById(R.id.tv_macd_tab).setOnClickListener(this);
        viewTab.findViewById(R.id.tv_kdj_tab).setOnClickListener(this);
        viewTab.findViewById(R.id.tv_rsi_tab).setOnClickListener(this);
        addView(viewTab,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Log.d("测试K","itemWidth:"+itemWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawGrid(canvas);
        drawText(canvas);
        drawValue(canvas);
        canvas.restore();
    }

    private void drawValue(Canvas canvas) {
        listX.clear();
        listY.clear();
        float startX=0,startY=0;
        if(arrList!=null&&itemCount<=arrList.size()){
            int startIndex=arrList.size()-itemCount;
            float lineH=lineHeight-(clearanceHeight*2);
            float itemW=(Float.valueOf(viewWidth)/Float.valueOf(this.itemCount));
            float minUpdown=0,maxUpdown=0,barMax=0,maxPreClose=0;
            for (int i=0;i<itemCount;i++) {
                KLine model=arrList.get(startIndex+i);
                float curp2=Float.valueOf(model.preClose)/100;
                float value=Float.valueOf(model.openp)/100;
                float value1=Float.valueOf(model.nowv)/100;
                float value2=Float.valueOf(model.lowp)/100;
                float value3=Float.valueOf(model.highp)/100;
                if(i==0){
                    minUpdown=value;
                }
                minUpdown=Math.min(value, minUpdown);
                maxUpdown=Math.max(value, maxUpdown);
                minUpdown=Math.min(value1, minUpdown);
                maxUpdown=Math.max(value1, maxUpdown);
                minUpdown=Math.min(value2, minUpdown);
                maxUpdown=Math.max(value2, maxUpdown);
                minUpdown=Math.min(value3, minUpdown);
                maxUpdown=Math.max(value3, maxUpdown);

                maxPreClose=Math.max(curp2, maxPreClose);
                barMax=Math.max(Float.valueOf(model.curVol), barMax);
            }
            setMaxCurp(Float.valueOf(maxUpdown),Float.valueOf(minUpdown),barMax,maxPreClose);
            for (int i=0;i<itemCount;i++){

                KLine model=arrList.get(startIndex+i);
                Log.d("测试K","timeStamp:"+model.timeStamp+"   highp:"+model.highp+"  openp:"+model.openp+"   nowv:"+model.nowv+"   preClose:"+model.preClose+"   lowp:"+model.lowp);
                float value=Float.valueOf(model.openp)/100;
                float value1=Float.valueOf(model.nowv)/100;

                float value2=Float.valueOf(model.lowp)/100;
                float value3=Float.valueOf(model.highp)/100;

                float v1,v2;
                if(value>value1){
                    v2=value;
                    v1=value1;
                }else {
                    v1 =value;
                    v2 =value1;
                }
                if(Float.valueOf(model.upDown)>=0){
                    kLinePaint.setColor(Color.RED);
                }else {
                    kLinePaint.setColor(Color.GREEN);
                }
                float limitH=(maxPoint-minPoint)/(lineH-0);
                float y=(lineH-((v1-minPoint)/limitH))+clearanceHeight;
                float y1=(lineH-((v2-minPoint)/limitH))+clearanceHeight;

                float y2=(lineH-((value2-minPoint)/limitH))+clearanceHeight;
                float y3=(lineH-((value3-minPoint)/limitH))+clearanceHeight;

                if(y1==y){
                    y=y+1;
                }

                float x=(Float.valueOf(viewWidth)/Float.valueOf(this.itemCount))*i;
                //x=x-itemClearanceWidth;
                if(i!=0){
                    startX=listX.get(i-1);
                    //startY=listY.get(i-1);
                }else {
                    //startY=y;
                }

                canvas.drawRect(startX+2,y1, x, y, kLinePaint);
                float x4=0;
                if(i==0){
                    x4= (itemW-2)/2;
                }else {
                    x4= x-((itemW-2)/2);
                }
                canvas.drawLine(x4,y2, x4, y3, kLinePaint);
                listX.add(x);
            }


        }
    }

    //绘制表格
    private void drawGrid(Canvas canvas) {

        //绘制边框线
        gridPaint.setColor(gridColor);//fa6268
        gridPaint.setStrokeWidth(1f);
        //分时图边框
        canvas.drawLine(0,1,viewWidth,1,gridPaint);
        canvas.drawLine(0,lineHeight,viewWidth,lineHeight,gridPaint);
        canvas.drawLine(1,1,1,lineHeight,gridPaint);
        canvas.drawLine(viewWidth,1,viewWidth,lineHeight,gridPaint);
        //底部Bar边框
        canvas.drawLine(0,lineHeight+labelHeight,viewWidth,lineHeight+labelHeight,gridPaint);
        canvas.drawLine(0,lineHeight+labelHeight+tabHeight,viewWidth,lineHeight+labelHeight+tabHeight,gridPaint);

        canvas.drawLine(0,viewHeight,viewWidth,viewHeight,gridPaint);
        canvas.drawLine(1,(viewHeight-barHeight),1,viewHeight,gridPaint);
        canvas.drawLine(viewWidth,(viewHeight-barHeight),viewWidth,viewHeight,gridPaint);

        // 虚线画笔
        float line= DisplayUtil.px2dip(getContext(),4);
        dottedPaint.setStyle(Paint.Style.STROKE);
        dottedPaint.setColor(gridColor);
        dottedPaint.setStrokeWidth(1);
        PathEffect effects = new DashPathEffect(new float[] {line, line, line, line}, 1);
        dottedPaint.setPathEffect(effects);

        //横线虚线
        Path path = new Path();
        path.moveTo(1, clearanceHeight);
        path.lineTo(viewWidth,clearanceHeight);
        canvas.drawPath(path,dottedPaint);

        float rowSpace = lineHeight / 2;
        Path path1 = new Path();
        path1.moveTo(1, rowSpace);
        path1.lineTo(viewWidth,rowSpace);
        canvas.drawPath(path1,dottedPaint);

        Path path2 = new Path();
        path2.moveTo(1, lineHeight-clearanceHeight);
        path2.lineTo(viewWidth,lineHeight-clearanceHeight);
        canvas.drawPath(path2,dottedPaint);



    }

    //绘制最高值和最低值，百分比
    private void drawText(Canvas canvas){
        textPaint.setColor(gridColor);
        textPaint.setTextSize(12f);
        Rect rect = new Rect();
        textPaint.getTextBounds(String.valueOf(maxPoint), 0, 1, rect);
        float paddingLeft=DisplayUtil.px2dip(mContext,5);

        canvas.drawText(String.valueOf(maxPoint), paddingLeft, clearanceHeight + rect.height()+paddingLeft, textPaint);
        canvas.drawText(String.valueOf(minPoint),paddingLeft, lineHeight - clearanceHeight-paddingLeft, textPaint);

    }


    public void  setData(ArrayList<KLine> list){
        this.arrList=list;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
        initViewSize();

    }

    //计算上边时分图高度、中间tab高度、底部圆柱高度
    private void initViewSize(){
        this.barHeight=DisplayUtil.px2dip(getContext(),this.barViewHeight);
        this.labelHeight=DisplayUtil.px2dip(getContext(),this.labelViewHeight);
        this.lineHeight=this.viewHeight-this.barHeight-this.labelHeight;
        this.clearanceHeight=DisplayUtil.px2dip(getContext(),this.clearanceViewHeight);
        this.tabHeight=DisplayUtil.px2dip(getContext(),this.tabViewHeight);
        this.itemClearanceWidth=DisplayUtil.px2dip(getContext(),this.itemClearance);
        int h= MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        viewTab.measure(h,h);
        viewTab.setTranslationY(this.lineHeight+this.labelHeight+(this.tabHeight-viewTab.getMeasuredHeight())/2);
        this.itemWidth= DisplayUtil.px2dip(getContext(),5)+itemClearanceWidth*2;
        Log.d("测试K","W:"+(viewWidth)+"   H:"+h+"     ItemCount:"+(viewWidth/(itemWidth))+"    itemW:"+itemWidth);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case  R.id.tv_vol_tab:
                selectTab(1,v);
                break;
            case  R.id.tv_macd_tab:
                selectTab(2,v);
                break;
            case  R.id.tv_kdj_tab:
                selectTab(3,v);
                break;
            case  R.id.tv_rsi_tab:
                selectTab(4,v);
                break;
        }

    }

    private void selectTab(int index,View v){
        if(index==selectTabIndex){
            return;
        }
        switch(selectTabIndex){
            case  1:
                viewTab.findViewById(R.id.tv_vol_tab).setBackgroundDrawable(null);
            break;
            case  2:
                viewTab.findViewById(R.id.tv_macd_tab).setBackgroundDrawable(null);
                break;
            case  3:
                viewTab.findViewById(R.id.tv_kdj_tab).setBackgroundDrawable(null);
                break;
            case  4:
                viewTab.findViewById(R.id.tv_rsi_tab).setBackgroundDrawable(null);
                break;
        }
        selectTabIndex=index;
        v.setBackgroundColor(Color.parseColor("#e3e3e3"));
        invalidate();
    }

    //设置最高值和最底值 [preClose:昨天收盘值]
    public void setMaxCurp(float maxCurp,float minCurp,float barMax,float preClose){
        //计算时分图的最高区域与最低区域
        if(maxCurp==minCurp&&maxCurp==preClose){
            if(maxCurp==0){
                this.maxPoint=0.0001f;
                this.minPoint=-0.0001f;
            }else {
                this.maxPoint=maxCurp*2;
                this.minPoint=-this.maxPoint/3;
            }
        }else {
            //当最大值减去昨天收盘值得绝对值是否大于今天最低值减去昨天收盘价的绝对值   为true说明 最大值的绘图区域高于最低值的绘图区域
            if(Math.abs(maxCurp-preClose)>=Math.abs(minCurp-preClose)){
                this.maxPoint=maxCurp;
                this.minPoint=preClose*2-maxCurp;
            }else {
                this.minPoint=minCurp;
                this.maxPoint=preClose*2-minCurp;
            }

        }
        Log.d("测试K","minPoint:"+minPoint+"   maxPoint:"+maxPoint);
        this.maxBar=barMax;
        //invalidate();
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("测试K","-----------------------onShowPress:"+e.getAction());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("测试K","-----------------------onSingleTapUp:"+e.getAction());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("测试K","-----------------------onScroll:MotionEvent1:"+e1.getAction()+"  MotionEvent2:"+e2.getAction()+"   X:"+distanceX+"     Y:"+distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("测试K","-----------------------onLongPress:"+e.getAction());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("测试K","-----------------------onFling:MotionEvent1:"+e1.getAction()+"  MotionEvent2:"+e2.getAction());
        return false;
    }

    private float mScaleX=0;
    private boolean touch=false,isLongPress = false,mMultipleTouch=false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    //长按之后移动
                    if (isLongPress) {
                        onLongPress(event);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isLongPress = false;
                touch = false;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                isLongPress = false;
                touch = false;
                invalidate();
                break;
        }
        mMultipleTouch=event.getPointerCount()>1;
        this.mDetector.onTouchEvent(event);
        this.mScaleDetector.onTouchEvent(event);
        return true;

    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float oldScale=mScaleX;
        mScaleX *= detector.getScaleFactor();
        Log.d("测试K","------------mScaleX:"+mScaleX);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        Log.d("测试K","-----------------------onScaleBegin:");
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.d("测试K","-----------------------onScaleEnd:");
    }
}

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Loki on 2017/6/13.
 */

public class KLineCharView extends ScrollAndScaleView implements View.OnClickListener {
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
    private float itemClearanceWidth=0;

    //private int itemCount=0;
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
    private float maxPoint=0,minPoint=0,maxBar=0,minBar=0;;
    private ArrayList<KLine> arrList;
    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //x轴的偏移量
    protected float mTranslateX = Float.MIN_VALUE;
    //item竖虚线间隔
    protected int dottedLine=15;
    //间隔占整个item的比率
    private float itemInterval=0.1f;


    //显示区域中X开始点在数组的位置
    protected int mStartIndex = 0;
    //显示区域中X结束点在数组的位置
    protected int mStopIndex = 0;
    private List<Float> mXs=new ArrayList<Float>();
    //Item的个数
    private int mItemCount=0;
    private float columnSpace=10;
    private float mDataLen;
    //长按之后选择的点的序号
    protected int mSelectedIndex;


    private void init(){
        setWillNotDraw(false);

        mDetector = new GestureDetectorCompat(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), this);

        viewTab = LayoutInflater.from(getContext()).inflate(R.layout.layout_stock_tab, null, false);
        viewTab.findViewById(R.id.tv_vol_tab).setOnClickListener(this);
        viewTab.findViewById(R.id.tv_macd_tab).setOnClickListener(this);
        viewTab.findViewById(R.id.tv_kdj_tab).setOnClickListener(this);
        viewTab.findViewById(R.id.tv_rsi_tab).setOnClickListener(this);
        addView(viewTab,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mStartIndex = indexOfTranslateX(xToTranslateX(0));
        mStopIndex = indexOfTranslateX(xToTranslateX(viewWidth));
        drawGrid(canvas);
        drawText(canvas);
        canvas.save();
        drawValue(canvas);

    }

    private void drawValue(Canvas canvas) {
        if(mItemCount<=0){
            return;
        }
        if(arrList!=null){
            float lineH=lineHeight-(clearanceHeight*2);
            float minUpdown=0,maxUpdown=0,barMax=0,maxPreClose=0,upDownMax=0,updown=0;
            float s1=columnSpace*itemInterval;
            float s2= ((columnSpace-s1*2)-s1)/2;
            for (int j = mStartIndex,k=0; j <= mStopIndex; j++,k++) {
                KLine model=arrList.get(j);
                float curp2=Float.valueOf(model.preClose)/100;
                float value=Float.valueOf(model.openp)/100;
                float value1=Float.valueOf(model.nowv)/100;
                float value2=Float.valueOf(model.lowp)/100;
                float value3=Float.valueOf(model.highp)/100;
                if(k==0){
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
                updown=Float.valueOf(model.upDown);
                Log.d("测试K",model.timeStamp+":----------------updown:"+updown);
                if(updown<0){
                    updown=Math.abs(updown);
                }
                upDownMax=Math.max(updown, upDownMax);

            }
            switch (selectTabIndex){
                case 1:
                    barMaxText(canvas,"手",String.valueOf(barMax));
                    break;
                case 2:
                    barMaxText(canvas,String.valueOf(-upDownMax),String.valueOf(upDownMax));
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
            setMaxCurp(Float.valueOf(maxUpdown),Float.valueOf(minUpdown),barMax,maxPreClose);
            canvas.translate(mTranslateX * mScaleX, 0);
            canvas.scale(mScaleX, 1);
            // 虚线画笔
            float line= DisplayUtil.px2dip(getContext(),4);
            dottedPaint.setStyle(Paint.Style.STROKE);
            dottedPaint.setColor(gridColor);
            dottedPaint.setStrokeWidth(1);
            PathEffect effects = new DashPathEffect(new float[] {line, line, line, line}, 1);
            dottedPaint.setPathEffect(effects);
            Log.d("测试K",mStartIndex+":----------------:"+mStopIndex);
            for (int i = mStartIndex; i <= mStopIndex; i++) {
                float currentPointX = getX(i);
                float lastX = i == 0 ? currentPointX : getX(i - 1);
                KLine model=arrList.get(i);
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
                    kLinePaint.setColor(Color.parseColor("#1dbd7d"));
                }
                float limitH=(maxPoint-minPoint)/(lineH-0);
                float y=(lineH-((v1-minPoint)/limitH))+clearanceHeight;
                float y1=(lineH-((v2-minPoint)/limitH))+clearanceHeight;
                float y2=(lineH-((value2-minPoint)/limitH))+clearanceHeight;
                float y3=(lineH-((value3-minPoint)/limitH))+clearanceHeight;
                if(y1==y){
                    y=y+1;
                }
                //float s1=columnSpace*itemInterval;
                //float s2= ((columnSpace-s1*2)-s1)/2;
                //Log.d("测试K","*----------------s1:"+s1+"  s2:"+s2);
                canvas.drawRect(lastX+s1,y1, lastX+(columnSpace-s1*2), y, kLinePaint);
                canvas.drawLine(lastX+s1+s2,y2, lastX+s1+s2, y3, kLinePaint);
                switch (selectTabIndex){
                    case 1:
                        drawBarValue(canvas,model.curVol,lastX+s1,lastX+(columnSpace-s1*2));
                        break;
                    case 2:
                        drawMACDValue(canvas,model.upDown,upDownMax,lastX+s1,lastX+(columnSpace-s1*2));
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                }

                if(i%dottedLine==0){
                    //横线虚线
                    Path path = new Path();
                    path.moveTo(lastX+s1+s2, 1);
                    path.lineTo(lastX+s1+s2,lineHeight);
                    canvas.drawPath(path,dottedPaint);
                    textPaint.setStrokeWidth(1);
                    textPaint.setColor(gridColor);
                    textPaint.setTextSize(12f);

                    Rect rect = new Rect();
                    String time=dateTimeToString(model.timeStamp);
                    textPaint.getTextBounds(time, 0, 1, rect);
                    float timeW=(textPaint.measureText(time) / 2);
                    canvas.drawText(time, lastX+s1+s2-timeW, lineHeight+clearanceHeight, textPaint);

                    Path path1 = new Path();
                    path1.moveTo(lastX+s1+s2, lineHeight+labelHeight+tabHeight);
                    path1.lineTo(lastX+s1+s2,viewHeight);
                    canvas.drawPath(path1,dottedPaint);
                }

            }
            canvas.restore();
        }

    }

    private void drawBarValue(Canvas canvas,String vol,float x,float endX){
        float barH=barHeight-tabHeight;
        float limitBarH=(maxBar-minBar)/(barH-minBar);
        float barY=barH-((Integer.parseInt(vol)-minBar)/limitBarH);
        barY=barY+(viewHeight-barH);
        kLinePaint.setAlpha(200);
        canvas.drawRect(x,barY,endX, viewHeight, kLinePaint);

    }

    private void drawMACDValue(Canvas canvas,String vol,float upDownMax,float x,float endX){
        float barH=barHeight-tabHeight;
        float v=Float.parseFloat(vol);
        barH=barH/2;
        float limitBarH=(upDownMax-(-upDownMax))/(barH-(-upDownMax));
        float barY=barH-((v-(-upDownMax))/limitBarH);
        kLinePaint.setAlpha(200);
        if(v>=0){
            barY=barY+(viewHeight-(barH*2));
            canvas.drawRect(x,barY,endX, viewHeight-barH, kLinePaint);
        }else {
            Log.d("测试K","--------------------barY:"+barY+"      upDownMax:"+upDownMax);
            barY=(barH-barY)+(viewHeight-barH);
            canvas.drawRect(x,viewHeight-barH,endX, barY, kLinePaint);
        }



    }



    public String dateTimeToString(String time){
        SimpleDateFormat format =  new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat format1 =  new SimpleDateFormat("HH:mm");
        try {
            Date date = format.parse(time);
            return format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  "";
    }
    public float getX(int position) {
        if(mXs==null||mXs.size()<=0){
            return 0;
        }

        return mXs.get(position);
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

    private void barMaxText(Canvas canvas,String text,String max){
        Rect rect = new Rect();
        textPaint.getTextBounds(max, 0, 1, rect);
        float paddingLeft=DisplayUtil.px2dip(mContext,5);
        canvas.drawText(max, paddingLeft, lineHeight+labelHeight+tabHeight + rect.height()+paddingLeft, textPaint);
        textPaint.setAlpha(200);
        canvas.drawText(text, paddingLeft, viewHeight-paddingLeft, textPaint);
    }


    public void  setData(ArrayList<KLine> list){
        this.arrList=list;
        if (arrList != null&&arrList.size()>0) {
            mItemCount=this.arrList.size();
            mXs.clear();
            for (int i = 0; i <= mItemCount; i++) {
                float x=columnSpace * i;
                mXs.add(x);
            }
            mDataLen = (mItemCount - 1) * columnSpace;
            checkAndFixScrollX();
            mTranslateX = mScrollX + (-mDataLen + viewWidth / mScaleX - columnSpace / 2);
        }
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

    //onScrollChanged


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mTranslateX = mScrollX + (-mDataLen + viewWidth / mScaleX - columnSpace / 2);
    }

    @Override
    protected void onScaleChanged(float scale, float oldScale) {
        checkAndFixScrollX();
        mTranslateX = mScrollX + (-mDataLen + viewWidth / mScaleX - columnSpace / 2);
        super.onScaleChanged(scale, oldScale);
    }

    @Override
    public void onLeftSide() {

    }

    @Override
    public void onRightSide() {

    }

    @Override
    public int getMinScrollX() {
        return -20;
    }

    @Override
    public int getMaxScrollX() {
        return  Math.round((columnSpace / 2) - (-mDataLen + viewWidth / mScaleX - columnSpace / 2));
    }

}

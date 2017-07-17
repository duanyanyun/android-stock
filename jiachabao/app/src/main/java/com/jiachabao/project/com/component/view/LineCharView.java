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

import com.jiachabao.project.com.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Loki on 2017/6/9.
 */

public class LineCharView extends RelativeLayout implements GestureDetector.OnGestureListener {
    public LineCharView(Context context) {
        super(context);
        init(context);
    }

    public LineCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineCharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private Context mContext;
    //View 宽 高
    private int viewHeight=0,viewWidth=0;
    //圆柱View的默认高度  中间label的默认高度
    private int barViewHeight=120,labelViewHeight=40,clearanceViewHeight=20;
    //分时线的总体高度，底部圆柱的总高度，label的总高度
    private float lineHeight=0f,barHeight= 0,labelHeight=0,clearanceHeight=0;
    //
    private int gridColor=Color.parseColor("#999999");
    //网格画笔
    protected Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //虚线画笔
    protected Paint dottedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //文本画笔
    protected Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //时分线画笔
    protected Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //
    private float maxCurp=0,minCurp=0,maxUpDown=0,minUpDown=0,maxBar=0;
    //是否在触摸中
    protected boolean touch = false;
    //是否是长按事件
    protected boolean isLongPress = false;
    //是否多指触碰
    private boolean mMultipleTouch=false;


    private String[] mRowList;
    private ArrayList<Float> arrLine,arrBar;
    private int lineCount=0,selectedIndex=-1,priorSelectedIndex=-1;

    private GestureDetectorCompat mDetector;
    private List<Float> listX=new ArrayList<>();
    private List<Float> listY=new ArrayList<>();



    private  void  init(Context context){
        setWillNotDraw(false);
        mContext=context;
        mDetector = new GestureDetectorCompat(mContext, this);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawColor(Color.WHITE);

        drawGrid(canvas);
        drawText(canvas);
        drawValue(canvas);
        if(isLongPress){
            drawHighlight(canvas);
        }
        canvas.restore();
    }
    //绘制长安高亮线
    private void drawHighlight(Canvas canvas) {
        gridPaint.setColor(Color.RED);
        gridPaint.setStrokeWidth(DisplayUtil.dip2px(mContext,1));
        float h = Float.valueOf(viewWidth) / Float.valueOf(lineCount);
        canvas.drawLine(0,listY.get(selectedIndex),viewWidth,listY.get(selectedIndex),gridPaint);
        canvas.drawLine(h*selectedIndex,0,h*selectedIndex,lineHeight,gridPaint);
        canvas.drawLine(h*selectedIndex,lineHeight+labelHeight,h*selectedIndex,viewHeight,gridPaint);
    }
    //绘制网格和虚线底部tab
    private void drawGrid(Canvas canvas){

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

        //绘制虚线和labels
        if(mRowList!=null&&mRowList.length>0) {
            //绘制树虚线
            float rowSpace1 = (viewWidth) / (mRowList.length-1);
            for(int i=0;i<mRowList.length-1;i++){
                if(i!=0&&i!=mRowList.length-1){
                    Path path3 = new Path();
                    path3.moveTo(rowSpace1*i,0);
                    path3.lineTo(rowSpace1*i,lineHeight);
                    canvas.drawPath(path3,dottedPaint);

                    Path path4 = new Path();
                    path4.moveTo(rowSpace1*i,(lineHeight+labelHeight));
                    path4.lineTo(rowSpace1*i,viewHeight);
                    canvas.drawPath(path4,dottedPaint);
                }
            }
            //底部时间段
            Rect rect = new Rect();
            gridPaint.getTextBounds(mRowList[0], 0, 1, rect);
            float h= (labelHeight-rect.height())/2;
            for (int i = 0; i < mRowList.length; i++) {
                String tab = mRowList[i];
                float w;
                if (i == 0) {
                    w = 0;
                } else if (i == mRowList.length - 1) {
                    w = rowSpace1 * i - gridPaint.measureText(tab);
                } else {
                    w = rowSpace1 * i - (gridPaint.measureText(tab) / 2);
                }
                canvas.drawText(tab, w, lineHeight + h+rect.height(), gridPaint);
            }

        }

    }
    //绘制最高值和最低值，百分比
    private void drawText(Canvas canvas){
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(12f);
        Rect rect = new Rect();
        textPaint.getTextBounds(getUpDownToString(maxUpDown), 0, 1, rect);
        float paddingLeft=DisplayUtil.px2dip(mContext,5);

        canvas.drawText(String.valueOf(maxCurp), paddingLeft, clearanceHeight + rect.height(), textPaint);
        canvas.drawText(getUpDownToString(maxUpDown),viewWidth-textPaint.measureText(getUpDownToString(maxUpDown))-paddingLeft, clearanceHeight+rect.height(), textPaint);

        textPaint.setColor(Color.GREEN);
        canvas.drawText(String.valueOf(minCurp),paddingLeft, lineHeight - clearanceHeight, textPaint);
        canvas.drawText(getUpDownToString(minUpDown), viewWidth - textPaint.measureText(getUpDownToString(minUpDown)) - paddingLeft, lineHeight - clearanceHeight, textPaint);


        textPaint.setColor(gridColor);
        canvas.drawText(String.valueOf(maxBar), paddingLeft, lineHeight+labelHeight+rect.height()+paddingLeft, textPaint);
        textPaint.setAlpha(200);
        canvas.drawText("手", 5, viewHeight-paddingLeft, textPaint);
    }
    //绘制线和底部圆柱
    private void drawValue(Canvas canvas){
        if(arrLine!=null){
            float startX=0,startY=0;

            linePaint.setColor(Color.BLUE);
            if(arrLine.size()>=this.lineCount){
                this.lineCount=arrLine.size();
            }
            listX.clear();
            listY.clear();
            float lineH=lineHeight-(clearanceHeight*2);
            for (int i=0;i<arrLine.size();i++){

                float limitH=(maxCurp-minCurp)/(lineH-0);
                float y=(lineH-((arrLine.get(i)-minCurp)/limitH))+clearanceHeight;
                float x=(Float.valueOf(viewWidth)/Float.valueOf(this.lineCount))*i;
                if(i!=0){
                    startX=listX.get(i-1);
                    startY=listY.get(i-1);
                }else {
                    startY=y;
                }
                canvas.drawLine(startX,y, x, startY, linePaint);
                listX.add(x);
                listY.add(y);
            }
        }

        if(arrBar!=null){
            List<Float> arrX=new ArrayList<>();
            float startX=0,startY=0;
            for (int i=0;i<arrBar.size();i++){
                if(i%2==0){
                    linePaint.setColor(Color.RED);
                }else {
                    linePaint.setColor(Color.GREEN);
                }
                float x=(Float.valueOf(viewWidth)/Float.valueOf(this.lineCount))*i;
                if(i!=0){
                    startX=arrX.get(i-1);
                }
                float minBar=0;
                float limitH=(maxBar-minBar)/(barHeight-0);
                float y=barHeight-((arrBar.get(i)-0)/limitH);

                float f;
                if(arrBar.get(i)<=0){
                    f =viewHeight-1;
                }else {
                    f = y + lineHeight+labelHeight;
                }
                linePaint.setAlpha(200);
                canvas.drawRect(startX,f, x, viewHeight, linePaint);
                arrX.add(x);
            }
        }

    }

    //设置最高值和最底值 [preClose:昨天收盘值]
    public void setMaxCurp(float maxCurp,float minCurp,float maxUpDown,float minUpDown,float barMax,float preClose){
        //计算时分图的最高区域与最低区域
        if(maxCurp==minCurp&&maxCurp==preClose){
            if(maxCurp==0){
                this.maxCurp=0.0001f;
                this.minCurp=-0.0001f;
            }else {
                this.maxCurp=maxCurp*2;
                this.minCurp=-this.maxCurp/3;
            }
        }else {
            //当最大值减去昨天收盘值得绝对值是否大于今天最低值减去昨天收盘价的绝对值   为true说明 最大值的绘图区域高于最低值的绘图区域
            if(Math.abs(maxCurp-preClose)>=Math.abs(minCurp-preClose)){
                this.maxCurp=maxCurp;
                this.minCurp=preClose*2-maxCurp;
            }else {
                this.minCurp=minCurp;
                this.maxCurp=preClose*2-minCurp;
            }

        }
        this.maxUpDown=maxUpDown;
        this.minUpDown=minUpDown;
        this.maxBar=barMax;
        invalidate();
    }

    //设置圆柱数据
    public void setBarData(ArrayList<Float> list){
        this.arrBar=list;
        invalidate();
    }
    //设置线数据
    public void setData(ArrayList<Float> list){
        this.arrLine=list;
        invalidate();
    }
    //成交率百分比
    private String getUpDownToString(float value){
        return String.valueOf(value)+"%";
    }
    //设置总共显示数据
    public void setLineCount(int count){
        this.lineCount=count;
        invalidate();
    }
    //设置tab
    public void setLabels(String[] labels){
        this.mRowList=labels;
        invalidate();
    }
    //view宽高初始化
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewHeight=h;
        this.viewWidth=w;
        initViewSize();
    }
    //计算上边时分图高度、中间tab高度、底部圆柱高度
    private void initViewSize(){
        this.barHeight=DisplayUtil.px2dip(getContext(),this.barViewHeight);
        this.labelHeight=DisplayUtil.px2dip(getContext(),this.labelViewHeight);
        this.lineHeight=this.viewHeight-this.barHeight-this.labelHeight;
        this.clearanceHeight=DisplayUtil.px2dip(getContext(),this.clearanceViewHeight);
    }

    //事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d("测试","onTouchEvent--------------ACTION_DOWN:");
                touch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.d("测试","onTouchEvent--------------ACTION_MOVE:");
                if (event.getPointerCount() == 1) {
                    float x = event.getX();//长安的X轴
                    float h = Float.valueOf(viewWidth) / Float.valueOf(lineCount);//总共数据和总宽度的 间隔值
                    if (x >= h) {
                        Log.d("测试", "onTouchEvent--------------:" + x + "  H:" + h);
                        for (int i = 0; i < listX.size(); i++) {
                            float item = listX.get(i);
                            if ((item - h / 2 < x) && (x < item + h / 2)) {  //计算出长按的下标
                                Log.d("测试1", "-----------------I:" + i);
                                selectedIndex=i;
                                break;
                            }
                        }
                        if (priorSelectedIndex!=selectedIndex&&selectedIndex>=0) {
                            onLongPress(event);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("测试","onTouchEvent--------------ACTION_POINTER_UP:");
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.d("测试","onTouchEvent--------------ACTION_UP:");
                if(isLongPress){
                    isLongPress=false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("测试","onTouchEvent--------------ACTION_CANCEL:");
                if(isLongPress){
                    isLongPress=false;
                    invalidate();
                }
                break;
        }
        mMultipleTouch=event.getPointerCount()>1;
        this.mDetector.onTouchEvent(event);
        return true; //super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d("测试","长按--------------onLongPress:"+e.getX());
        isLongPress=true;
        invalidate();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


}

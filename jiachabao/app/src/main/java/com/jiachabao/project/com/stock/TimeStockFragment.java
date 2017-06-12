package com.jiachabao.project.com.stock;


import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.jiachabao.project.com.R;
import com.jiachabao.project.com.component.MyCustomFormatter;
import com.jiachabao.project.com.data.Minutes;
import com.jiachabao.project.com.data.StockModel;
import com.jiachabao.project.com.databinding.FragmentTimeStockBinding;

import java.util.ArrayList;

/**
 * A fragment with a Google +1 button.
 */
public class TimeStockFragment extends Fragment {

    private FragmentTimeStockBinding binding;
    private View view;
    private XAxis xAxisLine;
    private YAxis yAxisLeftLine,yAxisRightLine;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_time_stock,container,false);

        view=binding.getRoot();
        initView();
        return view;
    }

    private void initView(){
        binding.lcTimeLine.setDrawGridBackground(true);
        binding.lcTimeLine.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);
        binding.lcTimeLine.setBackgroundColor(Color.WHITE);
        binding.lcTimeLine.getDescription().setEnabled(false);
        binding.lcTimeLine.setTouchEnabled(true);
        binding.lcTimeLine.setDragEnabled(true);
        binding.lcTimeLine.setScaleEnabled(false);
        binding.lcTimeLine.setPinchZoom(true);
        binding.lcTimeLine.fitScreen();

        binding.bcTimeBar.setDrawGridBackground(true);
        binding.bcTimeBar.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);
        binding.bcTimeBar.setBackgroundColor(Color.WHITE);
        binding.bcTimeBar.getDescription().setEnabled(false);
        binding.bcTimeBar.setTouchEnabled(true);
        binding.bcTimeBar.setDragEnabled(true);
        binding.bcTimeBar.setScaleEnabled(false);
        binding.bcTimeBar.getXAxis().setEnabled(false);
        binding.bcTimeBar.getAxisRight().setEnabled(false);
        binding.bcTimeBar.getAxisLeft().setEnabled(false);
        binding.bcTimeBar.setDrawBorders(true);
        binding.bcTimeBar.setBorderColor(getResources().getColor(R.color.color_999999));
        binding.bcTimeBar.setBorderWidth(0.8f);

        xAxisLine = binding.lcTimeLine.getXAxis();
        xAxisLine.enableGridDashedLine(10f,10f,10f);
        yAxisLeftLine=binding.lcTimeLine.getAxisLeft();
        yAxisRightLine=binding.lcTimeLine.getAxisRight();
        yAxisLeftLine.setDrawZeroLine(false);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxisRightLine.setEnabled(false);
        yAxisLeftLine.setEnabled(false);
        xAxisLine.setEnabled(false);
        binding.lcTimeLine.setDrawBorders(true);
        binding.lcTimeLine.setBorderColor(getResources().getColor(R.color.color_999999));
        binding.lcTimeLine.setBorderWidth(0.8f);

        binding.lcTimeLine.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                binding.bcTimeBar.highlightValue(new Highlight(h.getX(),Float.NaN,0));
            }

            @Override
            public void onNothingSelected() {
                binding.bcTimeBar.highlightValue(null);
            }
        });
        binding.bcTimeBar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                binding.lcTimeLine.highlightValue(new Highlight(h.getX(),Float.NaN,0));
            }

            @Override
            public void onNothingSelected() {
                binding.lcTimeLine.highlightValue(null);
            }
        });


    }

    public void setData(StockModel<Minutes> data){
        if (data.TimeData==null||data.TimeData.size() == 0) {
            binding.lcTimeLine.setNoDataText("暂无数据");
            binding.bcTimeBar.setNoDataText("暂无数据");
            return;
        }
        //中间红线
//        LimitLine line = new LimitLine(Float.valueOf(data.preClose), String.valueOf(data.preClose));
//        line.setLineWidth(1f);
//        line.enableDashedLine(10f, 10f, 0f);
//        line.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        line.setTextSize(10f);
//        yAxisLeftLine.setLabelCount(2);
//        yAxisLeftLine.addLimitLine(line);
//        yAxisLeftLine.setAxisMaximum(data.lowp);
//        yAxisLeftLine.setAxisMaximum(data.highp);
//        yAxisLeftLine.setGridColor(getResources().getColor(R.color.color_00ffffff));
//        yAxisLeftLine.setAxisLineColor(getResources().getColor(R.color.color_00ffffff));
//        xAxisLine.setValueFormatter(new MyCustomFormatter(data.timeaxisText));
//        xAxisLine.setLabelCount(data.timeaxisText.length);
//        xAxisLine.setTextColor(getResources().getColor(R.color.color_999999));


        ArrayList<Entry> values = new ArrayList<Entry>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        float minUpdown=0,maxUpdown=0,barMax=0,valUpdown=0,curp=0;
        for (int i=data.TimeData.size()-1,j=0;i>=0;i--,j++){
            Minutes stock= data.TimeData.get(i);
            curp=Float.valueOf(stock.curp)/1000;
            values.add(new Entry(j, curp));
            minUpdown=Math.min(stock.upDown, minUpdown);
            maxUpdown=Math.max(stock.upDown, maxUpdown);
            barMax=Math.max(stock.curVolume, barMax);
            if(stock.upDown>0) {
                yVals1.add(new BarEntry(j, Float.valueOf(stock.curVolume)));
            }else {
                yVals2.add(new BarEntry(j, Float.valueOf(stock.curVolume)));
            }
        }
        valUpdown= Math.abs(minUpdown)>Math.abs(maxUpdown)?Math.abs(minUpdown):Math.abs(maxUpdown);
        binding.tvMaxCurp.setText(String.valueOf(data.highp));
        binding.tvMinCurp.setText(String.valueOf(data.lowp));
        binding.tvMaxUpdown.setText(String.valueOf(maxUpdown)+"%");
        binding.tvMinUpdown.setText(String.valueOf(minUpdown)+"%");
        binding.tvMaxCurVolume.setText(String.valueOf(barMax));

        binding.bcTimeBar.getAxisLeft().setLabelCount(0);
        binding.bcTimeBar.getAxisLeft().setAxisMaximum(barMax+10);
        binding.bcTimeBar.getAxisLeft().setAxisMinimum(0);

        LineDataSet set1;
        BarDataSet set2,set3;
        if (binding.lcTimeLine.getData() != null && binding.lcTimeLine.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)binding.lcTimeLine.getData().getDataSetByIndex(0);
            set1.setValues(values);
            binding.lcTimeLine.getData().notifyDataChanged();
            binding.lcTimeLine.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "DataSet 1");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColor(Color.BLUE);//getResources().getColor(R.color.minute_blue)
            set1.setDrawCircles(false);
            set1.setLineWidth(0.3f);
            set1.setHighlightEnabled(true);
            set1.setHighLightColor(getResources().getColor(R.color.color_999999));
            set1.setDrawFilled(false);
            //set1.setDrawHorizontalHighlightIndicator(false);
//            set1.setFormLineWidth(1f);
//            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));

            set2 = new BarDataSet(yVals1, "BarDataSet 2");
            set2.setDrawValues(false);
            set2.setColor(getResources().getColor(R.color.color_fa6268),125);
            set2.setHighlightEnabled(true);
            set2.setHighLightAlpha(255);
            set2.setHighLightColor(getResources().getColor(R.color.color_999999));


            set3 = new BarDataSet(yVals2, "BarDataSet 3");
            set3.setDrawValues(false);
            set3.setColor(getResources().getColor(R.color.color_00be7b),125);
            set3.setHighlightEnabled(true);
            set3.setHighLightAlpha(255);
            set3.setHighLightColor(getResources().getColor(R.color.color_999999));

            ArrayList<IBarDataSet> dataSetsBet = new ArrayList<IBarDataSet>();
            dataSetsBet.add(set2);
            dataSetsBet.add(set3);

            BarData dataBar = new BarData(dataSetsBet);
            dataBar.setBarWidth(1f);
            dataBar.setHighlightEnabled(true);


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);
            LineData lineData = new LineData(dataSets);
            binding.lcTimeLine.setData(lineData);
            binding.bcTimeBar.setData(dataBar);
            binding.lcTimeLine.animateX(2500);
            binding.bcTimeBar.animateX(2500);
        }
        binding.lcTimeLine.getLegend().setEnabled(false);
        binding.bcTimeBar.getLegend().setEnabled(false);
        binding.lcTimeLine.setVisibleXRangeMinimum(data.scount);
        binding.bcTimeBar.setVisibleXRangeMinimum(data.scount);
        setOffset();

    }


    private void  setOffset(){
        float lineLeft = binding.lcTimeLine.getViewPortHandler().offsetLeft();
        float lineRight = binding.lcTimeLine.getViewPortHandler().offsetRight();

        float barLeft = binding.bcTimeBar.getViewPortHandler().offsetLeft();
        float barRight = binding.bcTimeBar.getViewPortHandler().offsetRight();
        float barBottom = binding.bcTimeBar.getViewPortHandler().offsetBottom();

        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;

        if (barLeft < lineLeft) {
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            binding.lcTimeLine.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }
        if (barRight < lineRight) {
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            binding.lcTimeLine.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        binding.bcTimeBar.setViewPortOffsets(transLeft, 13, transRight, 20);
    }


    public static TimeStockFragment newInstance() {
        return new TimeStockFragment();
    }

}

package com.vicksontan.roundfeedbackui;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.vicksontan.roundfeedbackui.custom.StringIValueFormatter;
import com.vicksontan.roundfeedbackui.custom.LineChartRenderer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LineChart mChart;
    private LineDataSet set1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mChart = findViewById(R.id.chart);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mChart.getRootView().post(new Runnable() {
            @Override
            public void run() {
                drawLineGraph();
            }
        });
    }

    private void drawLineGraph(){
        setupLineChart();

        // Get the paint renderer to create the line shading.
        Paint p = mChart.getRenderer().getPaintRender();
        int height = mChart.getHeight();

        // desire gradient color on line
        LinearGradient linGrad = new LinearGradient(0, 0, 0, height,
                Color.rgb(0, 255, 0),
                Color.rgb(255, 0, 0),
                Shader.TileMode.REPEAT);
        // implement the gradient into shader
        p.setShader(linGrad);

        setupLineChartData();

        // refresh the drawing
        mChart.invalidate();
    }

    private void setupLineChart()
    {
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // disable touch gestures
        mChart.setTouchEnabled(false);

        // disable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);

        // set an alternative background color
        mChart.setBackgroundColor(Color.rgb(20, 20, 20));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(100);
        leftAxis.setAxisMinimum(-100);
        leftAxis.setYOffset(20f);
        leftAxis.setDrawZeroLine(true);

        // disable all labels and legend
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getXAxis().setEnabled(false);
        mChart.getLegend().setEnabled(false);
    }

    private void setupLineChartData() {

        ArrayList<Entry> values = new ArrayList<Entry>();
        values.add(new Entry(0, 0f, null));
        values.add(new Entry(1, -21f, null));
        values.add(new Entry(2, -34f, null));
        values.add(new Entry(3, -31f, null));
        values.add(new Entry(4, -24f, null));
        values.add(new Entry(5, 24f, null));
        values.add(new Entry(6, 35f, null));
        values.add(new Entry(7, 44f, null));
        values.add(new Entry(8, 39f, null));
        values.add(new Entry(9, 18f, null));

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");
            Log.d("GetXMax", String.valueOf(set1.getXMax()));
            Log.d("GetYMax", String.valueOf(set1.getYMax()));
            Log.d("SimpleString", String.valueOf(set1.getValues()));

            int maxYPos = 2;
            int minYPos = 7;

            // reformat the value text
            set1.setValueFormatter(new StringIValueFormatter(maxYPos, minYPos));

            // make only maximum and minimum text value visible
            set1.setDrawIcons(false);
            List<Integer> colorList = new ArrayList<>();
            for (int i = 0; i < values.size(); i++)
            {
                if (i == 2) { colorList.add(Color.RED); }
                else if (i == 7) { colorList.add(Color.GREEN); }
                else { colorList.add(Color.TRANSPARENT); }
            }

            set1.setLineWidth(5f);
            set1.setDrawCircles(false);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(15f);
            set1.setValueTextColors(colorList);
            set1.setDrawFilled(true);
            set1.setFillColor(Color.BLACK); // default the fill color to black

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
            // rerender the graph with added gradient on fill
            mChart.setRenderer(new LineChartRenderer(mChart, mChart.getAnimator(), mChart.getViewPortHandler(), 30));
        }
    }
}

package com.vicksontan.roundfeedbackui.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by vickson on 19/05/2018.
 */

public class LineChartRenderer extends com.github.mikephil.charting.renderer.LineChartRenderer {

    private int yValueTextOffset;

    public LineChartRenderer(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, int yValueTextOffset) {
        super(chart, animator, viewPortHandler);
        this.yValueTextOffset = yValueTextOffset;
    }

    @Override
    public void drawValue(Canvas c, IValueFormatter formatter, float value, Entry entry, int dataSetIndex, float x, float y, int color) {
        if (value < 0) {
            super.drawValue(c, formatter, value, entry, dataSetIndex, x, y + yValueTextOffset, color);
            return;
        }
        super.drawValue(c, formatter, value, entry, dataSetIndex, x, y, color);
    }

    @Override
    protected void drawLinearFill(Canvas c, ILineDataSet dataSet, Transformer trans, XBounds bounds) {
        super.drawLinearFill(c, dataSet, trans, bounds);

        mRenderPaint.setStyle(Paint.Style.FILL);

        mRenderPaint.setColor(dataSet.getFillColor());
        // filled is drawn with less alpha
        mRenderPaint.setAlpha(dataSet.getFillAlpha());

        Path filled = generateFilledPath(dataSet.getFillFormatter().getFillLinePosition(dataSet, mChart), bounds, dataSet);

        trans.pathValueToPixel(filled);

        // GRADIENT BG - SET SHADER
        Log.d("draw", "drawLinearFill @LineChartRendererAmmended - c.getHeight()=" + c.getHeight());
        mRenderPaint.setShader(new LinearGradient(0, 0, 0, c.getHeight(), Color.rgb(0, 255, 0), Color.rgb(255, 0, 0),
                Shader.TileMode.REPEAT));

        c.drawPath(filled, mRenderPaint);

        // restore alpha
        mRenderPaint.setAlpha(255);

        // GRADIENT BG - REMOVE SHADER
//        mRenderPaint.setShader(null);
    }

    /**
     * Generates the path that is used for filled drawing.
     *
     * @param fillMin
     * @param bounds
     * @param dataSet
     * @return
     */
    private Path generateFilledPath(float fillMin, XBounds bounds, ILineDataSet dataSet) {
        Log.d("Draw1", "generateFilledPath @LineChartRendererAmmended");

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        Path filled = new Path();
        final Entry entry = dataSet.getEntryForIndex(bounds.min);
        filled.moveTo(entry.getX(), fillMin);
        filled.lineTo(entry.getX(), entry.getY() * phaseY);

        // create a new path
        for (int x = bounds.min + 1, count = (int) Math.ceil((bounds.max - bounds.min) * phaseX + bounds.min); x <= count; x++) {

            final Entry e = dataSet.getEntryForIndex(x);
            filled.lineTo(e.getX(), e.getY() * phaseY);
            if (x == count)
            {
                filled.lineTo(e.getX(), fillMin);
            }
        }

        // close up
        filled.lineTo(dataSet.getEntryForIndex(Math.max(Math.min((int) Math.ceil((bounds.max - bounds.min) * phaseX + bounds.min) - 1, dataSet.getEntryCount() - 1), 0))
                .getX(), fillMin);

        filled.close();

        return filled;
    }
}

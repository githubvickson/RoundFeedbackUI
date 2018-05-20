package com.vicksontan.roundfeedbackui.custom;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by vickson on 19/05/2018.
 */

public class StringIValueFormatter implements IValueFormatter {

    private int maxYPos;
    private int minYPos;

    public StringIValueFormatter(int maxYPos, int minYPos) {
        this.maxYPos = maxYPos;
        this.minYPos = minYPos;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        int x = (int) entry.getX();
        if (x == maxYPos) {
            int v = (int) value;
            return String.valueOf(v) + "%";
        } else if (x == minYPos) {
            int v = (int) value;
            return String.valueOf(v) + "%";
        }
        return String.valueOf(value);
    }
}
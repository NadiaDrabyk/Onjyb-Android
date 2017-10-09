package com.onjyb.helper;

import android.content.Context;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.onjyb.R;
import java.text.DecimalFormat;

public class MyBarValueFormatter implements ValueFormatter {
    private Context context;
    private DecimalFormat mFormat;
    int toggle = 0;
    float totalVal = 0.0f;

    public MyBarValueFormatter(Context context) {
        this.context = context;
        this.mFormat = new DecimalFormat("###,###,###,##0");
    }

    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (this.toggle % 2 == 0) {
            this.toggle++;
            this.totalVal = value;
            return "";
        }
        this.toggle++;
        this.totalVal += value;
        return this.mFormat.format((double) this.totalVal) + " " + this.context.getString(R.string.tv_hours);
    }
}

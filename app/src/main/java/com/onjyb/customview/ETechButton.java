package com.onjyb.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

public class ETechButton extends Button {
    public ETechButton(Context context) {
        super(context);
        setAllCaps(false);
    }

    public ETechButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
        setAllCaps(false);
    }

    public ETechButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
        setAllCaps(false);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
//        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
//        String customFont = a.getString(0);
        String customFont = null;

        if (customFont == null) {
            customFont = "Roboto-Bold.ttf";
        }
        setCustomFont(ctx, customFont);
//        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        try {
            setTypeface(Typeface.createFromAsset(ctx.getAssets(), "font/" + asset));
            return true;
        } catch (Exception e) {
            Log.e("setCustomFont", "Could not get typeface: " + e.getMessage());
            return false;
        }
    }
}

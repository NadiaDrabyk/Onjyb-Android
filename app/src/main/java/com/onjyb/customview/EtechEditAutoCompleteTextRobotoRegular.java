package com.onjyb.customview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

import com.onjyb.R;

public class EtechEditAutoCompleteTextRobotoRegular extends AutoCompleteTextView {
    private Drawable imgCloseButton = getResources().getDrawable(R.drawable.dropdown_close);
    private Drawable imgdropdownButton = getResources().getDrawable(R.drawable.dropdown1);
    DropDownItemClickListener listenerObj;

    class C05601 implements OnTouchListener {
        C05601() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            EtechEditAutoCompleteTextRobotoRegular et = EtechEditAutoCompleteTextRobotoRegular.this;
            int temp = ((et.getWidth() - et.getPaddingRight()) - EtechEditAutoCompleteTextRobotoRegular.this.imgCloseButton.getIntrinsicWidth()) + EtechEditAutoCompleteTextRobotoRegular.this.imgdropdownButton.getIntrinsicWidth();
            Log.d("eeevent.getX():", event.getX() + "" + "-------" + temp + "");
            Log.d("eeet.getWidth():", et.getWidth() + "" + "et.getPaddingRight():" + et.getPaddingRight() + "imgCloseButton.getIntrinsicWidth()" + EtechEditAutoCompleteTextRobotoRegular.this.imgCloseButton.getIntrinsicWidth());
            if (et.getCompoundDrawables()[2] == null || et.getText().toString().length() == 0) {
                EtechEditAutoCompleteTextRobotoRegular.this.showDropDown();
            } else if (event.getAction() == 1 && event.getX() > ((float) temp)) {
                et.setText("");
                EtechEditAutoCompleteTextRobotoRegular.this.handleClearButton();
            }
            return false;
        }
    }

    class C05612 implements TextWatcher {
        C05612() {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            EtechEditAutoCompleteTextRobotoRegular.this.handleClearButton();
        }

        public void afterTextChanged(Editable arg0) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    }

    class C05623 implements OnItemClickListener {
        C05623() {
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            EtechEditAutoCompleteTextRobotoRegular.this.handleClearButton();
            EtechEditAutoCompleteTextRobotoRegular.this.listenerObj.onItemOptionClick(parent, EtechEditAutoCompleteTextRobotoRegular.this, position, id);
        }
    }

    public interface DropDownItemClickListener {
        void onItemOptionClick(AdapterView<?> adapterView, View view, int i, long j);
    }

    public EtechEditAutoCompleteTextRobotoRegular(Context context) {
        super(context);
    }

    public EtechEditAutoCompleteTextRobotoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EtechEditAutoCompleteTextRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(Context context, DropDownItemClickListener listenerObj1) {
        this.listenerObj = listenerObj1;
        this.imgCloseButton.setBounds(0, 0, this.imgCloseButton.getIntrinsicWidth() / 2, this.imgCloseButton.getIntrinsicHeight() / 2);
        this.imgdropdownButton.setBounds(0, 0, this.imgdropdownButton.getIntrinsicWidth() / 2, this.imgdropdownButton.getIntrinsicHeight() / 2);
        handleClearButton();
        setOnTouchListener(new C05601());
        addTextChangedListener(new C05612());
        setOnItemClickListener(new C05623());
    }

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        Log.d("onFocusChange()--", focused + "");
        if (focused) {
            EtechEditAutoCompleteTextRobotoRegular obj = this;
            if (!focused || getText().toString().length() <= 0) {
                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], this.imgdropdownButton, getCompoundDrawables()[3]);
            } else {
                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], this.imgCloseButton, getCompoundDrawables()[3]);
            }
        }
    }

    public void handleClearButton() {
        if (getText().toString().equals("")) {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], this.imgdropdownButton, getCompoundDrawables()[3]);
        } else {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], this.imgCloseButton, getCompoundDrawables()[3]);
        }
    }
}

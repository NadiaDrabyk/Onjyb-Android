package com.onjyb.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.layout.RegisterTimeActivity;
import com.onjyb.util.Preference;

public class Header extends RelativeLayout {
    private Activity activity = null;
    private ImageView btnLeft = null;
    private ImageView btnRight = null;
    private ETechButton btnTextLeft;
    private ETechButton btnTextRight;
    private ETechTextView btnregistertime = null;
    private EditText fakeEdit = null;
    private RelativeLayout headerView;
    OnClickListener leftBtnClickListener = new C05642();
    OnClickListener registebtnClickListener = new C05631();
    private final String tag = "Header";
    private String title = null;
    private ETechTextView txtTitle = null;
    private ETechTextView txtsubTitle;

    class C05631 implements OnClickListener {
        C05631() {
        }

        public void onClick(View v) {
            String role = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
            Intent intent = new Intent();
            intent.setClass(Header.this.activity, RegisterTimeActivity.class);
            intent.setFlags(131072);
            intent.putExtra("isFromMenu", true);
            if (!role.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                intent.putExtra("is_from_mng_jobber", true);
            }
            Header.this.activity.startActivity(intent);
        }
    }

    class C05642 implements OnClickListener {
        C05642() {
        }

        public void onClick(View v) {
        }
    }

    public Header(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.header, this, true);
        this.btnLeft = (ImageView) findViewById(R.id.btnLeft);
        this.btnRight = (ImageView) findViewById(R.id.btnRight);
        this.btnregistertime = (ETechTextView) findViewById(R.id.btnregistertime);
        this.txtTitle = (ETechTextView) findViewById(R.id.txtTitle);
    }

    public void init(Activity activity, String title) {
        init(activity, title, null, null, Boolean.valueOf(false));
    }

    public void setLeftButtonText(String text) {
        this.btnTextLeft.setText(text);
        this.btnTextLeft.setVisibility(View.VISIBLE);
        this.btnLeft.setVisibility(GONE);
    }

    public void setSubTitle(String text) {
        this.txtsubTitle.setVisibility(View.VISIBLE);
        this.txtsubTitle.setText(text);
    }

    public void init(Activity activity, String title, String rightBtntitle, OnClickListener rightBtnClickListener, Boolean addMenu) {
        this.activity = activity;
        this.title = title;
        if (title != null) {
            this.txtTitle.setText(this.title);
        }
        this.btnLeft.setOnClickListener(this.leftBtnClickListener);
        this.btnregistertime.setOnClickListener(this.registebtnClickListener);
    }

    public void setRightBtnImage(int rightBtnImage) {
        this.btnRight.setVisibility(View.VISIBLE);
        this.btnRight.setImageResource(rightBtnImage);
    }

    public void hideLeftBtn() {
        this.btnLeft.setVisibility(View.INVISIBLE);
    }

    public void hideRightBtn() {
        this.btnRight.setVisibility(GONE);
    }

    public void setLeftBtnImage(int leftBtnImage) {
        this.btnLeft.setImageResource(leftBtnImage);
    }

    public void setLeftBtnTitle(int leftBtnTitle) {
        this.btnLeft.setImageResource(leftBtnTitle);
    }

    public void setLeftBtnClickListener(OnClickListener btnLeftClickListener) {
        this.btnLeft.setOnClickListener(btnLeftClickListener);
    }

    public void setRightBtnClickListener(OnClickListener btnRightClickListener) {
        this.btnRight.setOnClickListener(btnRightClickListener);
    }

    public void setRightTextBtnClickListener(OnClickListener btnRightClickListener) {
        this.btnTextRight.setOnClickListener(btnRightClickListener);
    }

    public void setTitle(String title) {
        this.txtTitle.setText(title);
    }

    public void setTitleImage(int titleImg) {
        this.txtTitle.setBackgroundResource(titleImg);
    }

    public void setRightBtnText(String title) {
        this.btnTextRight.setVisibility(View.VISIBLE);
        this.btnTextRight.setText(title);
    }

    public void shoowRegisterButton() {
        this.btnregistertime.setVisibility(View.VISIBLE);
    }

    public void setBackGroundColor(int resId) {
    }
}

package com.onjyb.customview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.onjyb.R;

public class TimeStampView extends LinearLayout {
    public static int HrsCount = 0;
    public static int MinCount = 0;
    private Activity activity = null;
    private Button btnHrsMinus;
    private Button btnHrsPlus;
    OnClickListener btnHrsPlusClickListener = new C05761();
    OnClickListener btnHrsminusClickListener = new C05772();
    private Button btnMinMinus;
    OnClickListener btnMinMinusClickListener = new C05794();
    private Button btnMinPlus;
    OnClickListener btnMinPlusClickListener = new C05783();
    private Boolean isEditable = Boolean.valueOf(false);
    TimeStampClickListener tObj;
    private String title = null;
    private ETechTextView tvHrs;
    private ETechTextView tvMin;
    private ETechTextView txtsubTitle;

    class C05761 implements OnClickListener {
        C05761() {
        }

        public void onClick(View v) {
            if (Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) < 0) {
                Toast.makeText(TimeStampView.this.activity, "invalid Hours", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) < 24) {
                TimeStampView.this.tvHrs.setText(String.format("%02d", new Object[]{Integer.valueOf(Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) + 1)}));
                if (TimeStampView.this.tObj != null) {
                    TimeStampClickListener timeStampClickListener = TimeStampView.this.tObj;
                    int i = TimeStampView.HrsCount + 1;
                    TimeStampView.HrsCount = i;
                    timeStampClickListener.onTimeChange(i, TimeStampView.MinCount);
                }
            }
        }
    }

    class C05772 implements OnClickListener {
        C05772() {
        }

        public void onClick(View v) {
            if (Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) > 0) {
                TimeStampView.this.tvHrs.setText(String.format("%02d", new Object[]{Integer.valueOf(Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) - 1)}));
                if (TimeStampView.this.tObj != null) {
                    TimeStampClickListener timeStampClickListener = TimeStampView.this.tObj;
                    int i = TimeStampView.HrsCount - 1;
                    TimeStampView.HrsCount = i;
                    timeStampClickListener.onTimeChange(i, TimeStampView.MinCount);
                    return;
                }
                return;
            }
            Toast.makeText(TimeStampView.this.activity, "invalid Hours", Toast.LENGTH_SHORT).show();
        }
    }

    class C05783 implements OnClickListener {
        C05783() {
        }

        public void onClick(View v) {
            if (Integer.parseInt(TimeStampView.this.tvMin.getText().toString()) < 0 || Integer.parseInt(TimeStampView.this.tvMin.getText().toString()) >= 60) {
                Toast.makeText(TimeStampView.this.activity, "invalid Numbers", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(TimeStampView.this.tvMin.getText().toString()) == 59) {
                TimeStampView.this.tvMin.setText(String.format("%02d", new Object[]{Integer.valueOf(0)}));
                if (TimeStampView.this.tObj != null) {
                    TimeStampView.this.tObj.onTimeChange(TimeStampView.HrsCount, TimeStampView.MinCount);
                }
                if (Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) < 0) {
                    Toast.makeText(TimeStampView.this.activity, "invalid Hours", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) < 24) {
                    TimeStampView.this.tvHrs.setText(String.format("%02d", new Object[]{Integer.valueOf(Integer.parseInt(TimeStampView.this.tvHrs.getText().toString()) + 1)}));
                    if (TimeStampView.this.tObj != null) {
                        TimeStampView.this.tObj.onTimeChange(TimeStampView.HrsCount, TimeStampView.MinCount);
                    }
                }
            } else {
                TimeStampView.this.tvMin.setText(String.format("%02d", new Object[]{Integer.valueOf(Integer.parseInt(TimeStampView.this.tvMin.getText().toString()) + 1)}));
            }
            TimeStampClickListener timeStampClickListener;
            int i;
            if (TimeStampView.MinCount >= 59) {
                TimeStampView.MinCount = 0;
                if (TimeStampView.this.tObj != null) {
                    timeStampClickListener = TimeStampView.this.tObj;
                    i = TimeStampView.HrsCount + 1;
                    TimeStampView.HrsCount = i;
                    timeStampClickListener.onTimeChange(i, TimeStampView.MinCount);
                }
            } else if (TimeStampView.this.tObj != null) {
                timeStampClickListener = TimeStampView.this.tObj;
                i = TimeStampView.HrsCount;
                int i2 = TimeStampView.MinCount + 1;
                TimeStampView.MinCount = i2;
                timeStampClickListener.onTimeChange(i, i2);
            }
        }
    }

    class C05794 implements OnClickListener {
        C05794() {
        }

        public void onClick(View v) {
            if (Integer.parseInt(TimeStampView.this.tvMin.getText().toString()) > 0) {
                TimeStampView.this.tvMin.setText(String.format("%02d", new Object[]{Integer.valueOf(Integer.parseInt(TimeStampView.this.tvMin.getText().toString()) - 1)}));
                if (TimeStampView.this.tObj != null) {
                    TimeStampClickListener timeStampClickListener = TimeStampView.this.tObj;
                    int i = TimeStampView.HrsCount;
                    int i2 = TimeStampView.MinCount - 1;
                    TimeStampView.MinCount = i2;
                    timeStampClickListener.onTimeChange(i, i2);
                    return;
                }
                return;
            }
            Toast.makeText(TimeStampView.this.activity, "invalid Numbers", Toast.LENGTH_SHORT).show();
        }
    }

    public interface TimeStampClickListener {
        void onTimeChange(int i, int i2);

        void onTimeChange1(long j);
    }

    public TimeStampView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.timestamp, this, true);
        this.btnHrsMinus = (Button) findViewById(R.id.btnminus);
        this.btnHrsPlus = (Button) findViewById(R.id.btnplus);
        this.btnMinMinus = (Button) findViewById(R.id.btnminusm);
        this.btnMinPlus = (Button) findViewById(R.id.btnplusm);
        this.btnHrsPlus.setOnClickListener(this.btnHrsPlusClickListener);
        this.btnHrsMinus.setOnClickListener(this.btnHrsminusClickListener);
        this.btnMinPlus.setOnClickListener(this.btnMinPlusClickListener);
        this.btnMinMinus.setOnClickListener(this.btnMinMinusClickListener);
        this.tvHrs = (ETechTextView) findViewById(R.id.tvtimehrs);
        this.tvMin = (ETechTextView) findViewById(R.id.tvtimemin);
    }

    public void init(Activity activity, Boolean iseditable, TimeStampClickListener tObj) {
        this.isEditable = iseditable;
        init(activity, this.isEditable, 0, 0, tObj);
    }

    public void init(Activity activity, Boolean isEditable, int timeHrs, int timeMin) {
        this.tObj = this.tObj;
        this.isEditable = isEditable;
        this.activity = activity;
        setTvHrs(timeHrs);
        setTvMin(timeMin);
        if (!isEditable.booleanValue()) {
            this.btnHrsPlus.setClickable(false);
            this.btnHrsPlus.setVisibility(View.INVISIBLE);
            this.btnHrsMinus.setClickable(false);
            this.btnHrsMinus.setVisibility(View.INVISIBLE);
            this.btnMinPlus.setClickable(false);
            this.btnMinPlus.setVisibility(View.INVISIBLE);
            this.btnMinMinus.setClickable(false);
            this.btnMinMinus.setVisibility(View.INVISIBLE);
        }
    }

    public void init(Activity activity, Boolean isEditable, int timeHrs, int timeMin, TimeStampClickListener tObj) {
        this.tObj = tObj;
        this.isEditable = isEditable;
        this.activity = activity;
        setTvHrs(timeHrs);
        setTvMin(timeMin);
        if (!isEditable.booleanValue()) {
            this.btnHrsPlus.setClickable(false);
            this.btnHrsPlus.setVisibility(View.INVISIBLE);
            this.btnHrsMinus.setClickable(false);
            this.btnHrsMinus.setVisibility(View.INVISIBLE);
            this.btnMinPlus.setClickable(false);
            this.btnMinPlus.setVisibility(View.INVISIBLE);
            this.btnMinMinus.setClickable(false);
            this.btnMinMinus.setVisibility(View.INVISIBLE);
        }
    }

    public void init(Activity activity, Boolean isEditable, int timeHrs, int timeMin, OnClickListener btnHrsPlusClickListener, OnClickListener btnHrsminusClickListener, OnClickListener btnMinPlusClickListener, OnClickListener btnMinMinusClickListener) {
        this.activity = activity;
        setTvHrs(timeHrs);
        setTvMin(timeMin);
    }

    public void setTvHrs(int txtHrs) {
        this.tvHrs.setText(String.format("%02d", new Object[]{Integer.valueOf(txtHrs)}));
        this.tvHrs.setPaintFlags(this.tvHrs.getPaintFlags() | 8);
    }

    public void setTvMin(int txtmin) {
        this.tvMin.setText(String.format("%02d", new Object[]{Integer.valueOf(txtmin)}));
        this.tvMin.setPaintFlags(this.tvMin.getPaintFlags() | 8);
    }

    public int getTvHrs() {
        return Integer.parseInt(this.tvHrs.getText().toString());
    }

    public int getTvMin() {
        return Integer.parseInt(this.tvMin.getText().toString());
    }
}

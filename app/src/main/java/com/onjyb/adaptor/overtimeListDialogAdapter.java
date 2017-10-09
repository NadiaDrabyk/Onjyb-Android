package com.onjyb.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.customview.ETechTextView;
import com.onjyb.db.OvertimeRule;
import com.onjyb.util.Preference;
import java.util.ArrayList;

public class overtimeListDialogAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<OvertimeRule> myList;

    private class MyViewHolder {
        ETechTextView tvOvertimeData;
        ETechTextView tvovetimelbl;

        public MyViewHolder(View item) {
            this.tvOvertimeData = (ETechTextView) item.findViewById(R.id.tvovertimeData);
            this.tvovetimelbl = (ETechTextView) item.findViewById(R.id.tvovertimelbl);
        }
    }

    public overtimeListDialogAdapter(Context context, ArrayList<OvertimeRule> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    public int getCount() {
        return this.myList.size();
    }

    public OvertimeRule getItem(int position) {
        return (OvertimeRule) this.myList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.overtimelistdilog_items, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        OvertimeRule currenttime = getItem(position);
        if (!(currenttime.getRule_per() == null || currenttime.getRule_per().equalsIgnoreCase(""))) {
            mViewHolder.tvovetimelbl.setText(currenttime.getRule_per() + "%");
        }
        if (!(currenttime.getTotal_overtime_per_rule() == null || currenttime.getTotal_overtime_per_rule().equalsIgnoreCase(""))) {
            displayHoursInView(mViewHolder.tvOvertimeData, Integer.parseInt(currenttime.getTotal_overtime_per_rule()));
        }
        String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        return convertView;
    }

    private void displayHoursInView(ETechTextView view, int time) {
        int workHrs = 0;
        int workMn = 0;
        try {
            workHrs = time / 60;
            workMn = time % 60;
            if (workHrs < 0) {
                workHrs = 0;
            }
            if (workMn < 0) {
                workMn = 0;
            }
        } catch (Exception e) {
        }
        view.setText(String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + this.context.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + this.context.getResources().getString(R.string.txt_m));
    }
}

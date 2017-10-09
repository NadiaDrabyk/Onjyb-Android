package com.onjyb.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.onjyb.R;
import java.util.List;

public class SpinnerListAdapter extends BaseAdapter {
    List<String> Date;
    ViewHolder holder;
    Context myc;

    static class ViewHolder {
        TextView subject;
        TextView txtBadge;
        TextView txt_spin;

        ViewHolder() {
        }
    }

    public SpinnerListAdapter(Context c, List<String> messageList) {
        this.myc = c;
        this.Date = messageList;
    }

    public int getCount() {
        return this.Date.size();
    }

    public Object getItem(int arg0) {
        return this.Date.get(arg0);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int pos, View convertview, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) this.myc.getSystemService("layout_inflater");
        if (convertview == null) {
            convertview = li.inflate(R.layout.spinner_initialview, parent, false);
            this.holder = new ViewHolder();
            this.holder.txt_spin = (TextView) convertview.findViewById(R.id.tvinitial);
            convertview.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) convertview.getTag();
        }
        this.holder.txt_spin.setText((CharSequence) this.Date.get(pos));
        return convertview;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = ((LayoutInflater) this.myc.getSystemService("layout_inflater")).inflate(R.layout.spinner_extra_service_layout, parent, false);
        ((TextView) convertView.findViewById(R.id.tvdropview)).setText((CharSequence) this.Date.get(position));
        return convertView;
    }
}

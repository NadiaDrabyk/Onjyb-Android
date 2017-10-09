package com.onjyb.adaptor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.onjyb.R;
import java.util.List;

public class SpinnerAdapterLeave extends BaseAdapter {
    List<String> Date;
    ViewHolder holder;
    Context myc;

    static class ViewHolder {
        ImageView imageView1;
        TextView subject;
        TextView txtBadge;
        TextView txt_spin;

        ViewHolder() {
        }
    }

    public SpinnerAdapterLeave(Context c, List<String> messageList) {
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
            convertview = li.inflate(R.layout.spinnerview_leave, parent, false);
            this.holder = new ViewHolder();
            this.holder.txt_spin = (TextView) convertview.findViewById(R.id.tvlvinitialview);
            this.holder.imageView1 = (ImageView) convertview.findViewById(R.id.imageViewsp);
            Drawable img = this.myc.getResources().getDrawable(R.drawable.dropdown1);
            img.setBounds(0, 0, img.getIntrinsicWidth() / 2, img.getIntrinsicWidth() / 2);
            this.holder.imageView1.setImageDrawable(img);
            convertview.setTag(this.holder);
        } else {
            this.holder = (ViewHolder) convertview.getTag();
        }
        this.holder.txt_spin.setText((CharSequence) this.Date.get(pos));
        return convertview;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = ((LayoutInflater) this.myc.getSystemService("layout_inflater")).inflate(R.layout.spinner_textview, parent, false);
        ((TextView) convertView.findViewById(R.id.tvdropdownview)).setText((CharSequence) this.Date.get(position));
        return convertView;
    }
}

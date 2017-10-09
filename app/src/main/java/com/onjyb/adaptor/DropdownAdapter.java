package com.onjyb.adaptor;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
//import android.widget.Filter.FilterResults;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class DropdownAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final List<String> mDepartments;
    private final List<String> mDepartments_All;
    private final List<String> mDepartments_Suggestion;
    private final int mLayoutResourceId;
    private final int mTextviewResourceId;

    class C05321 extends Filter {
        C05321() {
        }

        public String convertResultToString(Object resultValue) {
            return (String) resultValue;
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                DropdownAdapter.this.mDepartments_Suggestion.clear();
                for (String department : DropdownAdapter.this.mDepartments_All) {
                    if (department.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        DropdownAdapter.this.mDepartments_Suggestion.add(department);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = DropdownAdapter.this.mDepartments_Suggestion;
                filterResults.count = DropdownAdapter.this.mDepartments_Suggestion.size();
                return filterResults;
            }
            //jin
//            filterResults = new FilterResults();
//            filterResults.values = DropdownAdapter.this.mDepartments_All;
//            filterResults.count = DropdownAdapter.this.mDepartments_All.size();
//            return filterResults;
            return null;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            DropdownAdapter.this.mDepartments.clear();
            if (results != null && results.count > 0) {
                //jin
//                for (Object object : results.values) {
//                    if (object instanceof String) {
//                        DropdownAdapter.this.mDepartments.add((String) object);
//                    }
//                }

                for( int i = 0; i < results.count; i++ )
                    DropdownAdapter.this.mDepartments.add((String)results.values.toString());

            } else if (constraint == null) {
                DropdownAdapter.this.mDepartments.addAll(DropdownAdapter.this.mDepartments_All);
            }
            DropdownAdapter.this.notifyDataSetChanged();
        }
    }

    public DropdownAdapter(Context context, int resource, int mTextviewResourceId, List<String> departments) {
        super(context, resource, departments);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mDepartments = new ArrayList(departments);
        this.mDepartments_All = new ArrayList(departments);
        this.mTextviewResourceId = mTextviewResourceId;
        this.mDepartments_Suggestion = new ArrayList(departments);
    }

    public int getCount() {
        return this.mDepartments.size();
    }

    public String getItem(int position) {
        return (String) this.mDepartments.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            try {
                convertView = ((Activity) this.mContext).getLayoutInflater().inflate(this.mLayoutResourceId, parent, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ((TextView) convertView).setText(getItem(position));
        return convertView;
    }

    public Filter getFilter() {
        return new C05321();
    }
}

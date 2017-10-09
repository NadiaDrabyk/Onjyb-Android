package com.onjyb.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressHelper {
    Context context;
    private ProgressDialog pgdialog = null;
    private String strMsg;

    public ProgressHelper(Context context) {
        this.context = context;
        this.pgdialog = new ProgressDialog(context);
    }

    public void showDialog(String message) {
        try {
            if (this.pgdialog != null && this.pgdialog.isShowing()) {
                this.pgdialog.dismiss();
            }
            this.pgdialog.setCancelable(false);
            this.pgdialog = ProgressDialog.show(this.context, "", message, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog() {
        try {
            if (this.pgdialog.isShowing()) {
                this.pgdialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

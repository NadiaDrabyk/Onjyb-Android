package com.onjyb.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import com.onjyb.R;

public class AlertDialogHelper {

    public interface OnMyDialogResult {
        void onOkClick(int i, int i2);
    }

    public static void getNotificatonAlert(Context context, String title, String message) {
        showAlert(context, title, message, null, null, 0, 0, false, null, null, -1, -1);
    }

    public static void getNotificatonAlert(Context context, String title, String message, int titleResId, int btnResId) {
        showAlert(context, title, message, null, null, titleResId, btnResId, false, null, null, -1, -1);
    }

    public static Dialog getNotificatonAlert(Context context, String title, OnClickListener btnOkClickListener, String message, int titleResId, int btnResId, boolean onlyOk) {
        return showAlert(context, title, message, btnOkClickListener, null, titleResId, btnResId, onlyOk, null, null, -1, -1);
    }

    public static Dialog getNotificatonAlert(Context context, String title, OnClickListener btnOkClickListener, String message, int titleResId, int btnResId, boolean onlyOk, String btn1text, String btn2text) {
        return showAlert(context, title, message, btnOkClickListener, null, titleResId, btnResId, onlyOk, btn1text, btn2text, -1, -1);
    }

    private static Dialog showAlert(Context context, String title, String message, OnClickListener btnOkClickListener, OnClickListener btnCancelClickListener,
                                    int titleResId, int btnResId, boolean onlyOk, String btn1text, String btn2text, int titleResIdsecond, int btnResId2second) {
        final Dialog dialog = new Dialog(context);
        try {
            dialog.requestWindowFeature(1);
            Window window = dialog.getWindow();
            LayoutParams wlp = window.getAttributes();
            wlp.gravity = 17;
            window.setAttributes(wlp);
            dialog.getWindow().addFlags(2);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setContentView(R.layout.custom_alert);
            dialog.setCancelable(true);
            dialog.getWindow().setLayout(-1, -2);
            dialog.getWindow().addFlags(2);
            TextView txtDiaTitle = (TextView) dialog.findViewById(R.id.txtDiaTitle);
            final int titleResId1 = titleResId;
            final int btnResId1 = btnResId;
            final int titleResId2 = titleResIdsecond;
            final int btnResId2 = btnResId2second;
            if (!(titleResId2 == -1 || btnResId2 == -1)) {
                dialog.setCancelable(false);
            }
            txtDiaTitle.setText(title);
            ((TextView) dialog.findViewById(R.id.txtDiaMsg)).setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            if (btn1text != null) {
                btnOk.setText(btn1text);
            }
            if (btnOkClickListener == null) {
                btnOk.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else if (onlyOk) {
                dialog.dismiss();
                btnOk.setOnClickListener(btnOkClickListener);
            } else {
                final OnMyDialogResult onMyDialogResult = (OnMyDialogResult) context;
                btnOk.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        onMyDialogResult.onOkClick(titleResId1, btnResId1);
                    }
                });
            }
            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            if (btn2text != null) {
                btnCancel.setText(btn2text);
            }
            if (btnCancelClickListener != null) {
                final Context context2 = context;
                btnCancel.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (titleResId2 != -1 && btnResId2 != -1) {
//jin                            context2.onOkClick(titleResId2, btnResId2);
                        }
                    }
                });
            } else {
                btnCancel.setVisibility(View.GONE);
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }

    public static Dialog getConfirmationAlert(Context context, String title, OnClickListener btnOkonClickListener, OnClickListener btnCancelonClickListener, String message, int titleResId, int btnResId, boolean okclick, String btn1Text, String btn2Text, int btn2title, int btn2Restid) {
        return showAlert(context, title, message, btnOkonClickListener, btnCancelonClickListener, titleResId, btnResId, okclick, btn1Text, btn2Text, btn2title, btn2Restid);
    }

    public static Dialog getConfirmationAlert(Context context, String title, OnClickListener btnOkonClickListener, OnClickListener btnCancelonClickListener, String message, int titleResId, int btnResId, boolean okclick) {
        return showAlert(context, title, message, btnOkonClickListener, btnCancelonClickListener, titleResId, btnResId, okclick, null, null, -1, -1);
    }
}

package com.onjyb.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HomeWatcher {
    static final String TAG = "hg";
    public static boolean isregister = false;
    private Context mContext;
    private IntentFilter mFilter;
    private OnHomePressedListener mListener;
    private InnerRecevier mRecevier = null;

    class InnerRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        InnerRecevier() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                String reason = intent.getStringExtra("reason");
                if (reason != null) {
                    Log.d(HomeWatcher.TAG, "action:" + action + ",reason:" + reason);
                    if (HomeWatcher.this.mListener == null) {
                        return;
                    }
                    if (reason.equals("homekey")) {
                        HomeWatcher.this.mListener.onHomePressed();
                    } else if (reason.equals("recentapps")) {
                        HomeWatcher.this.mListener.onHomeLongPressed();
                    }
                }
            }
        }
    }

    public interface OnHomePressedListener {
        void onHomeLongPressed();

        void onHomePressed();
    }

    public HomeWatcher(Context context) {
        this.mContext = context;
        this.mFilter = new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
    }

    public void setOnHomePressedListener(OnHomePressedListener listener) {
        this.mListener = listener;
        this.mRecevier = new InnerRecevier();
    }

    public void startWatch() {
        if (this.mRecevier != null) {
            this.mContext.registerReceiver(this.mRecevier, this.mFilter);
            isregister = true;
        }
    }

    public void stopWatch() {
        if (this.mRecevier != null) {
            this.mContext.unregisterReceiver(this.mRecevier);
            isregister = false;
        }
    }
}

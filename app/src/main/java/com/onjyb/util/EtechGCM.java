package com.onjyb.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
//import com.google.android.gms.gcm.GoogleCloudMessaging;
//import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.onjyb.R;
import com.onjyb.reqreshelper.ActionCallback;

public class EtechGCM {
    public static final String EXTRA_MESSAGE = "message";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String TAG = "EtechGCM";
    private String SENDER_ID = null;
    private Context context;
    private String gcmPrefName = null;

    public EtechGCM(Context context, String SENDER_ID, String gcmPrefName) {
        this.SENDER_ID = SENDER_ID;
        this.gcmPrefName = gcmPrefName;
        this.context = context;
    }

    public EtechGCM(Context context, String gcmPrefName) {
        this.gcmPrefName = gcmPrefName;
        this.context = context;
    }

    public static boolean checkPlayServices(Context context) {
        return true;
    }

    public void registerInBackground(final ActionCallback callback) {
        new AsyncTask<Void, Void, String>() {
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(EtechGCM.this.context);
                    String regid = InstanceID.getInstance(EtechGCM.this.context).getToken(EtechGCM.this.SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.e(EtechGCM.TAG, "GCM Registration Token: " + regid);
                    msg = EtechGCM.this.context.getString(R.string.device_register) + regid;
                    Log.i(EtechGCM.TAG, msg);
                    EtechGCM.this.storeRegistrationId(EtechGCM.this.context, regid);
                    callback.onActionComplete(100, "", "");
                    return msg;
                } catch (Exception ex) {
                    msg = EtechGCM.this.context.getString(R.string.error) + ex.getMessage();
                    Log.e(EtechGCM.TAG, msg);
                    return msg;
                }
            }

            protected void onPostExecute(String msg) {
                if (msg.contains(EtechGCM.this.context.getString(R.string.error))) {
                    callback.onActionComplete(101, "", "");
                } else {
                    callback.onActionComplete(100, "", "");
                }
            }
        }.execute(new Void[]{null, null, null});
    }

    public void storeRegistrationId(Context context, String regId) {
        SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        System.out.println("====reg_id PREFS====" + regId);
        Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    public String getRegistrationId() {
        SharedPreferences prefs = getGCMPreferences(this.context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        } else if (prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE) == getAppVersion(this.context)) {
            return registrationId;
        } else {
            Log.i(TAG, "App version changed.");
            return "";
        }
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(this.gcmPrefName, 0);
    }

    private static int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}

package com.onjyb.receiver;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.onjyb.Constants;
import com.onjyb.R;
import com.onjyb.beans.UserHelper;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.EtechGCM;
import com.onjyb.util.Preference;
import com.onjyb.util.notificationUtil;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = new String[]{"global"};
    private Context context = this;
    UserHelper helper;
    private ActionCallback registerApiCallBack = new C07921();

    class C07921 implements ActionCallback {
        C07921() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                try {
                    Log.d(RegistrationIntentService.TAG, ">>>service Register..------");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(RegistrationIntentService.TAG, "onActionComplete > Exception: " + e.toString());
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(RegistrationIntentService.this.getApplicationContext(), RegistrationIntentService.this.getString(R.string.app_name), res.toString());
        }
    }

    public RegistrationIntentService() {
        super(TAG);
    }

    protected void onHandleIntent(Intent intent) {
        try {
            Log.d(TAG, ">>>service Strated");
            Preference.setSharedPref(Constants.PREF_SENDER_ID, Constants.SENDER_ID);
            String regid = InstanceID.getInstance(this).getToken(notificationUtil.getGCMSENDERID(this), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + regid);
            new EtechGCM(getApplicationContext(), "").storeRegistrationId(this, regid);
            this.helper = new UserHelper(this.context);
            this.helper.apiregisterDeviceForNotification(this.registerApiCallBack, regid);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
    }
}

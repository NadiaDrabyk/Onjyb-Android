package com.onjyb.layout;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.onjyb.R;
import com.onjyb.beans.workSheetUploadHelper;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import java.util.HashMap;
import org.apache.commons.lang3.time.DateUtils;

public class ImageUploadService extends Service {
    Context context;
    HashMap<String, String> imgRecord = new HashMap();
    private ActionCallback imgUploadCallBack = new C06521();
    long interval = DateUtils.MILLIS_PER_MINUTE;
    public boolean isRunning = false;

    class C06521 implements ActionCallback {
        C06521() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                try {
                    DatabaseHelper databaseHelper = new DatabaseHelper(ImageUploadService.this.getApplicationContext());
                    DatabaseHelper.getDBAdapterInstance(ImageUploadService.this.getApplicationContext());
                    ImageUploadService.this.imgRecord = databaseHelper.selSingleRecordFromDB("select * from  tblattachmentmap LIMIT 1", null);
                    databaseHelper.deleteRecordInDB("tblattachmentmap", "attachment_id=?", new String[]{(String) ImageUploadService.this.imgRecord.get("attachment_id")});
                    ImageUploadService.this.fileUpload();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(ImageUploadService.this.context, ImageUploadService.this.getString(R.string.app_name), res.toString());
        }
    }

    @Nullable
    public IBinder onBind(Intent arg0) {
        Log.d("IMAGEUPLOADSERVICE:::::::", "OnBind()");
        return null;
    }

    public void onCreate() {
        super.onCreate();
        Log.d("IMAGEUPLOADSERVICE:::::::", "OnCreate()");
    }

    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("IMAGEUPLOADSERVICE:::::::", "OnRebind()");
    }

    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d("IMAGEUPLOADSERVICE:::::::", "OnDestroy()");
        if (this.isRunning) {
        }
    }

    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("IMAGEUPLOADSERVICE:::::::", "OnStart()");
        if (!this.isRunning) {
            this.isRunning = true;
        }
        fileUpload();
    }

    private void fileUpload() {
        if (checkInternetConnection()) {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            DatabaseHelper.getDBAdapterInstance(getApplicationContext());
            this.imgRecord = databaseHelper.selSingleRecordFromDB("select * from tblattachmentmap LIMIT 1", null);
            if (this.imgRecord != null) {
                new workSheetUploadHelper(getApplicationContext()).apiUploadImages(this.imgRecord, this.imgUploadCallBack);
            } else {
                databaseHelper.close();
            }
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService("connectivity");
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }
}

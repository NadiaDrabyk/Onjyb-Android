package com.onjyb.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.R;
import com.onjyb.layout.GroupChatActivity;
import com.onjyb.layout.SplashActivity;
import com.onjyb.util.Preference;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class GcmBroadcastReceiver extends BroadcastReceiver {
    static final int NOTIFICATION_ID = 1;
    static final String TAG = "GcmBroadcastReceiver";
    public static int notificationId = 0;
    String UserID;
    Builder builder;
    private Context ctx;
    String current_status;
    String leave_id;
    private NotificationManager mNotificationManager;
    String msg;
    String operation;
    String type;
    String worksheet_id;

    class C07911 extends AsyncTask<String, Void, Bitmap> {
        C07911() {
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            try {
                mIcon11 = BitmapFactory.decodeStream(new URL(urls[0]).openStream());
            } catch (Exception e) {
//                Log.e("sendBigPicNotification() Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Intent intent = new Intent(new Intent(GcmBroadcastReceiver.this.ctx, SplashActivity.class));
            if (GcmBroadcastReceiver.this.ctx.getResources().getIdentifier("icon_silhouette", "drawable", GcmBroadcastReceiver.this.ctx.getPackageName()) == 0) {
            }
            if (GcmBroadcastReceiver.this.ctx.getResources().getIdentifier("icon", "drawable", GcmBroadcastReceiver.this.ctx.getPackageName()) == 0) {
            }
            PendingIntent pi = PendingIntent.getActivity(GcmBroadcastReceiver.this.ctx, 0, intent, 134217728);
            Builder builder = new Builder(GcmBroadcastReceiver.this.ctx);
        }
    }

    public void onReceive(Context context, Intent intent) {
        JSONException e;
        JSONObject jSONObject;
        String usrId;
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        this.ctx = context;
        Log.d("intent gcm", intent.getExtras().toString());
        Log.i(TAG, "Message from GCM");
        String messageType = gcm.getMessageType(intent);
        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            sendNotification(context.getString(R.string.send_error) + intent.getExtras().toString());
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            sendNotification(context.getString(R.string.delete_msg_from_server) + intent.getExtras().toString());
        } else {
            Bundle b = intent.getExtras();
            this.msg = b.getString("message");
            try {
                JSONObject jsonObject = new JSONObject(b.getString("other_details"));
                this.UserID = jsonObject.getString("user_id");
                this.type = jsonObject.getString("type");
                if (this.type == null || !this.type.equalsIgnoreCase("gm")) {
                    try {
                        if (this.type != null && this.type.equalsIgnoreCase("ws")) {
                            this.worksheet_id = jsonObject.getString("worksheet_id");
                        } else if (this.type != null && this.type.equalsIgnoreCase("lm")) {
                            this.leave_id = jsonObject.getString("leave_id");
                        }
                    } catch (JSONException e2) {
                        e = e2;
                        jSONObject = jsonObject;
                        e.printStackTrace();
                        Log.i(TAG, "USER ID : " + this.UserID);
                        Log.i(TAG, "MESSAGE : " + this.msg);
                        Log.e(TAG, "isActivityVisible : " + OnjybApp.isActivityVisible());
                        usrId = Preference.getSharedPref(Constants.PREF_USER_ID, "");
                        sendNotification(this.msg);
                        Log.d(TAG, "Data:-->UserId->" + this.UserID + ",OPERATION->" + this.operation + ",MSG" + this.msg);
                        setResultCode(-1);
                    }
                }
                this.worksheet_id = "";
                this.leave_id = "";
                if (GroupChatActivity.isInFront) {
                    Intent intent1 = new Intent();
                    intent1.setAction("com.example.SendBroadcast");
                    intent1.addFlags(32);
                    context.sendBroadcast(intent1);
                }
                jSONObject = jsonObject;
            } catch (JSONException e3) {
                e = e3;
                e.printStackTrace();
                Log.i(TAG, "USER ID : " + this.UserID);
                Log.i(TAG, "MESSAGE : " + this.msg);
                Log.e(TAG, "isActivityVisible : " + OnjybApp.isActivityVisible());
                usrId = Preference.getSharedPref(Constants.PREF_USER_ID, "");
                sendNotification(this.msg);
                Log.d(TAG, "Data:-->UserId->" + this.UserID + ",OPERATION->" + this.operation + ",MSG" + this.msg);
                setResultCode(-1);
            }
            if (this.msg != null && this.msg.length() > 0) {
                Log.i(TAG, "USER ID : " + this.UserID);
                Log.i(TAG, "MESSAGE : " + this.msg);
                Log.e(TAG, "isActivityVisible : " + OnjybApp.isActivityVisible());
                usrId = Preference.getSharedPref(Constants.PREF_USER_ID, "");
                if (!(OnjybApp.isActivityVisible() || usrId.equalsIgnoreCase(""))) {
                    sendNotification(this.msg);
                }
            }
            Log.d(TAG, "Data:-->UserId->" + this.UserID + ",OPERATION->" + this.operation + ",MSG" + this.msg);
        }
        setResultCode(-1);
    }

    private void sendNotification(String msg) {
        NotificationManager nm = (NotificationManager) this.ctx.getSystemService("notification");
        Intent intent = new Intent(new Intent(this.ctx, SplashActivity.class));
        intent.putExtra("notification_user_id", this.UserID);
        intent.putExtra("notification_type", this.type);
        if (this.type != null && this.type.equalsIgnoreCase("ws")) {
            intent.putExtra("notification_work_id", this.worksheet_id);
        } else if (this.type != null && this.type.equalsIgnoreCase("lm")) {
            intent.putExtra("notification_leave_id", this.leave_id);
        }
        if (!(this.type == null || this.type.equalsIgnoreCase("gm"))) {
            intent.putExtra("current_status", this.current_status);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this.ctx, 1000, intent, 268435456);
        Builder mBuilder = new Builder(this.ctx).setSmallIcon(R.drawable.ic_launcher).setContentTitle(this.ctx.getString(R.string.app_name)).setStyle(new BigTextStyle().bigText(msg)).setContentText(msg).setDefaults(1).setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        nm.notify(1, mBuilder.build());
        notificationId++;
    }

    public void sendBasicNotification(String msg) {
        Notification notification = new Builder(this.ctx).setContentTitle(this.ctx.getString(R.string.basic_notify)).setContentText(msg).setSmallIcon(R.drawable.ic_launcher).build();
        notification.flags |= 16;
        ((NotificationManager) this.ctx.getSystemService("notification")).notify(0, notification);
    }

    public void sendBigPictureNotification(String offerDesc, String bigPic) {
        try {
            new C07911().execute(new String[]{Constants.BASE_IMAGE_URL + bigPic});
        } catch (Exception e) {
            Log.e(TAG, "sendBigPictureNotification()" + e, e);
        }
    }
}

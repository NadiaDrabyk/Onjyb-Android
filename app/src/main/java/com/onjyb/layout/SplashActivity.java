package com.onjyb.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.android.volley.toolbox.ImageRequest;
import com.onjyb.R;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.beans.LeavesApiHelper;
import com.onjyb.beans.UserHelper;
import com.onjyb.db.AttachmentMap;
import com.onjyb.db.LeaveType;
import com.onjyb.db.OvertimeRule;
import com.onjyb.db.Service;
import com.onjyb.db.WorkSheet;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import java.util.ArrayList;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

public class SplashActivity extends Activity {
    public static int SPLASH_TIME_OUT = 1000;//1000;
    private final String TAG = LoginActivity.class.getName();
    private ActionCallback WorksheetDetailsCallBack = new C07723();
    Context context = this;
    private String is_force_update = "no";
    private ActionCallback leaveDetailsCallBack = new C07672();
    ProgressHelper progressHelper;
    private String status;
    private String userId;

    class C07651 implements Runnable {
        C07651() {
        }

        public void run() {
            Constants.updateActivityCall = true;
            SplashActivity.this.userId = SplashActivity.this.getIntent().getStringExtra("notification_user_id");
            String prefuId = Preference.getSharedPref(Constants.PREF_USER_ID, "");
            SplashActivity.this.is_force_update = Preference.getSharedPref(Constants.forceUpdateApp, "no");
            Intent i;
            if (SplashActivity.this.is_force_update.equalsIgnoreCase("yes") && !SplashActivity.this.isVersionUpdated()) {
                i = new Intent(SplashActivity.this.context, UpdateAvailableActivity.class);
                i.setClass(SplashActivity.this.context, UpdateAvailableActivity.class);
                i.setFlags(67108864);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();
            } else if (Preference.getSharedPref(Constants.PREF_FILE_DBUSER_USERNAME, "").isEmpty()) {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                SplashActivity.this.finish();
            } else if (SplashActivity.this.userId == null || SplashActivity.this.userId.isEmpty() || !SplashActivity.this.userId.equalsIgnoreCase(prefuId)) {
                SplashActivity.this.redirectToHome();
            } else if (SplashActivity.this.userId != null && !SplashActivity.this.userId.isEmpty() && SplashActivity.this.userId.equalsIgnoreCase(prefuId)) {
                String type = SplashActivity.this.getIntent().getStringExtra("notification_type");
                SplashActivity.this.status = SplashActivity.this.getIntent().getStringExtra("current_status");
                if (type.equalsIgnoreCase("gm")) {
                    i = new Intent(SplashActivity.this, GroupChatActivity.class);
                    i.setFlags(131072);
                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();
                } else if (type.equalsIgnoreCase("ws")) {
                    String work_id = SplashActivity.this.getIntent().getStringExtra("notification_work_id");
                    if (work_id == null || work_id.isEmpty()) {
                        SplashActivity.this.redirectToHome();
                        return;
                    }
                    WorkSheet workSheet = new WorkSheet();
                    workSheet.setServerWorkSheetId(work_id);
                    UserHelper helper = new UserHelper(SplashActivity.this.context);
                    SplashActivity.this.progressHelper.showDialog("Loading..");
                    helper.apiGetWorksheetDetails(workSheet, SplashActivity.this.WorksheetDetailsCallBack);
                } else if (type.equalsIgnoreCase("lm")) {
                    String leave_id = SplashActivity.this.getIntent().getStringExtra("notification_leave_id");
                    if (leave_id == null || leave_id.isEmpty()) {
                        SplashActivity.this.redirectToHome();
                        return;
                    }
                    LeaveType leaveType = new LeaveType();
                    leaveType.setLeaveId(leave_id);
                    LeavesApiHelper helper2 = new LeavesApiHelper(SplashActivity.this.context);
                    SplashActivity.this.progressHelper.showDialog("Loading..");
                    helper2.apiGetLeaveDetail(SplashActivity.this.status, leaveType, SplashActivity.this.leaveDetailsCallBack);
                }
            }
        }
    }

    class C07672 implements ActionCallback {

        class C07661 extends TypeReference<ArrayList<LeaveType>> {
            C07661() {
            }
        }

        C07672() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (SplashActivity.this.progressHelper != null) {
                SplashActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    ArrayList<LeaveType> leaveDetailList = (ArrayList) new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(((JSONObject) res).getJSONArray("leave_details").toString(), new C07661());
                    if (leaveDetailList.get(0) == null || ((LeaveType) leaveDetailList.get(0)).getApproveStatus() == null) {
                        SplashActivity.this.redirectToHome();
                        return;
                    }
                    LeaveType currsheet = (LeaveType) leaveDetailList.get(0);
                    Intent intent;
                    if (!currsheet.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                        intent = new Intent(SplashActivity.this, LeaveDisplay.class);
                        intent.putExtra("LeaveDetails", currsheet);
                        SplashActivity.this.startActivity(intent);
                        SplashActivity.this.finish();
                        return;
                    } else if (Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                        intent = new Intent(SplashActivity.this, LeaveFormActivity.class);
                        intent.putExtra("LeaveDetails", currsheet);
                        SplashActivity.this.startActivity(intent);
                        SplashActivity.this.finish();
                        return;
                    } else {
                        intent = new Intent(SplashActivity.this, LeaveDisplay.class);
                        intent.putExtra("LeaveDetails", currsheet);
                        SplashActivity.this.startActivity(intent);
                        SplashActivity.this.finish();
                        return;
                    }
                } catch (Exception e) {
                    AlertDialogHelper.getNotificatonAlert(SplashActivity.this.context, SplashActivity.this.getString(R.string.app_name), SplashActivity.this.getResources().getString(R.string.error));
                    SplashActivity.this.redirectToHome();
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(SplashActivity.this.context, SplashActivity.this.getString(R.string.app_name), res.toString());
            SplashActivity.this.redirectToHome();
        }
    }

    class C07723 implements ActionCallback {

        class C07681 extends TypeReference<ArrayList<WorkSheet>> {
            C07681() {
            }
        }

        class C07692 extends TypeReference<ArrayList<AttachmentMap>> {
            C07692() {
            }
        }

        class C07703 extends TypeReference<ArrayList<Service>> {
            C07703() {
            }
        }

        class C07714 extends TypeReference<ArrayList<OvertimeRule>> {
            C07714() {
            }
        }

        C07723() {
        }

        /* JADX WARNING: inconsistent code. */
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (progressHelper != null) {
                progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    //0925
                    JSONObject dic = ((JSONObject) res);
                    ArrayList<WorkSheet> items = new ObjectMapper().readValue(dic.getJSONArray("worksheet_details").toString(),
                            new C07681());
                    WorkSheet workSheet = items.get(0);

                    ArrayList<AttachmentMap> mapList = new ObjectMapper().readValue(dic.getJSONArray("workimage_details").toString(),
                            new C07692());
                    workSheet.setAttachementList(mapList);

                    ArrayList<Service> extraServices = new ObjectMapper().readValue(dic.getJSONArray("extraservice_details").toString(),
                            new C07703());
                    workSheet.setServiceObjectsArray(extraServices);

                    ArrayList<OvertimeRule> overtimeRules = new ObjectMapper().readValue(dic.getJSONArray("overtime_details").toString(),
                            new C07714());
                    workSheet.setOvertimeRuleArrayList(overtimeRules);

                    String isOvertimeautomatic = dic.getString("isworkovertimeautomatic");
                    workSheet.setIsworksheetAutomaticEditmode(isOvertimeautomatic);

                    String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
                    if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)){
                    }else {
                        if(!user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER)){
                            Intent intent = new Intent(SplashActivity.this, RegisterTimeActivity.class);
                            intent.putExtra("WorksheetEditData",workSheet);
                            startActivity(intent);
                            return;
                        }
                    }
                    String status = workSheet.getApproveStatus();
                    if(status.equalsIgnoreCase("pennding")) {
                    }else{
                        if(!status.equalsIgnoreCase("reject")){
                            Intent intent = new Intent(SplashActivity.this, DisplayMyTaskDetailActivity.class);
                            intent.putExtra("WorksheetEditData", workSheet);
                            startActivity(intent);
                            return;
                        }
                    }
                    Intent intent = new Intent(SplashActivity.this, RegisterTimeActivity.class);
                    intent.putExtra("WorksheetEditData", workSheet);
                    if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER))
                        intent.putExtra("EditFlag", "approve");

                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            AlertDialogHelper.getNotificatonAlert(SplashActivity.this.context, SplashActivity.this.getString(R.string.app_name), res.toString());
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppUtils.setStatusbarColor(this);
        this.progressHelper = new ProgressHelper(this.context);
        new Handler().postDelayed(new C07651(), (long) SPLASH_TIME_OUT);
    }

    public void redirectToHome() {
        if (Preference.getSharedPref(Constants.PREF_FILE_DBUSER_USERNAME, "").isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        Intent i = new Intent(this, EmpHomeActivity.class);
        i.putExtra("inFromSplash", true);
        startActivity(i);
        finish();
    }

    private boolean isVersionUpdated() {
        String currentversion = AppUtils.getCurrentAppVersion(this.context);
        String preVersion = Preference.getSharedPref(Constants.Current_version_app, "");
        if (currentversion == null) {
            return true;
        }
        try {
            if (currentversion.isEmpty() || preVersion.isEmpty()) {
                return true;
            }
            Log.d(this.TAG, "isVersionUpdated > Cur: " + Float.parseFloat(currentversion) + ", Pre: " + Float.parseFloat(preVersion));
            if (Float.parseFloat(currentversion) >= Float.parseFloat(preVersion)) {
                Log.d(this.TAG, "isVersionUpdated: true");
                return true;
            }
            Log.d(this.TAG, "isVersionUpdated: false");
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected void onResume() {
        super.onResume();
        OnjybApp.activityResumed();
    }

    protected void onPause() {
        super.onPause();
        OnjybApp.activityPaused();
    }
}

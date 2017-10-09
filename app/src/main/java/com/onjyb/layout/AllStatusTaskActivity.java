package com.onjyb.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechTextView;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.Preference;

public class AllStatusTaskActivity extends BaseDrawerActivity {
    OnClickListener ApprovellClickListener = new C05853();
    OnClickListener NotApprovellClickListener = new C05875();
    OnClickListener PenddingllClickListener = new C05864();
    private ActionCallback apiCallBackUnReadCount = new C05886();
    ETechTextView btnApprove;
    ETechTextView btnNotApprove;
    ETechTextView btnPendding;
    boolean is_from_mng_jobber = false;
    OnClickListener leaveApprovellClickListener = new C05831();
    LinearLayout llApprove;
    RelativeLayout llLeaveApprove;
    LinearLayout llNotApprove;
    LinearLayout llPastLeave;
    LinearLayout llPendding;
    RelativeLayout ll_circular_btns;
    LinearLayout ll_unread_leave;
    LinearLayout ll_unread_leave_task;
    OnClickListener pastLeavellClickListener = new C05842();
    ETechTextView tvBadgeLeave;
    ETechTextView tvBadgeTask;

    class C05831 implements OnClickListener {
        C05831() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(AllStatusTaskActivity.this.context, MangerLeaveListActivity.class);
            intent.setFlags(131072);
            intent.putExtra("Status", LeaveLisingActivity.APPROVE_STATUS_PENDING);
            Preference.setSharedPref(Constants.PREF_UNREAD_MANAGER_APPROVE_leave, "0");
            AllStatusTaskActivity.this.startActivity(intent);
        }
    }

    class C05842 implements OnClickListener {
        C05842() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(AllStatusTaskActivity.this.context, MangerLeaveListActivity.class);
            intent.setFlags(131072);
            intent.putExtra("Status", LeaveLisingActivity.APPROVE_STATUS_NOT_PENDING);
            AllStatusTaskActivity.this.startActivity(intent);
        }
    }

    class C05853 implements OnClickListener {
        C05853() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(AllStatusTaskActivity.this.context, MyTaskActivity.class);
            intent.setFlags(131072);
            if (Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                intent.putExtra("Status", MyTaskActivity.APPROVE_STATUS_APPROVE);
            } else {
                if (AllStatusTaskActivity.this.is_from_mng_jobber) {
                    intent.putExtra("Status", MyTaskActivity.APPROVE_STATUS_APPROVE);
                } else {
                    intent.putExtra("Status", MyTaskActivity.APPROVE_STATUS_NOT_PENDING);
                }
                intent.putExtra("is_from_mng_jobber", AllStatusTaskActivity.this.is_from_mng_jobber);
            }
            AllStatusTaskActivity.this.startActivity(intent);
        }
    }

    class C05864 implements OnClickListener {
        C05864() {
        }

        public void onClick(View v) {
            String roleid = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
            Intent intent = new Intent();
            intent.setClass(AllStatusTaskActivity.this.context, MyTaskActivity.class);
            intent.setFlags(131072);
            intent.putExtra("Status", MyTaskActivity.APPROVE_STATUS_PENDING);
            intent.putExtra("is_from_mng_jobber", AllStatusTaskActivity.this.is_from_mng_jobber);
            AllStatusTaskActivity.this.startActivity(intent);
        }
    }

    class C05875 implements OnClickListener {
        C05875() {
        }

        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(AllStatusTaskActivity.this.context, MyTaskActivity.class);
            intent.setFlags(131072);
            intent.putExtra("Status", MyTaskActivity.APPROVE_STATUS_REJECT);
            intent.putExtra("is_from_mng_jobber", AllStatusTaskActivity.this.is_from_mng_jobber);
            AllStatusTaskActivity.this.startActivity(intent);
        }
    }

    class C05886 implements ActionCallback {
        C05886() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (AllStatusTaskActivity.this.progressHelper != null) {
                AllStatusTaskActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                AllStatusTaskActivity.this.setBadge();
            }
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        updateviewMethod();
    }

    private void updateviewMethod() {
        setContentView(R.layout.activity_all_status_task);
        this.header.setTitle(getString(R.string.txt_my_Task));
        this.header.hideRightBtn();
        getViews();
        this.is_from_mng_jobber = getIntent().getBooleanExtra("is_from_mng_jobber", false);
        setBadge();
        if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && !this.is_from_mng_jobber) {
            setlayout();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_status_task);
        this.header.setTitle(getString(R.string.txt_my_Task));
        this.header.hideRightBtn();
        getViews();
        this.is_from_mng_jobber = getIntent().getBooleanExtra("is_from_mng_jobber", false);
        setBadge();
        if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && !this.is_from_mng_jobber) {
            setlayout();
        }
    }

    private void setBadge() {
        String task = Preference.getSharedPref(Constants.PREF_UNREAD_WORK_COUNT, "");
        String leave = Preference.getSharedPref(Constants.PREF_UNREAD_MANAGER_APPROVE_leave, "");
        String roleid = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        String managertask = Preference.getSharedPref(Constants.PREF_UNREAD_MANAGER_APPROVE_TASK, "");
        if ((roleid.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || !this.is_from_mng_jobber) && !roleid.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
            if (!roleid.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                if (managertask == null || managertask.length() <= 0 || managertask.equalsIgnoreCase("0")) {
                    this.ll_unread_leave_task.setVisibility(View.GONE);
                } else {
                    this.ll_unread_leave_task.setVisibility(View.VISIBLE);
                    if (Integer.parseInt(managertask) > 99) {
                        this.tvBadgeTask.setText("99+");
                    } else {
                        this.tvBadgeTask.setText(managertask);
                    }
                }
            }
        } else if (task == null || task.length() <= 0 || task.equalsIgnoreCase("0")) {
            this.ll_unread_leave_task.setVisibility(View.GONE);
        } else {
            this.ll_unread_leave_task.setVisibility(View.VISIBLE);
            if (Integer.parseInt(task) > 99) {
                this.tvBadgeTask.setText("99+");
            } else {
                this.tvBadgeTask.setText(task);
            }
        }
        if (!roleid.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
            if (leave == null || leave.length() <= 0 || leave.equalsIgnoreCase("0")) {
                this.ll_unread_leave.setVisibility(View.GONE);
                return;
            }
            this.ll_unread_leave.setVisibility(View.VISIBLE);
            if (Integer.parseInt(leave) > 99) {
                this.tvBadgeLeave.setText("99+");
            } else {
                this.tvBadgeLeave.setText(leave);
            }
        }
    }

    private void setlayout() {
        this.header.setTitle(getString(R.string.txt_my_Task));
        this.llNotApprove.setVisibility(View.GONE);
        this.ll_circular_btns.setVisibility(View.VISIBLE);
        this.btnApprove.setText(getResources().getString(R.string.past_task_new));
        this.btnPendding.setText(getResources().getString(R.string.new_task));
    }

    private void getViews() {
        this.llApprove = (LinearLayout) findViewById(R.id.lin_approve);
        this.llPendding = (LinearLayout) findViewById(R.id.lin_Pendding);
        this.llNotApprove = (LinearLayout) findViewById(R.id.lin_notApprove);
        this.llLeaveApprove = (RelativeLayout) findViewById(R.id.lin_leave_approve);
        this.llPastLeave = (LinearLayout) findViewById(R.id.lin_leave_past);
        this.btnApprove = (ETechTextView) findViewById(R.id.approvebtn);
        this.btnPendding = (ETechTextView) findViewById(R.id.btnpendding);
        this.btnNotApprove = (ETechTextView) findViewById(R.id.btnnotApprove);
        this.ll_circular_btns = (RelativeLayout) findViewById(R.id.ll_circular_btns);
        this.ll_unread_leave = (LinearLayout) findViewById(R.id.ll_unread_leave_leave);
        this.ll_unread_leave_task = (LinearLayout) findViewById(R.id.ll_unread_leave_task);
        this.tvBadgeLeave = (ETechTextView) findViewById(R.id.txtBadgeleave);
        this.tvBadgeTask = (ETechTextView) findViewById(R.id.txtBadgeleaveTask);
        this.llApprove.setOnClickListener(this.ApprovellClickListener);
        this.llPendding.setOnClickListener(this.PenddingllClickListener);
        this.llNotApprove.setOnClickListener(this.NotApprovellClickListener);
        this.llLeaveApprove.setOnClickListener(this.leaveApprovellClickListener);
        this.llPastLeave.setOnClickListener(this.pastLeavellClickListener);
    }

    protected void onResume() {
        super.onResume();
        new UserHelper(this.context).apiGetUnReadCount(this.apiCallBackUnReadCount);
    }
}

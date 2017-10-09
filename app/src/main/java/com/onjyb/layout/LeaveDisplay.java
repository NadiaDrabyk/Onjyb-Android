package com.onjyb.layout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.beans.LeavesApiHelper;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.Header;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.LeaveType;
import com.onjyb.helper.MyBroadcastReceiver;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AlertDialogHelper.OnMyDialogResult;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;

public class LeaveDisplay extends Activity implements OnMyDialogResult {
    ETechTextView EndDate;
    private String TAG = LeaveDisplay.class.getName();
    private ActionCallback apiCallBackUnReadCount = new C06615();
    private ActionCallback approveLeaveApiCallBack = new C06636();
    ETechButton btnApply;
    ETechButton btnCancel;
    ETechButton btnUpade;
    OnClickListener cancelLeaveBtnClickListener = new C06604();
    private Context context = this;
    DatabaseHelper dbHelper;
    ETechEditText edtNote;
    ImageButton imgvEndDate;
    ImageButton imgvStartDate;
    boolean is_from_mng_leave = false;
    LeaveType leaveType = new LeaveType();
    HashMap<String, String> leaveTypeId;
    List<String> leaveTypeList;
    LinearLayout ll_btn_update;
    ImageView profileImg;
    ProgressHelper progressHelper;
    HashMap<String, String> projectid;
    ETechTextView strtDate;
    ETechTextView tvCommentsText;
    ETechTextView tvLVcomment;
    ETechTextView tvLeaveDasys;
    ETechEditText tvLeaveReson;
    ETechEditText tvLeaveTiltle;
    ETechTextView tvLeftDays;
    TextView tvNoComment;
    TextView tvNoImgs;
    TextView tvNoNote;
    ETechTextView tvNoteText;
    ETechTextView tvProjectName;
    ETechTextView tvRequestedDate;
    ETechTextView tvUserName;
    ETechTextView tvleaveType;
    ETechTextView tvnote;
    ETechTextView txtstatuslbl;

    LinearLayout ll_pto_hours;
    ETechTextView tvPTOHours;

    OnClickListener updateLeaveBtnClickListener = new C06573();

    class C06542 implements OnClickListener {
        C06542() {
        }

        public void onClick(View v) {
            LeaveDisplay.this.finish();
        }
    }

    class C06573 implements OnClickListener {

        class C06551 implements OnClickListener {
            C06551() {
            }

            public void onClick(View v) {
            }
        }

        class C06562 implements OnClickListener {
            C06562() {
            }

            public void onClick(View v) {
            }
        }

        C06573() {
        }

        public void onClick(View v) {
            if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && LeaveDisplay.this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                AlertDialogHelper.getConfirmationAlert(LeaveDisplay.this.context, LeaveDisplay.this.getString(R.string.app_name), new C06551(), new C06562(), LeaveDisplay.this.getResources().getString(R.string.are_you_sure_approve_leave), 0, 2, false, LeaveDisplay.this.getResources().getString(R.string.txt_yes), LeaveDisplay.this.getResources().getString(R.string.txt_no), -1, -1);
            }
        }
    }

    class C06604 implements OnClickListener {

        class C06581 implements OnClickListener {
            C06581() {
            }

            public void onClick(View v) {
            }
        }

        class C06592 implements OnClickListener {
            C06592() {
            }

            public void onClick(View v) {
            }
        }

        C06604() {
        }

        public void onClick(View v) {
            if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && LeaveDisplay.this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                AlertDialogHelper.getConfirmationAlert(LeaveDisplay.this.context, LeaveDisplay.this.getString(R.string.app_name), new C06581(), new C06592(), LeaveDisplay.this.getResources().getString(R.string.alert_reject_lv), 0, 1, false, LeaveDisplay.this.getResources().getString(R.string.txt_yes), LeaveDisplay.this.getResources().getString(R.string.txt_no), -1, -1);
            }
        }
    }

    class C06615 implements ActionCallback {
        C06615() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                Log.d(LeaveDisplay.this.TAG, "UNREADCOUNT---Success");
                Intent i = new Intent("any string");
                i.setClass(LeaveDisplay.this.context, MyBroadcastReceiver.class);
                LeaveDisplay.this.sendBroadcast(i);
            }
        }
    }

    class C06636 implements ActionCallback {
        C06636() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (LeaveDisplay.this.progressHelper != null) {
                LeaveDisplay.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    new UserHelper(LeaveDisplay.this.context).apiGetUnReadCount(LeaveDisplay.this.apiCallBackUnReadCount);
                    String msg = ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG);
                    final Dialog finalDialog = new Dialog(LeaveDisplay.this.context);
                    AlertDialogHelper.getNotificatonAlert(LeaveDisplay.this.context, LeaveDisplay.this.getString(R.string.app_name), new OnClickListener() {
                        public void onClick(View v) {
                            finalDialog.dismiss();
                            //jin LeaveDisplay.this.startActivity(new Intent(LeaveDisplay.this, EmpHomeActivity.class));
                            LeaveDisplay.this.finish();
                        }
                    }, msg, 0, 0, true);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(LeaveDisplay.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_display);
        AppUtils.setStatusbarColor(this);
        setHeader();
        getViews();
        this.leaveType = (LeaveType) getIntent().getSerializableExtra("LeaveDetails");
        this.is_from_mng_leave = getIntent().getBooleanExtra("is_from_mng_leave", false);
        if (this.leaveType != null) {
            String master = this.leaveType.getLeave_master_type();
            if(master.equalsIgnoreCase("ptobank")){
                this.ll_pto_hours.setVisibility(View.VISIBLE);
                String total = this.leaveType.getTotal_hours();
                int nTotal = Integer.parseInt(total);
                int h = (int)(nTotal / 60);
                int m = (int)(nTotal % 60);
                this.tvPTOHours.setText(String.format("%02d:%02d", h, m));

            }else{
                this.ll_pto_hours.setVisibility(View.GONE);
            }
            String leftLeave = Preference.getSharedPref(Constants.PREF_LEFT_LEAVE, "0");
            if (leftLeave != null) {
                this.tvLeftDays.setText(leftLeave + " " + getResources().getString(R.string.txt_days));
            }
            if (!(this.leaveType.getLeavetypename() == null || this.leaveType.getLeavetypename().equalsIgnoreCase(""))) {
                this.tvleaveType.setText(this.leaveType.getLeavetypename());
            }
            if (!(this.leaveType.getProject_id() == null || this.leaveType.getProject_id().equalsIgnoreCase(""))) {
                String qry = "select * from tblproject where project_id = '" + this.leaveType.getProject_id() + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
                HashMap<String, String> project = this.dbHelper.selSingleRecordFromDB(qry, null);
                if (project != null) {
                    this.tvProjectName.setText((String) project.get("project_name"));
                }
            }
            String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || this.is_from_mng_leave) {
                this.profileImg.setVisibility(View.GONE);
                this.tvUserName.setVisibility(View.GONE);
            } else {
                if (!(this.leaveType.getFirstName() == null || this.leaveType.getFirstName().equalsIgnoreCase(""))) {
                    this.tvUserName.setText(this.leaveType.getFirstName());
                }
                if (!(this.leaveType.getProfile_image() == null || this.leaveType.getProfile_image().equalsIgnoreCase(""))) {
                    Glide.with(this.context).load(Constants.BASE_IMAGE_URL + this.leaveType.getProfile_image()).asBitmap().centerCrop().placeholder((int) R.drawable.profile_pic).error((int) R.drawable.profile_pic).into(new BitmapImageViewTarget(this.profileImg) {
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(LeaveDisplay.this.context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            LeaveDisplay.this.profileImg.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }
            this.tvLeaveReson.setText(this.leaveType.getLvReason());
            this.edtNote.setText(this.leaveType.getNote());
            SimpleDateFormat input1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                this.tvRequestedDate.setText(new SimpleDateFormat("dd MMM yy").format(input1.parse(this.leaveType.getCreateDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                this.btnUpade.setText(getResources().getString(R.string.btn_txt_accept));
                this.btnCancel.setText(getResources().getString(R.string.btn_txt_reject));
            }
            if (!user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && !this.is_from_mng_leave) {
                this.txtstatuslbl.setText(getResources().getString(R.string.absent_vacation));
                this.tvLVcomment.setText(getResources().getString(R.string.employee_note));
                this.tvLeaveReson.setVisibility(View.GONE);
                if (this.leaveType.getLvReason().equalsIgnoreCase("")) {
                    this.tvNoComment.setVisibility(View.VISIBLE);
                } else {
                    this.tvCommentsText.setVisibility(View.VISIBLE);
                    this.tvCommentsText.setText(this.leaveType.getLvReason());
                }
                this.tvnote.setVisibility(View.VISIBLE);
                this.edtNote.setVisibility(View.VISIBLE);
                if (this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                    this.btnUpade.setText(getResources().getString(R.string.btn_txt_accept));
                    this.btnCancel.setText(getResources().getString(R.string.btn_txt_reject));
                } else {
                    this.ll_btn_update.setVisibility(View.GONE);
                    this.edtNote.setVisibility(View.GONE);
                    if (this.leaveType.getNote().equalsIgnoreCase("")) {
                        this.tvNoNote.setVisibility(View.VISIBLE);
                    } else {
                        this.tvNoteText.setVisibility(View.VISIBLE);
                        this.tvNoteText.setText(this.leaveType.getNote());
                    }
                }
            } else if (!this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                if (this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                    this.txtstatuslbl.setText(getResources().getString(R.string.pendding_leave));
                } else if (this.leaveType.getApproveStatus().equalsIgnoreCase("approve")) {
                    this.txtstatuslbl.setText(getResources().getString(R.string.approved_leave));
                } else if (this.leaveType.getApproveStatus().equalsIgnoreCase("reject")) {
                    this.txtstatuslbl.setText(getResources().getString(R.string.rejected_leave));
                } else {
                    this.txtstatuslbl.setText(getResources().getString(R.string.cancel_leave));
                }
                this.ll_btn_update.setVisibility(View.GONE);
                this.edtNote.setVisibility(View.GONE);
                this.tvLeaveReson.setVisibility(View.GONE);
                this.tvnote.setVisibility(View.VISIBLE);
                this.tvnote.setText(getResources().getString(R.string.managernote));
                this.tvLVcomment.setText(getResources().getString(R.string.employee_note));
                if (this.leaveType.getLvReason().equalsIgnoreCase("")) {
                    this.tvNoComment.setVisibility(View.VISIBLE);
                } else {
                    this.tvCommentsText.setVisibility(View.VISIBLE);
                    this.tvCommentsText.setText(this.leaveType.getLvReason());
                }
                if (this.leaveType.getNote().equalsIgnoreCase("")) {
                    this.tvNoNote.setVisibility(View.VISIBLE);
                } else {
                    this.tvNoteText.setVisibility(View.VISIBLE);
                    this.tvNoteText.setText(this.leaveType.getNote());
                }
            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat(" dd MMM yy");
            try {
                Date date1 = input.parse(this.leaveType.getLeaveFromDt());
                Date date2 = input.parse(this.leaveType.getLeaveTillDt());
                String days = getDateDiffString(date1, date2);
                String str1 = output.format(date1);
                String str2 = output.format(date2);
                this.strtDate.setText(str1);
                this.EndDate.setText(str2);
                if (days != null && Integer.parseInt(days) > 0) {
                    setDays(this.tvLeaveDasys, date1, date2);
                }
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
        }
        this.btnCancel.setOnClickListener(this.cancelLeaveBtnClickListener);
        this.btnUpade.setOnClickListener(this.updateLeaveBtnClickListener);
    }

    public void setDays(TextView tv, Date strdate, Date endDate) {
        try {
            String days = getDateDiffString(strdate, endDate);
            int businessDayCount = AppUtils.getBuissnesDays(strdate, endDate);
            Log.d(this.TAG, "Business Day: " + businessDayCount);
            if (this.leaveTypeId != null) {
                if (((String) this.leaveTypeId.get("leave_master_type")).equalsIgnoreCase("vacation")) {
                    tv.setText(businessDayCount + " " + getResources().getString(R.string.txt_days));
                } else {
                    tv.setText(days + " " + getResources().getString(R.string.txt_days));
                }
            } else if (this.leaveType == null) {
                tv.setText(days + " " + getResources().getString(R.string.txt_days));
            } else if (this.leaveType.getLeave_master_type().equalsIgnoreCase("vacation")) {
                tv.setText(businessDayCount + " " + getResources().getString(R.string.txt_days));
            } else {
                tv.setText(days + " " + getResources().getString(R.string.txt_days));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.TAG, e.getMessage());
        }
    }

    private void setHeader() {
        try {
            Header header = (Header) findViewById(R.id.headerleave);
            header.setLeftBtnImage(R.drawable.ic_back_arrow);
            header.hideRightBtn();
            header.setLeftBtnClickListener(new C06542());
            header.setTitle(getString(R.string.txt_leave));
        } catch (Exception e) {
            e.printStackTrace();
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

    public String getDateDiffString(Date dateOne, Date dateTwo) {
        long delta = (dateTwo.getTime() - dateOne.getTime()) / DateUtils.MILLIS_PER_DAY;
        if (delta >= 0) {
            return String.valueOf(1 + delta);
        }
        return String.valueOf(delta * -1);
    }

    private void getViews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.tvUserName = (ETechTextView) findViewById(R.id.txtUsername);
        this.tvleaveType = (ETechTextView) findViewById(R.id.txtleavetype);
        this.tvProjectName = (ETechTextView) findViewById(R.id.txtpname);
        this.strtDate = (ETechTextView) findViewById(R.id.txtstrtdt1);
        this.EndDate = (ETechTextView) findViewById(R.id.txttilDt);
        this.tvLeaveDasys = (ETechTextView) findViewById(R.id.txtLeaveDays);
        this.tvLeaveReson = (ETechEditText) findViewById(R.id.edtLVReason);
        this.tvRequestedDate = (ETechTextView) findViewById(R.id.txtrequestedDate);
        this.profileImg = (ImageView) findViewById(R.id.imgprofPic);
        this.ll_btn_update = (LinearLayout) findViewById(R.id.ll_leave_update);
        this.btnApply = (ETechButton) findViewById(R.id.btnapply);
        this.btnCancel = (ETechButton) findViewById(R.id.btncacelLeave);
        this.btnUpade = (ETechButton) findViewById(R.id.btnupdateLeave);
        this.txtstatuslbl = (ETechTextView) findViewById(R.id.txtStatusofHours);
        this.tvnote = (ETechTextView) findViewById(R.id.tvnote);
        this.edtNote = (ETechEditText) findViewById(R.id.edtnot);
        this.tvCommentsText = (ETechTextView) findViewById(R.id.tvdcomment1);
        this.tvLVcomment = (ETechTextView) findViewById(R.id.tvLVcomment);
        this.tvNoteText = (ETechTextView) findViewById(R.id.tvnote1);
        this.imgvStartDate = (ImageButton) findViewById(R.id.imgvstart);
        this.imgvEndDate = (ImageButton) findViewById(R.id.imgEnd);
        this.tvNoComment = (TextView) findViewById(R.id.txtNocommentsLv);
        this.tvNoNote = (TextView) findViewById(R.id.txtNoNotesLv);
        this.tvLeftDays = (ETechTextView) findViewById(R.id.txtRemainDays);
        this.ll_pto_hours = (LinearLayout) findViewById(R.id.ll_ptohours);
        this.tvPTOHours = (ETechTextView) findViewById(R.id.txtPTOHours);
    }

    public void onOkClick(int titleId1, int btnresId) {
        String status = "";
        if (btnresId == 2) {
            status = LeaveLisingActivity.APPROVE_STATUS_APPROVE;
        } else if (btnresId == 1) {
            status = LeaveLisingActivity.APPROVE_STATUS_REJECT;
        }
        this.leaveType.setNote(this.edtNote.getText().toString());
        LeavesApiHelper helper = new LeavesApiHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        helper.apiApproveLeave(status, this.leaveType, this.approveLeaveApiCallBack);
    }
}

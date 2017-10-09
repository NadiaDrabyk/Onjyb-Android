package com.onjyb.layout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.onjyb.R;
import com.onjyb.adaptor.SpinnerAdapterLeave;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.adaptor.SpinnerListAdapter;
import com.onjyb.beans.LeavesApiHelper;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular.DropDownItemClickListener;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;

public class LeaveFormActivity extends BaseDrawerActivity implements OnMyDialogResult, DropDownItemClickListener {
    private EtechEditAutoCompleteTextRobotoRegular AutoProject;
    Spinner Autoleavetypes;
    ETechTextView EndDate;
    private List<String> PnameList;
    private String TAG = LeaveFormActivity.class.getName();
    private ActionCallback addEmpLeaveFormCallBack = new C06758();
    private ActionCallback apiCallBackUnReadCount = new C06769();
    OnClickListener applyLeaveBtnClickListener = new C06673();
    private ActionCallback approveLeaveApiCallBack = new ActionCallback() {
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (LeaveFormActivity.this.progressHelper != null) {
                LeaveFormActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    String msg = ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG);
                    final String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
                    new UserHelper(LeaveFormActivity.this.context).apiGetUnReadCount(LeaveFormActivity.this.apiCallBackUnReadCount);
                    AlertDialogHelper.getNotificatonAlert(LeaveFormActivity.this.context, LeaveFormActivity.this.getString(R.string.app_name), new OnClickListener() {
                        public void onClick(View v) {
                            Intent intent;
                            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || LeaveFormActivity.this.is_from_mng_leave) {
                                intent = new Intent(LeaveFormActivity.this, LeaveLisingActivity.class);
                                if (LeaveFormActivity.this.is_from_mng_leave) {
                                    intent.putExtra("is_from_mng_leave", true);
                                }
                            } else {
                                intent = new Intent(LeaveFormActivity.this, AllStatusTaskActivity.class);
                            }
                            LeaveFormActivity.this.startActivity(intent);
                            LeaveFormActivity.this.finish();
                        }
                    }, msg, 0, 0, true);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(LeaveFormActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    };
    ETechButton btnApply;
    ETechButton btnCancel;
    ETechButton btnUpade;
    OnClickListener cancelLeaveBtnClickListener = new C06704();
    private Context context = this;
    DatabaseHelper dbHelper;
    int dcount = 0;
    View devider_caslv;
    View devider_seeklv;
    ImageView dropdownimg;
    ETechEditText edtNote;
    ImageButton imgvEndDate;
    ImageButton imgvStartDate;
    Boolean isPastDateEnable = Boolean.valueOf(false);
    boolean is_from_mng_leave = false;
    TextView lblSeekLv;
    LeaveType leaveType = new LeaveType();
    HashMap<String, String> leaveTypeId;
    List<String> leaveTypeList;
    String leaveTypename;
    LinearLayout ll_btn_update;
    LinearLayout ll_lbl_SeekLv;
    LinearLayout ll_lbl_vacLv;
    String lvFrom;
    String lvTill;
    private ArrayAdapter<String> myProjectAdapter;
    ProgressHelper progressHelper;
    HashMap<String, String> projectid;
    ETechTextView strtDate;
    ETechTextView tvCommentsText;
    ETechTextView tvLVcomment;
    ETechTextView tvLeaveDasys;
    ETechEditText tvLeaveReson;
    TextView tvLeftVac;
    TextView tvNoComment;
    TextView tvNoNote;
    ETechTextView tvNoteText;
    TextView tvPlanedVac;
    ETechTextView tvRemainDays;
    TextView tvUsedVac;
    ETechTextView tvnote;
    private String txtEndDate = "";
    private String txtStartDate = "";
    ETechTextView txtstatuslbl;
    OnClickListener updateLeaveBtnClickListener = new C06715();

    Spinner spPTOHours;
    Spinner spPTOMinutes;
    LinearLayout llPTO;
    View deviderPTO;
    String strPTOHours;
    String strPTOMinutes;
    long totalPTOMinutes;
    int nRemainTotalHours;

    class C06651 implements OnClickListener {
        C06651() {
        }

        public void onClick(View v) {
            LeaveFormActivity.this.finish();
        }
    }

    class C06662 implements OnClickListener {
        C06662() {
        }

        public void onClick(View v) {
            LeaveFormActivity.this.finish();
        }
    }

    class C06673 implements OnClickListener {
        C06673() {
        }

        public void onClick(View v) {
            try {
                AppUtils.hideKeyBoard(LeaveFormActivity.this);
                String str = LeaveFormActivity.this.validateData();
                if (str == null) {
                    LeaveType leaveType1 = new LeaveType();
                    leaveType1.setLeavetypename(LeaveFormActivity.this.Autoleavetypes.getSelectedItem().toString());
                    leaveType1.setLeaveFromDt(LeaveFormActivity.this.strtDate.getText().toString());
                    leaveType1.setLeaveTillDt(LeaveFormActivity.this.EndDate.getText().toString());
                    leaveType1.setLvReason(LeaveFormActivity.this.tvLeaveReson.getText().toString());
                    leaveType1.setLeaveDays(LeaveFormActivity.this.tvLeaveDasys.getText().toString());
                    if (LeaveFormActivity.this.projectid != null) {
                        leaveType1.setProject_id((String) LeaveFormActivity.this.projectid.get("project_id"));
                    }
                    leaveType1.setProject_name(LeaveFormActivity.this.AutoProject.getText().toString());
                    if (LeaveFormActivity.this.leaveTypeId != null || LeaveFormActivity.this.leaveType == null) {
                        leaveType1.setLeaveTypeId((String) LeaveFormActivity.this.leaveTypeId.get("leavetype_id"));
                    } else {
                        leaveType1.setLeaveTypeId(LeaveFormActivity.this.leaveType.getRefLeaveTypeId());
                    }

                    String master = LeaveFormActivity.this.leaveTypeId.get("leave_master_type");

                    if(master.equalsIgnoreCase("ptobank")){

                        int days = (int)(nRemainTotalHours / (60 * 7.5));
                        int left = (int)(nRemainTotalHours % (60 * 7.5));
                        if(left > 0)
                            days++;
                        if(days < LeaveFormActivity.this.dcount || totalPTOMinutes > nRemainTotalHours) {
                            AlertDialogHelper.getNotificatonAlert(LeaveFormActivity.this.context,
                                    LeaveFormActivity.this.getString(R.string.app_name),
                                    LeaveFormActivity.this.getString(R.string.str_cant_select_more));
                            return ;
                        }
                        else if(totalPTOMinutes == 0){
                            AlertDialogHelper.getNotificatonAlert(LeaveFormActivity.this.context,
                                    LeaveFormActivity.this.getString(R.string.app_name),
                                    LeaveFormActivity.this.getString(R.string.txt_Enter_service_time));
                            return ;
                        }
                        String total = String.format("%02d:%02d", totalPTOMinutes / 60, totalPTOMinutes % 60);
                        leaveType1.setTotal_hours(total);

                    }
                    LeavesApiHelper leavesApiHelper = new LeavesApiHelper(LeaveFormActivity.this.context);
                    LeaveFormActivity.this.progressHelper.showDialog("Loading..");
                    leavesApiHelper.apiAddEmpLeaveForm(leaveType1, LeaveFormActivity.this.addEmpLeaveFormCallBack);
                    return;
                }
                AlertDialogHelper.getNotificatonAlert(LeaveFormActivity.this.context, LeaveFormActivity.this.getString(R.string.app_name), str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C06704 implements OnClickListener {

        class C06681 implements OnClickListener {
            C06681() {
            }

            public void onClick(View v) {
            }
        }

        class C06692 implements OnClickListener {
            C06692() {
            }

            public void onClick(View v) {
            }
        }

        C06704() {
        }

        public void onClick(View v) {
            if (Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || LeaveFormActivity.this.is_from_mng_leave) {
                if (LeaveFormActivity.this.leaveType.getApproveStatus().equalsIgnoreCase("reject")) {
                    LeaveFormActivity.this.ll_btn_update.setVisibility(View.GONE);
                } else {
                    AlertDialogHelper.getConfirmationAlert(LeaveFormActivity.this.context, LeaveFormActivity.this.getString(R.string.app_name), new C06681(), new C06692(), LeaveFormActivity.this.getResources().getString(R.string.alert_cancel_lv), 0, 1, false, LeaveFormActivity.this.getResources().getString(R.string.txt_yes), LeaveFormActivity.this.getResources().getString(R.string.txt_no), -1, -1);
                }
            } else if (LeaveFormActivity.this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                String status = LeaveLisingActivity.APPROVE_STATUS_REJECT;
                LeavesApiHelper helper = new LeavesApiHelper(LeaveFormActivity.this.context);
                LeaveFormActivity.this.progressHelper.showDialog("Loading..");
                helper.apiApproveLeave(status, LeaveFormActivity.this.leaveType, LeaveFormActivity.this.approveLeaveApiCallBack);
            }
        }
    }

    class C06715 implements OnClickListener {
        C06715() {
        }

        public void onClick(View v) {
            if (Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || LeaveFormActivity.this.is_from_mng_leave) {
                if (LeaveFormActivity.this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                    String str = LeaveFormActivity.this.validateData();
                    if (str == null) {
                        LeaveType leaveType1 = new LeaveType();
                        leaveType1.setLeavetypename(LeaveFormActivity.this.Autoleavetypes.getSelectedItem().toString());
                        leaveType1.setLeaveFromDt(LeaveFormActivity.this.strtDate.getText().toString());
                        leaveType1.setLeaveTillDt(LeaveFormActivity.this.EndDate.getText().toString());
                        leaveType1.setLvReason(LeaveFormActivity.this.tvLeaveReson.getText().toString());
                        leaveType1.setLeaveDays(LeaveFormActivity.this.tvLeaveDasys.getText().toString());
                        leaveType1.setLeaveId(LeaveFormActivity.this.leaveType.getLeaveId());
                        leaveType1.setApproveStatus(LeaveFormActivity.this.leaveType.getApproveStatus());
                        if (LeaveFormActivity.this.projectid != null) {
                            leaveType1.setProject_id((String) LeaveFormActivity.this.projectid.get("project_id"));
                        } else {
                            leaveType1.setProject_id(LeaveFormActivity.this.leaveType.getProject_id());
                        }
                        leaveType1.setProject_name(LeaveFormActivity.this.AutoProject.getText().toString());
                        if (LeaveFormActivity.this.leaveTypeId != null || LeaveFormActivity.this.leaveType == null) {
                            leaveType1.setLeaveTypeId((String) LeaveFormActivity.this.leaveTypeId.get("leavetype_id"));
                        } else {
                            leaveType1.setLeaveTypeId(LeaveFormActivity.this.leaveType.getRefLeaveTypeId());
                        }
                        LeavesApiHelper leavesApiHelper = new LeavesApiHelper(LeaveFormActivity.this.context);
                        LeaveFormActivity.this.progressHelper.showDialog("Loading..");
                        leavesApiHelper.apiAddEmpLeaveForm(leaveType1, LeaveFormActivity.this.addEmpLeaveFormCallBack);
                        return;
                    }
                    AlertDialogHelper.getNotificatonAlert(LeaveFormActivity.this.context, LeaveFormActivity.this.getString(R.string.app_name), str);
                }
            } else if (LeaveFormActivity.this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                String status = LeaveLisingActivity.APPROVE_STATUS_APPROVE;
                LeaveFormActivity.this.leaveType.setNote(LeaveFormActivity.this.edtNote.getText().toString());
                LeavesApiHelper helper = new LeavesApiHelper(LeaveFormActivity.this.context);
                LeaveFormActivity.this.progressHelper.showDialog("Loading..");
                helper.apiApproveLeave(status, LeaveFormActivity.this.leaveType, LeaveFormActivity.this.approveLeaveApiCallBack);
            }
        }
    }

    class C06726 implements OnItemSelectedListener {
        C06726() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            String qry = "select * from tblleavetype where leave_type='" + LeaveFormActivity.this.Autoleavetypes.getSelectedItem().toString() + "'";
            LeaveFormActivity.this.dbHelper = new DatabaseHelper(LeaveFormActivity.this.context);
            LeaveFormActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(LeaveFormActivity.this.context);
            LeaveFormActivity.this.leaveTypeId = LeaveFormActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(LeaveFormActivity.this);
            if (LeaveFormActivity.this.leaveTypeId != null && LeaveFormActivity.this.txtStartDate != null && !LeaveFormActivity.this.txtStartDate.isEmpty() && LeaveFormActivity.this.txtEndDate != null && !LeaveFormActivity.this.txtEndDate.isEmpty()) {
                try {
                    SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
                    LeaveFormActivity.this.setDays(input.parse(LeaveFormActivity.this.txtStartDate), input.parse(LeaveFormActivity.this.txtEndDate));
                } catch (Exception e) {
                }
            } else if (LeaveFormActivity.this.leaveType == null) {
                LeaveFormActivity.this.tvLeaveDasys.setText("0 " + LeaveFormActivity.this.getResources().getString(R.string.txt_days));
                LeaveFormActivity.this.dcount = 0;
            }
            try {
                String masterType = LeaveFormActivity.this.leaveTypeId.get("leave_master_type");

                String days = getResources().getString(R.string.txt_days);
                LeaveType vacation = null, sick = null, ptobank = null;
                if (OnjybApp.leaveTypesArray != null && OnjybApp.leaveTypesArray.size() > 0) {
                    int i = 0;
                    while (i < OnjybApp.leaveTypesArray.size()) {
                        LeaveType leaveType = OnjybApp.leaveTypesArray.get(i);
                        String master = leaveType.getLeave_master_type();
                        if(master.equalsIgnoreCase("vacation"))
                            vacation = leaveType;
                        else if(master.equalsIgnoreCase("sick"))
                            sick = leaveType;
                        else if(master.equalsIgnoreCase("ptobank"))
                            ptobank = leaveType;
                        i++;
                    }
                }
                if(masterType.equalsIgnoreCase("ptobank") == true){
                    llPTO.setVisibility(View.VISIBLE);
                    deviderPTO.setVisibility(View.VISIBLE);

                    if(ptobank != null){
                        String sHour = getResources().getString(R.string.txt_h);
                        String sMin = getResources().getString(R.string.txt_m);

                        int nUsedMins = Integer.parseInt(ptobank.getUsed_leave());
                        int nPlaned = Integer.parseInt(ptobank.getPlaned_leave());
                        nRemainTotalHours = Integer.parseInt(ptobank.getTotal_remaining_leave());
                        String sUsedMins = nUsedMins / 60 + " " + sHour + " " + nUsedMins % 60 + " " + sMin;
                        String sPlanedMins = nPlaned / 60 + " " + sHour + " " + nPlaned % 60 + " " + sMin;
                        String sRemainMins = nRemainTotalHours / 60 + " " + sHour + " " + nRemainTotalHours % 60 + " " + sMin;

                        tvUsedVac.setText(getResources().getString(R.string.str_used) + " " + sUsedMins);
                        tvPlanedVac.setText(getResources().getString(R.string.str_planed) + " " + sPlanedMins);
                        tvLeftVac.setText(getResources().getString(R.string.str_left) + " " + sRemainMins);
                    }

                }else{
                    llPTO.setVisibility(View.GONE);
                    deviderPTO.setVisibility(View.GONE);

                    if(masterType.equalsIgnoreCase("vacation") && vacation != null) {
                        tvUsedVac.setText(getResources().getString(R.string.str_usedvacation) + " " + vacation.getUsed_leave() + " " + days);
                        tvPlanedVac.setText(getResources().getString(R.string.str_planedvacation) + " " + vacation.getPlaned_leave() + " " + days);
                        tvLeftVac.setText(getResources().getString(R.string.str_leftvacation) + " " + vacation.getTotal_remaining_leave() + " " + days);

                    }else if(masterType.equalsIgnoreCase("sick") && sick != null){
                        tvUsedVac.setText(getResources().getString(R.string.str_used) + " " + sick.getUsed_leave() + " " + days);
                        tvPlanedVac.setText(getResources().getString(R.string.str_planed) + " " + sick.getPlaned_leave() + " " + days);
                        tvLeftVac.setText(getResources().getString(R.string.str_left) + " " + sick.getTotal_remaining_leave() + " " + days);
                    }



                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06737 implements OnGlobalLayoutListener {
        C06737() {
        }

        @TargetApi(16)
        public void onGlobalLayout() {
            LeaveFormActivity.this.AutoProject.setDropDownWidth(LeaveFormActivity.this.AutoProject.getWidth());
            LeaveFormActivity.this.AutoProject.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    class C06758 implements ActionCallback {
        C06758() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (LeaveFormActivity.this.progressHelper != null) {
                LeaveFormActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    new UserHelper(LeaveFormActivity.this.context).apiGetUnReadCount(LeaveFormActivity.this.apiCallBackUnReadCount);
                    final String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
                    String resMSG = ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG);
                    LeaveFormActivity.this.setclearViews();
                    AlertDialogHelper.getNotificatonAlert(LeaveFormActivity.this.context, LeaveFormActivity.this.getString(R.string.app_name), new OnClickListener() {
                        public void onClick(View v) {
                            Intent intent;
                            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || LeaveFormActivity.this.is_from_mng_leave) {
                                intent = new Intent(LeaveFormActivity.this, LeaveLisingActivity.class);
                                if (LeaveFormActivity.this.is_from_mng_leave) {
                                    intent.putExtra("is_from_mng_leave", true);
                                }
                            } else {
                                intent = new Intent(LeaveFormActivity.this, AllStatusTaskActivity.class);
                            }
                            LeaveFormActivity.this.startActivity(intent);
                            LeaveFormActivity.this.finish();
                        }
                    }, resMSG, 0, 0, true);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(LeaveFormActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    }

    class C06769 implements ActionCallback {
        C06769() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                Log.d(LeaveFormActivity.this.TAG, "UNREADCOUNT---Success");
                Intent i = new Intent("any string");
                i.setClass(LeaveFormActivity.this.context, MyBroadcastReceiver.class);
                LeaveFormActivity.this.sendBroadcast(i);
            }
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment1 extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            //jin
//            if (LeaveFormActivity.this.strtDate != null && LeaveFormActivity.this.strtDate.getText().toString().length() > 0) {
//                try {
//                    dp.getDatePicker().setMinDate(new SimpleDateFormat("dd/MM/yyyy").parse(LeaveFormActivity.this.txtStartDate).getTime());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            ((LeaveFormActivity)getActivity()).setMinDate(dp);
            return dp;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((LeaveFormActivity)getActivity()).populateSetDate1(yy, mm + 1, dd);
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment extends DialogFragment implements OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        @Override
        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((LeaveFormActivity)getActivity()).populateSetDate(yy, mm + 1, dd);
        }
    }
    public void setMinDate(DatePickerDialog dp){
        if (LeaveFormActivity.this.strtDate != null && LeaveFormActivity.this.strtDate.getText().toString().length() > 0) {
            try {
                long min = new SimpleDateFormat("dd/MM/yyyy").parse(LeaveFormActivity.this.txtStartDate).getTime();
                dp.getDatePicker().setMinDate(min);

                String master = LeaveFormActivity.this.leaveTypeId.get("leave_master_type");

                if(master.equalsIgnoreCase("ptobank")){

                    int days = (int)(nRemainTotalHours / (60 * 7.5));
                    int left = (int)(nRemainTotalHours % (60 * 7.5));
                    if(left > 0)
                        days++;
                    long max = min;
                    dp.getDatePicker().setMaxDate(max + days * (3600 * 24));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        updateViewMethod();
    }

    private void updateViewMethod() {
        setContentView(R.layout.activity_leave_form);
        this.header.setTitle(getString(R.string.title_activity_leave_form));
        boolean isFromMenu = getIntent().getBooleanExtra("isFromMenu", false);
        this.is_from_mng_leave = getIntent().getBooleanExtra("is_from_mng_leave", false);
        if (isFromMenu) {
            this.header.setLeftBtnImage(R.drawable.ic_back_arrow);
            this.header.setLeftBtnClickListener(new C06651());
        }
        this.header.hideRightBtn();
        getViews();
        this.tvLeaveDasys.setText("0 " + getResources().getString(R.string.txt_days));
        this.dcount = 0;
        this.txtEndDate = "";
        this.txtStartDate = "";
        this.leaveType = null;
        this.leaveTypeList = getListFromLocalDB("tblleavetype", "leave_type");
        if (this.leaveTypeList != null) {
            getLeaveTypeList(this.leaveTypeList);
        }
        this.PnameList = getListFromLocalDB("tblproject", "project_name", "active", "project_status");
        if (this.PnameList != null) {
            getProjectListAdapter(this.PnameList);
        }
        if (OnjybApp.leaveTypesArray != null && OnjybApp.leaveTypesArray.size() > 0) {
            int i = 0;
            while (i < OnjybApp.leaveTypesArray.size()) {
                if (((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type() != null && ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type().equalsIgnoreCase("vacation")) {
                    this.tvUsedVac.setText(getResources().getString(R.string.txt_Used_vacation) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getUsed_leave() + " " + getResources().getString(R.string.txt_days));
                    this.tvPlanedVac.setText(getResources().getString(R.string.txt_Planed_vacation) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getPlaned_leave() + " " + getResources().getString(R.string.txt_days));
                    this.tvLeftVac.setText(getResources().getString(R.string.txt_Left_vacation) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_remaining_leave() + " " + getResources().getString(R.string.txt_days));
                } else if (((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type() != null && ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type().equalsIgnoreCase("sick")) {
                    this.lblSeekLv.setText(getResources().getString(R.string.txt_total_vacation_hrs) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_leave() + " " + getResources().getString(R.string.txt_days) + ", " + getResources().getString(R.string.txt_total_use_this_year) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_earn_leave() + getResources().getString(R.string.txt_days) + " , " + getResources().getString(R.string.txt_left) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_remaining_leave() + " " + getResources().getString(R.string.txt_days));
                }
                i++;
            }
        }
        if (this.leaveType != null) {
            if (Preference.getSharedPref(Constants.PREF_LEFT_LEAVE, "0") != null) {
                this.btnApply.setVisibility(View.GONE);
                this.ll_lbl_vacLv.setVisibility(View.GONE);
                this.ll_btn_update.setVisibility(View.VISIBLE);
            } else {
                this.btnApply.setVisibility(View.GONE);
                this.ll_lbl_vacLv.setVisibility(View.GONE);
                this.ll_btn_update.setVisibility(View.VISIBLE);
            }
            if (!(this.leaveType.getLeavetypename() == null || "".equalsIgnoreCase(this.leaveType.getLeavetypename()))) {
                this.Autoleavetypes.setSelection(getIndex(this.Autoleavetypes, this.leaveType.getLeavetypename()));
            }
            this.tvLeaveReson.setText(this.leaveType.getLvReason());
            this.edtNote.setText(this.leaveType.getNote());
            if (this.dbHelper == null) {
                this.dbHelper = new DatabaseHelper(this.context);
                this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            }
            HashMap<String, String> project = this.dbHelper.selSingleRecordFromDB("select * from tblproject where project_id = '" + this.leaveType.getProject_id() + "'", null);
            if (project != null) {
                this.AutoProject.setText((String) project.get("project_name"));
            }
            if (this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_leave));
            } else if (this.leaveType.getApproveStatus().equalsIgnoreCase("approve")) {
                this.txtstatuslbl.setText(getResources().getString(R.string.approved_leave));
            } else if (this.leaveType.getApproveStatus().equalsIgnoreCase("reject")) {
                this.txtstatuslbl.setText(getResources().getString(R.string.rejected_leave));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.cancel_leave));
            }
            if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && !this.is_from_mng_leave) {
                setNotEditableLeaveForm();
                this.tvLVcomment.setText("Employee Comments");
                this.tvLeaveReson.setVisibility(View.GONE);
                if (this.leaveType.getLvReason().equalsIgnoreCase("")) {
                    this.tvNoComment.setVisibility(View.VISIBLE);
                } else {
                    this.tvCommentsText.setVisibility(View.VISIBLE);
                    this.tvCommentsText.setText(this.leaveType.getLvReason());
                }
                this.tvnote.setVisibility(View.VISIBLE);
                this.tvnote.setText("Manager Comments");
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
            } else if (this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                this.ll_lbl_vacLv.setVisibility(View.VISIBLE);
            } else {
                this.ll_lbl_vacLv.setVisibility(View.GONE);
                setNotEditableLeaveForm();
                this.edtNote.setVisibility(View.GONE);
                this.tvLeaveReson.setVisibility(View.GONE);
                this.tvnote.setVisibility(View.VISIBLE);
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
                if (this.leaveType.getApproveStatus().equalsIgnoreCase("approve")) {
                    this.btnUpade.setVisibility(View.GONE);
                } else {
                    this.ll_btn_update.setVisibility(View.GONE);
                }
            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
            SimpleDateFormat expected = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date1 = input.parse(this.leaveType.getLeaveFromDt());
                Date date2 = input.parse(this.leaveType.getLeaveTillDt());
                this.dcount = Integer.parseInt(getDateDiffString(date1, date2));
                String str1 = output.format(date1);
                String str2 = output.format(date2);
                this.strtDate.setText(str1);
                this.EndDate.setText(str2);
                this.txtStartDate = expected.format(date1);
                this.txtEndDate = expected.format(date2);
                setDays(date1, date2);
                return;
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
        }
        this.AutoProject.setEnabled(true);
        this.Autoleavetypes.setEnabled(true);
        this.imgvStartDate.setClickable(true);
        this.imgvEndDate.setClickable(true);
        this.tvLeaveReson.setEnabled(true);
    }

    private void setPTOTimeViewAdapter() {
        List<String> HrsList = new ArrayList();
        List<String> MinsList = new ArrayList();
        HrsList = Arrays.asList(getResources().getStringArray(R.array.hrs_array));
        MinsList = Arrays.asList(getResources().getStringArray(R.array.minits_array));
        spPTOHours.setAdapter(new SpinnerListAdapter(this.context, HrsList));
        spPTOMinutes.setAdapter(new SpinnerListAdapter(this.context, MinsList));
        spPTOHours.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strPTOHours = spPTOHours.getSelectedItem().toString();
                String hrs = "";
                String min = "";
                if (strPTOHours == null || "".equalsIgnoreCase(strPTOHours)) {
                    hrs = "0";
                } else {
                    hrs = strPTOHours.substring(0, (strPTOHours.length() - getResources().getString(R.string.Hrs).length()) - 1);
                }
                if (strPTOMinutes == null || "".equalsIgnoreCase(strPTOMinutes)) {
                    min = "0";
                } else {
                    min = strPTOMinutes.substring(0, (strPTOMinutes.length() - getResources().getString(R.string.Mins).length()) - 1);
                }
                if ((hrs != null && hrs.length() > 0) || (min != null && min.length() > 0)) {
                    totalPTOMinutes = AppUtils.getMinutesFromHours(hrs, min);
//                    displayOverTimeinView(RegisterTimeActivity.this.overtime2Mins, RegisterTimeActivity.this.sHrsHalfOvertime, RegisterTimeActivity.this.sMinHalfOvertime);
//                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.overtime2Mins);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spPTOMinutes.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strPTOMinutes = spPTOMinutes.getSelectedItem().toString();
                String hrs = "";
                String min = "";
                if (strPTOHours == null || "".equalsIgnoreCase(strPTOHours)) {
                    hrs = "0";
                } else {
                    hrs = strPTOHours.substring(0, (strPTOHours.length() - getResources().getString(R.string.Hrs).length()) - 1);
                }
                if (strPTOMinutes == null || "".equalsIgnoreCase(strPTOMinutes)) {
                    min = "0";
                } else {
                    min = strPTOMinutes.substring(0, (strPTOMinutes.length() - getResources().getString(R.string.Mins).length()) - 1);
                }
                if ((hrs != null && hrs.length() > 0) || (min != null && min.length() > 0)) {
                    totalPTOMinutes = AppUtils.getMinutesFromHours(hrs, min);
//                    RegisterTimeActivity.this.displayOverTimeinView(RegisterTimeActivity.this.overtime2Mins, RegisterTimeActivity.this.sHrsHalfOvertime, RegisterTimeActivity.this.sMinHalfOvertime);
//                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.overtime2Mins);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_form);
        this.header.setTitle(getString(R.string.title_activity_leave_form));
        boolean isFromMenu = getIntent().getBooleanExtra("isFromMenu", false);
        this.is_from_mng_leave = getIntent().getBooleanExtra("is_from_mng_leave", false);
        this.header.setLeftBtnImage(R.drawable.ic_back_arrow);
        this.header.setLeftBtnClickListener(new C06662());
        this.header.hideRightBtn();
        getViews();
        setPTOTimeViewAdapter();

        this.leaveType = (LeaveType) getIntent().getSerializableExtra("LeaveDetails");
        this.leaveTypeList = getListFromLocalDB("tblleavetype", "leave_type");
        if (this.leaveTypeList != null) {
            getLeaveTypeList(this.leaveTypeList);
        }
        this.PnameList = getListFromLocalDB("tblproject", "project_name", "active", "project_status");
        if (this.PnameList != null) {
            getProjectListAdapter(this.PnameList);
        }
        if (OnjybApp.leaveTypesArray != null && OnjybApp.leaveTypesArray.size() > 0) {
            int i = 0;
            while (i < OnjybApp.leaveTypesArray.size()) {
                if (((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type() != null && ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type().equalsIgnoreCase("vacation")) {
                    this.tvUsedVac.setText(getResources().getString(R.string.txt_Used_vacation) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getUsed_leave() + " " + getResources().getString(R.string.txt_days));
                    this.tvPlanedVac.setText(getResources().getString(R.string.txt_Planed_vacation) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getPlaned_leave() + " " + getResources().getString(R.string.txt_days));
                    this.tvLeftVac.setText(getResources().getString(R.string.txt_Left_vacation) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_remaining_leave() + " " + getResources().getString(R.string.txt_days));
                } else if (((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type() != null && ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getLeave_master_type().equalsIgnoreCase("sick")) {
                    this.lblSeekLv.setText(getResources().getString(R.string.txt_total_seek_hrs) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_leave() + " " + getResources().getString(R.string.txt_days) + ",\n" + getResources().getString(R.string.txt_total_use_this_year) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_earn_leave() + " " + getResources().getString(R.string.txt_days) + ", " + getResources().getString(R.string.txt_left) + " " + ((LeaveType) OnjybApp.leaveTypesArray.get(i)).getTotal_remaining_leave() + " " + getResources().getString(R.string.txt_days));
                }
                i++;
            }
        }
        if (this.leaveType != null) {
            String leftLeave = Preference.getSharedPref(Constants.PREF_LEFT_LEAVE, "0");
            if (leftLeave != null) {
                this.tvRemainDays.setText(leftLeave + " " + getResources().getString(R.string.txt_days));
            }
            this.btnApply.setVisibility(View.GONE);
            this.ll_btn_update.setVisibility(View.VISIBLE);
            if (!(this.leaveType.getLeavetypename() == null || "".equalsIgnoreCase(this.leaveType.getLeavetypename()))) {
                this.Autoleavetypes.setSelection(getIndex(this.Autoleavetypes, this.leaveType.getLeavetypename()));
            }
            //jin
            String total = this.leaveType.getTotal_hours();
            if(total != null){
                int nTotal = Integer.parseInt(total);
                int h = (int)(nTotal / 60);
                this.spPTOHours.setSelection(h);
                int m = (int)(nTotal % 60);
                this.spPTOMinutes.setSelection(m);
            }
            this.tvLeaveReson.setText(this.leaveType.getLvReason());
            this.edtNote.setText(this.leaveType.getNote());
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            HashMap<String, String> project = this.dbHelper.selSingleRecordFromDB("select * from tblproject where project_id = '" + this.leaveType.getProject_id() + "'", null);
            if (project != null) {
                this.AutoProject.setText((String) project.get("project_name"));
            }
            if (this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_leave));
            } else if (this.leaveType.getApproveStatus().equalsIgnoreCase("approve")) {
                this.txtstatuslbl.setText(getResources().getString(R.string.approved_leave));
            } else if (this.leaveType.getApproveStatus().equalsIgnoreCase("reject")) {
                this.txtstatuslbl.setText(getResources().getString(R.string.rejected_leave));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.cancel_leave));
            }
            if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && !this.is_from_mng_leave) {
                setNotEditableLeaveForm();
                this.tvLVcomment.setText("Employee Comments");
                this.tvLeaveReson.setVisibility(View.GONE);
                if (this.leaveType.getLvReason().equalsIgnoreCase("")) {
                    this.tvNoComment.setVisibility(View.VISIBLE);
                } else {
                    this.tvCommentsText.setVisibility(View.VISIBLE);
                    this.tvCommentsText.setText(this.leaveType.getLvReason());
                }
                this.tvnote.setVisibility(View.VISIBLE);
                this.tvnote.setText("Manager Comments");
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
            } else if (this.leaveType.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                this.ll_lbl_vacLv.setVisibility(View.VISIBLE);
            } else {
                this.ll_lbl_vacLv.setVisibility(View.GONE);
                setNotEditableLeaveForm();
                this.edtNote.setVisibility(View.GONE);
                this.tvLeaveReson.setVisibility(View.GONE);
                this.tvnote.setVisibility(View.VISIBLE);
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
                if (this.leaveType.getApproveStatus().equalsIgnoreCase("approve")) {
                    this.btnUpade.setVisibility(View.GONE);
                } else {
                    this.ll_btn_update.setVisibility(View.GONE);
                }
            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
            SimpleDateFormat expected = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date1 = input.parse(this.leaveType.getLeaveFromDt());
                Date date2 = input.parse(this.leaveType.getLeaveTillDt());
                this.dcount = Integer.parseInt(getDateDiffString(date1, date2));
                String str1 = output.format(date1);
                String str2 = output.format(date2);
                this.strtDate.setText(str1);
                this.EndDate.setText(str2);
                this.txtStartDate = expected.format(date1);
                this.txtEndDate = expected.format(date2);
                setDays(date1, date2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void setNotEditableLeaveForm() {
        this.AutoProject.setEnabled(false);
        this.Autoleavetypes.setEnabled(false);
        this.imgvStartDate.setClickable(false);
        this.imgvEndDate.setClickable(false);
        this.tvLeaveReson.setEnabled(false);
    }

    private void setclearViews() {
        this.strtDate.setText("");
        this.EndDate.setText("");
        this.tvLeaveDasys.setText("");
        this.dcount = 0;
        this.tvLeaveReson.setText("");
        this.AutoProject.setText("");
    }

    private String validateData() {
        Date fromdate = null;
        try {
            SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
            Date todate = input.parse(this.txtStartDate);
            if (!(this.txtEndDate == null || this.txtEndDate.isEmpty())) {
                fromdate = input.parse(this.txtEndDate);
            }
            if (this.Autoleavetypes.getAdapter().getCount() <= 0) {
                return getString(R.string.enter_leavetype);
            }
            this.leaveTypename = this.Autoleavetypes.getSelectedItem().toString().trim();
            this.lvFrom = this.strtDate.getText().toString().trim();
            this.lvTill = this.EndDate.getText().toString().trim();
            if (this.leaveTypename.length() == 0) {
                return getString(R.string.enter_leavetype);
            }
            if (this.lvFrom.length() == 0) {
                return getString(R.string.enter_leave_from);
            }
            if (this.lvTill.length() == 0) {
                return getString(R.string.enter_leave_till);
            }
            if (fromdate == null || fromdate.compareTo(todate) < 0 || this.dcount <= 0) {
                return getString(R.string.invalid_date_selection);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getListFromLocalDB(String tblname, String list_name) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        ArrayList<HashMap<String, String>> list = this.dbHelper.selectRecordsFromDBList("select * from " + tblname, null);
        List<String> fieldlist = new ArrayList();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (tblname.equalsIgnoreCase("tblproject")) {
                    fieldlist.add(((String) ((HashMap) list.get(i)).get("project_number")) + " | " + ((String) ((HashMap) list.get(i)).get(list_name)));
                } else {
                    fieldlist.add(((String) ((HashMap) list.get(i)).get(list_name)));
                }
            }
        }
        return fieldlist;
    }

    private List<String> getListFromLocalDB(String tblname, String list_name, String status, String field) {
        this.dbHelper = new DatabaseHelper(this.context);
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        String qry = "select * from " + tblname + " where " + field + "='" + status + "'";
        if (tblname.equalsIgnoreCase("tblservice")) {
            qry = qry + " where ref_company_id != '0'";
        }
        ArrayList<HashMap<String, String>> list = this.dbHelper.selectRecordsFromDBList(qry, null);
        List<String> fieldlist = new ArrayList();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                fieldlist.add(((String) ((HashMap) list.get(i)).get("project_number")) + " | " + ((String) ((HashMap) list.get(i)).get(list_name)));
            }
        }
        return fieldlist;
    }

    private void getLeaveTypeList(List item) {
        this.leaveTypeList = item;
        this.Autoleavetypes.setAdapter(new SpinnerAdapterLeave(this.context, item));
        this.Autoleavetypes.setOnItemSelectedListener(new C06726());
    }

    private void getViews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.Autoleavetypes = (Spinner) findViewById(R.id.atctv_leave);
        this.AutoProject = (EtechEditAutoCompleteTextRobotoRegular) findViewById(R.id.atctv_projectname);
        this.AutoProject.init(this.context, this);
        this.strtDate = (ETechTextView) findViewById(R.id.txtstrtdt1);
        this.EndDate = (ETechTextView) findViewById(R.id.txttilDt);
        this.tvLeaveDasys = (ETechTextView) findViewById(R.id.txtLeaveDays);
        this.tvLeaveReson = (ETechEditText) findViewById(R.id.edtLVReason);
        this.tvRemainDays = (ETechTextView) findViewById(R.id.txtRemainDays);
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
        this.lblSeekLv = (TextView) findViewById(R.id.txtRemainSeekleave);
        this.ll_lbl_SeekLv = (LinearLayout) findViewById(R.id.ll_lbl_SeekLv);
        this.ll_lbl_vacLv = (LinearLayout) findViewById(R.id.ll_lbl_vacLv);
        this.tvUsedVac = (TextView) findViewById(R.id.txtUsedVac);
        this.tvPlanedVac = (TextView) findViewById(R.id.txtPlanedVac);
        this.tvLeftVac = (TextView) findViewById(R.id.txtLeftVac);
        this.devider_caslv = findViewById(R.id.devider_caslv);
        this.devider_seeklv = findViewById(R.id.devider_seeklv);
        this.AutoProject.getViewTreeObserver().addOnGlobalLayoutListener(new C06737());
        this.dropdownimg = (ImageView) findViewById(R.id.imageView1);
        Drawable img = getResources().getDrawable(R.drawable.dropdown1);
        img.setBounds(0, 0, img.getIntrinsicWidth() / 2, img.getIntrinsicWidth() / 2);
        this.dropdownimg.setImageDrawable(img);
        this.btnApply.setOnClickListener(this.applyLeaveBtnClickListener);
        this.btnCancel.setOnClickListener(this.cancelLeaveBtnClickListener);
        this.btnUpade.setOnClickListener(this.updateLeaveBtnClickListener);

        spPTOHours = (Spinner)findViewById(R.id.spPTOHours);
        spPTOMinutes = (Spinner)findViewById(R.id.spPTOMinutes);
        llPTO = (LinearLayout)findViewById(R.id.ll_ptobank);
        deviderPTO = findViewById(R.id.devider_ptobank);

    }

    public void selectDateTill(View view) {
        new SelectDateFragment1().show(getSupportFragmentManager(), "DatePicker");
    }

    public void selectLeaveFrom(View view) {
        new SelectDateFragment().show(getSupportFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        String time = day + "/" + month + "/" + year;
        this.txtStartDate = time;
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
        try {
            Date date1 = input.parse(time);
            String str1 = output.format(date1);
            Log.d(this.TAG, " Start Day: " + date1.getDay());
            if (!this.txtEndDate.equalsIgnoreCase("")) {
                this.EndDate.setText("");
            }
            this.txtEndDate = "";
            this.dcount = 0;
            this.tvLeaveDasys.setText("0 " + getResources().getString(R.string.txt_days));
            this.strtDate.setText(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void onOkClick(int resId1, int titleResId1) {
        String status = "";
        if (titleResId1 == 1) {
            LeavesApiHelper helper = new LeavesApiHelper(this.context);
            this.progressHelper.showDialog("Loading..");
            helper.apiApproveLeave("cancel", this.leaveType, this.approveLeaveApiCallBack);
        }
    }

    public void openSpinnerleaveType(View view) {
        this.Autoleavetypes.performClick();
    }

    public void openSpinnerProjectName(View view) {
        if (this.AutoProject != null || this.AutoProject.getText().toString().equalsIgnoreCase("")) {
            this.AutoProject.showDropDown();
        }
    }

    public void onItemOptionClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (view.getId() == R.id.atctv_projectname) {
            String pName = this.AutoProject.getText().toString();
            String qry = "select project_id from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            this.projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(this);
        }
    }

    public void populateSetDate1(int year, int month, int day) {
        String time = day + "/" + month + "/" + year;
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
        this.txtEndDate = time;
        try {
            Date date1 = input.parse(time);
            Log.d(this.TAG, " End Day: " + date1.getDay());
            String str1 = output.format(date1);
            if (this.txtStartDate.equalsIgnoreCase("")) {
                AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.select_start_date_first));
                return;
            }
            Date date2 = input.parse(this.txtStartDate);
            String days = getDateDiffString(date2, date1);
            if (Integer.parseInt(days) == 0) {
                AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.to_date_start_not_same));
            } else if (Integer.parseInt(days) < 0) {
                AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.to_date_must_greater));
            } else {
                this.EndDate.setText(str1);
                setDays(date2, date1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDays(Date strdate, Date endDate) {
        try {
            String days = getDateDiffString(strdate, endDate);
            int businessDayCount = AppUtils.getBuissnesDays(strdate, endDate);
            Log.d(this.TAG, "Business Day: " + businessDayCount);
            if (this.leaveTypeId != null) {
                if (((String) this.leaveTypeId.get("leave_master_type")).equalsIgnoreCase("vacation")) {
                    if (businessDayCount > 0) {
                        this.tvLeaveDasys.setText(businessDayCount + " " + getResources().getString(R.string.txt_days));
                    } else {
                        this.tvLeaveDasys.setText("0 " + getResources().getString(R.string.txt_days));
                    }
                    this.dcount = businessDayCount;
                    return;
                }
                if (Integer.parseInt(days) > 0) {
                    this.tvLeaveDasys.setText(days + " " + getResources().getString(R.string.txt_days));
                } else {
                    this.tvLeaveDasys.setText("0 " + getResources().getString(R.string.txt_days));
                }
                this.dcount = Integer.parseInt(days);
            } else if (this.leaveType == null) {
                if (Integer.parseInt(days) > 0) {
                    this.tvLeaveDasys.setText(days + " " + getResources().getString(R.string.txt_days));
                } else {
                    this.tvLeaveDasys.setText("0 " + getResources().getString(R.string.txt_days));
                }
                this.dcount = Integer.parseInt(days);
            } else if (this.leaveType.getLeave_master_type().equalsIgnoreCase("vacation")) {
                if (businessDayCount > 0) {
                    this.tvLeaveDasys.setText(businessDayCount + " " + getResources().getString(R.string.txt_days));
                } else {
                    this.tvLeaveDasys.setText("0 " + getResources().getString(R.string.txt_days));
                }
                this.dcount = businessDayCount;
            } else {
                if (Integer.parseInt(days) > 0) {
                    this.tvLeaveDasys.setText(days + " " + getResources().getString(R.string.txt_days));
                } else {
                    this.tvLeaveDasys.setText("0 " + getResources().getString(R.string.txt_days));
                }
                this.dcount = Integer.parseInt(days);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(this.TAG, e.getMessage());
        }
    }

    private String getDateDiffString(Date dateOne, Date dateTwo) {
        long delta = (dateTwo.getTime() - dateOne.getTime()) / DateUtils.MILLIS_PER_DAY;
        Log.d(this.TAG, "Day: " + dateOne.getDay());
        if (delta >= 0) {
            return String.valueOf(1 + delta);
        }
        return String.valueOf(delta);
    }

    private void getProjectListAdapter(List item) {
        this.PnameList = item;
        this.myProjectAdapter = new ArrayAdapter(this, R.layout.auto_complete_textview, this.PnameList);
        this.AutoProject.setAdapter(this.myProjectAdapter);
        this.AutoProject.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String pName = LeaveFormActivity.this.AutoProject.getText().toString();
                String qry = "select project_id from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
                LeaveFormActivity.this.dbHelper = new DatabaseHelper(LeaveFormActivity.this.context);
                LeaveFormActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(LeaveFormActivity.this.context);
                LeaveFormActivity.this.projectid = LeaveFormActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
                AppUtils.hideKeyBoard(LeaveFormActivity.this);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}

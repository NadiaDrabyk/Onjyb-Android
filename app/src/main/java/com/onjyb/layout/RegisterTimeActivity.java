package com.onjyb.layout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.onjyb.R;
import com.onjyb.adaptor.SpinnerListAdapter;
import com.onjyb.Constants;
import com.onjyb.beans.UserHelper;
import com.onjyb.beans.workSheetUploadHelper;
import com.onjyb.customview.DrawingView;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular.DropDownItemClickListener;
import com.onjyb.customview.ImageSelectionView;
import com.onjyb.customview.TimeStampView.TimeStampClickListener;
import com.onjyb.db.AttachmentMap;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.OvertimeRule;
import com.onjyb.db.Service;
import com.onjyb.db.WorkServiceMap;
import com.onjyb.db.WorkSheet;
import com.onjyb.helper.MyBroadcastReceiver;
import com.onjyb.helper.SlideShowActivity;
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
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterTimeActivity extends BaseDrawerActivity implements TimeStampClickListener, OnMyDialogResult, DropDownItemClickListener {
    private static final int REQUEST_CONTACTS_CODE = 100;
    List<String> BranchList;
    private final int IMAGE_CROP = 3;
    String Km;
    List<String> PnameList;
    private int REQ_CODE_EXTRA_SERVICE = 1;
    private int REQ_CODE_EXTRA_SERVICE_EDIT = 2;
    private final String TAG = RegisterTimeActivity.class.getName();
    private ActionCallback apiCallBackUnReadCount = new ActionCallback() {
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                Log.d("UnreadCount>>>>>>", "Success");
                Intent i = new Intent("any string");
                i.setClass(RegisterTimeActivity.this.context, MyBroadcastReceiver.class);
                RegisterTimeActivity.this.sendBroadcast(i);
                RegisterTimeActivity.this.tvUnreadTaskCount.setText("0");
            }
        }
    };
    ArrayList<WorkServiceMap> arrExtraService = new ArrayList();
    AutoCompleteTextView atctvbranch;
    AutoCompleteTextView atctvservice;
    EtechEditAutoCompleteTextRobotoRegular atvproject;
    ArrayAdapter<String> autocompleteAdapterB;
    ArrayAdapter<String> autocompleteAdapterS;
    HashMap<String, String> branchId;
    long breakMins = 0;
    ETechButton btnendTime;
    ETechButton btnstartTime;
    Context context = this;
    private int curEditingServiceIndex = -1;
    WorkSheet curWorkSheet = null;
    DatabaseHelper dbHelper;
    String defaultWorkTime = "0";
    private Dialog dialog;
    ImageButton down_arrow_img;
    DropDownItemClickListener dropDownItemClickListener;
    String editFlage;
    ETechEditText edtcomment;
    OnClickListener endTimeClickListener = new C07619();
    ImageSelectionView imageSelectionView;
    ImageButton imgbtnOvertime1End;
    ImageButton imgbtnOvertime1Start;
    ImageButton imgbtnOvertime2End;
    ImageButton imgbtnOvertime2Start;
    ImageButton imgbtnWorkHrsEnd;
    ImageButton imgbtnWorkHrsStart;
    ImageButton imgbtnaddservice;
    OnClickListener imgbtnaddserviceclicklistener = new OnClickListener() {
        public void onClick(View v) {
            if (RegisterTimeActivity.this.isExtraServiceEdit.booleanValue()) {
                RegisterTimeActivity.this.isExtraServiceEdit = Boolean.valueOf(false);
            }
            RegisterTimeActivity.this.startActivityForResult(new Intent(RegisterTimeActivity.this, ExtraServiceActivity.class), RegisterTimeActivity.this.REQ_CODE_EXTRA_SERVICE);
        }
    };
    Boolean isEditable = Boolean.valueOf(false);
    Boolean isExtraServiceEdit = Boolean.valueOf(false);
    boolean isFromMenu = false;
    Boolean isImgFromURL = Boolean.valueOf(false);
    String isUserDependOvertimeAutomatic = Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO;
    boolean is_from_mng_jobber = false;
    boolean iscreate = false;
    EditText kelometer;
    LinearLayout llAdditionalWork;
    LinearLayout llOfOverTimeView;
    LinearLayout ll_100_overtime;
    LinearLayout ll_50_overtime;
    LinearLayout ll_dropdon_overtime;
    LinearLayout ll_parenttextview;
    ListView lsasService;
    private int mHour;
    private int mMinute;
    ArrayAdapter<String> myAutoCompleteAdapter;
    long overTime1Mins = 0;
    long overtime2Mins = 0;
    ArrayList<TextView> overtimeHrsTVList = new ArrayList();
    ArrayList<TextView> overtimeMinTVList = new ArrayList();
    ArrayList<TextView> overtimeTextviewList = new ArrayList();
    ProgressHelper progressHelper;
    HashMap<String, String> projectid;
    RelativeLayout rl_kms;
    public ArrayList<OvertimeRule> rulesList = new ArrayList();
    ETechTextView sHrs;
    ETechTextView sHrsFullOvertime;
    ETechTextView sHrsHalfOvertime;
    ETechTextView sMin;
    ETechTextView sMinFullOvertime;
    ETechTextView sMinHalfOvertime;
    ETechButton save;

    //0926
    LinearLayout ll_ptobank;
    ImageView iv_ptobank;
    boolean is_ptobank = false;
    //end

    //For Signature
//    private String project_id;
    Button sign;
    OnClickListener signbtnclickListener = new SignClickListener();
    private String strSignedBy;
    private String strSignedEmail;
    private String strCPerson;
    private String strCEmail;
    private Bitmap singedBitmap;

    class SignClickListener implements OnClickListener {
        SignClickListener() {
        }
        public void onClick(View v) {
            ShowSigningDialog();
        }
    }
    private void ShowSigningDialog() {
        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            final Dialog signDialog;
            signDialog = new Dialog(this);
            signDialog.requestWindowFeature(1);
            signDialog.setContentView(R.layout.activity_signaturedialog);
            final DrawingView drawView = (DrawingView)signDialog.findViewById(R.id.drawingview);
            drawView.setBrushSize(3.0f);

            String strContactName = "";
            String strContactEmail = "";
            String strFirstName = Preference.getSharedPref(Constants.PREF_USER_FNAME, "");
            String strEmail = Preference.getSharedPref(Constants.PREF_USER_EMAIL, "");
            try {
                String details = Preference.getSharedPref("project_service_branch_details", "");
                JSONObject jObj = new JSONObject((String) details.toString());
                JSONObject resobj = jObj.getJSONObject(Constants.RESPONSE_KEY_OBJ);
                JSONArray proj_detail = resobj.getJSONArray("project_details");
                if (proj_detail != null && proj_detail.length() > 0) {
                    for (int j = 0; j < proj_detail.length(); j++) {
                        JSONObject jsonObject = proj_detail.getJSONObject(j);
                        if(jsonObject.get("project_id").equals(this.projectid.get("project_id")))
                        {
                            strContactName = (String) jsonObject.get("project_contact_name");
                            strContactEmail = (String) jsonObject.get("project_contact_email");
                            break;
                        }
                    }
                }
            } catch (Throwable throwable){
                throwable.printStackTrace();
            }

            Button cancelbtn = (Button) signDialog.findViewById(R.id.btnCancel);
            final EditText etFirstName = ((EditText) signDialog.findViewById(R.id.et_firstname));
            etFirstName.setText(strFirstName);
            final EditText etEmail = ((EditText) signDialog.findViewById(R.id.et_email));
            etEmail.setText(strEmail);
            final EditText etContactPerson = ((EditText) signDialog.findViewById(R.id.et_contactperson));
            etContactPerson.setText(strContactName);
            final EditText etContactEmail = ((EditText) signDialog.findViewById(R.id.et_contactemail));
            etContactEmail.setText(strContactEmail);

            cancelbtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    signDialog.dismiss();
                }});
            Button savebtn = (Button) signDialog.findViewById(R.id.btnSave);
            savebtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    strSignedBy = etFirstName.getText().toString();
                    strSignedEmail =  etEmail.getText().toString();
                    strCPerson =  etContactPerson.getText().toString();
                    strCEmail =  etContactEmail.getText().toString();

                    if(strCPerson == null || strCPerson.length() == 0){
                        AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, getString(R.string.app_name),
                                getResources().getString(R.string.str_add_contact_person));
                        return;
                    }
                    if(strCEmail == null || strCEmail.length() == 0){
                        AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, getString(R.string.app_name),
                                getResources().getString(R.string.str_add_contact_email));
                        return;
                    }
                    drawView.setDrawingCacheEnabled(true);
                    Bitmap cachedBitmap = drawView.getDrawingCache();
                    singedBitmap = cachedBitmap.copy(Bitmap.Config.RGB_565, true);
                    drawView.destroyDrawingCache();
                    signDialog.dismiss();
                }});

            Button nullsignaturebtn = (Button) signDialog.findViewById(R.id.btnNullSignature);
            nullsignaturebtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    drawView.startNew();
                }});

            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
//            signDialog.getWindow().setLayout(width*9/10, (height * 3) / 4);
            signDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //end

    OnClickListener savebtnclickListener = new OnClickListener() {

        class C07481 implements OnClickListener {
            C07481() {
            }

            @TargetApi(16)
            public void onClick(View v) {
            }
        }

        class C07492 implements OnClickListener {
            C07492() {
            }

            public void onClick(View v) {
            }
        }

        class C07503 implements OnClickListener {
            C07503() {
            }

            public void onClick(View v) {
            }
        }

        class C07514 implements OnClickListener {
            C07514() {
            }

            public void onClick(View v) {
            }
        }

        public void onClick(View v) {
            String flag = RegisterTimeActivity.this.validation();
            if (flag == null) {
                try {
                    String strHrs;
                    String strMin;
                    RegisterTimeActivity.this.workSheet = new WorkSheet();
                    String userid = Preference.getSharedPref(Constants.PREF_USER_ID, "");
                    String compId = Preference.getSharedPref(Constants.PREF_COMPANY_ID, "");
                    String roleId = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
                    if (RegisterTimeActivity.this.isEditable.booleanValue()) {
                        RegisterTimeActivity.this.workSheet.setServerWorkSheetId(RegisterTimeActivity.this.curWorkSheet.getServerWorkSheetId());
                        if (RegisterTimeActivity.this.editFlage != null && RegisterTimeActivity.this.editFlage.equalsIgnoreCase("approve")) {
                            RegisterTimeActivity.this.workSheet.setApproveStatus("approve");
                        }
                        if (!(roleId.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || RegisterTimeActivity.this.isFromMenu || RegisterTimeActivity.this.is_from_mng_jobber)) {
                            RegisterTimeActivity.this.workSheet.setApproveStatus("approve");
                        }
                        if (roleId.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || RegisterTimeActivity.this.is_from_mng_jobber) {
                            if (RegisterTimeActivity.this.curWorkSheet.getApproveStatus() != null && RegisterTimeActivity.this.curWorkSheet.getApproveStatus().equalsIgnoreCase("reject")) {
                                RegisterTimeActivity.this.workSheet.setApproveStatus(Constants.STATUS_PENDING);
                            }
                        } else if (!RegisterTimeActivity.this.is_from_mng_jobber) {
                            RegisterTimeActivity.this.workSheet.setApproveStatus("approve");
                        }
                    }
                    RegisterTimeActivity.this.workSheet.setProjectName(RegisterTimeActivity.this.atvproject.getText().toString());
                    RegisterTimeActivity.this.workSheet.setkMDrive(RegisterTimeActivity.this.kelometer.getText().toString());
                    RegisterTimeActivity.this.workSheet.setWorkDate(RegisterTimeActivity.this.tvstartdt.getText().toString());
                    RegisterTimeActivity.this.workSheet.setWorkEndDate(RegisterTimeActivity.this.tvendDate.getText().toString());
                    RegisterTimeActivity.this.workSheet.setRefUserId(userid);
                    RegisterTimeActivity.this.workSheet.setCompanyId(compId);
                    RegisterTimeActivity.this.workSheet.setWorkStartTime(RegisterTimeActivity.this.tvworkHrsStart.getText().toString());
                    RegisterTimeActivity.this.workSheet.setWorkEndTime(RegisterTimeActivity.this.tvworkHrsEnd.getText().toString());
                    if (RegisterTimeActivity.this.workMins > 0) {
                        RegisterTimeActivity.this.workSheet.setWorkHrs(String.valueOf(RegisterTimeActivity.this.workMins));
                    }
                    try {
                        strHrs = "";
                        strMin = "";
                        if (!RegisterTimeActivity.this.selectedFullHrs.toString().equalsIgnoreCase("")) {
                            strHrs = RegisterTimeActivity.this.selectedFullHrs.substring(0, (RegisterTimeActivity.this.selectedFullHrs.length() - RegisterTimeActivity.this.getResources().getString(R.string.Hrs).length()) - 1);
                        }
                        if (!RegisterTimeActivity.this.selectedFullMin.toString().equalsIgnoreCase("")) {
                            strMin = RegisterTimeActivity.this.selectedFullMin.substring(0, (RegisterTimeActivity.this.selectedFullMin.length() - RegisterTimeActivity.this.getResources().getString(R.string.Mins).length()) - 1);
                        }
                        if (!strHrs.equalsIgnoreCase("")) {
                            if (!strMin.equalsIgnoreCase("")) {
                                RegisterTimeActivity.this.workSheet.setWorkOverTime1(String.valueOf((Integer.parseInt(strHrs) * 60) + Integer.parseInt(strMin)));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        strHrs = RegisterTimeActivity.this.selectedHalfHrs.substring(0, (RegisterTimeActivity.this.selectedHalfHrs.length() - RegisterTimeActivity.this.getResources().getString(R.string.Hrs).length()) - 1);
                        strMin = RegisterTimeActivity.this.selectedHalfMin.substring(0, (RegisterTimeActivity.this.selectedHalfMin.length() - RegisterTimeActivity.this.getResources().getString(R.string.Mins).length()) - 1);
                        if (!strHrs.equalsIgnoreCase("")) {
                            if (!strMin.equalsIgnoreCase("")) {
                                RegisterTimeActivity.this.workSheet.setWorkOverTime2(String.valueOf((Integer.parseInt(strHrs) * 60) + Integer.parseInt(strMin)));
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    RegisterTimeActivity.this.workSheet.setBreakTime(String.valueOf(RegisterTimeActivity.this.breakMins));
                    RegisterTimeActivity.this.workSheet.setComments(RegisterTimeActivity.this.edtcomment.getText().toString());
                    if (RegisterTimeActivity.this.selServiceMap != null) {
                        RegisterTimeActivity.this.workSheet.setRefServiceId((String) RegisterTimeActivity.this.selServiceMap.get("service_id"));
                    } else if (RegisterTimeActivity.this.curWorkSheet != null) {
                        RegisterTimeActivity.this.workSheet.setRefServiceId(RegisterTimeActivity.this.curWorkSheet.getRefServiceId());
                    }
                    if (RegisterTimeActivity.this.projectid != null) {
                        RegisterTimeActivity.this.workSheet.setRefProjectId((String) RegisterTimeActivity.this.projectid.get("project_id"));
                    } else if (RegisterTimeActivity.this.curWorkSheet != null) {
                        RegisterTimeActivity.this.workSheet.setRefProjectId(RegisterTimeActivity.this.curWorkSheet.getRefProjectId());
                    }
                    if (RegisterTimeActivity.this.branchId != null) {
                        RegisterTimeActivity.this.workSheet.setRefBranchId((String) RegisterTimeActivity.this.branchId.get("branch_id"));
                    } else if (RegisterTimeActivity.this.curWorkSheet != null) {
                        RegisterTimeActivity.this.workSheet.setRefBranchId(RegisterTimeActivity.this.curWorkSheet.getRefBranchId());
                    }
                    RegisterTimeActivity.this.workSheet.setAttachementList(RegisterTimeActivity.this.imageSelectionView.getImagesList());
                    if (RegisterTimeActivity.this.isEditable.booleanValue()) {
                        RegisterTimeActivity.this.workSheet.setAttachementToDelete(RegisterTimeActivity.this.imageSelectionView.getImagesListToDelete());
                    }
                    String totalHrs = RegisterTimeActivity.this.getTimeInMinutes(RegisterTimeActivity.this.sHrs.getText().toString(), RegisterTimeActivity.this.sMin.getText().toString());
                    if (totalHrs != null) {
                        RegisterTimeActivity.this.workSheet.setTotalWorkTime(totalHrs);
                    }
                    RegisterTimeActivity.this.workSheet.setIsworksheetAutomaticEditmode(RegisterTimeActivity.this.isUserDependOvertimeAutomatic);
                    if (RegisterTimeActivity.this.onTimeget()) {
                        AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), RegisterTimeActivity.this.getResources().getString(R.string.impropper_data));
                        return;
                    }
                    //0926
                    if(is_ptobank)
                        workSheet.setIs_ptobank("yes");
                    else
                        workSheet.setIs_ptobank("no");

                    int temp = Integer.parseInt(RegisterTimeActivity.this.defaultWorkTime) / 60;
                    Log.e(RegisterTimeActivity.this.TAG, "totalHrs::" + totalHrs + "  defaultWorkTime::" + RegisterTimeActivity.this.defaultWorkTime);
                    if (totalHrs != null && Integer.parseInt(totalHrs) > Integer.parseInt(RegisterTimeActivity.this.defaultWorkTime)) {
                        if (roleId.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || RegisterTimeActivity.this.is_from_mng_jobber) {
                            AlertDialogHelper.getConfirmationAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name),
                                    new C07481(), new C07492(),
                                    RegisterTimeActivity.this.getResources().getString(R.string.greater_than_working_timepart1) + " " + temp +
                                            RegisterTimeActivity.this.getResources().getString(R.string.greater_than_working_timepart2), 0, 0, false);
                            return;
                        }
                    }
                    if (roleId.equalsIgnoreCase(Constants.USER_ROLE_MANAGER) && RegisterTimeActivity.this.workSheet.getApproveStatus() != null && RegisterTimeActivity.this.workSheet.getApproveStatus().equalsIgnoreCase("approve")) {
                        AlertDialogHelper.getConfirmationAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), new C07503(), new C07514(), RegisterTimeActivity.this.getResources().getString(R.string.msg_accept_task), 0, 0, false, RegisterTimeActivity.this.getResources().getString(R.string.txt_yes), RegisterTimeActivity.this.getResources().getString(R.string.txt_no), -1, -1);
                        return;
                    }
                    RegisterTimeActivity.this.progressHelper.showDialog("Loading..");
                    new workSheetUploadHelper(RegisterTimeActivity.this.context).apiUploadWorksheetToServer(strSignedBy,strSignedEmail,strCPerson,strCEmail,singedBitmap, //0918
                            RegisterTimeActivity.this.workSheet, RegisterTimeActivity.this.arrExtraService, RegisterTimeActivity.this.rulesList, RegisterTimeActivity.this.overtime2Mins, RegisterTimeActivity.this.overTime1Mins, RegisterTimeActivity.this.uploadWorksheetActionCallback);
                    return;
                } catch (Exception e22) {
                    e22.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), flag);
        }
    };
    HashMap<String, String> selServiceMap;
    private String selectedFullHrs = "";
    private String selectedFullMin = "";
    private String selectedHalfHrs = "";
    private String selectedHalfMin = "";
    List<String> serviceList;
    Spinner spFullHrsList;
    Spinner spFullMinList;
    Spinner spHalfHrsList;
    Spinner spHalfMinList;
    Spinner spKmList;
    Spinner spbreaktimeList;
    OnClickListener startTimeClickListener = new C07608();
    OnClickListener timeBtnClickListener = new OnClickListener() {

        class C07461 implements OnTimeSetListener {
            C07461() {
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                RegisterTimeActivity.this.tvworkHrsStart.setText(String.format("%02d", new Object[]{Integer.valueOf(hourOfDay)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minute)}));
                long minutes = RegisterTimeActivity.this.getTimeDifferenceinMinutes(RegisterTimeActivity.this.tvworkHrsStart.getText().toString(), RegisterTimeActivity.this.tvworkHrsEnd.getText().toString());
                if (minutes == 0) {
                    AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), RegisterTimeActivity.this.getResources().getString(R.string.start_end_not_same));
                } else if (minutes < 0) {
                    RegisterTimeActivity.this.tvworkHrsStart.setText(String.format("%02d", new Object[]{Integer.valueOf(hourOfDay)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minute)}));
                    String resultDate = AppUtils.getIncrementByOne(RegisterTimeActivity.this.tvstartdt.getText().toString(), 1);
                    if (resultDate != null) {
                        RegisterTimeActivity.this.tvendDate.setText(resultDate);
                    }
                    int startinter = (hourOfDay * 60) + minute;
                    RegisterTimeActivity.this.workMins = (long) ((int) (1440 + minutes));
                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
                } else {
                    RegisterTimeActivity.this.workMins = minutes;
                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
                }
            }
        }

        class C07472 implements OnTimeSetListener {
            C07472() {
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                RegisterTimeActivity.this.tvworkHrsEnd.setText(String.format("%02d", new Object[]{Integer.valueOf(hourOfDay)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minute)}));
                long minutes = RegisterTimeActivity.this.getTimeDifferenceinMinutes(RegisterTimeActivity.this.tvworkHrsStart.getText().toString(), RegisterTimeActivity.this.tvworkHrsEnd.getText().toString());
                if (minutes == 0) {
                    RegisterTimeActivity.this.tvworkHrsEnd.setText(String.format("%02d", new Object[]{Integer.valueOf(0)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(0)}));
                    AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), RegisterTimeActivity.this.getResources().getString(R.string.start_end_not_same));
                } else if (minutes < 0) {
                    RegisterTimeActivity.this.tvworkHrsEnd.setText(String.format("%02d", new Object[]{Integer.valueOf(hourOfDay)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(minute)}));
                    String resultDate = AppUtils.getIncrementByOne(RegisterTimeActivity.this.tvstartdt.getText().toString(), 1);
                    if (resultDate != null) {
                        RegisterTimeActivity.this.tvendDate.setText(resultDate);
                    }
                    int startinter = (hourOfDay * 60) + minute;
                    RegisterTimeActivity.this.workMins = (long) ((int) (1440 + minutes));
                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
                } else {
                    RegisterTimeActivity.this.workMins = minutes;
                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
                }
            }
        }

        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_work_hrs_start:
                    RegisterTimeActivity.this.mHour = Integer.parseInt(RegisterTimeActivity.this.tvworkHrsStart.getText().toString().substring(0, 2));
                    RegisterTimeActivity.this.mMinute = Integer.parseInt(RegisterTimeActivity.this.tvworkHrsStart.getText().toString().substring(3, 5));
                    new TimePickerDialog(RegisterTimeActivity.this.context, new C07461(), RegisterTimeActivity.this.mHour, RegisterTimeActivity.this.mMinute, true).show();
                    return;
                case R.id.img_work_hrs_end:
                    RegisterTimeActivity.this.mHour = Integer.parseInt(RegisterTimeActivity.this.tvworkHrsEnd.getText().toString().substring(0, 2));
                    RegisterTimeActivity.this.mMinute = Integer.parseInt(RegisterTimeActivity.this.tvworkHrsEnd.getText().toString().substring(3, 5));
                    new TimePickerDialog(RegisterTimeActivity.this.context, new C07472(), RegisterTimeActivity.this.mHour, RegisterTimeActivity.this.mMinute, true).show();
                    return;
                default:
                    return;
            }
        }
    };
    long totalmins = 0;
    OnClickListener tvClickLIstener = new OnClickListener() {
        public void onClick(View v) {
            RegisterTimeActivity.this.kelometer.requestFocus();
            ((InputMethodManager) RegisterTimeActivity.this.getSystemService("input_method")).showSoftInput(RegisterTimeActivity.this.kelometer, 0);
        }
    };
    TextView tvNorejectnote;
    ETechTextView tvOverTime1End;
    ETechTextView tvOverTime1Start;
    ETechTextView tvOverTime2End;
    ETechTextView tvOverTime2Start;
    ETechTextView tvRejectReason;
    ETechTextView tv_km;
    ETechTextView tvaddexservice;
    TextView tvendDate;
    TextView tvnoeditprojectname;
    TextView tvstartdt;
    ETechTextView tvworkHrsEnd;
    ETechTextView tvworkHrsStart;
    TextView txtNoExtraService;
    ETechTextView txtRejectMngNote;
    ImageButton up_arrow_img;
    private ActionCallback uploadWorksheetActionCallback = new ActionCallback() {

        class C07521 implements OnClickListener {
            C07521() {
            }

            public void onClick(View v) {
                Intent intent = new Intent(RegisterTimeActivity.this, MyTaskActivity.class);
                if (RegisterTimeActivity.this.is_from_mng_jobber) {
                    intent.putExtra("is_from_mng_jobber", RegisterTimeActivity.this.is_from_mng_jobber);
                }
                RegisterTimeActivity.this.startActivity(intent);
                RegisterTimeActivity.this.finish();
            }
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (RegisterTimeActivity.this.progressHelper != null) {
                RegisterTimeActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    String strProject = RegisterTimeActivity.this.atvproject.getText().toString();
                    Preference.setSharedPref("LastWorkedProject", strProject);

                    new UserHelper(RegisterTimeActivity.this.context).apiGetUnReadCount(RegisterTimeActivity.this.apiCallBackUnReadCount);
                    String resMSG = ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG);
                    RegisterTimeActivity.this.startService(new Intent(RegisterTimeActivity.this, ImageUploadService.class));
                    AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), new C07521(), resMSG, 0, 0, true);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), res.toString());
        }
    };
    long workMins = 0;
    WorkSheet workSheet;

    class C07531 implements OnClickListener {
        C07531() {
        }

        public void onClick(View v) {
            RegisterTimeActivity.this.finish();
        }
    }

    class C07542 implements OnItemSelectedListener {
        C07542() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            String text = RegisterTimeActivity.this.spbreaktimeList.getSelectedItem().toString();
//            String breakMins1 = text.substring(0, text.length() - 5);
            String min = getResources().getString(R.string.Min);
            String breakMins1 = text.substring(0, text.length() - (min.length() + 1));//jin for " m"

            RegisterTimeActivity.this.breakMins = (long) Integer.parseInt(breakMins1);
            if (RegisterTimeActivity.this.iscreate) {
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.breakMins);
            } else {
                RegisterTimeActivity.this.iscreate = true;
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C07553 implements OnItemSelectedListener {
        C07553() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            String text = RegisterTimeActivity.this.spbreaktimeList.getSelectedItem().toString();
            String breakMins1 = text.substring(0, text.length() - 5);
            RegisterTimeActivity.this.breakMins = (long) Integer.parseInt(breakMins1);
            if (RegisterTimeActivity.this.iscreate) {
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.breakMins);
            } else {
                RegisterTimeActivity.this.iscreate = true;
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C07564 implements OnItemSelectedListener {
        C07564() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            RegisterTimeActivity.this.selectedHalfHrs = RegisterTimeActivity.this.spHalfHrsList.getSelectedItem().toString();
            String hrs = "";
            String min = "";
            if (RegisterTimeActivity.this.selectedHalfHrs == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedHalfHrs)) {
                hrs = "0";
            } else {
                hrs = RegisterTimeActivity.this.selectedHalfHrs.substring(0, (RegisterTimeActivity.this.selectedHalfHrs.length() - RegisterTimeActivity.this.getResources().getString(R.string.Hrs).length()) - 1);
            }
            if (RegisterTimeActivity.this.selectedHalfMin == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedHalfMin)) {
                min = "0";
            } else {
                min = RegisterTimeActivity.this.selectedHalfMin.substring(0, (RegisterTimeActivity.this.selectedHalfMin.length() - RegisterTimeActivity.this.getResources().getString(R.string.Mins).length()) - 1);
            }
            if ((hrs != null && hrs.length() > 0) || (min != null && min.length() > 0)) {
                RegisterTimeActivity.this.overtime2Mins = AppUtils.getMinutesFromHours(hrs, min);
                RegisterTimeActivity.this.displayOverTimeinView(RegisterTimeActivity.this.overtime2Mins, RegisterTimeActivity.this.sHrsHalfOvertime, RegisterTimeActivity.this.sMinHalfOvertime);
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.overtime2Mins);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C07575 implements OnItemSelectedListener {
        C07575() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            RegisterTimeActivity.this.selectedHalfMin = RegisterTimeActivity.this.spHalfMinList.getSelectedItem().toString();
            String hrs = "";
            String min = "";
            if (RegisterTimeActivity.this.selectedHalfHrs == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedHalfHrs)) {
                hrs = "0";
            } else {
                hrs = RegisterTimeActivity.this.selectedHalfHrs.substring(0, (RegisterTimeActivity.this.selectedHalfHrs.length() - RegisterTimeActivity.this.getResources().getString(R.string.Hrs).length()) - 1);
            }
            if (RegisterTimeActivity.this.selectedHalfMin == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedHalfMin)) {
                min = "0";
            } else {
                min = RegisterTimeActivity.this.selectedHalfMin.substring(0, (RegisterTimeActivity.this.selectedHalfMin.length() - RegisterTimeActivity.this.getResources().getString(R.string.Mins).length()) - 1);
            }
            if ((hrs != null && hrs.length() > 0) || (min != null && min.length() > 0)) {
                RegisterTimeActivity.this.overtime2Mins = AppUtils.getMinutesFromHours(hrs, min);
                RegisterTimeActivity.this.displayOverTimeinView(RegisterTimeActivity.this.overtime2Mins, RegisterTimeActivity.this.sHrsHalfOvertime, RegisterTimeActivity.this.sMinHalfOvertime);
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.overtime2Mins);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C07586 implements OnItemSelectedListener {
        C07586() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            RegisterTimeActivity.this.selectedFullHrs = RegisterTimeActivity.this.spFullHrsList.getSelectedItem().toString();
            String hrs = "";
            String min = "";
            if (RegisterTimeActivity.this.selectedFullHrs == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedFullHrs)) {
                hrs = "0";
            } else {
                hrs = RegisterTimeActivity.this.selectedFullHrs.substring(0, (RegisterTimeActivity.this.selectedFullHrs.length() - RegisterTimeActivity.this.getResources().getString(R.string.Hrs).length()) - 1);
            }
            if (RegisterTimeActivity.this.selectedFullMin == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedFullMin)) {
                min = "0";
            } else {
                min = RegisterTimeActivity.this.selectedFullMin.substring(0, (RegisterTimeActivity.this.selectedFullMin.length() - RegisterTimeActivity.this.getResources().getString(R.string.Mins).length()) - 1);
            }
            if ((hrs != null && hrs.length() > 0) || (min != null && min.length() > 0)) {
                RegisterTimeActivity.this.overTime1Mins = AppUtils.getMinutesFromHours(hrs, min);
                RegisterTimeActivity.this.displayOverTimeinView(RegisterTimeActivity.this.overTime1Mins, RegisterTimeActivity.this.sHrsFullOvertime, RegisterTimeActivity.this.sMinFullOvertime);
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.overTime1Mins);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C07597 implements OnItemSelectedListener {
        C07597() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            RegisterTimeActivity.this.selectedFullMin = RegisterTimeActivity.this.spFullMinList.getSelectedItem().toString();
            String hrs = "";
            String min = "";
            if (RegisterTimeActivity.this.selectedFullHrs == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedFullHrs)) {
                hrs = "0";
            } else {
                hrs = RegisterTimeActivity.this.selectedFullHrs.substring(0, (RegisterTimeActivity.this.selectedFullHrs.length() - RegisterTimeActivity.this.getResources().getString(R.string.Hrs).length()) - 1);
            }
            if (RegisterTimeActivity.this.selectedFullMin == null || "".equalsIgnoreCase(RegisterTimeActivity.this.selectedFullMin)) {
                min = "0";
            } else {
                min = RegisterTimeActivity.this.selectedFullMin.substring(0, (RegisterTimeActivity.this.selectedFullMin.length() - RegisterTimeActivity.this.getResources().getString(R.string.Mins).length()) - 1);
            }
            if ((hrs != null && hrs.length() > 0) || (min != null && min.length() > 0)) {
                RegisterTimeActivity.this.overTime1Mins = AppUtils.getMinutesFromHours(hrs, min);
                RegisterTimeActivity.this.displayOverTimeinView(RegisterTimeActivity.this.overTime1Mins, RegisterTimeActivity.this.sHrsFullOvertime, RegisterTimeActivity.this.sMinFullOvertime);
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.overTime1Mins);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C07608 implements OnClickListener {
        C07608() {
        }

        public void onClick(View v) {
            Calendar c = Calendar.getInstance();
            RegisterTimeActivity.this.mHour = c.get(Calendar.HOUR);
            RegisterTimeActivity.this.mMinute = c.get(Calendar.MINUTE);
            if (RegisterTimeActivity.this.mHour > 0 || RegisterTimeActivity.this.mMinute > 0) {
                RegisterTimeActivity.this.tvworkHrsStart.setText(String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mHour)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mMinute)}));
                long minutes = RegisterTimeActivity.this.getTimeDifferenceinMinutes(RegisterTimeActivity.this.tvworkHrsStart.getText().toString(), RegisterTimeActivity.this.tvworkHrsEnd.getText().toString());
                if (minutes == 0) {
                    AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), RegisterTimeActivity.this.getResources().getString(R.string.start_end_not_same));
                } else if (minutes < 0) {
                    RegisterTimeActivity.this.tvworkHrsStart.setText(String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mHour)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mMinute)}));
                    String result = AppUtils.getIncrementByOne(RegisterTimeActivity.this.tvendDate.getText().toString(), 1);
                    if (result != null) {
                        RegisterTimeActivity.this.tvendDate.setText(result);
                    }
                    int startinter = (RegisterTimeActivity.this.mHour * 60) + RegisterTimeActivity.this.mMinute;
                    RegisterTimeActivity.this.workMins = (long) ((int) (1440 + minutes));
                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
                } else {
                    RegisterTimeActivity.this.workMins = minutes;
                    RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
                    RegisterTimeActivity.this.tvworkHrsStart.setText(String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mHour)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mMinute)}));
                }
            }
        }
    }

    class C07619 implements OnClickListener {
        C07619() {
        }

        public void onClick(View v) {
            Calendar c = Calendar.getInstance();
            RegisterTimeActivity.this.mHour = c.get(Calendar.HOUR);
            RegisterTimeActivity.this.mMinute = c.get(Calendar.MINUTE);
            long minutes = RegisterTimeActivity.this.getTimeDifferenceinMinutes(RegisterTimeActivity.this.tvworkHrsStart.getText().toString(), RegisterTimeActivity.this.mHour + ":" + RegisterTimeActivity.this.mMinute);
            if (minutes == 0) {
                RegisterTimeActivity.this.tvworkHrsEnd.setText(String.format("%02d", new Object[]{Integer.valueOf(0)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(0)}));
                AlertDialogHelper.getNotificatonAlert(RegisterTimeActivity.this.context, RegisterTimeActivity.this.getString(R.string.app_name), RegisterTimeActivity.this.getResources().getString(R.string.start_end_not_same));
            } else if (minutes < 0) {
                RegisterTimeActivity.this.tvworkHrsEnd.setText(String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mHour)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mMinute)}));
                String result = AppUtils.getIncrementByOne(RegisterTimeActivity.this.tvendDate.getText().toString(), 1);
                if (result != null) {
                    RegisterTimeActivity.this.tvendDate.setText(result);
                }
                int startinter = (RegisterTimeActivity.this.mHour * 60) + RegisterTimeActivity.this.mMinute;
                RegisterTimeActivity.this.workMins = (long) ((int) (1440 + minutes));
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
            } else {
                RegisterTimeActivity.this.workMins = minutes;
                RegisterTimeActivity.this.onTimeget(RegisterTimeActivity.this.workMins);
                RegisterTimeActivity.this.tvworkHrsEnd.setText(String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mHour)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(RegisterTimeActivity.this.mMinute)}));
            }
        }
    }

    public class MyBaseAdapter extends BaseAdapter {
        ImageButton btnCancel;
        Context context;
        LayoutInflater inflater;
        ETechTextView tvAttachment;
        ETechTextView tvasServiceName;
        ETechTextView tvasServiceTime;

        private class MyViewHolder {
            public MyViewHolder(View item) {
                MyBaseAdapter.this.tvasServiceName = (ETechTextView) item.findViewById(R.id.txtasServiceName);
                MyBaseAdapter.this.tvasServiceTime = (ETechTextView) item.findViewById(R.id.txtasServiceTime);
                MyBaseAdapter.this.btnCancel = (ImageButton) item.findViewById(R.id.imgbtncancel);
                MyBaseAdapter.this.tvAttachment = (ETechTextView) item.findViewById(R.id.tvatachment1);
            }
        }

        public MyBaseAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        public int getCount() {
            return RegisterTimeActivity.this.arrExtraService.size();
        }

        public WorkServiceMap getItem(int position) {
            return (WorkServiceMap) RegisterTimeActivity.this.arrExtraService.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.registertime_extraservice_ls_items, parent, false);
                convertView.setTag(new MyViewHolder(convertView));
                WorkServiceMap extService = getItem(position);
                this.tvasServiceName.setText(extService.getRefServiceName());
                RegisterTimeActivity.this.displayHoursInView(this.tvasServiceTime, Integer.parseInt(extService.getServiceTime()), RegisterTimeActivity.this.getResources().getString(R.string.tv_hours));
            }
            View finalConvertView = convertView;
            this.btnCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    try {
                        RegisterTimeActivity.this.arrExtraService.remove(position);
                        RegisterTimeActivity.this.removeImagesFromLocal(position);
                        MyBaseAdapter.this.notifyDataSetChanged();
                        if (RegisterTimeActivity.this.arrExtraService != null) {
                            try {
                                RegisterTimeActivity.this.lsasService.setAdapter(new MyBaseAdapter(MyBaseAdapter.this.context));
                                AppUtils.setListViewHeightBasedOnChildrenLatest(RegisterTimeActivity.this.lsasService);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        RegisterTimeActivity.this.lsasService.setVisibility(View.GONE);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            });
            this.tvAttachment.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WorkServiceMap extService = MyBaseAdapter.this.getItem(position);
                    if (extService.getAttachmentList() == null || extService.getAttachmentList().size() <= 0) {
                        Toast.makeText(MyBaseAdapter.this.context, "No Attachments", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(RegisterTimeActivity.this, SlideShowActivity.class);
                    intent.putExtra("ImageList", extService.getAttachmentList());
                    RegisterTimeActivity.this.startActivity(intent);
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WorkServiceMap extService = MyBaseAdapter.this.getItem(position);
                    RegisterTimeActivity.this.curEditingServiceIndex = position;
                    RegisterTimeActivity.this.isExtraServiceEdit = Boolean.valueOf(true);
                    Intent intent = new Intent(RegisterTimeActivity.this, ExtraServiceActivity.class);
                    intent.putExtra("EditExtraService", extService);
                    intent.putExtra("isEditable", RegisterTimeActivity.this.isExtraServiceEdit);
                    RegisterTimeActivity.this.startActivityForResult(intent, RegisterTimeActivity.this.REQ_CODE_EXTRA_SERVICE_EDIT);
                }
            });
            return convertView;
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment1 extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((RegisterTimeActivity)getActivity()).populateSetDate1(yy, mm + 1, dd);
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return dp;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((RegisterTimeActivity)getActivity()).populateSetDate(yy, mm + 1, dd);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createview();
    }

    private void createview() {
        int i;
        setContentView(R.layout.activity_register_time);
        this.isFromMenu = getIntent().getBooleanExtra("isFromMenu", false);
        this.is_from_mng_jobber = getIntent().getBooleanExtra("is_from_mng_jobber", false);
        if (this.isFromMenu) {
            this.header.setLeftBtnImage(R.drawable.ic_back_arrow);
            this.header.setLeftBtnClickListener(new C07531());
        }
        this.header.setTitle(getString(R.string.title_activity_register_time));
        this.header.hideRightBtn();
        getviews();
        setOverTimeViewAdapter();
        String isOvertimeautomatic = Preference.getSharedPref(Constants.PREF_WORK_OVERTIME_AUTOMATIC, Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO);
        this.defaultWorkTime = Preference.getSharedPref(Constants.PREF_DEFAULT_WORKHRS, "0");
        if (isOvertimeautomatic != null) {
            this.isUserDependOvertimeAutomatic = isOvertimeautomatic;
        }
        this.curWorkSheet = (WorkSheet) getIntent().getSerializableExtra("WorksheetEditData");
        if (this.curWorkSheet != null) {
            this.isEditable = Boolean.valueOf(true);
            this.isImgFromURL = Boolean.valueOf(true);
            if (this.curWorkSheet.getApproveStatus() == null || !this.curWorkSheet.getApproveStatus().equalsIgnoreCase("reject")) {
                this.tvNorejectnote.setVisibility(View.GONE);
                this.tvRejectReason.setVisibility(View.GONE);
                this.txtRejectMngNote.setVisibility(View.GONE);
            } else {
                this.txtRejectMngNote.setVisibility(View.VISIBLE);
                if (this.curWorkSheet.getRejectComments() == null || this.curWorkSheet.getRejectComments().equalsIgnoreCase("")) {
                    this.tvNorejectnote.setVisibility(View.VISIBLE);
                } else {
                    this.tvRejectReason.setVisibility(View.VISIBLE);
                    this.tvRejectReason.setText(this.curWorkSheet.getRejectComments());
                }
            }
            if (this.curWorkSheet.getOvertimeRuleArrayList() != null && this.curWorkSheet.getOvertimeRuleArrayList().size() > 0) {
                this.rulesList.clear();
                this.rulesList.addAll(this.curWorkSheet.getOvertimeRuleArrayList());
            }
            if (this.curWorkSheet.getIsworksheetAutomaticEditmode() != null) {
                this.isUserDependOvertimeAutomatic = this.curWorkSheet.getIsworksheetAutomaticEditmode();
                if (this.isUserDependOvertimeAutomatic != null && this.isUserDependOvertimeAutomatic.equalsIgnoreCase(Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO)) {
                    this.llOfOverTimeView.setVisibility(View.VISIBLE);
                    this.overtime2Mins = (long) Integer.parseInt(((OvertimeRule) this.rulesList.get(0)).getActualvalue());
                    displayHoursInView(this.spHalfHrsList, this.spHalfMinList, Integer.parseInt(((OvertimeRule) this.rulesList.get(0)).getActualvalue()));
                    displayOverTimeinView(this.overtime2Mins, this.sHrsHalfOvertime, this.sMinHalfOvertime);
                    this.overTime1Mins = (long) Integer.parseInt(((OvertimeRule) this.rulesList.get(1)).getActualvalue());
                    displayHoursInView(this.spFullHrsList, this.spFullMinList, Integer.parseInt(((OvertimeRule) this.rulesList.get(1)).getActualvalue()));
                    displayOverTimeinView(this.overTime1Mins, this.sHrsFullOvertime, this.sMinFullOvertime);
                }
            }
        } else if (Constants.UserDependsOvertimeRulesList != null && Constants.UserDependsOvertimeRulesList.size() > 0) {
            this.rulesList.clear();
            this.rulesList.addAll(Constants.UserDependsOvertimeRulesList);
        }
        if (this.isUserDependOvertimeAutomatic.equalsIgnoreCase(Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO)) {
            this.llOfOverTimeView.setVisibility(View.VISIBLE);
            this.ll_dropdon_overtime.setVisibility(View.VISIBLE);
            this.ll_50_overtime.setVisibility(View.VISIBLE);
            this.ll_100_overtime.setVisibility(View.VISIBLE);
        } else {
            this.llOfOverTimeView.setVisibility(View.GONE);
            this.ll_dropdon_overtime.setVisibility(View.GONE);
            this.ll_50_overtime.setVisibility(View.GONE);
            this.ll_100_overtime.setVisibility(View.GONE);
        }
        hideOvertimeViewInAutomatic();
        setDefaultDate(this.tvstartdt);
        setDefaultDate(this.tvendDate);
        if (this.curWorkSheet == null) {
            this.tvworkHrsStart.setText("07:00");
            int temp = Integer.parseInt(this.defaultWorkTime) + 420;
            int i2 = 0;
            int workMn = 0;
            try {
                i2 = temp / 60;
                workMn = temp % 60;
                if (i2 < 0) {
                    i2 = 0;
                }
                if (workMn < 0) {
                    workMn = 0;
                }
            } catch (Exception e) {
            }
            this.tvworkHrsEnd.setText(String.format("%02d", new Object[]{Integer.valueOf(i2)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(workMn)}));
            this.workMins = Long.parseLong(this.defaultWorkTime);
            this.breakMins = 30;
            onTimeget(this.workMins);
        }
        getImageSelectionView();
        this.progressHelper = new ProgressHelper(this);
        String role = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
        List<String> breakminitsList = new ArrayList();
        List<String> kmsList = new ArrayList();
        for (i = 0; i <= 120; i += 10) {
            breakminitsList.add(String.format("%02d", new Object[]{Integer.valueOf(i)}) + " " + getResources().getString(R.string.Min));
        }
        for (i = 0; i <= 60; i += 5) {
            kmsList.add(String.format("%02d", new Object[]{Integer.valueOf(i)}) + " " + getResources().getString(R.string.km));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, R.layout.spinner_text_register_screen, breakminitsList);
        ArrayAdapter<String> kmListAdapter = new ArrayAdapter(this, R.layout.spinner_text_register_screen, kmsList);
        this.spbreaktimeList.setAdapter(arrayAdapter);
        this.spbreaktimeList.setSelection(3);
        this.spbreaktimeList.setOnItemSelectedListener(new C07542());
        this.curWorkSheet = (WorkSheet) getIntent().getSerializableExtra("WorksheetEditData");
        if (this.curWorkSheet != null) {
            String qry;
            HashMap<String, String> service;
            this.isEditable = Boolean.valueOf(true);
            this.isImgFromURL = Boolean.valueOf(true);
            this.atvproject.setText(this.curWorkSheet.getProjectName());
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
            try {
                this.tvstartdt.setText(output.format(input.parse(this.curWorkSheet.getWorkDate())));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            try {
                this.tvendDate.setText(output.format(input.parse(this.curWorkSheet.getWorkEndDate())));
            } catch (ParseException e22) {
                e22.printStackTrace();
            }
            this.edtcomment.setText(this.curWorkSheet.getComments());
            if (this.curWorkSheet.getAttachementList() != null) {
                this.imageSelectionView.setImageInView(this.curWorkSheet.getAttachementList());
            }
            if (this.curWorkSheet.getServiceObjectsArray() != null) {
                for (i = 0; i < this.curWorkSheet.getServiceObjectsArray().size(); i++) {
                    Service service2 = (Service) this.curWorkSheet.getServiceObjectsArray().get(i);
                    WorkServiceMap workServiceMap = new WorkServiceMap();
                    workServiceMap.setRefServiceId(service2.getRefServiceId());
                    workServiceMap.setRefServiceName(service2.getServiceName());
                    workServiceMap.setServiceTime(service2.getServiceTime());
                    workServiceMap.setAttachmentList(service2.getAttachmentList());
                    workServiceMap.setService_comments(service2.getExtraServiceComment());
                    workServiceMap.setBaseService(this.curWorkSheet.getRefServiceId());
                    this.arrExtraService.add(workServiceMap);
                }
                this.txtNoExtraService.setVisibility(View.GONE);
                this.lsasService.setVisibility(View.VISIBLE);
                this.lsasService.setAdapter(new MyBaseAdapter(this.context));
                AppUtils.setListViewHeightBasedOnChildrenLatest(this.lsasService);
            }
            if (this.curWorkSheet.getWorkHrs() != null) {
                this.workMins = (long) Integer.parseInt(this.curWorkSheet.getWorkHrs());
            }
            if (this.curWorkSheet.getBreakTime() != null) {
                this.breakMins = (long) Integer.parseInt(this.curWorkSheet.getBreakTime());
                String str = String.format("%02d", new Object[]{Long.valueOf(this.breakMins)}) + " " + getResources().getString(R.string.Min);
                this.spbreaktimeList.setSelection(getIndex(this.spbreaktimeList, str));
            }
            if (this.curWorkSheet.getkMDrive() != null) {
                this.kelometer.setText(this.curWorkSheet.getkMDrive());
            }
            if (this.curWorkSheet.getWorkStartTime() != null) {
                this.tvworkHrsStart.setText(this.curWorkSheet.getWorkStartTime().substring(0, this.curWorkSheet.getWorkStartTime().length() - 3));
            }
            if (this.curWorkSheet.getWorkEndTime() != null) {
                this.tvworkHrsEnd.setText(this.curWorkSheet.getWorkEndTime().substring(0, this.curWorkSheet.getWorkEndTime().length() - 3));
            }
            //0928
            String strIsPTOBank = this.curWorkSheet.getIs_ptobank();
            this.is_ptobank = false;
            if(strIsPTOBank.equalsIgnoreCase("yes"))
                this.is_ptobank = true;
            if(is_ptobank)
                iv_ptobank.setImageDrawable(getResources().getDrawable(R.drawable.selection_mark_on));
            else
                iv_ptobank.setImageDrawable(getResources().getDrawable(R.drawable.selection_mark_off));

            try {
                long minutes = getTimeDifferenceinMinutes(this.tvworkHrsStart.getText().toString(), this.tvworkHrsEnd.getText().toString());
                if (minutes < 0) {
                    minutes = (long) ((int) (1440 + minutes));
                    this.workMins = minutes;
                    onTimeget(minutes);
                } else {
                    this.workMins = minutes;
                    onTimeget(minutes);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            if (this.curWorkSheet.getTotalWorkTime() != null) {
                displayHoursInView((long) Integer.parseInt(this.curWorkSheet.getTotalWorkTime()));
            }
            onTimeget(this.workMins);
            if (this.curWorkSheet.getRefProjectId() != null) {
                qry = "select * from tblproject where project_id = '" + this.curWorkSheet.getRefProjectId() + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
                HashMap<String, String> project = this.dbHelper.selSingleRecordFromDB(qry, null);
                if (project != null) {
                    this.atvproject.setText(((String) project.get("project_number")) + " | " + ((String) project.get("project_name")));
                    this.projectid = project;
                }
            }
            if (getIntent().hasExtra("EditFlag")) {
                this.editFlage = getIntent().getStringExtra("EditFlag");
                if (this.editFlage == null || !this.editFlage.equalsIgnoreCase("approve")) {
                    this.tvnoeditprojectname.setVisibility(View.GONE);
                    this.atvproject.setVisibility(View.VISIBLE);
                } else {
                    this.save.setText(getResources().getString(R.string.btn_title_approve));
                    this.tvnoeditprojectname.setVisibility(View.VISIBLE);
                    if (!(this.atvproject.getText() == null || this.atvproject.getText().toString().equalsIgnoreCase(""))) {
                        this.tvnoeditprojectname.setText(this.atvproject.getText().toString());
                    }
                    this.atvproject.setVisibility(View.GONE);
                }
            }
            if (this.curWorkSheet.getRefServiceId() != null) {
                qry = "select * from tblservice where service_id = '" + this.curWorkSheet.getRefServiceId() + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                service = this.dbHelper.selSingleRecordFromDB(qry, null);
                if (service != null) {
                    this.atctvservice.setText((String) service.get("service_name"));
                }
            }
            if (this.curWorkSheet.getRefBranchId() != null) {
                qry = "select * from tblbranch where branch_id = '" + this.curWorkSheet.getRefBranchId() + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
                service = this.dbHelper.selSingleRecordFromDB(qry, null);
                if (service != null) {
                    this.atctvbranch.setText((String) service.get("branch_name"));
                }
            }
        } else {
            setClearView();
            this.isEditable = Boolean.valueOf(false);

            String project = Preference.getSharedPref("LastWorkedProject", "");
            if(project != null && project.length() > 0){
                this.atvproject.setText(project);
                String qry = "select project_id from tblproject where project_name='" + project.substring(project.indexOf("|") + 2) + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
                this.projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
            }

        }
        this.PnameList = getListFromLocalDB("tblproject", "project_name", "active", "project_status");
        if (this.PnameList != null) {
            getProjectListAdapter(this.PnameList);
        }
        this.serviceList = getListFromLocalDB("tblservice", "service_name");
        if (this.serviceList != null) {
            getServiceAdapter(this.serviceList);
        }
        this.BranchList = getListFromLocalDB("tblbranch", "branch_name");
        if (this.BranchList != null) {
            getBranchAdapter(this.BranchList);
        }
        this.save.setOnClickListener(this.savebtnclickListener);
        this.llAdditionalWork.setOnClickListener(this.imgbtnaddserviceclicklistener);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        createview();
    }

    public void hideOvertimeViewInAutomatic() {
        if (this.isUserDependOvertimeAutomatic.equalsIgnoreCase(Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_YES) && this.rulesList.size() > 0) {
            this.overtimeTextviewList.clear();
            this.overtimeHrsTVList.clear();
            this.overtimeMinTVList.clear();
            for (int i = 0; i < this.rulesList.size(); i++) {
                View labels = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.overtimetextview, null);
                TextView lblRulename = (TextView) labels.findViewById(R.id.lblrulename);
                TextView tvHrs = (TextView) labels.findViewById(R.id.tvtotaltimehrsovertm1);
                TextView tvMin = (TextView) labels.findViewById(R.id.tvtotaltimeminsovertm1);
                if (this.rulesList != null && this.rulesList.size() > 0) {
                    lblRulename.setText(((OvertimeRule) this.rulesList.get(i)).getRuleName() + " % ");
                }
                this.ll_parenttextview.addView(labels);
                this.overtimeTextviewList.add(lblRulename);
                this.overtimeHrsTVList.add(tvHrs);
                this.overtimeMinTVList.add(tvMin);
            }
        }
    }

    private void setbreaktimeDropdown(List<String> breakminitsList) {
        this.spbreaktimeList.setAdapter(new ArrayAdapter(this, R.layout.spinner_text_register_screen, breakminitsList));
        this.spbreaktimeList.setSelection(3);
        this.spbreaktimeList.setOnItemSelectedListener(new C07553());
    }

    private void setOverTimeViewAdapter() {
        List<String> HrsList = new ArrayList();
        List<String> MInsList = new ArrayList();
        HrsList = Arrays.asList(getResources().getStringArray(R.array.hrs_array));
        MInsList = Arrays.asList(getResources().getStringArray(R.array.minits_array));
        this.spHalfHrsList.setAdapter(new SpinnerListAdapter(this.context, HrsList));
        this.spHalfMinList.setAdapter(new SpinnerListAdapter(this.context, MInsList));
        this.spFullHrsList.setAdapter(new SpinnerListAdapter(this.context, HrsList));
        this.spFullMinList.setAdapter(new SpinnerListAdapter(this.context, MInsList));
        this.spHalfHrsList.setOnItemSelectedListener(new C07564());
        this.spHalfMinList.setOnItemSelectedListener(new C07575());
        this.spFullHrsList.setOnItemSelectedListener(new C07586());
        this.spFullMinList.setOnItemSelectedListener(new C07597());
    }

    private void setDefaultDate(TextView view) {
        String formattedDate = new SimpleDateFormat("dd MMM yy").format(Calendar.getInstance().getTime());
        Log.d("current time>>>", formattedDate);
        view.setText(formattedDate);
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void onTimeget(long totalminutes) {
        long temp = this.workMins - this.breakMins;
        if (this.isUserDependOvertimeAutomatic == null || !this.isUserDependOvertimeAutomatic.equalsIgnoreCase(Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_YES)) {
            if (this.rulesList != null && this.rulesList.size() > 0) {
                ((OvertimeRule) this.rulesList.get(0)).setActualvalue(String.valueOf(this.overtime2Mins));
                ((OvertimeRule) this.rulesList.get(1)).setActualvalue(String.valueOf(this.overTime1Mins));
            }
            this.totalmins = ((this.workMins - this.overTime1Mins) - this.overtime2Mins) - this.breakMins;
            if (this.overtime2Mins > 0) {
                displayOverTimeinView(this.overtime2Mins, this.sHrsHalfOvertime, this.sMinHalfOvertime);
            }
            if (this.overTime1Mins > 0) {
                displayOverTimeinView(this.overTime1Mins, this.sHrsFullOvertime, this.sMinFullOvertime);
            }
            displayHoursInView(this.totalmins);
        } else {
            if (temp > 0) {
                setOvertimelabels((int) temp, Integer.parseInt(this.defaultWorkTime));
            }
            displayHoursInView(this.totalmins);
        }
        Log.d(this.TAG, "ONTIMEGET()--->>WorkMins--" + (this.workMins / 60) + "Totalmins--" + (this.totalmins / 60));
    }

    private boolean onTimeget() {
        long totalmins1 = this.totalmins;
        if (totalmins1 < 0) {
            return true;
        }
        displayHoursInView(totalmins1);
        return false;
    }

    private long getTimeDifferenceinMinutes(String tvworkHrsStart, String tvworkHrsEnd) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            long mills = format.parse(tvworkHrsEnd).getTime() - format.parse(tvworkHrsStart).getTime();
            return (long) ((((int) (mills / DateUtils.MILLIS_PER_HOUR)) * 60) + (((int) (mills / DateUtils.MILLIS_PER_MINUTE)) % 60));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void setClearView() {
        this.atvproject.setText("");
        this.edtcomment.setText("");
        this.atctvservice.setText("");
        this.atctvbranch.setText("");
        this.kelometer.setText("");
        this.arrExtraService.clear();
    }

    private void clearTextviewArrayList() {
        int i;
        for (i = 0; i < this.overtimeHrsTVList.size(); i++) {
            ((TextView) this.overtimeHrsTVList.get(i)).setText(String.format("%02d", new Object[]{Integer.valueOf(0)}) + getResources().getString(R.string.txt_h));
        }
        for (i = 0; i < this.overtimeHrsTVList.size(); i++) {
            ((TextView) this.overtimeMinTVList.get(i)).setText(String.format("%02d", new Object[]{Integer.valueOf(0)}) + getResources().getString(R.string.txt_m));
        }
    }

    private void displayHoursInView(long time) {
        long workHrs = 0;
        long workMn = 0;
        try {
            workHrs = time / 60;
            workMn = time % 60;
            if (workHrs < 0) {
                workHrs = 0;
            }
            if (workMn < 0) {
                workMn = 0;
            }
        } catch (Exception e) {
        }
        this.sHrs.setText(String.format("%02d", new Object[]{Long.valueOf(workHrs)}) + getResources().getString(R.string.txt_h));
        this.sMin.setText(String.format("%02d", new Object[]{Long.valueOf(workMn)}) + getResources().getString(R.string.txt_m));
    }

    private void displayOverTimeinView(long time, TextView sHrs1, TextView sMin1) {
        long workHrs = 0;
        long workMn = 0;
        try {
            workHrs = time / 60;
            workMn = time % 60;
            if (workHrs < 0) {
                workHrs = 0;
            }
            if (workMn < 0) {
                workMn = 0;
            }
        } catch (Exception e) {
        }
        sHrs1.setText(String.format("%02d", new Object[]{Long.valueOf(workHrs)}) + getResources().getString(R.string.txt_h));
        sMin1.setText(String.format("%02d", new Object[]{Long.valueOf(workMn)}) + getResources().getString(R.string.txt_m));
    }

    private void getImageSelectionView() {
        int deviceWidth = getResources().getDisplayMetrics().widthPixels - AppUtils.dpToPixel(30.0f, this);
        AppUtils.hideKeyBoard(this);
        this.dialog = new Dialog(this.context);
        this.dialog.requestWindowFeature(1);
        this.imageSelectionView = (ImageSelectionView) findViewById(R.id.imgselectionView2);
        LayoutParams layoutParams = new LayoutParams(deviceWidth, -2);
        layoutParams.setMargins(AppUtils.dpToPixel(15.0f, this), 0, AppUtils.dpToPixel(15.0f, this), 0);
        this.imageSelectionView.setLayoutParams(layoutParams);
    }

    private List<String> getListFromLocalDB(String tblname, String list_name) {
        this.dbHelper = new DatabaseHelper(this.context);
        String qry = "select * from " + tblname;
        if (tblname.equalsIgnoreCase("tblservice")) {
            qry = qry + " where ref_company_id != '0'";
        }
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        ArrayList<HashMap<String, String>> list = this.dbHelper.selectRecordsFromDBList(qry, null);
        List<String> fieldlist = new ArrayList();
        if (list != null && list.size() > 0 && list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (tblname.equalsIgnoreCase("tblproject")) {
                    fieldlist.add(((String) ((HashMap) list.get(i)).get("project_number")) + " | " + ((String) ((HashMap) list.get(i)).get(list_name)));
                } else {
//                    fieldlist.add(((HashMap) list.get(i)).get(list_name));
                    fieldlist.add((String) ((HashMap) list.get(i)).get(list_name));

                }
            }
        }
        return fieldlist;
    }

    private List<String> getListFromLocalDB(String tblname, String list_name, String status, String field) {
        this.dbHelper = new DatabaseHelper(this.context);
        String qry = "select * from " + tblname + " where " + field + "='" + status + "'";
        if (tblname.equalsIgnoreCase("tblservice")) {
            qry = qry + " where ref_company_id != '0'";
        }
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        ArrayList<HashMap<String, String>> list = this.dbHelper.selectRecordsFromDBList(qry, null);
        List<String> fieldlist = new ArrayList();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                fieldlist.add(((String) ((HashMap) list.get(i)).get("project_number")) + " | " + ((String) ((HashMap) list.get(i)).get(list_name)));
            }
        }
        return fieldlist;
    }

    private void displayHoursInView(Spinner spexServiceHrsList, Spinner spexServiceMinList, int time) {
        int workHrs = 0;
        int workMn = 0;
        try {
            workHrs = time / 60;
            workMn = time % 60;
            if (workHrs < 0) {
                workHrs = 0;
            }
            if (workMn < 0) {
                workMn = 0;
            }
        } catch (Exception e) {
        }
        spexServiceHrsList.setSelection(getIndex(spexServiceHrsList, String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + " " + getResources().getString(R.string.Hrs)));
        spexServiceMinList.setSelection(getIndex(spexServiceMinList, String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + " " + getResources().getString(R.string.Mins)));
    }

    private void setTypeOfServiceAttachment(ArrayList<WorkServiceMap> workServiceMaps) {
        if (workServiceMaps != null && workServiceMaps.size() > 0) {
            int i = 0;
            while (i < workServiceMaps.size()) {
                if (!(((WorkServiceMap) workServiceMaps.get(i)).getAttachmentList() == null || ((WorkServiceMap) workServiceMaps.get(i)).getAttachmentList() == null)) {
                    for (int j = 0; j < ((WorkServiceMap) workServiceMaps.get(i)).getAttachmentList().size(); j++) {
                        ((AttachmentMap) ((WorkServiceMap) workServiceMaps.get(i)).getAttachmentList().get(i)).setType(Constants.ATTACHEMENT_TYPE_URL);
                    }
                }
                i++;
            }
        }
    }

    private String getTimeInMinutes(String tvHrs, String tvMin) {
        return String.valueOf((Integer.parseInt(tvHrs.substring(0, tvHrs.length() - 1)) * 60) + Integer.parseInt(tvMin.substring(0, tvMin.length() - 1)));
    }

    private String validation() {
        try {
            String project = this.atvproject.getText().toString();
            String service = this.atctvservice.getText().toString().trim();
            String branch = this.atctvbranch.getText().toString().trim();
            String comment = this.edtcomment.getText().toString().trim();
            String workstrt = this.tvworkHrsStart.getText().toString().trim();
            String workend = this.tvworkHrsEnd.getText().toString().trim();
            String strtDate = this.tvstartdt.getText().toString().trim();
            String kmstr = this.kelometer.getText().toString().trim();
            String regex = "^[0-9]";
            if (project.length() == 0) {
                return getString(R.string.txt_select_any_project);
            }
            if (this.projectid == null) {
                return getString(R.string.txt_select_any_project);
            }
            String qry = "select project_id from tblproject where project_name='" + project.substring(project.indexOf("|") + 2) + "'";
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            this.projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
            if (this.projectid == null) {
                return getString(R.string.txt_select_any_project);
            }
            if (strtDate.length() == 0) {
                return getString(R.string.txt_select_Start_date);
            }
            if (workstrt.equalsIgnoreCase("00:00")) {
                return getString(R.string.txt_enter_start_time);
            }
            if (workend.equalsIgnoreCase("00:00")) {
                return getString(R.string.txt_enter_end_time);
            }
            if (comment.length() == 0) {
                return getString(R.string.txt_Enter_comment);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 3) {
            try {
                this.imageSelectionView.handleImageData(data);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        WorkServiceMap workServiceMap = (WorkServiceMap) data.getSerializableExtra("ServiceDataObject");
        if (this.isExtraServiceEdit.booleanValue()) {
            this.isExtraServiceEdit = Boolean.valueOf(false);
            this.arrExtraService.set(this.curEditingServiceIndex, workServiceMap);
        } else {
            this.arrExtraService.add(workServiceMap);
        }
        if (this.arrExtraService != null) {
            try {
                this.txtNoExtraService.setVisibility(View.GONE);
                this.lsasService.setVisibility(View.VISIBLE);
                this.lsasService.setAdapter(new MyBaseAdapter(this.context));
                AppUtils.setListViewHeightBasedOnChildrenLatest(this.lsasService);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void getBranchAdapter(List branchList) {
        this.BranchList = branchList;
        this.autocompleteAdapterB = new ArrayAdapter(this, R.layout.auto_complete_textview, this.BranchList);
        this.atctvbranch.setAdapter(this.autocompleteAdapterB);
        this.atctvbranch.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String qry = "select branch_id from tblbranch where branch_name='" + RegisterTimeActivity.this.atctvbranch.getText().toString() + "'";
                RegisterTimeActivity.this.dbHelper = new DatabaseHelper(RegisterTimeActivity.this.context);
                RegisterTimeActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(RegisterTimeActivity.this.context);
                RegisterTimeActivity.this.branchId = RegisterTimeActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
                AppUtils.hideKeyBoard(RegisterTimeActivity.this);
            }
        });
        this.atctvbranch.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                RegisterTimeActivity.this.atctvbranch.showDropDown();
                return false;
            }
        });
    }

    private void getServiceAdapter(List slist) {
        this.serviceList = slist;
        this.autocompleteAdapterS = new ArrayAdapter(this, R.layout.auto_complete_textview, this.serviceList);
        this.atctvservice.setAdapter(this.autocompleteAdapterS);
        this.atctvservice.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String qry = "select * from tblservice where service_name='" + RegisterTimeActivity.this.atctvservice.getText().toString() + "'";
                RegisterTimeActivity.this.dbHelper = new DatabaseHelper(RegisterTimeActivity.this.context);
                RegisterTimeActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(RegisterTimeActivity.this.context);
                RegisterTimeActivity.this.selServiceMap = RegisterTimeActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
                AppUtils.hideKeyBoard(RegisterTimeActivity.this);
            }
        });
        this.atctvservice.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                RegisterTimeActivity.this.atctvservice.showDropDown();
                return false;
            }
        });
    }

    private void getProjectListAdapter(List item) {
        this.PnameList = item;
        this.myAutoCompleteAdapter = new ArrayAdapter(this, R.layout.auto_complete_textview, this.PnameList);
        this.atvproject.setAdapter(this.myAutoCompleteAdapter);
        this.atvproject.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean b) {
                Log.d(RegisterTimeActivity.this.TAG, "onFocusChange()--" + b);
            }
        });
    }

    private void getviews() {
        this.tvstartdt = (TextView) findViewById(R.id.txtstrtdtregister);
        this.tvendDate = (TextView) findViewById(R.id.txtenddateregister);
        this.atvproject = (EtechEditAutoCompleteTextRobotoRegular) findViewById(R.id.atctv_proname);
        this.atvproject.init(this.context, this);
        this.atctvservice = (AutoCompleteTextView) findViewById(R.id.actv_service);
        this.atctvbranch = (AutoCompleteTextView) findViewById(R.id.actvbranch);
        this.tvaddexservice = (ETechTextView) findViewById(R.id.tvaddexservice);
        this.edtcomment = (ETechEditText) findViewById(R.id.edtcomment);
        this.imgbtnaddservice = (ImageButton) findViewById(R.id.imgbtnaddservice);
        this.save = (ETechButton) findViewById(R.id.btnsave);
        //For Signature
        this.sign = (Button) findViewById(R.id.btnsign);
        this.sign.setOnClickListener(this.signbtnclickListener);

        this.lsasService = (ListView) findViewById(R.id.lsasservice);
        this.sHrs = (ETechTextView) findViewById(R.id.tvtotaltimehrs);
        this.sMin = (ETechTextView) findViewById(R.id.tvtotaltimemins);
        this.sHrsHalfOvertime = (ETechTextView) findViewById(R.id.tvtotaltimehrsovertm);
        this.sMinHalfOvertime = (ETechTextView) findViewById(R.id.tvtotaltimeminsovertm);
        this.sHrsFullOvertime = (ETechTextView) findViewById(R.id.tvtotaltimehrsovertmfull);
        this.sMinFullOvertime = (ETechTextView) findViewById(R.id.tvtotaltimeminsovertmfull);
        this.txtNoExtraService = (TextView) findViewById(R.id.txtnoextraService);
        this.imgbtnWorkHrsStart = (ImageButton) findViewById(R.id.img_work_hrs_start);
        this.imgbtnWorkHrsEnd = (ImageButton) findViewById(R.id.img_work_hrs_end);
        this.btnstartTime = (ETechButton) findViewById(R.id.btnstartWork);
        this.btnendTime = (ETechButton) findViewById(R.id.btnfinishWork);
        this.tvworkHrsStart = (ETechTextView) findViewById(R.id.tv_WorkHrs_start);
        this.tvworkHrsEnd = (ETechTextView) findViewById(R.id.tv_WorkHrs_end);
        this.llAdditionalWork = (LinearLayout) findViewById(R.id.imgbtnaddservice1);
        this.llOfOverTimeView = (LinearLayout) findViewById(R.id.ll_of_overtimeview);
        this.spbreaktimeList = (Spinner) findViewById(R.id.breakTime_list);
        this.down_arrow_img = (ImageButton) findViewById(R.id.down_arrow_img);
        this.up_arrow_img = (ImageButton) findViewById(R.id.up_arrow_img);
        this.spFullHrsList = (Spinner) findViewById(R.id.spfullHrsList);
        this.spFullMinList = (Spinner) findViewById(R.id.spfullMinList);
        this.spHalfHrsList = (Spinner) findViewById(R.id.spHalfHrsList);
        this.spHalfMinList = (Spinner) findViewById(R.id.spHalfMinList);
        this.kelometer = (EditText) findViewById(R.id.km_list);
        this.tv_km = (ETechTextView) findViewById(R.id.tv_km_name);
        this.rl_kms = (RelativeLayout) findViewById(R.id.ll_of_break_hrs);
        this.btnstartTime.setOnClickListener(this.startTimeClickListener);
        this.btnendTime.setOnClickListener(this.endTimeClickListener);
        this.imgbtnWorkHrsStart.setOnClickListener(this.timeBtnClickListener);
        this.imgbtnWorkHrsEnd.setOnClickListener(this.timeBtnClickListener);
        this.tv_km.setOnClickListener(this.tvClickLIstener);
        this.ll_parenttextview = (LinearLayout) findViewById(R.id.ll_parenttextview);
        this.ll_dropdon_overtime = (LinearLayout) findViewById(R.id.ll_dropdon_overtime);
        this.ll_100_overtime = (LinearLayout) findViewById(R.id.ll_100_overtime);
        this.ll_50_overtime = (LinearLayout) findViewById(R.id.ll_50_overtime);
        this.tvNorejectnote = (TextView) findViewById(R.id.txtNomngcomments);
        this.txtRejectMngNote = (ETechTextView) findViewById(R.id.tvcommentmanager);
        this.tvRejectReason = (ETechTextView) findViewById(R.id.tvmngrejcetcomments);
        this.tvnoeditprojectname = (TextView) findViewById(R.id.tvnoeditprojectname);

        //0926
        this.iv_ptobank = (ImageView)findViewById(R.id.iv_ptobank);
        this.ll_ptobank = (LinearLayout)findViewById(R.id.ll_ptobank);
        this.ll_ptobank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                is_ptobank = !is_ptobank;
                if(is_ptobank)
                    iv_ptobank.setImageDrawable(getResources().getDrawable(R.drawable.selection_mark_on));
                else
                    iv_ptobank.setImageDrawable(getResources().getDrawable(R.drawable.selection_mark_off));
            }
        });
//        LinearLayout ll_calendar = (LinearLayout)findViewById(R.id.ll_calendar);
//        ll_calendar.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new SelectDateFragment().show(getFragmentManager(), "DatePicker");
//            }
//        });
        setClearView();
        clearTextviewArrayList();
    }

    public void selectDate(View view) {
        new SelectDateFragment().show(getSupportFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        String dateInFormate = formateDate(day + "/" + month + "/" + year);
        if (dateInFormate != null) {
            String days = AppUtils.getDateDifference(dateInFormate, this.tvendDate.getText().toString(), "dd MMM yy");
            this.tvstartdt.setText(dateInFormate);
        }
    }

    public String formateDate(String time) {
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return new SimpleDateFormat("dd MMM yy").format(input.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void populateSetDate1(int year, int month, int day) {
        String enddateInFormate = formateDate(day + "/" + month + "/" + year);
        if (enddateInFormate == null) {
            return;
        }
        if (Integer.parseInt(AppUtils.getDateDifference(this.tvstartdt.getText().toString(), enddateInFormate, "dd MMM yy")) < 0) {
            AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.start_must_greter));
        } else {
            this.tvendDate.setText(enddateInFormate);
        }
    }

    public void toggel_content(View view) {
        if (this.llOfOverTimeView.isShown()) {
            this.up_arrow_img.setVisibility(View.GONE);
            this.down_arrow_img.setVisibility(View.VISIBLE);
            this.llOfOverTimeView.setVisibility(View.GONE);
            return;
        }
        this.llOfOverTimeView.setVisibility(View.VISIBLE);
        this.up_arrow_img.setVisibility(View.VISIBLE);
        this.down_arrow_img.setVisibility(View.GONE);
    }

    public void selectEndDate(View view) {
        new SelectDateFragment1().show(getSupportFragmentManager(), "DatePicker");
    }

    public void onOkClick(int resId1, int titleResId1) {
        if (resId1 == 0) {
            this.progressHelper.showDialog("Loading..");
            new workSheetUploadHelper(this.context).apiUploadWorksheetToServer(strSignedBy,strSignedEmail,strCPerson,strCEmail,singedBitmap, //0918
                    this.workSheet, this.arrExtraService, this.rulesList, this.overtime2Mins, this.overTime1Mins, this.uploadWorksheetActionCallback);
            return;
        }
        this.imageSelectionView.removeImage(this.context);
    }

    public void onItemOptionClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (view.getId() == R.id.atctv_proname) {
            Log.i(this.TAG, "VIEW ARE MATCH>>>>>>");
            String qry = "select project_id from tblproject where project_name='" + this.atvproject.getText().toString().substring(this.atvproject.getText().toString().indexOf("|") + 2) + "'";
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            this.projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(this);
        }
    }

    private void displayHoursInView(ETechTextView view, int time, String editString) {
        int workHrs = 0;
        int workMn = 0;
        try {
            workHrs = time / 60;
            workMn = time % 60;
            if (workHrs < 0) {
                workHrs = 0;
            }
            if (workMn < 0) {
                workMn = 0;
            }
        } catch (Exception e) {
        }
        view.setText(String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + getResources().getString(R.string.txt_m));
    }

    private void removeImagesFromLocal(int position) {
    }

    public void onTimeChange(int Hrs, int Min) {
    }

    public void onTimeChange1(long min) {
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 100) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (!AppUtils.verifyAllPermissions(grantResults)) {
            Toast.makeText(this.context, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void setOvertimelabels(int workMins, int regularMins) {
        this.totalmins = (long) workMins;
        if (workMins > regularMins) {
            int overtimeMins = workMins - regularMins;
            for (int i = 0; i < this.rulesList.size(); i++) {
                int ruleValue = Integer.parseInt(((OvertimeRule) this.rulesList.get(i)).getRuleValue());
                if (ruleValue > overtimeMins) {
                    ((OvertimeRule) this.rulesList.get(i)).setActualvalue(String.valueOf(overtimeMins));
                    displayOverTimeinView((long) overtimeMins, (TextView) this.overtimeHrsTVList.get(i), (TextView) this.overtimeMinTVList.get(i));
                    this.totalmins -= (long) overtimeMins;
                    overtimeMins = 0;
                    break;
                }
                overtimeMins -= ruleValue;
                ((OvertimeRule) this.rulesList.get(i)).setActualvalue(String.valueOf(ruleValue));
                displayOverTimeinView((long) ruleValue, (TextView) this.overtimeHrsTVList.get(i), (TextView) this.overtimeMinTVList.get(i));
                this.totalmins -= (long) ruleValue;
            }
            if (overtimeMins > 0 && this.rulesList.size() > 0) {
                int index = this.rulesList.size() - 1;
                int defaulLastOt = Integer.parseInt(((OvertimeRule) this.rulesList.get(index)).getActualvalue());
                if (defaulLastOt > 0) {
                    overtimeMins += defaulLastOt;
                    this.totalmins += (long) defaulLastOt;
                }
                ((OvertimeRule) this.rulesList.get(index)).setActualvalue(String.valueOf(overtimeMins));
                displayOverTimeinView((long) overtimeMins, (TextView) this.overtimeHrsTVList.get(index), (TextView) this.overtimeMinTVList.get(index));
                this.totalmins -= (long) overtimeMins;
                return;
            }
            return;
        }
        clearTextviewArrayList();
    }
}

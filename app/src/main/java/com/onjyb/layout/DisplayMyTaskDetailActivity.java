package com.onjyb.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.Header;
import com.onjyb.customview.ImageSelectionView;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.OvertimeRule;
import com.onjyb.db.Service;
import com.onjyb.db.WorkServiceMap;
import com.onjyb.db.WorkSheet;
import com.onjyb.helper.MyBroadcastReceiver;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.ProgressHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

public class DisplayMyTaskDetailActivity extends Activity {
    private final String TAG = LoginActivity.class.getName();
    private ActionCallback actionCallBackRejectTask = new C06013();
    private ActionCallback apiCallBackUnReadCount = new C06024();
    Context context = this;
    DatabaseHelper dbHelper;
    ETechEditText edtReject;
    ListView exServiceListview;
    ArrayList<WorkServiceMap> extraServiceList = new ArrayList();
    ImageSelectionView imageSelectionViewWorksheet;
    private String isUserDependOvertimeAutomatic = Constants.PREF_WORK_OVERTIME_AUTOMATIC_STATUS_NO;
    private LinearLayout ll_parenttextview;
    private ArrayList<TextView> overtimeHrsTVList = new ArrayList();
    private ArrayList<TextView> overtimeMinTVList = new ArrayList();
    private ArrayList<TextView> overtimeTextviewList = new ArrayList();
    ProgressHelper progressHelper;
    ETechButton reject;
    OnClickListener rejectBTNClickListener = new C05981();
    public ArrayList<OvertimeRule> rulesListinDisply = new ArrayList();
    ETechTextView tvBranch;
    ETechTextView tvBreakTime;
    ETechTextView tvComments;
    ETechTextView tvDate;
    ETechTextView tvKMDrive;
    TextView tvNoComment;
    TextView tvNoExServices;
    TextView tvNoImgs;
    TextView tvNoRejectComments;
    ETechTextView tvOverTime;
    ETechTextView tvOverTime2;
    ETechTextView tvReject;
    ETechTextView tvService;
    ETechTextView tvStatus;
    ETechTextView tvTotalTime;
    ETechTextView tvWorkHrs;
    ETechTextView tvcreateDate;
    ETechTextView tvdrejectcomment;
    ETechTextView tvproj;
    WorkSheet workSheet;

    class C05981 implements OnClickListener {
        C05981() {
        }

        public void onClick(View v) {
            DisplayMyTaskDetailActivity.this.workSheet.setRejectComments(DisplayMyTaskDetailActivity.this.edtReject.getText().toString());
            new UserHelper(DisplayMyTaskDetailActivity.this.context).apiCallRejectTask(DisplayMyTaskDetailActivity.this.actionCallBackRejectTask, DisplayMyTaskDetailActivity.this.workSheet);
        }
    }

    class C05992 implements OnClickListener {
        C05992() {
        }

        public void onClick(View v) {
            DisplayMyTaskDetailActivity.this.finish();
        }
    }

    class C06013 implements ActionCallback {

        class C06001 implements OnClickListener {
            C06001() {
            }

            public void onClick(View v) {
                DisplayMyTaskDetailActivity.this.startActivity(new Intent(DisplayMyTaskDetailActivity.this.context, MyTaskActivity.class));
                DisplayMyTaskDetailActivity.this.finish();
            }
        }

        C06013() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (DisplayMyTaskDetailActivity.this.progressHelper != null) {
                DisplayMyTaskDetailActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                new UserHelper(DisplayMyTaskDetailActivity.this.context).apiGetUnReadCount(DisplayMyTaskDetailActivity.this.apiCallBackUnReadCount);
                try {
                    AlertDialogHelper.getNotificatonAlert(DisplayMyTaskDetailActivity.this.context, DisplayMyTaskDetailActivity.this.getString(R.string.app_name), new C06001(), ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG), 0, 0, true);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(DisplayMyTaskDetailActivity.this.context, DisplayMyTaskDetailActivity.this.getString(R.string.app_name), res.toString());
        }
    }

    class C06024 implements ActionCallback {
        C06024() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                Log.d("UnreadCount>>>>>>", "Success");
                Intent i = new Intent("any string");
                i.setClass(DisplayMyTaskDetailActivity.this.context, MyBroadcastReceiver.class);
                DisplayMyTaskDetailActivity.this.context.sendBroadcast(i);
            }
        }
    }

    public class MyBaseAdapter extends BaseAdapter {
        ImageButton btnCancel;
        Context context;
        LayoutInflater inflater;
        ArrayList<WorkServiceMap> myList;
        ArrayList<WorkServiceMap> pnameList;
        ETechTextView tvasServiceName;
        ETechTextView tvasServiceTime;

        private class MyViewHolder {
            public MyViewHolder(View item) {
                MyBaseAdapter.this.tvasServiceName = (ETechTextView) item.findViewById(R.id.txtasServiceName);
                MyBaseAdapter.this.tvasServiceTime = (ETechTextView) item.findViewById(R.id.txtasServiceTime);
                MyBaseAdapter.this.btnCancel = (ImageButton) item.findViewById(R.id.imgbtncancel);
            }
        }

        public MyBaseAdapter(Context context, ArrayList<WorkServiceMap> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        public int getCount() {
            return this.myList.size();
        }

        public WorkServiceMap getItem(int position) {
            return (WorkServiceMap) this.myList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.registertime_extraservice_ls_items, parent, false);
                convertView.setTag(new MyViewHolder(convertView));
                WorkServiceMap extraservice = getItem(position);
                this.tvasServiceName.setText(extraservice.getRefServiceName());
                DisplayMyTaskDetailActivity.this.displayHoursInView(this.tvasServiceTime, Integer.parseInt(extraservice.getServiceTime()));
            }
            View finalConvertView = convertView;
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    WorkServiceMap extService = MyBaseAdapter.this.getItem(position);
                    Intent intent = new Intent(DisplayMyTaskDetailActivity.this, ExtraServiceDisplay.class);
                    intent.putExtra("displayExtraService", extService);
                    DisplayMyTaskDetailActivity.this.startActivity(intent);
                }
            });
            return convertView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_my_task_detail);
        AppUtils.setStatusbarColor(this);
        setHeader();
        getViews();
        this.workSheet = (WorkSheet) getIntent().getSerializableExtra("WorksheetEditData");
        Boolean isFromRejectBtn = Boolean.valueOf(getIntent().getBooleanExtra("FromisReject", false));
        if (this.workSheet != null) {
            if (this.workSheet.getOvertimeRuleArrayList() != null && this.workSheet.getOvertimeRuleArrayList().size() > 0) {
                this.rulesListinDisply.clear();
                this.rulesListinDisply.addAll(this.workSheet.getOvertimeRuleArrayList());
            }
            if (this.workSheet.getIsworksheetAutomaticEditmode() != null) {
                this.isUserDependOvertimeAutomatic = this.workSheet.getIsworksheetAutomaticEditmode();
            }
        }
        if (this.rulesListinDisply != null && this.rulesListinDisply.size() > 0) {
            addTextviewOverTimeList();
        }
        if (isFromRejectBtn.booleanValue()) {
            this.edtReject.setVisibility(View.VISIBLE);
            this.reject.setVisibility(View.VISIBLE);
            this.tvReject.setVisibility(View.VISIBLE);
            this.reject.setOnClickListener(this.rejectBTNClickListener);
        } else {
            this.tvReject.setVisibility(View.GONE);
        }
        if (this.workSheet != null) {
            String qry;
            HashMap<String, String> service;
            if (this.workSheet.getAttachementList() != null && this.workSheet.getAttachementList().size() > 0) {
                this.imageSelectionViewWorksheet.setVisibility(View.VISIBLE);
                this.tvNoImgs.setVisibility(View.GONE);
                this.imageSelectionViewWorksheet.hideAddImageBtn();
                this.imageSelectionViewWorksheet.setImageInView(this.workSheet.getAttachementList());
            }
            if (this.workSheet.getRefProjectId() != null) {
                qry = "select * from tblproject where project_id = '" + this.workSheet.getRefProjectId() + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                HashMap<String, String> project = this.dbHelper.selSingleRecordFromDB(qry, null);
                if (project != null) {
                    this.tvproj.setText(((String) project.get("project_number")) + " | " + ((String) project.get("project_name")));
                }
            }
            if (this.workSheet.getRefServiceId() != null) {
                qry = "select * from tblservice where service_id = '" + this.workSheet.getRefServiceId() + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                service = this.dbHelper.selSingleRecordFromDB(qry, null);
                if (service != null) {
                    this.tvService.setText((String) service.get("service_name"));
                }
            }
            if (this.workSheet.getRefBranchId() != null) {
                qry = "select * from tblbranch where branch_id = '" + this.workSheet.getRefBranchId() + "'";
                this.dbHelper = new DatabaseHelper(this.context);
                this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
                service = this.dbHelper.selSingleRecordFromDB(qry, null);
                if (service != null) {
                    this.tvBranch.setText((String) service.get("branch_name"));
                } else {
                    this.tvBranch.setText(getResources().getString(R.string.not_available));
                }
            }
            if (!(this.workSheet.getWorkStartTime() == null || this.workSheet.getWorkEndTime() == null)) {
                this.tvWorkHrs.setText(this.workSheet.getWorkStartTime().substring(0, this.workSheet.getWorkStartTime().length() - 3) + " - " + this.workSheet.getWorkEndTime().substring(0, this.workSheet.getWorkEndTime().length() - 3));
            }
            displayTimeinViews(this.tvBreakTime, this.workSheet.getBreakTime());
            displayTimeinViews(this.tvTotalTime, this.workSheet.getWorkHrs());
            displayTimeinViews(this.tvOverTime, this.workSheet.getWorkOverTime1());
            displayTimeinViews(this.tvOverTime2, this.workSheet.getWorkOverTime2());
            this.tvKMDrive.setText(this.workSheet.getkMDrive() + " " + getResources().getString(R.string.Km));
            if (!this.workSheet.getComments().equalsIgnoreCase("")) {
                this.tvNoComment.setVisibility(View.GONE);
                this.tvComments.setVisibility(View.VISIBLE);
                this.tvComments.setText(this.workSheet.getComments());
            }
            if (this.workSheet.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                this.tvStatus.setText(getResources().getString(R.string.pendding_hrs));
            } else if (this.workSheet.getApproveStatus().equalsIgnoreCase("approve")) {
                this.tvStatus.setText(getResources().getString(R.string.approved_hrs));
            } else if (this.workSheet.getApproveStatus().equalsIgnoreCase("reject")) {
                this.tvStatus.setText(getResources().getString(R.string.rejected_hrs));
            } else {
                this.tvStatus.setText(getResources().getString(R.string.cancel_hrs));
            }
            if (this.workSheet.getApproveStatus().equalsIgnoreCase("reject") && this.workSheet.getRejectComments() != null) {
                this.tvReject.setVisibility(View.VISIBLE);
                if (this.workSheet.getRejectComments().equalsIgnoreCase("") || this.workSheet.getRejectComments() == null) {
                    this.tvNoRejectComments.setVisibility(View.VISIBLE);
                } else {
                    this.tvdrejectcomment.setVisibility(View.VISIBLE);
                    this.tvdrejectcomment.setText(this.workSheet.getRejectComments());
                }
            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
            try {
                this.tvDate.setText(output.format(input.parse(this.workSheet.getUpdatedate())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.tvcreateDate.setText(output.format(input.parse(this.workSheet.getWorkDate())));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            if (this.workSheet.getServiceObjectsArray() != null) {
                for (int i = 0; i < this.workSheet.getServiceObjectsArray().size(); i++) {
                    Service service2 = (Service) this.workSheet.getServiceObjectsArray().get(i);
                    WorkServiceMap workServiceMap = new WorkServiceMap();
                    workServiceMap.setRefServiceId(service2.getRefServiceId());
                    workServiceMap.setRefServiceName(service2.getServiceName());
                    workServiceMap.setServiceTime(service2.getServiceTime());
                    workServiceMap.setAttachmentList(service2.getAttachmentList());
                    workServiceMap.setService_comments(service2.getExtraServiceComment());
                    workServiceMap.setBaseService(this.workSheet.getRefServiceId());
                    this.extraServiceList.add(workServiceMap);
                }
            }
            if (this.extraServiceList != null && this.extraServiceList.size() > 0) {
                try {
                    this.exServiceListview.setVisibility(View.VISIBLE);
                    this.tvNoExServices.setVisibility(View.GONE);
                    this.exServiceListview.setAdapter(new MyBaseAdapter(this.context, this.extraServiceList));
                    AppUtils.setListViewHeightBasedOnChildrenLatest(this.exServiceListview);
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            }
        }
    }

    private void setHeader() {
        try {
            Header header = (Header) findViewById(R.id.headerDispltscreen);
            header.setLeftBtnImage(R.drawable.ic_back_arrow);
            header.setLeftBtnClickListener(new C05992());
            header.setTitle(getString(R.string.MyTaskDisplay));
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

    private void getViews() {
        this.tvproj = (ETechTextView) findViewById(R.id.tvpnameDisply);
        this.tvService = (ETechTextView) findViewById(R.id.tvServiceDisply);
        this.tvBranch = (ETechTextView) findViewById(R.id.tvBranchDisply);
        this.tvDate = (ETechTextView) findViewById(R.id.txtdt);
        this.tvWorkHrs = (ETechTextView) findViewById(R.id.tvworkHrsDisply);
        this.tvOverTime = (ETechTextView) findViewById(R.id.tvOverTimeDisply);
        this.tvOverTime2 = (ETechTextView) findViewById(R.id.tvOverTime2Disply);
        this.tvBreakTime = (ETechTextView) findViewById(R.id.tvBreakDisply);
        this.tvTotalTime = (ETechTextView) findViewById(R.id.tvtotalHrs);
        this.tvKMDrive = (ETechTextView) findViewById(R.id.tvKMDDisply);
        this.tvComments = (ETechTextView) findViewById(R.id.tvdcomment);
        this.tvStatus = (ETechTextView) findViewById(R.id.txtStatusofHours1);
        this.tvNoImgs = (TextView) findViewById(R.id.txtnoImages);
        this.tvNoExServices = (TextView) findViewById(R.id.txtnoextraService);
        this.tvNoComment = (TextView) findViewById(R.id.txtNocomments);
        this.exServiceListview = (ListView) findViewById(R.id.lsdisplySheet);
        this.tvReject = (ETechTextView) findViewById(R.id.tvRejcomment);
        this.tvNoRejectComments = (TextView) findViewById(R.id.txtNorejectcomments);
        this.edtReject = (ETechEditText) findViewById(R.id.edtRejComment1);
        this.tvdrejectcomment = (ETechTextView) findViewById(R.id.tvdrejectcomment);
        this.reject = (ETechButton) findViewById(R.id.btnReject);
        this.imageSelectionViewWorksheet = (ImageSelectionView) findViewById(R.id.imgselectionView2);
        this.ll_parenttextview = (LinearLayout) findViewById(R.id.ll_overtime_list_dip);
        this.tvcreateDate = (ETechTextView) findViewById(R.id.tvcreateDate);
    }

    private void displayTimeinViews(ETechTextView view, String value) {
        int workHrs = 0;
        int workMn = 0;
        try {
            workHrs = Integer.parseInt(value) / 60;
            workMn = Integer.parseInt(value) % 60;
            if (workHrs < 0) {
                workHrs = 0;
            }
            if (workMn < 0) {
                workMn = 0;
            }
        } catch (Exception e) {
        }
        String str = String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + " " + getString(R.string.Hrs);
        view.setText(str);
    }

    private void displayHoursInView(ETechTextView view, int time) {
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
        view.setText(String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(workMn)}));
    }

    public void addTextviewOverTimeList() {
        if (this.rulesListinDisply != null && this.rulesListinDisply.size() > 0) {
            for (int i = 0; i < this.rulesListinDisply.size(); i++) {
                View labels = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.displyovertimeview, null);
                TextView lblRulename = (TextView) labels.findViewById(R.id.lblrulename);
                TextView tvHrs = (TextView) labels.findViewById(R.id.tvtotaltimehrsovertm1);
                TextView tvMin = (TextView) labels.findViewById(R.id.tvtotaltimeminsovertm1);
                lblRulename.setText(((OvertimeRule) this.rulesListinDisply.get(i)).getRuleName() + " % ");
                this.ll_parenttextview.addView(labels);
                this.overtimeTextviewList.add(lblRulename);
                this.overtimeHrsTVList.add(tvHrs);
                this.overtimeMinTVList.add(tvMin);
                displayOverTimeinView((long) Integer.parseInt(((OvertimeRule) this.rulesListinDisply.get(i)).getActualvalue()), (TextView) this.overtimeHrsTVList.get(i), (TextView) this.overtimeMinTVList.get(i));
            }
        }
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
}

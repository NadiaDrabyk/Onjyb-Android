package com.onjyb.layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.beans.LeavesApiHelper;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular.DropDownItemClickListener;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.LeaveType;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

public class LeaveLisingActivity extends BaseDrawerActivity implements DropDownItemClickListener {
    public static String APPROVE_STATUS_APPROVE = "approve";
    public static String APPROVE_STATUS_CANCEL = "cancel";
    public static String APPROVE_STATUS_NOT_PENDING = "not_pendding";
    public static String APPROVE_STATUS_PENDING = Constants.STATUS_PENDING;
    public static String APPROVE_STATUS_REJECT = "reject";
    MyBaseAdapter Adapter;
    String BaseStatus = APPROVE_STATUS_PENDING;
    private List<String> PnameList;
    boolean apiCalled = false;
    private ActionCallback approveLeaveApiCallBack = new ActionCallback() {
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (LeaveLisingActivity.this.progressHelper != null) {
                LeaveLisingActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    Toast.makeText(LeaveLisingActivity.this.context, ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG), Toast.LENGTH_LONG).show();
                    LeaveLisingActivity.this.status = LeaveLisingActivity.APPROVE_STATUS_PENDING;
                    LeaveLisingActivity.this.pageNo = 1;
                    LeaveLisingActivity.this.leaveList = new ArrayList();
                    LeavesApiHelper helper = new LeavesApiHelper(LeaveLisingActivity.this.context);
                    LeaveLisingActivity.this.progressHelper.showDialog("Loading..");
                    if (!LeaveLisingActivity.this.apiCalled) {
                        LeaveLisingActivity.this.apiCalled = true;
                        helper.apiLeaveListing(LeaveLisingActivity.this.status, LeaveLisingActivity.this.pageNo, LeaveLisingActivity.this.is_from_mng_leave, LeaveLisingActivity.this.leaveListApiCallback);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(LeaveLisingActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    };
    private ArrayAdapter<String> autoSpinnerAdapterStatus;
    private ArrayAdapter<String> autocompleteAdapterleave;
    ETechButton btnLeaveHistory;
    OnClickListener btnLeaveHistoryClickListener = new OnClickListener() {
        public void onClick(View v) {
            LeaveLisingActivity.this.leaveList = new ArrayList();
            LeaveLisingActivity.this.pageNo = 1;
            LeaveLisingActivity.this.status = LeaveLisingActivity.APPROVE_STATUS_NOT_PENDING;
            LeavesApiHelper helper = new LeavesApiHelper(LeaveLisingActivity.this.context);
            LeaveLisingActivity.this.progressHelper.showDialog("Loading..");
            if (LeaveLisingActivity.this.apiCalled) {
//                Log.e("ManagerleaveListingActivity:", "apicalled :" + LeaveLisingActivity.this.apiCalled);
                return;
            }
            LeaveLisingActivity.this.apiCalled = true;
            helper.apiLeaveListing(LeaveLisingActivity.this.status, LeaveLisingActivity.this.pageNo, LeaveLisingActivity.this.is_from_mng_leave, LeaveLisingActivity.this.leaveListApiCallback);
        }
    };
    ETechButton btnLeavePendding;
    OnClickListener btnLeavePenddingClickListener = new C06869();
    OnClickListener cancelBtnClickListener = new C06836();
    OnClickListener clearBtnClickListener = new C06825();
    DatabaseHelper dbHelper;
    Boolean isFilterEnable = Boolean.valueOf(false);
    boolean is_from_mng_leave = false;
    ArrayList<LeaveType> leaveList = new ArrayList();
    private ActionCallback leaveListApiCallback = new ActionCallback() {

        class C06771 extends TypeReference<ArrayList<LeaveType>> {
            C06771() {
            }
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (LeaveLisingActivity.this.progressHelper != null) {
                LeaveLisingActivity.this.progressHelper.dismissDialog();
            }
            LeaveLisingActivity.this.apiCalled = false;
            if (statusCode == 1) {
                try {
                    LeaveLisingActivity.this.pageNo++;
                    LeaveLisingActivity.this.leaveList.addAll((ArrayList) new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(((JSONObject) res).getJSONArray("leave_details").toString(), new C06771()));
                    if (LeaveLisingActivity.this.leaveList == null) {
                        return;
                    }
                    if (LeaveLisingActivity.this.isFilterEnable.booleanValue()) {
                        LeaveLisingActivity.this.isFilterEnable = Boolean.valueOf(false);
                        LeaveLisingActivity.this.Adapter = new MyBaseAdapter(LeaveLisingActivity.this.context, LeaveLisingActivity.this.leaveList);
                        LeaveLisingActivity.this.myLeaveListview.setAdapter(LeaveLisingActivity.this.Adapter);
                        return;
                    }
                    LeaveLisingActivity.this.Adapter.notifyDataSetChanged();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(LeaveLisingActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    };
    HashMap<String, String> leaveTypeId;
    private List<String> leaveTypeList;
    private ArrayAdapter<String> myAutoCompleteAdapter;
    ListView myLeaveListview;
    int pageNo = 1;
    ProgressHelper progressHelper;
    HashMap<String, String> projectid;
    HashMap<String, String> projectname;
    OnClickListener searchBtnClickListener = new C06814();
    private List<String> serviceList;
    String status = APPROVE_STATUS_PENDING;
    private List<String> statusList;
    ETechTextView txtstatuslbl;

    class C06781 implements OnClickListener {
        C06781() {
        }

        public void onClick(View v) {
            LeaveLisingActivity.this.autoProject.setDropDownWidth(LeaveLisingActivity.this.autoProject.getWidth());
            LeaveLisingActivity.this.mDrawerLayout.openDrawer(LeaveLisingActivity.this.linMenuViewRight);
        }
    }

    class C06792 implements OnScrollListener {
        C06792() {
        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0) {
                Boolean flag = Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_LEAVE, "")));
                if (firstVisibleItem + visibleItemCount == totalItemCount && visibleItemCount > 0 && flag.booleanValue()) {
                    LeavesApiHelper helper = new LeavesApiHelper(LeaveLisingActivity.this.context);
                    if (flag.booleanValue()) {
                        LeaveLisingActivity.this.progressHelper.showDialog("Loading..");
                        if (LeaveLisingActivity.this.apiCalled) {
//                            Log.e("ManagerleaveListingActivity:", "apicalled :" + LeaveLisingActivity.this.apiCalled);
                            return;
                        }
                        LeaveLisingActivity.this.apiCalled = true;
                        helper.apiLeaveListing(LeaveLisingActivity.this.status, LeaveLisingActivity.this.pageNo, LeaveLisingActivity.this.is_from_mng_leave, LeaveLisingActivity.this.leaveListApiCallback);
                    }
                }
            }
        }
    }

    class C06803 implements OnItemSelectedListener {
        C06803() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            String pName = LeaveLisingActivity.this.autoLeaveType.getSelectedItem().toString();
            AppUtils.hideKeyBoard(LeaveLisingActivity.this);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06814 implements OnClickListener {
        C06814() {
        }

        public void onClick(View v) {
            AppUtils.hideKeyBoard(LeaveLisingActivity.this);
            LeaveLisingActivity.this.leaveList = new ArrayList();
            LeaveLisingActivity.this.mDrawerLayout.closeDrawers();
            String pId = "";
            String lId = "";
            if (LeaveLisingActivity.this.projectid != null) {
                if (LeaveLisingActivity.this.autoProject.getText().toString().equalsIgnoreCase("")) {
                    pId = "";
                } else if (LeaveLisingActivity.this.autoProject.getText().toString().equalsIgnoreCase(((String) LeaveLisingActivity.this.projectid.get("project_number")) + " | " + ((String) LeaveLisingActivity.this.projectid.get("project_name")))) {
                    pId = (String) LeaveLisingActivity.this.projectid.get("project_id");
                } else {
                    pId = "";
                }
            }
            if (LeaveLisingActivity.this.leaveTypeId != null) {
                lId = (String) LeaveLisingActivity.this.leaveTypeId.get("leavetype_id");
            }
            String strtdate = LeaveLisingActivity.this.strDate.getText().toString();
            String enddate = LeaveLisingActivity.this.endDate.getText().toString();
            LeaveType leaveType = new LeaveType();
            leaveType.setLeaveFromDt(strtdate);
            leaveType.setLeaveTillDt(enddate);
            if (LeaveLisingActivity.this.autoLeaveType.getSelectedItem().toString().equalsIgnoreCase("All")) {
                leaveType.setLeaveTypeId("");
            } else {
                leaveType.setLeaveTypeId(lId);
            }
            leaveType.setProject_id(pId);
            if (!LeaveLisingActivity.this.autostatusList.getSelectedItem().toString().equalsIgnoreCase("All")) {
                LeaveLisingActivity.this.BaseStatus = LeaveLisingActivity.this.status;
                LeaveLisingActivity.this.status = LeaveLisingActivity.this.autostatusList.getSelectedItem().toString();
            }
            LeaveLisingActivity.this.pageNo = 1;
            LeavesApiHelper helper = new LeavesApiHelper(LeaveLisingActivity.this.context);
            LeaveLisingActivity.this.isFilterEnable = Boolean.valueOf(true);
            LeaveLisingActivity.this.progressHelper.showDialog("Loading..");
            if (LeaveLisingActivity.this.apiCalled) {
//                Log.e("ManagerleaveListingActivity:", "apicalled :" + LeaveLisingActivity.this.apiCalled);
                return;
            }
            LeaveLisingActivity.this.apiCalled = true;
            helper.apiLeaveListingWithFilter(LeaveLisingActivity.this.status, LeaveLisingActivity.this.pageNo, leaveType, LeaveLisingActivity.this.is_from_mng_leave, LeaveLisingActivity.this.leaveListApiCallback);
        }
    }

    class C06825 implements OnClickListener {
        C06825() {
        }

        public void onClick(View v) {
            if (LeaveLisingActivity.this.mDrawerLayout.isDrawerOpen((int) GravityCompat.END)) {
                LeaveLisingActivity.this.autoProject.setText("");
                LeaveLisingActivity.this.autoLeaveType.setSelection(0);
                LeaveLisingActivity.this.autostatusList.setSelection(0);
                LeaveLisingActivity.this.strDate.setText("");
                LeaveLisingActivity.this.endDate.setText("");
                LeaveLisingActivity.this.status = "";
            }
        }
    }

    class C06836 implements OnClickListener {
        C06836() {
        }

        public void onClick(View v) {
            LeaveLisingActivity.this.mDrawerLayout.closeDrawers();
        }
    }

    class C06847 implements OnItemSelectedListener {
        C06847() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            String qry = "select leavetype_id from tblleavetype where leave_type='" + LeaveLisingActivity.this.autoLeaveType.getSelectedItem().toString() + "'";
            LeaveLisingActivity.this.dbHelper = new DatabaseHelper(LeaveLisingActivity.this.context);
            LeaveLisingActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(LeaveLisingActivity.this.context);
            LeaveLisingActivity.this.leaveTypeId = LeaveLisingActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(LeaveLisingActivity.this);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06858 implements OnItemClickListener {
        C06858() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String pName = LeaveLisingActivity.this.autoProject.getText().toString();
            String qry = "select * from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
            LeaveLisingActivity.this.dbHelper = new DatabaseHelper(LeaveLisingActivity.this.context);
            LeaveLisingActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(LeaveLisingActivity.this.context);
            LeaveLisingActivity.this.projectid = LeaveLisingActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(LeaveLisingActivity.this);
        }
    }

    class C06869 implements OnClickListener {
        C06869() {
        }

        public void onClick(View v) {
            LeaveLisingActivity.this.leaveList = new ArrayList();
            LeaveLisingActivity.this.pageNo = 1;
            LeaveLisingActivity.this.status = LeaveLisingActivity.APPROVE_STATUS_PENDING;
            LeavesApiHelper helper = new LeavesApiHelper(LeaveLisingActivity.this.context);
            LeaveLisingActivity.this.progressHelper.showDialog("Loading..");
            if (LeaveLisingActivity.this.apiCalled) {
//                Log.e("ManagerleaveListingActivity:", "apicalled :" + LeaveLisingActivity.this.apiCalled);
                return;
            }
            LeaveLisingActivity.this.apiCalled = true;
            helper.apiLeaveListing(LeaveLisingActivity.this.status, LeaveLisingActivity.this.pageNo, LeaveLisingActivity.this.is_from_mng_leave, LeaveLisingActivity.this.leaveListApiCallback);
        }
    }

    public class MyBaseAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<LeaveType> myList;
        ArrayList<LeaveType> pnameList;
        ETechTextView tvAttachment;

        public MyBaseAdapter(Context context, ArrayList<LeaveType> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        public int getCount() {
            return this.myList.size();
        }

        public LeaveType getItem(int position) {
            return (LeaveType) this.myList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.leave_list_items, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            final LeaveType currLeave = getItem(position);
            mViewHolder.tvLeaveType.setText(currLeave.getLeavetypename());
            if (Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || LeaveLisingActivity.this.is_from_mng_leave) {
                if (currLeave.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                    mViewHolder.approveStatus.setTextColor(LeaveLisingActivity.this.getResources().getColor(R.color.Dark_blue));
                    mViewHolder.approveStatus.setText(LeaveLisingActivity.this.getResources().getString(R.string.pendding));
                } else if (currLeave.getApproveStatus().equalsIgnoreCase("reject") || currLeave.getApproveStatus().equalsIgnoreCase("cancel")) {
                    mViewHolder.approveStatus.setTextColor(LeaveLisingActivity.this.getResources().getColor(R.color.red));
                    if (currLeave.getApproveStatus().equalsIgnoreCase("reject")) {
                        mViewHolder.approveStatus.setText(LeaveLisingActivity.this.getResources().getString(R.string.reject1));
                    } else {
                        mViewHolder.approveStatus.setText(LeaveLisingActivity.this.getResources().getString(R.string.cancel1));
                    }
                } else {
                    mViewHolder.approveStatus.setTextColor(LeaveLisingActivity.this.getResources().getColor(R.color.green));
                    mViewHolder.approveStatus.setText(LeaveLisingActivity.this.getResources().getString(R.string.approved));
                }
            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat(" dd MMM yy");
            try {
                Date date1 = input.parse(currLeave.getLeaveFromDt());
                Date date2 = input.parse(currLeave.getLeaveTillDt());
                String days = LeaveLisingActivity.this.getDateDiffString(date1, date2);
                String str1 = output.format(date1);
                String str2 = output.format(date2);
                mViewHolder.tvLeaveFrom.setText(str1);
                mViewHolder.tvLeaveTo.setText(str2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!LeaveLisingActivity.this.isSidemenuOpen()) {
                        Intent intent;
                        if (currLeave.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                            intent = new Intent(LeaveLisingActivity.this, LeaveFormActivity.class);
                            intent.putExtra("LeaveDetails", currLeave);
                            intent.putExtra("is_from_mng_leave", LeaveLisingActivity.this.is_from_mng_leave);
                            LeaveLisingActivity.this.startActivity(intent);
                            LeaveLisingActivity.this.finish();
                            return;
                        }
                        intent = new Intent(LeaveLisingActivity.this, LeaveDisplay.class);
                        intent.putExtra("LeaveDetails", currLeave);
                        intent.putExtra("is_from_mng_leave", LeaveLisingActivity.this.is_from_mng_leave);
                        LeaveLisingActivity.this.startActivity(intent);
                    }
                }
            });
            return convertView;
        }
    }

    private class MyViewHolder {
        ETechTextView approveStatus;
        ETechButton btnAccept;
        ETechButton btnCancel;
        ETechButton btnReject;
        LinearLayout linBtnView;
        LinearLayout linBtnView1;
        ETechTextView proId;
        ETechTextView tvLeaveDays;
        ETechTextView tvLeaveFrom;
        ETechTextView tvLeaveTitle;
        ETechTextView tvLeaveTo;
        ETechTextView tvLeaveType;

        public MyViewHolder(View item) {
            this.tvLeaveTitle = (ETechTextView) item.findViewById(R.id.txtLvTiltle);
            this.tvLeaveType = (ETechTextView) item.findViewById(R.id.txtLvtype);
            this.tvLeaveFrom = (ETechTextView) item.findViewById(R.id.txtLvFrom);
            this.tvLeaveTo = (ETechTextView) item.findViewById(R.id.txtLvTo);
            this.tvLeaveDays = (ETechTextView) item.findViewById(R.id.txtLvDays);
            this.approveStatus = (ETechTextView) item.findViewById(R.id.txtStatus);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        method();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_lising);
        this.status = getIntent().getStringExtra("Status");
        this.is_from_mng_leave = getIntent().getBooleanExtra("is_from_mng_leave", false);
        setLeaveFilterMenu(this.is_from_mng_leave);
        this.header.setTitle(getString(R.string.title_activity_leave_e));
        this.header.setRightBtnImage(R.drawable.more);
        this.autoProject.init(this.context, this);
        this.header.setRightBtnClickListener(new C06781());
        String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        getView();
        try {
            this.Adapter = new MyBaseAdapter(this.context, this.leaveList);
            this.myLeaveListview.setAdapter(this.Adapter);
            this.myLeaveListview.setOnScrollListener(new C06792());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.status == null) {
            this.status = "";
        }
        if (this.status.equalsIgnoreCase(Constants.STATUS_PENDING)) {
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_leave));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_hrs_manager));
            }
        }
        if (this.status.equalsIgnoreCase("approve")) {
            this.txtstatuslbl.setText(getResources().getString(R.string.approved_hrs));
        }
        if (this.status.equalsIgnoreCase("not_pendding")) {
            this.txtstatuslbl.setText(getResources().getString(R.string.tv_history));
        }
        this.PnameList = getListFromLocalDB("tblproject", "project_name");
        if (this.PnameList != null) {
            getProjectListAdapter(this.PnameList);
        }
        this.leaveTypeList = getListFromLocalDB("tblleavetype", "leave_type");
        if (this.leaveTypeList != null) {
            getLeaveTypeList(this.leaveTypeList);
        }
        this.statusList = getStatusList();
        if (this.statusList != null) {
            getStatusTypeList(this.statusList);
        }
        LeavesApiHelper helper = new LeavesApiHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        this.apiCalled = true;
        helper.apiLeaveListing(this.status, this.pageNo, this.is_from_mng_leave, this.leaveListApiCallback);
    }

    private void getStatusTypeList(List items) {
        this.statusList = items;
        this.statusList.add(0, getResources().getString(R.string.all));
        this.autoSpinnerAdapterStatus = new ArrayAdapter(this, R.layout.spinner_textview_filter, this.statusList);
        this.autostatusList.setAdapter(this.autoSpinnerAdapterStatus);
        this.autostatusList.setOnItemSelectedListener(new C06803());
    }

    private ArrayList<String> getStatusList() {
        ArrayList<String> sList = new ArrayList();
        sList.add("reject");
        sList.add(Constants.STATUS_PENDING);
        sList.add("approve");
        sList.add("cancel");
        return sList;
    }

    private void getLeaveTypeList(List item) {
        this.leaveTypeList = item;
        this.leaveTypeList.add(0, getResources().getString(R.string.all));
        this.autocompleteAdapterleave = new ArrayAdapter(this, R.layout.spinner_textview_filter, this.leaveTypeList);
        this.autoLeaveType.setAdapter(this.autocompleteAdapterleave);
        this.autoLeaveType.setOnItemSelectedListener(new C06847());
    }

    private void getProjectListAdapter(List item) {
        this.PnameList = item;
        this.myAutoCompleteAdapter = new ArrayAdapter(this, R.layout.auto_complete_textview, this.PnameList);
        this.autoProject.init(this.context, this);
        this.autoProject.setAdapter(this.myAutoCompleteAdapter);
        this.autoProject.setOnItemClickListener(new C06858());
    }

    private List<String> getListFromLocalDB(String tblname, String list_name) {
        this.dbHelper = new DatabaseHelper(this.context);
        String qry = "select * from " + tblname;
//        new String[1][0] = "project_name,project_id";
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        ArrayList<HashMap<String, String>> list = this.dbHelper.selectRecordsFromDBList(qry, null);
        List<String> fieldlist = new ArrayList();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (tblname.equalsIgnoreCase("tblproject")) {
                    fieldlist.add(((String) ((HashMap) list.get(i)).get("project_number")) + " | " + ((String) ((HashMap) list.get(i)).get(list_name)));
                } else {
                    fieldlist.add(((String)((HashMap) list.get(i)).get(list_name)));
                }
            }
        }
        return fieldlist;
    }

    private void getView() {
        this.progressHelper = new ProgressHelper(this.context);
        this.myLeaveListview = (ListView) findViewById(R.id.ls_leave_list);
        this.btnLeavePendding = (ETechButton) findViewById(R.id.btnLvPendding);
        this.btnLeaveHistory = (ETechButton) findViewById(R.id.btnLvHistory);
        this.txtstatuslbl = (ETechTextView) findViewById(R.id.txtStatusofHours);
        this.btnsearch.setOnClickListener(this.searchBtnClickListener);
        this.btnclr.setOnClickListener(this.clearBtnClickListener);
        this.btncancel.setOnClickListener(this.cancelBtnClickListener);
        this.btnLeavePendding.setOnClickListener(this.btnLeavePenddingClickListener);
        this.btnLeaveHistory.setOnClickListener(this.btnLeaveHistoryClickListener);
    }

    public void onItemOptionClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (view.getId() == R.id.auto_proname) {
            String pName = this.autoProject.getText().toString();
            String qry = "select * from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            this.projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(this);
        }
    }

    public String getDateDiffString(Date dateOne, Date dateTwo) {
        long delta = (dateTwo.getTime() - dateOne.getTime()) / DateUtils.MILLIS_PER_DAY;
        if (delta > 0) {
            return String.valueOf(delta);
        }
        return String.valueOf(delta * -1);
    }

    private void method() {
        setContentView(R.layout.activity_leave_lising);
        this.status = getIntent().getStringExtra("Status");
        this.is_from_mng_leave = getIntent().getBooleanExtra("is_from_mng_leave", false);
        setLeaveFilterMenu(this.is_from_mng_leave);
        this.header.setTitle(getString(R.string.title_activity_leave_e));
        this.header.setRightBtnImage(R.drawable.more);
        this.autoProject.init(this.context, this);
        this.leaveList = new ArrayList();
        this.header.setRightBtnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LeaveLisingActivity.this.autoProject.setDropDownWidth(LeaveLisingActivity.this.autoProject.getWidth());
                LeaveLisingActivity.this.mDrawerLayout.openDrawer(LeaveLisingActivity.this.linMenuViewRight);
            }
        });
        String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        getView();
        this.pageNo = 1;
        try {
            this.Adapter = new MyBaseAdapter(this.context, this.leaveList);
            this.myLeaveListview.setAdapter(this.Adapter);
            this.myLeaveListview.setOnScrollListener(new OnScrollListener() {
                public void onScrollStateChanged(AbsListView arg0, int arg1) {
                }

                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (totalItemCount > 0) {
                        Boolean flag = Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_LEAVE, "")));
                        if (firstVisibleItem + visibleItemCount == totalItemCount && visibleItemCount > 0 && flag.booleanValue()) {
                            LeavesApiHelper helper = new LeavesApiHelper(LeaveLisingActivity.this.context);
                            if (flag.booleanValue()) {
                                LeaveLisingActivity.this.progressHelper.showDialog("Loading..");
                                if (!LeaveLisingActivity.this.apiCalled) {
                                    LeaveLisingActivity.this.apiCalled = true;
                                    helper.apiLeaveListing(LeaveLisingActivity.this.status, LeaveLisingActivity.this.pageNo, LeaveLisingActivity.this.is_from_mng_leave, LeaveLisingActivity.this.leaveListApiCallback);
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.status == null) {
            this.status = "";
        }
        if (this.status.equalsIgnoreCase(Constants.STATUS_PENDING)) {
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_leave));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_hrs_manager));
            }
        }
        if (this.status.equalsIgnoreCase("approve")) {
            this.txtstatuslbl.setText(getResources().getString(R.string.approved_hrs));
        }
        if (this.status.equalsIgnoreCase("not_pendding")) {
            this.txtstatuslbl.setText(getResources().getString(R.string.tv_history));
        }
        this.PnameList = getListFromLocalDB("tblproject", "project_name");
        if (this.PnameList != null) {
            getProjectListAdapter(this.PnameList);
        }
        this.leaveTypeList = getListFromLocalDB("tblleavetype", "leave_type");
        if (this.leaveTypeList != null) {
            getLeaveTypeList(this.leaveTypeList);
        }
        this.statusList = getStatusList();
        if (this.statusList != null) {
            getStatusTypeList(this.statusList);
        }
        LeavesApiHelper helper = new LeavesApiHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        this.apiCalled = true;
        helper.apiLeaveListing(this.status, this.pageNo, this.is_from_mng_leave, this.leaveListApiCallback);
    }
}

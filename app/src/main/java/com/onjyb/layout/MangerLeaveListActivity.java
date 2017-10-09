package com.onjyb.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.beans.LeavesApiHelper;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular.DropDownItemClickListener;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.LeaveType;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AlertDialogHelper.OnMyDialogResult;
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

public class MangerLeaveListActivity extends BaseDrawerActivity implements OnMyDialogResult, DropDownItemClickListener {
    public static String APPROVE_STATUS_APPROVE = "approve";
    public static String APPROVE_STATUS_CANCEL = "cancel";
    public static String APPROVE_STATUS_NOT_PENDING = "not_pendding";
    public static String APPROVE_STATUS_PENDING = Constants.STATUS_PENDING;
    public static String APPROVE_STATUS_REJECT = "reject";
    MyBaseAdapter Adapter1;
    String BaseStatus = APPROVE_STATUS_PENDING;
    private List<String> PnameList;
    boolean apiCalled = false;
    private ActionCallback approveLeaveApiCallBack = new ActionCallback() {

        class C06921 implements OnClickListener {
            C06921() {
            }

            public void onClick(View v) {
            }
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (MangerLeaveListActivity.this.progressHelper != null) {
                MangerLeaveListActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    AlertDialogHelper.getNotificatonAlert(MangerLeaveListActivity.this.context, MangerLeaveListActivity.this.getString(R.string.app_name), new C06921(), ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG), 2, 0, false);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(MangerLeaveListActivity.this.context, MangerLeaveListActivity.this.getString(R.string.app_name), res.toString());
        }
    };
    private ArrayAdapter<String> autoSpinnerAdapterStatus;
    private ArrayAdapter<String> autocompleteAdapterleave;
    private ArrayAdapter<String> autocopmleteEmployee;
    OnClickListener cancelBtnClickListener = new C07018();
    OnClickListener clearBtnClickListener = new C07007();
    DatabaseHelper dbHelper;
    ListView empLeaveListview;
    ArrayList<LeaveType> empleaveList = new ArrayList();
    HashMap<String, String> employeeId;
    private List<String> employeeList;
    Boolean isFilterEnable = Boolean.valueOf(false);
    private ActionCallback leaveListApiCallback = new ActionCallback() {

        class C06931 extends TypeReference<ArrayList<LeaveType>> {
            C06931() {
            }
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (MangerLeaveListActivity.this.progressHelper != null) {
                MangerLeaveListActivity.this.progressHelper.dismissDialog();
            }
            MangerLeaveListActivity.this.apiCalled = false;
            if (statusCode == 1) {
                try {
                    MangerLeaveListActivity.this.pageNo++;
                    MangerLeaveListActivity.this.empleaveList.addAll((ArrayList) new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(((JSONObject) res).getJSONArray("leave_details").toString(), new C06931()));
                    if (MangerLeaveListActivity.this.empleaveList != null) {
                        if (MangerLeaveListActivity.this.isFilterEnable.booleanValue()) {
                            MangerLeaveListActivity.this.isFilterEnable = Boolean.valueOf(false);
                            MangerLeaveListActivity.this.Adapter1 = new MyBaseAdapter(MangerLeaveListActivity.this.context, MangerLeaveListActivity.this.empleaveList);
                            MangerLeaveListActivity.this.empLeaveListview.setAdapter(MangerLeaveListActivity.this.Adapter1);
                        }
                        MangerLeaveListActivity.this.Adapter1.notifyDataSetChanged();
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(MangerLeaveListActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    };
    LeaveType leaveType = new LeaveType();
    HashMap<String, String> leaveTypeId;
    LeaveType leaveTypeIdentity = null;
    private List<String> leaveTypeList;
    private ArrayAdapter<String> myAutoCompleteAdapter;
    int pageNo = 1;
    ProgressHelper progressHelper;
    HashMap<String, String> projectid;
    OnClickListener searchBtnClickListener = new C06996();
    private List<String> serviceList;
    String status = APPROVE_STATUS_PENDING;
    private List<String> statusList;
    ETechTextView txtNoDatalbl;
    ETechTextView txtstatuslbl;

    class C06941 implements OnClickListener {
        C06941() {
        }

        public void onClick(View v) {
            MangerLeaveListActivity.this.autoProject.setDropDownWidth(MangerLeaveListActivity.this.autoProject.getWidth());
            MangerLeaveListActivity.this.autoEmployees.setDropDownWidth(MangerLeaveListActivity.this.autoEmployees.getWidth());
            MangerLeaveListActivity.this.mDrawerLayout.openDrawer(MangerLeaveListActivity.this.linMenuViewRight);
        }
    }

    class C06952 implements OnScrollListener {
        C06952() {
        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0) {
                Boolean flag = Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_LEAVE, "")));
                if (firstVisibleItem + visibleItemCount == totalItemCount && visibleItemCount > 0 && flag.booleanValue()) {
                    LeavesApiHelper helper = new LeavesApiHelper(MangerLeaveListActivity.this.context);
                    if (!flag.booleanValue()) {
                        return;
                    }
                    if (MangerLeaveListActivity.this.apiCalled) {
//                        Log.e("ManagerleaveListingActivity:", "apicalled :" + MangerLeaveListActivity.this.apiCalled);
                        return;
                    }
                    MangerLeaveListActivity.this.progressHelper.showDialog("Loading..");
                    MangerLeaveListActivity.this.apiCalled = true;
                    helper.apiLeaveListingWithFilter(MangerLeaveListActivity.this.status, MangerLeaveListActivity.this.pageNo, MangerLeaveListActivity.this.leaveType, false, MangerLeaveListActivity.this.leaveListApiCallback);
                }
            }
        }
    }

    class C06963 implements OnClickListener {
        C06963() {
        }

        public void onClick(View v) {
            MangerLeaveListActivity.this.autoProject.setDropDownWidth(MangerLeaveListActivity.this.autoProject.getWidth());
            MangerLeaveListActivity.this.autoEmployees.setDropDownWidth(MangerLeaveListActivity.this.autoEmployees.getWidth());
            MangerLeaveListActivity.this.mDrawerLayout.openDrawer(MangerLeaveListActivity.this.linMenuViewRight);
        }
    }

    class C06974 implements OnScrollListener {
        C06974() {
        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0) {
                Boolean flag = Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_LEAVE, "")));
                if (firstVisibleItem + visibleItemCount == totalItemCount && visibleItemCount > 0 && flag.booleanValue()) {
                    LeavesApiHelper helper = new LeavesApiHelper(MangerLeaveListActivity.this.context);
                    if (!flag.booleanValue()) {
                        return;
                    }
                    if (MangerLeaveListActivity.this.apiCalled) {
//                        Log.e("ManagerleaveListingActivity:", "apicalled :" + MangerLeaveListActivity.this.apiCalled);
                        return;
                    }
                    MangerLeaveListActivity.this.progressHelper.showDialog("Loading..");
                    MangerLeaveListActivity.this.apiCalled = true;
                    helper.apiLeaveListingWithFilter(MangerLeaveListActivity.this.status, MangerLeaveListActivity.this.pageNo, MangerLeaveListActivity.this.leaveType, false, MangerLeaveListActivity.this.leaveListApiCallback);
                }
            }
        }
    }

    class C06985 implements OnItemSelectedListener {
        C06985() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06996 implements OnClickListener {
        C06996() {
        }

        public void onClick(View v) {
            AppUtils.hideKeyBoard(MangerLeaveListActivity.this);
            MangerLeaveListActivity.this.empleaveList.clear();
            MangerLeaveListActivity.this.Adapter1.notifyDataSetChanged();
            MangerLeaveListActivity.this.mDrawerLayout.closeDrawers();
            String pId = "";
            String lId = "";
            String eId = "";
            if (MangerLeaveListActivity.this.projectid != null) {
                if (MangerLeaveListActivity.this.autoProject.getText().toString().equalsIgnoreCase("")) {
                    pId = "";
                } else if (MangerLeaveListActivity.this.autoProject.getText().toString().equalsIgnoreCase(((String) MangerLeaveListActivity.this.projectid.get("project_number")) + " | " + ((String) MangerLeaveListActivity.this.projectid.get("project_name")))) {
                    pId = (String) MangerLeaveListActivity.this.projectid.get("project_id");
                } else {
                    pId = "";
                }
            }
            if (MangerLeaveListActivity.this.leaveTypeId != null) {
                lId = (String) MangerLeaveListActivity.this.leaveTypeId.get("leavetype_id");
            }
            if (MangerLeaveListActivity.this.employeeId != null) {
                eId = (String) MangerLeaveListActivity.this.employeeId.get("employee_id");
            }
            String strtdate = MangerLeaveListActivity.this.strDate.getText().toString();
            String enddate = MangerLeaveListActivity.this.endDate.getText().toString();
            MangerLeaveListActivity.this.leaveType = new LeaveType();
            MangerLeaveListActivity.this.leaveType.setLeaveFromDt(strtdate);
            MangerLeaveListActivity.this.leaveType.setLeaveTillDt(enddate);
            MangerLeaveListActivity.this.leaveType.setLeaveTypeId(lId);
            MangerLeaveListActivity.this.leaveType.setProject_id(pId);
            MangerLeaveListActivity.this.leaveType.setEmployeeId(eId);
            if (!MangerLeaveListActivity.this.autoEmployees.getText().toString().equalsIgnoreCase("")) {
                MangerLeaveListActivity.this.leaveType.setFirstName(MangerLeaveListActivity.this.autoEmployees.getText().toString());
            }
            MangerLeaveListActivity.this.pageNo = 1;
            LeavesApiHelper helper = new LeavesApiHelper(MangerLeaveListActivity.this.context);
            MangerLeaveListActivity.this.progressHelper.showDialog("Loading..");
            MangerLeaveListActivity.this.isFilterEnable = Boolean.valueOf(true);
            if (MangerLeaveListActivity.this.apiCalled) {
//                Log.e("ManagerleaveListingActivity:", "apicalled :" + MangerLeaveListActivity.this.apiCalled);
                return;
            }
            MangerLeaveListActivity.this.apiCalled = true;
            helper.apiLeaveListingWithFilter(MangerLeaveListActivity.this.status, MangerLeaveListActivity.this.pageNo, MangerLeaveListActivity.this.leaveType, false, MangerLeaveListActivity.this.leaveListApiCallback);
        }
    }

    class C07007 implements OnClickListener {
        C07007() {
        }

        public void onClick(View v) {
            if (MangerLeaveListActivity.this.mDrawerLayout.isDrawerOpen((int) GravityCompat.END)) {
                MangerLeaveListActivity.this.autoProject.setText("");
                MangerLeaveListActivity.this.autoLeaveType.setSelection(0);
                MangerLeaveListActivity.this.autostatusList.setSelection(0);
                MangerLeaveListActivity.this.strDate.setText("");
                MangerLeaveListActivity.this.endDate.setText("");
                MangerLeaveListActivity.this.autoEmployees.setText("");
                MangerLeaveListActivity.this.status = MangerLeaveListActivity.this.BaseStatus;
                MangerLeaveListActivity.this.employeeId = null;
                MangerLeaveListActivity.this.projectid = null;
                MangerLeaveListActivity.this.leaveTypeId = null;
            }
        }
    }

    class C07018 implements OnClickListener {
        C07018() {
        }

        public void onClick(View v) {
            MangerLeaveListActivity.this.mDrawerLayout.closeDrawers();
        }
    }

    class C07029 implements OnItemClickListener {
        C07029() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String qry = "select employee_id from tblemployee where first_name='" + MangerLeaveListActivity.this.autoEmployees.getText().toString() + "'";
            MangerLeaveListActivity.this.dbHelper = new DatabaseHelper(MangerLeaveListActivity.this.context);
            MangerLeaveListActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(MangerLeaveListActivity.this.context);
            MangerLeaveListActivity.this.employeeId = MangerLeaveListActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(MangerLeaveListActivity.this);
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
            final MyViewHolder mViewHolder;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.leave_lis_item_manager, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            final LeaveType currLeave = getItem(position);
            mViewHolder.tvEmpName.setText(AppUtils.UpperCaseWords(currLeave.getFirstName() + " " + currLeave.getLastName()));
            mViewHolder.tvLeaveType.setText(currLeave.getLeavetypename());
            if (currLeave.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                mViewHolder.pipe.setVisibility(View.GONE);
            } else if (currLeave.getApproveStatus().equalsIgnoreCase("reject") || currLeave.getApproveStatus().equalsIgnoreCase("cancel")) {
                mViewHolder.tvStatus.setTextColor(MangerLeaveListActivity.this.getResources().getColor(R.color.red));
                if (currLeave.getApproveStatus().equalsIgnoreCase("reject")) {
                    mViewHolder.tvStatus.setText(MangerLeaveListActivity.this.getResources().getString(R.string.reject1));
                } else {
                    mViewHolder.tvStatus.setText(MangerLeaveListActivity.this.getResources().getString(R.string.cancel1));
                }
            } else {
                mViewHolder.tvStatus.setTextColor(MangerLeaveListActivity.this.getResources().getColor(R.color.green));
                mViewHolder.tvStatus.setText(MangerLeaveListActivity.this.getResources().getString(R.string.approved));
            }
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat output = new SimpleDateFormat(" dd MMM yy");
            try {
                Date date1 = input.parse(currLeave.getLeaveFromDt());
                Date date2 = input.parse(currLeave.getLeaveTillDt());
                mViewHolder.tvLeaveDays.setText(MangerLeaveListActivity.this.getDateDiffString(date1, date2) + " Days");
                String str1 = output.format(date1);
                String str2 = output.format(date2);
                mViewHolder.tvLeaveFrom.setText(str1);
                mViewHolder.tvLeaveTo.setText(str2);
                String url_static = "http://api.androidhive.info/images/glide/small/deadpool.jpg";
                if (currLeave.getProfile_image() != null) {
                    Glide.with(this.context).load(Constants.BASE_IMAGE_URL + currLeave.getProfile_image()).asBitmap().centerCrop().placeholder((int) R.drawable.profile_pic).error((int) R.drawable.profile_pic).into(new BitmapImageViewTarget(mViewHolder.imgview) {
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MyBaseAdapter.this.context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mViewHolder.imgview.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mViewHolder.btnReject.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!MangerLeaveListActivity.this.isSidemenuOpen() && !Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                        Intent intent = new Intent(MangerLeaveListActivity.this, LeaveDisplay.class);
                        intent.putExtra("LeaveDetails", currLeave);
                        MangerLeaveListActivity.this.startActivity(intent);
                    }
                }
            });
            final int i = position;
            mViewHolder.btnAccept.setOnClickListener(new OnClickListener() {

                class C07051 implements OnClickListener {
                    C07051() {
                    }

                    public void onClick(View v) {
                    }
                }

                class C07062 implements OnClickListener {
                    C07062() {
                    }

                    public void onClick(View v) {
                    }
                }

                public void onClick(View v) {
                    if (!MangerLeaveListActivity.this.isSidemenuOpen()) {
                        AlertDialogHelper.getConfirmationAlert(MyBaseAdapter.this.context, MangerLeaveListActivity.this.getString(R.string.app_name), new C07051(), new C07062(), MangerLeaveListActivity.this.getResources().getString(R.string.are_you_sure_approve_leave), 0, i + 1, false, MangerLeaveListActivity.this.getResources().getString(R.string.txt_yes), MangerLeaveListActivity.this.getResources().getString(R.string.txt_no), -1, -1);
                    }
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!MangerLeaveListActivity.this.isSidemenuOpen()) {
                        Intent intent = new Intent(MangerLeaveListActivity.this, LeaveDisplay.class);
                        intent.putExtra("LeaveDetails", currLeave);
                        MangerLeaveListActivity.this.startActivity(intent);
                    }
                }
            });
            return convertView;
        }
    }

    private class MyViewHolder {
        ETechButton btnAccept;
        ETechButton btnCancel;
        ETechButton btnReject;
        ImageView imgview;
        LinearLayout linBtnView;
        LinearLayout linBtnView1;
        TextView pipe;
        ETechTextView tvEmpName;
        ETechTextView tvLeaveDays;
        ETechTextView tvLeaveFrom;
        ETechTextView tvLeaveTo;
        ETechTextView tvLeaveType;
        ETechTextView tvStatus;

        public MyViewHolder(View item) {
            this.imgview = (ImageView) item.findViewById(R.id.imgprofile);
            this.tvEmpName = (ETechTextView) item.findViewById(R.id.txtemployeeName);
            this.tvLeaveType = (ETechTextView) item.findViewById(R.id.txtleavetype);
            this.tvLeaveFrom = (ETechTextView) item.findViewById(R.id.txtLvFrom1);
            this.tvLeaveTo = (ETechTextView) item.findViewById(R.id.txtLvTo1);
            this.tvLeaveDays = (ETechTextView) item.findViewById(R.id.txtLvDays1);
            this.tvStatus = (ETechTextView) item.findViewById(R.id.txtStatus);
            this.btnAccept = (ETechButton) item.findViewById(R.id.btnacceptLv);
            this.btnReject = (ETechButton) item.findViewById(R.id.btnrejectLv);
            this.pipe = (TextView) item.findViewById(R.id.pipe);
            if (MangerLeaveListActivity.this.status.equalsIgnoreCase("not_pendding")) {
                this.btnAccept.setVisibility(View.GONE);
                this.btnReject.setVisibility(View.GONE);
            }
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        updateViewMethod();
    }

    private void updateViewMethod() {
        setContentView(R.layout.activity_manger_leave_list);
        setLeaveFilterMenu(false);
        this.header.setTitle(getString(R.string.title_activity_manger_leave_list));
        this.header.setRightBtnImage(R.drawable.more);
        this.autoProject.init(this.context, this);
        this.header.setRightBtnClickListener(new C06941());
        this.btnsearch.setOnClickListener(this.searchBtnClickListener);
        this.btnclr.setOnClickListener(this.clearBtnClickListener);
        this.btncancel.setOnClickListener(this.cancelBtnClickListener);
        getviews();
        try {
            this.empleaveList = new ArrayList();
            this.Adapter1 = new MyBaseAdapter(this.context, this.empleaveList);
            this.empLeaveListview.setAdapter(this.Adapter1);
            this.empLeaveListview.setOnScrollListener(new C06952());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.status = getIntent().getStringExtra("Status");
        if (this.status == null) {
            this.status = "";
        }
        this.txtstatuslbl.setVisibility(View.GONE);
        this.PnameList = getListFromLocalDB("tblproject", "project_name");
        if (this.PnameList != null) {
            getProjectListAdapter(this.PnameList);
        }
        this.leaveTypeList = getListFromLocalDB("tblleavetype", "leave_type");
        if (this.leaveTypeList != null) {
            getLeaveTypeList(this.leaveTypeList);
        }
        this.employeeList = getListFromLocalDB("tblemployee", "first_name");
        if (this.employeeList != null) {
            getEmployeeList(this.employeeList);
        }
        this.statusList = getStatusList();
        if (this.statusList != null) {
            getStatusTypeList(this.statusList);
        }
        this.pageNo = 1;
        LeavesApiHelper helper = new LeavesApiHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        this.apiCalled = true;
        helper.apiLeaveListing(this.status, this.pageNo, false, this.leaveListApiCallback);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger_leave_list);
        setLeaveFilterMenu(false);
        this.header.setTitle(getString(R.string.title_activity_manger_leave_list));
        this.header.setRightBtnImage(R.drawable.more);
        this.autoProject.init(this.context, this);
        this.header.setRightBtnClickListener(new C06963());
        this.btnsearch.setOnClickListener(this.searchBtnClickListener);
        this.btnclr.setOnClickListener(this.clearBtnClickListener);
        this.btncancel.setOnClickListener(this.cancelBtnClickListener);
        getviews();
        try {
            this.Adapter1 = new MyBaseAdapter(this.context, this.empleaveList);
            this.empLeaveListview.setAdapter(this.Adapter1);
            this.empLeaveListview.setOnScrollListener(new C06974());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.status = getIntent().getStringExtra("Status");
        if (this.status == null) {
            this.status = "";
        }
        this.txtstatuslbl.setVisibility(View.GONE);
        this.PnameList = getListFromLocalDB("tblproject", "project_name");
        if (this.PnameList != null) {
            getProjectListAdapter(this.PnameList);
        }
        this.leaveTypeList = getListFromLocalDB("tblleavetype", "leave_type");
        if (this.leaveTypeList != null) {
            getLeaveTypeList(this.leaveTypeList);
        }
        this.employeeList = getListFromLocalDB("tblemployee", "first_name");
        if (this.employeeList != null) {
            getEmployeeList(this.employeeList);
        }
        this.statusList = getStatusList();
        if (this.statusList != null) {
            getStatusTypeList(this.statusList);
        }
        LeavesApiHelper helper = new LeavesApiHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        this.apiCalled = true;
        helper.apiLeaveListing(this.status, this.pageNo, false, this.leaveListApiCallback);
    }

    private ArrayList<String> getStatusList() {
        ArrayList<String> sList = new ArrayList();
        sList.add(getResources().getString(R.string.st_reject));
        sList.add(getResources().getString(R.string.st_approve));
        sList.add(getResources().getString(R.string.st_cancel));
        sList.add(getResources().getString(R.string.st_pendding));
        return sList;
    }

    private void getStatusTypeList(List items) {
        this.statusList = items;
        this.statusList.add(0, getResources().getString(R.string.all));
        this.autoSpinnerAdapterStatus = new ArrayAdapter(this, R.layout.spinner_textview_filter, this.statusList);
        this.autostatusList.setAdapter(this.autoSpinnerAdapterStatus);
        this.autostatusList.setOnItemSelectedListener(new C06985());
    }

    private void getviews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.empLeaveListview = (ListView) findViewById(R.id.ls_leave_man_list);
        this.txtstatuslbl = (ETechTextView) findViewById(R.id.txtStatusofHours1);
        this.txtNoDatalbl = (ETechTextView) findViewById(R.id.txtnoLvdata);
    }

    private void getEmployeeList(List item) {
        this.employeeList = item;
        this.autocopmleteEmployee = new ArrayAdapter(this, R.layout.auto_complete_textview, this.employeeList);
        this.autoEmployees.setAdapter(this.autocopmleteEmployee);
        this.autoEmployees.setOnItemClickListener(new C07029());
        this.autoEmployees.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                MangerLeaveListActivity.this.autoEmployees.showDropDown();
                return false;
            }
        });
    }

    private void getLeaveTypeList(List item) {
        this.leaveTypeList = item;
        this.leaveTypeList.add(0, getResources().getString(R.string.all));
        this.autocompleteAdapterleave = new ArrayAdapter(this, R.layout.spinner_textview_filter, this.leaveTypeList);
        this.autoLeaveType.setAdapter(this.autocompleteAdapterleave);
        this.autoLeaveType.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String qry = "select leavetype_id from tblleavetype where leave_type='" + MangerLeaveListActivity.this.autoLeaveType.getSelectedItem().toString() + "'";
                MangerLeaveListActivity.this.dbHelper = new DatabaseHelper(MangerLeaveListActivity.this.context);
                MangerLeaveListActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(MangerLeaveListActivity.this.context);
                MangerLeaveListActivity.this.leaveTypeId = MangerLeaveListActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getProjectListAdapter(List item) {
        this.PnameList = item;
        this.myAutoCompleteAdapter = new ArrayAdapter(this, R.layout.auto_complete_textview, this.PnameList);
        this.autoProject.setAdapter(this.myAutoCompleteAdapter);
        this.autoProject.init(this.context, this);
        this.autoProject.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String pName = MangerLeaveListActivity.this.autoProject.getText().toString();
                String qry = "select * from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
                MangerLeaveListActivity.this.dbHelper = new DatabaseHelper(MangerLeaveListActivity.this.context);
                MangerLeaveListActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(MangerLeaveListActivity.this.context);
                MangerLeaveListActivity.this.projectid = MangerLeaveListActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            }
        });
    }

    /* JADX WARNING: inconsistent code. */
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

    public void onOkClick(int titleResid, int btnResId) {
        if (titleResid == 2) {
            Intent intent = new Intent();
            intent.setClass(this, MangerLeaveListActivity.class);
            intent.setFlags(67108864);
            intent.putExtra("Status", LeaveLisingActivity.APPROVE_STATUS_PENDING);
            startActivity(intent);
        }
        if (btnResId != 0) {
            this.leaveTypeIdentity = (LeaveType) this.empleaveList.get(btnResId - 1);
            if (this.leaveTypeIdentity != null && !Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && this.leaveTypeIdentity.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                String status = LeaveLisingActivity.APPROVE_STATUS_APPROVE;
                this.leaveTypeIdentity.setNote("");
                LeavesApiHelper helper = new LeavesApiHelper(this.context);
                this.progressHelper.showDialog("Loading..");
                helper.apiApproveLeave(status, this.leaveTypeIdentity, this.approveLeaveApiCallBack);
            }
        }
    }

    public void onItemOptionClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (view.getId() == R.id.auto_proname) {
            String pName = this.autoProject.getText().toString();
            String qry = "select * from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            this.projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
        }
    }

    public String getDateDiffString(Date dateOne, Date dateTwo) {
        long delta = (dateTwo.getTime() - dateOne.getTime()) / DateUtils.MILLIS_PER_DAY;
        if (delta > 0) {
            return String.valueOf(delta);
        }
        return String.valueOf(delta * -1);
    }
}

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular.DropDownItemClickListener;
import com.onjyb.customview.ImageSelectionView;
import com.onjyb.db.AttachmentMap;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.OvertimeRule;
import com.onjyb.db.Service;
import com.onjyb.db.WorkSheet;
import com.onjyb.helper.SlideShowActivity;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

public class MyTaskActivity extends BaseDrawerActivity implements DropDownItemClickListener {
    public static String APPROVE_STATUS_APPROVE = "approve";
    public static String APPROVE_STATUS_NOT_PENDING = "approve,reject";
    public static String APPROVE_STATUS_PENDING = Constants.STATUS_PENDING;
    public static String APPROVE_STATUS_REJECT = "reject";
    MyBaseAdapter Adapter;
    private List<String> BranchList;
    private ActionCallback MyTaskListOnlyCallback = new ActionCallback() {

        class C07091 extends TypeReference<ArrayList<WorkSheet>> {
            C07091() {
            }
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (MyTaskActivity.this.progressHelper != null) {
                MyTaskActivity.this.progressHelper.dismissDialog();
            }
            MyTaskActivity.this.apiCalled = false;
            if (statusCode == 1) {
                try {
                    MyTaskActivity.this.pageNo++;
                    MyTaskActivity.this.tasklist.addAll((ArrayList) new ObjectMapper().readValue(((JSONObject) res).getJSONArray("work_details").toString(), new C07091()));
                    if (MyTaskActivity.this.tasklist == null) {
                        return;
                    }
                    if (MyTaskActivity.this.isFilterEnable.booleanValue()) {
                        MyTaskActivity.this.isFilterEnable = Boolean.valueOf(false);
                        MyTaskActivity.this.Adapter = new MyBaseAdapter(MyTaskActivity.this.context, MyTaskActivity.this.tasklist);
                        MyTaskActivity.this.mytaskListview.setAdapter(MyTaskActivity.this.Adapter);
                        return;
                    }
                    MyTaskActivity.this.Adapter.notifyDataSetChanged();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(MyTaskActivity.this.TAG, "onActionComplete > Exception: " + e.toString());
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(MyTaskActivity.this.context, MyTaskActivity.this.getString(R.string.app_name), res.toString());
        }
    };
    private List<String> PnameList;
    private final String TAG = LoginActivity.class.getName();
    boolean apiCalled = false;
    OnClickListener approveBtnClicklistener = new OnClickListener() {
        public void onClick(View v) {
            MyTaskActivity.this.resetButtonView(MyTaskActivity.this.btnApprove);
            MyTaskActivity.this.tasklist = new ArrayList();
            MyTaskActivity.this.pageNo = 1;
            MyTaskActivity.this.status = MyTaskActivity.APPROVE_STATUS_APPROVE;
            MyTaskActivity.this.userHelper.apiGetTaskList(MyTaskActivity.this.MyTaskListOnlyCallback, MyTaskActivity.this.is_from_mng_jobber, MyTaskActivity.this.status, MyTaskActivity.this.pageNo);
        }
    };
    private ArrayAdapter<String> autocompleteAdapterB;
    private ArrayAdapter<String> autocompleteAdapterS;
    private ArrayAdapter<String> autocopmleteEmployee;
    String bId = "";
    HashMap<String, String> branchId = new HashMap();
    ETechButton btnApprove;
    ETechButton btnNewTask;
    ETechButton btnNotApprove;
    ETechButton btnPastTask;
    ETechButton btnpending;
    OnClickListener cancelBtnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MyTaskActivity.this.mDrawerLayout.closeDrawers();
        }
    };
    OnClickListener clearBtnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (MyTaskActivity.this.mDrawerLayout.isDrawerOpen((int) GravityCompat.END)) {
                MyTaskActivity.this.autoProject.setText("");
                MyTaskActivity.this.autoBranch.setText("");
                MyTaskActivity.this.autoService.setText("");
                MyTaskActivity.this.autoEmployees.setText("");
                MyTaskActivity.this.strDate.setText("");
                MyTaskActivity.this.endDate.setText("");
                MyTaskActivity.this.branchId = null;
                MyTaskActivity.this.projectid = null;
                MyTaskActivity.this.serviceid = null;
                MyTaskActivity.this.employeeId = null;
                MyTaskActivity.this.pId = null;
                MyTaskActivity.this.sId = null;
                MyTaskActivity.this.bId = null;
                MyTaskActivity.this.eId = null;
            }
        }
    };
    Context context = this;
    DatabaseHelper dbHelper;
    String eId = "";
    HashMap<String, String> employeeId = new HashMap();
    private List<String> employeeList;
    String enddate;
    Boolean isApproveBtn = Boolean.valueOf(false);
    Boolean isFilterEnable = Boolean.valueOf(false);
    Boolean isRejectBtn = Boolean.valueOf(false);
    boolean isSeeAttachment = false;
    boolean is_from_mng_jobber = false;
    LinearLayout ll_btns_Emp;
    LinearLayout ll_btns_manager;
    private ArrayAdapter<String> myAutoCompleteAdapter;
    ListView mytaskListview;
    OnClickListener newTaskclickListener = new OnClickListener() {
        public void onClick(View v) {
            MyTaskActivity.this.resetButtonView(MyTaskActivity.this.btnNewTask);
            MyTaskActivity.this.tasklist = new ArrayList();
            MyTaskActivity.this.pageNo = 1;
            MyTaskActivity.this.status = MyTaskActivity.APPROVE_STATUS_PENDING;
            MyTaskActivity.this.userHelper.apiGetTaskList(MyTaskActivity.this.MyTaskListOnlyCallback, MyTaskActivity.this.is_from_mng_jobber, MyTaskActivity.this.status, MyTaskActivity.this.pageNo);
        }
    };
    OnClickListener notApproveBtnClicklistener = new OnClickListener() {
        public void onClick(View v) {
            MyTaskActivity.this.resetButtonView(MyTaskActivity.this.btnNotApprove);
            MyTaskActivity.this.tasklist = new ArrayList();
            MyTaskActivity.this.pageNo = 1;
            MyTaskActivity.this.status = MyTaskActivity.APPROVE_STATUS_REJECT;
            MyTaskActivity.this.userHelper.apiGetTaskList(MyTaskActivity.this.MyTaskListOnlyCallback, MyTaskActivity.this.is_from_mng_jobber, MyTaskActivity.this.status, MyTaskActivity.this.pageNo);
        }
    };
    String pId = "";
    int pageNo = 1;
    OnClickListener pastTaskclickListener = new OnClickListener() {
        public void onClick(View v) {
            MyTaskActivity.this.resetButtonView(MyTaskActivity.this.btnPastTask);
            MyTaskActivity.this.tasklist = new ArrayList();
            MyTaskActivity.this.pageNo = 1;
            MyTaskActivity.this.status = MyTaskActivity.APPROVE_STATUS_NOT_PENDING;
            MyTaskActivity.this.userHelper.apiGetTaskList(MyTaskActivity.this.MyTaskListOnlyCallback, MyTaskActivity.this.is_from_mng_jobber, MyTaskActivity.this.status, MyTaskActivity.this.pageNo);
        }
    };
    OnClickListener pendingBtnClicklistener = new OnClickListener() {
        public void onClick(View v) {
            MyTaskActivity.this.resetButtonView(MyTaskActivity.this.btnpending);
            MyTaskActivity.this.tasklist = new ArrayList();
            MyTaskActivity.this.pageNo = 1;
            MyTaskActivity.this.status = MyTaskActivity.APPROVE_STATUS_PENDING;
            MyTaskActivity.this.userHelper.apiGetTaskList(MyTaskActivity.this.MyTaskListOnlyCallback, MyTaskActivity.this.is_from_mng_jobber, MyTaskActivity.this.status, MyTaskActivity.this.pageNo);
        }
    };
    ProgressHelper progressHelper;
    HashMap<String, String> projectid = new HashMap();
    String sId = "";
    OnClickListener searchBtnClickListener = new OnClickListener() {
        public void onClick(View v) {
            AppUtils.hideKeyBoard(MyTaskActivity.this);
            MyTaskActivity.this.isFilterEnable = Boolean.valueOf(true);
            MyTaskActivity.this.tasklist = new ArrayList();
            MyTaskActivity.this.mDrawerLayout.closeDrawers();
            if (MyTaskActivity.this.projectid != null) {
                if (MyTaskActivity.this.autoProject.getText().toString().equalsIgnoreCase("")) {
                    MyTaskActivity.this.pId = "";
                } else if (MyTaskActivity.this.autoProject.getText().toString().equalsIgnoreCase(((String) MyTaskActivity.this.projectid.get("project_number")) + " | " + ((String) MyTaskActivity.this.projectid.get("project_name")))) {
                    MyTaskActivity.this.pId = (String) MyTaskActivity.this.projectid.get("project_id");
                } else {
                    MyTaskActivity.this.pId = "";
                }
            }
            if (MyTaskActivity.this.serviceid != null) {
                MyTaskActivity.this.sId = (String) MyTaskActivity.this.serviceid.get("service_id");
            }
            if (MyTaskActivity.this.branchId != null) {
                MyTaskActivity.this.bId = (String) MyTaskActivity.this.branchId.get("branch_id");
            }
            if (MyTaskActivity.this.employeeId != null) {
                MyTaskActivity.this.eId = (String) MyTaskActivity.this.employeeId.get("employee_id");
            }
            MyTaskActivity.this.strtdate = MyTaskActivity.this.strDate.getText().toString();
            MyTaskActivity.this.enddate = MyTaskActivity.this.endDate.getText().toString();
            MyTaskActivity.this.pageNo = 1;
            MyTaskActivity.this.userHelper.apiGetTaskList(MyTaskActivity.this.MyTaskListOnlyCallback, MyTaskActivity.this.status, MyTaskActivity.this.pageNo, MyTaskActivity.this.pId, MyTaskActivity.this.sId, MyTaskActivity.this.bId, MyTaskActivity.this.eId, MyTaskActivity.this.strtdate, MyTaskActivity.this.enddate, MyTaskActivity.this.is_from_mng_jobber);
        }
    };
    private List<String> serviceList;
    HashMap<String, String> serviceid = new HashMap();
    String status = APPROVE_STATUS_APPROVE;
    String strtdate;
    ArrayList<WorkSheet> tasklist = new ArrayList();
    ETechTextView txt;
    ETechTextView txtstatuslbl;
    UserHelper userHelper = new UserHelper(this.context);
    ArrayList<WorkSheet> workSheets;

    class C07101 implements OnClickListener {
        C07101() {
        }

        public void onClick(View v) {
            MyTaskActivity.this.autoProject.setDropDownWidth(MyTaskActivity.this.autoProject.getWidth());
            MyTaskActivity.this.autoService.setDropDownWidth(MyTaskActivity.this.autoService.getWidth());
            MyTaskActivity.this.autoBranch.setDropDownWidth(MyTaskActivity.this.autoBranch.getWidth());
            MyTaskActivity.this.autoEmployees.setDropDownWidth(MyTaskActivity.this.autoEmployees.getWidth());
            MyTaskActivity.this.mDrawerLayout.openDrawer(MyTaskActivity.this.linMenuViewRight);
        }
    }

    class C07112 implements OnClickListener {
        C07112() {
        }

        public void onClick(View v) {
            MyTaskActivity.this.autoProject.setDropDownWidth(MyTaskActivity.this.autoProject.getWidth());
            MyTaskActivity.this.autoService.setDropDownWidth(MyTaskActivity.this.autoService.getWidth());
            MyTaskActivity.this.autoBranch.setDropDownWidth(MyTaskActivity.this.autoBranch.getWidth());
            MyTaskActivity.this.autoEmployees.setDropDownWidth(MyTaskActivity.this.autoEmployees.getWidth());
            MyTaskActivity.this.mDrawerLayout.openDrawer(MyTaskActivity.this.linMenuViewRight);
        }
    }

    class C07123 implements OnScrollListener {
        C07123() {
        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0) {
                Boolean flag = Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_APPROVE, "")));
                if (firstVisibleItem + visibleItemCount == totalItemCount && visibleItemCount > 0 && flag.booleanValue()) {
                    MyTaskActivity.this.userHelper = new UserHelper(MyTaskActivity.this.context);
                    if (flag.booleanValue()) {
                        if (MyTaskActivity.this.isFilterEnable.booleanValue()) {
                            MyTaskActivity.this.isFilterEnable = Boolean.valueOf(false);
                        }
                        MyTaskActivity.this.progressHelper.showDialog("Loading..");
                        if (!MyTaskActivity.this.apiCalled) {
                            MyTaskActivity.this.apiCalled = true;
                            MyTaskActivity.this.userHelper.apiGetTaskList(MyTaskActivity.this.MyTaskListOnlyCallback, MyTaskActivity.this.status, MyTaskActivity.this.pageNo, MyTaskActivity.this.pId, MyTaskActivity.this.sId, MyTaskActivity.this.bId, MyTaskActivity.this.eId, MyTaskActivity.this.strtdate, MyTaskActivity.this.enddate, MyTaskActivity.this.is_from_mng_jobber);
                        }
                    }
                }
            }
        }
    }

    class C07134 implements OnItemClickListener {
        C07134() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String qry = "select employee_id from tblemployee where first_name='" + MyTaskActivity.this.autoEmployees.getText().toString() + "'";
            MyTaskActivity.this.dbHelper = new DatabaseHelper(MyTaskActivity.this.context);
            MyTaskActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(MyTaskActivity.this.context);
            MyTaskActivity.this.employeeId = MyTaskActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(MyTaskActivity.this);
        }
    }

    class C07145 implements OnTouchListener {
        C07145() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            MyTaskActivity.this.autoEmployees.showDropDown();
            return false;
        }
    }

    class C07156 implements OnItemClickListener {
        C07156() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String pName = MyTaskActivity.this.autoProject.getText().toString();
            String qry = "select * from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
            MyTaskActivity.this.dbHelper = new DatabaseHelper(MyTaskActivity.this.context);
            MyTaskActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(MyTaskActivity.this.context);
            MyTaskActivity.this.projectid = MyTaskActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(MyTaskActivity.this);
        }
    }

    class C07167 implements OnItemClickListener {
        C07167() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String qry = "select branch_id from tblbranch where branch_name='" + MyTaskActivity.this.autoBranch.getText().toString() + "'";
            MyTaskActivity.this.dbHelper = new DatabaseHelper(MyTaskActivity.this.context);
            MyTaskActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(MyTaskActivity.this.context);
            MyTaskActivity.this.branchId = MyTaskActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(MyTaskActivity.this);
        }
    }

    class C07178 implements OnTouchListener {
        C07178() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            MyTaskActivity.this.autoBranch.showDropDown();
            return false;
        }
    }

    class C07189 implements OnItemClickListener {
        C07189() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String qry = "select service_id from tblservice where service_name='" + MyTaskActivity.this.autoService.getText().toString() + "'";
            MyTaskActivity.this.dbHelper = new DatabaseHelper(MyTaskActivity.this.context);
            MyTaskActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(MyTaskActivity.this.context);
            MyTaskActivity.this.serviceid = MyTaskActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(MyTaskActivity.this);
        }
    }

    public class MyBaseAdapter extends BaseAdapter {
        private ActionCallback WorksheetDetailsCallBack = new C07286();
        Context context;
        LayoutInflater inflater;
        ArrayList<WorkSheet> myList;
        ArrayList<WorkSheet> pnameList;
        ETechTextView tvAttachment;

        class C07286 implements ActionCallback {

            class C07241 extends TypeReference<ArrayList<WorkSheet>> {
                C07241() {
                }
            }

            class C07252 extends TypeReference<ArrayList<AttachmentMap>> {
                C07252() {
                }
            }

            class C07263 extends TypeReference<ArrayList<Service>> {
                C07263() {
                }
            }

            class C07274 extends TypeReference<ArrayList<OvertimeRule>> {
                C07274() {
                }
            }

            C07286() {
            }

            /* JADX WARNING: inconsistent code. */
            public void onActionComplete(int statusCode, String callbackString, Object res) {
                if (MyTaskActivity.this.progressHelper != null) {
                    MyTaskActivity.this.progressHelper.dismissDialog();
                }
                if (statusCode == 1) {
                    try {
                        //0925
                        JSONObject dic = ((JSONObject) res);
                        ArrayList<WorkSheet> items = new ObjectMapper().readValue(dic.getJSONArray("worksheet_details").toString(),
                                new C07241());
                        WorkSheet workSheet = items.get(0);

                        ArrayList<AttachmentMap> mapList = new ObjectMapper().readValue(dic.getJSONArray("workimage_details").toString(),
                                new C07252());
                        workSheet.setAttachementList(mapList);

                        ArrayList<Service> extraServices = new ObjectMapper().readValue(dic.getJSONArray("extraservice_details").toString(),
                                new C07263());
                        workSheet.setServiceObjectsArray(extraServices);

                        ArrayList<OvertimeRule> overtimeRules = new ObjectMapper().readValue(dic.getJSONArray("overtime_details").toString(),
                                new C07274());
                        workSheet.setOvertimeRuleArrayList(overtimeRules);

                        String isOvertimeautomatic = dic.getString("isworkovertimeautomatic");
                        workSheet.setIsworksheetAutomaticEditmode(isOvertimeautomatic);

                        //r19
                        String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);

                        if(MyTaskActivity.this.isSeeAttachment == true){
                            MyTaskActivity.this.isSeeAttachment = false;
                            ArrayList<AttachmentMap> imageList = new ArrayList();
                            imageList.addAll(workSheet.getAttachementList());

                            ArrayList<Service> services = workSheet.getServiceObjectsArray();
                            if(services != null && services.size() > 0) {
                                for(int i = 0; i < services.size(); i++){
                                    Service service = services.get(i);
                                    imageList.addAll(service.getAttachmentList());
                                }
                                Intent intent = new Intent(MyTaskActivity.this.context, SlideShowActivity.class);
                                intent.putExtra("ImageList", imageList);
                                MyTaskActivity.this.context.startActivity(intent);
                            }
                            if (imageList.size() == 0)
                                Toast.makeText(MyTaskActivity.this.context, "No Attachments", Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if(MyTaskActivity.this.isRejectBtn == true){
                            MyTaskActivity.this.isRejectBtn = false;
                            Intent intent = new Intent(MyTaskActivity.this.context, DisplayMyTaskDetailActivity.class);
                            intent.putExtra("WorksheetEditData", workSheet);
                            intent.putExtra("FromisReject", true);
                            MyTaskActivity.this.context.startActivity(intent);
                        }

                        if(MyTaskActivity.this.isApproveBtn == true){
                            if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) != false){
                                if(workSheet.getIsEditable().equalsIgnoreCase("unlock") == false){
                                    if(workSheet.getApproveStatus().equalsIgnoreCase("pendding") == false){
                                        Intent intent = new Intent(MyTaskActivity.this, DisplayMyTaskDetailActivity.class);
                                        intent.putExtra("WorksheetEditData", workSheet);
                                        startActivity(intent);
                                        return;
                                    }
                                }
                            }
                            MyTaskActivity.this.isApproveBtn = false;
                            Intent intent = new Intent(MyTaskActivity.this, RegisterTimeActivity.class);
                            intent.setFlags(131072);
                            intent.putExtra("WorksheetEditData", workSheet);
                            intent.putExtra("EditFlag", "approve");
                            startActivity(intent);
                            return;
                        }


                        if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)){
                        }else {
                            if(!user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER)){
                                Intent intent = new Intent(MyTaskActivity.this, RegisterTimeActivity.class);
                                intent.setFlags(131072);
                                intent.putExtra("is_from_mng_jobber", MyTaskActivity.this.is_from_mng_jobber);
                                intent.putExtra("WorksheetEditData",workSheet);
                                startActivity(intent);
                                return;
                            }
                        }
                        String status = workSheet.getApproveStatus();
                        if(status.equalsIgnoreCase("pennding")) {
                        }else{
                            if(!status.equalsIgnoreCase("reject")){
                                Intent intent = new Intent(MyTaskActivity.this, DisplayMyTaskDetailActivity.class);
                                intent.putExtra("WorksheetEditData", workSheet);
                                startActivity(intent);
                                return;
                            }
                        }
                        Intent intent = new Intent(MyTaskActivity.this, RegisterTimeActivity.class);
                        intent.setFlags(131072);
                        intent.putExtra("WorksheetEditData", workSheet);
                        intent.putExtra("is_from_mng_jobber", MyTaskActivity.this.is_from_mng_jobber);

                        if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER))
                            intent.putExtra("EditFlag", "approve");

                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                AlertDialogHelper.getNotificatonAlert(MyTaskActivity.this.context, MyTaskActivity.this.getString(R.string.app_name), res.toString());
            }
        }

        private class MyViewHolder {
            ETechButton btnAccept;
            ETechButton btnReject;
            LinearLayout linBtnView;
            TextView pipe;
            ImageView profile_mytask;
            ETechTextView tvAddress;
            ETechTextView tvHrs;
            ETechTextView tvOverTime;
            ETechTextView tvProName;
            ETechTextView tvStatus;
            ETechTextView tvmonth;
            ETechTextView txtOverHrs;
            ETechTextView txtTaskCreateDate;
            ETechTextView txtTotalHrs;

            public MyViewHolder(View item) {
                this.tvmonth = (ETechTextView) item.findViewById(R.id.txtmonth);
                this.tvProName = (ETechTextView) item.findViewById(R.id.txtproname);
                this.tvAddress = (ETechTextView) item.findViewById(R.id.txtaddress);
                this.tvHrs = (ETechTextView) item.findViewById(R.id.txthrs);
                this.btnAccept = (ETechButton) item.findViewById(R.id.btnaccept);
                this.btnReject = (ETechButton) item.findViewById(R.id.btnreject);
                this.linBtnView = (LinearLayout) item.findViewById(R.id.linBtnView);
                MyBaseAdapter.this.tvAttachment = (ETechTextView) item.findViewById(R.id.tvatachment);
                this.tvOverTime = (ETechTextView) item.findViewById(R.id.txtOvertimeHrs);
                this.txtTotalHrs = (ETechTextView) item.findViewById(R.id.tvhrs1);
                this.txtOverHrs = (ETechTextView) item.findViewById(R.id.tvoverhrs);
                this.tvStatus = (ETechTextView) item.findViewById(R.id.txtStatus1);
                this.pipe = (TextView) item.findViewById(R.id.pipe1);
                this.txtTaskCreateDate = (ETechTextView) item.findViewById(R.id.txtTaskCreateDate);
                this.txtTaskCreateDate.setTextSize(10.0f);
            }
        }

        public MyBaseAdapter(Context context, ArrayList<WorkSheet> myList) {
            this.myList = myList;
            this.context = context;
            this.inflater = LayoutInflater.from(this.context);
        }

        public int getCount() {
            return this.myList.size();
        }

        public WorkSheet getItem(int position) {
            return (WorkSheet) this.myList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final MyViewHolder mViewHolder;
            SimpleDateFormat input;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.mytask_listview_items, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                mViewHolder.profile_mytask = (ImageView) convertView.findViewById(R.id.imgprofiletask);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            final WorkSheet currWorksheet = getItem(position);
            int i = 0;
            int workMn = 0;
            try {
                i = Integer.parseInt(currWorksheet.getTotalWorkTime()) / 60;
                workMn = Integer.parseInt(currWorksheet.getTotalWorkTime()) % 60;
                if (i < 0) {
                    i = 0;
                }
                if (workMn < 0) {
                    workMn = 0;
                }
            } catch (Exception e) {
            }
            String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
            final int i2 = position;
            mViewHolder.btnAccept.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!MyTaskActivity.this.isSidemenuOpen()) {
                        MyTaskActivity.this.isApproveBtn = Boolean.valueOf(true);
                        MyTaskActivity.this.isRejectBtn = Boolean.valueOf(false);
                        new UserHelper(MyBaseAdapter.this.context).apiGetWorksheetDetails(MyBaseAdapter.this.getItem(i2), MyBaseAdapter.this.WorksheetDetailsCallBack);
                    }
                }
            });
            mViewHolder.btnReject.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (!MyTaskActivity.this.isSidemenuOpen()) {
                        MyTaskActivity.this.isApproveBtn = Boolean.valueOf(false);
                        MyTaskActivity.this.isRejectBtn = Boolean.valueOf(true);
                        new UserHelper(MyBaseAdapter.this.context).apiGetWorksheetDetails(MyBaseAdapter.this.getItem(i2), MyBaseAdapter.this.WorksheetDetailsCallBack);
                    }
                }
            });
            mViewHolder.tvHrs.setText(String.format("%02d", new Object[]{Integer.valueOf(i)}) + MyTaskActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + MyTaskActivity.this.getResources().getString(R.string.txt_m));
            displayHoursInView(mViewHolder.tvOverTime, Integer.parseInt(currWorksheet.getTotal_work_overtime()));
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || MyTaskActivity.this.is_from_mng_jobber) {
                mViewHolder.pipe.setVisibility(View.GONE);
                mViewHolder.tvStatus.setVisibility(View.GONE);
                mViewHolder.linBtnView.setVisibility(View.GONE);
                mViewHolder.profile_mytask.setVisibility(View.GONE);
                mViewHolder.txtTaskCreateDate.setVisibility(View.GONE);
                mViewHolder.tvmonth.setVisibility(View.VISIBLE);
                mViewHolder.tvProName.setText(currWorksheet.getProject_Number() + " | " + currWorksheet.getProjectName());
                if (currWorksheet.getAddress() == null || currWorksheet.getAddress().length() <= 0) {
                    mViewHolder.tvAddress.setText("");
                } else {
                    mViewHolder.tvAddress.setText(currWorksheet.getAddress());
                }
            } else {
                if (!(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || MyTaskActivity.this.is_from_mng_jobber)) {
                    if (currWorksheet.getApproveStatus().equalsIgnoreCase(Constants.STATUS_PENDING)) {
                        mViewHolder.pipe.setVisibility(View.GONE);
                    } else if (currWorksheet.getApproveStatus().equalsIgnoreCase("reject") || currWorksheet.getApproveStatus().equalsIgnoreCase("cancel")) {
                        mViewHolder.pipe.setVisibility(View.VISIBLE);
                        mViewHolder.tvStatus.setVisibility(View.VISIBLE);
                        mViewHolder.tvStatus.setTextColor(MyTaskActivity.this.getResources().getColor(R.color.red));
                        if (currWorksheet.getApproveStatus().equalsIgnoreCase("reject")) {
                            mViewHolder.tvStatus.setText(MyTaskActivity.this.getResources().getString(R.string.reject1));
                        } else {
                            mViewHolder.tvStatus.setText(MyTaskActivity.this.getResources().getString(R.string.cancel1));
                        }
                    } else if (currWorksheet.getApproveStatus().equalsIgnoreCase("approve")) {
                        mViewHolder.pipe.setVisibility(View.VISIBLE);
                        mViewHolder.tvStatus.setVisibility(View.VISIBLE);
                        mViewHolder.tvStatus.setTextColor(MyTaskActivity.this.getResources().getColor(R.color.green));
                        mViewHolder.tvStatus.setText(MyTaskActivity.this.getResources().getString(R.string.approved));
                    }
                }
                mViewHolder.txtTotalHrs.setTextColor(MyTaskActivity.this.getResources().getColor(R.color.grey));
                mViewHolder.txtOverHrs.setTextColor(MyTaskActivity.this.getResources().getColor(R.color.grey));
                mViewHolder.tvHrs.setTextColor(MyTaskActivity.this.getResources().getColor(R.color.grey));
                mViewHolder.tvOverTime.setTextColor(MyTaskActivity.this.getResources().getColor(R.color.grey));
                mViewHolder.tvAddress.setTextColor(MyTaskActivity.this.getResources().getColor(R.color.Dark_blue));
                mViewHolder.tvAddress.setTextSize(10.0f);
                if (!(currWorksheet.getWorkDate() == null || currWorksheet.getWorkDate().isEmpty())) {
                    mViewHolder.txtTaskCreateDate.setVisibility(View.VISIBLE);
                    input = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        mViewHolder.txtTaskCreateDate.setText(new SimpleDateFormat("dd MMM yy").format(input.parse(currWorksheet.getWorkDate())));
                    } catch (ParseException e2) {
                        e2.printStackTrace();
                    }
                }
                mViewHolder.profile_mytask.setVisibility(View.VISIBLE);
                if (currWorksheet.getProfile_image() != null) {
                    Glide.with(this.context).load(Constants.BASE_IMAGE_URL + currWorksheet.getProfile_image()).asBitmap().centerCrop().placeholder((int) R.drawable.profile_pic).error((int) R.drawable.profile_pic).into(new BitmapImageViewTarget(mViewHolder.profile_mytask) {
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(MyBaseAdapter.this.context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mViewHolder.profile_mytask.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                mViewHolder.tvmonth.setVisibility(View.GONE);
                mViewHolder.tvProName.setText(currWorksheet.getFirstName() + " " + currWorksheet.getLastName());
                if (currWorksheet.getApproveStatus().equalsIgnoreCase(MyTaskActivity.APPROVE_STATUS_PENDING)) {
                    mViewHolder.linBtnView.setVisibility(View.VISIBLE);
                } else {
                    mViewHolder.linBtnView.setVisibility(View.GONE);
                }
                mViewHolder.tvAddress.setText(currWorksheet.getProject_Number() + " | " + currWorksheet.getProjectName());
            }
            String date = currWorksheet.getWorkDate();
            input = new SimpleDateFormat("yyyy-MM-dd");
            try {
                mViewHolder.tvmonth.setText(new SimpleDateFormat("MMM dd").format(input.parse(date)));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            this.tvAttachment.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    MyTaskActivity.this.isSeeAttachment = true;
                    new UserHelper(MyBaseAdapter.this.context).apiGetWorksheetDetails(currWorksheet, MyBaseAdapter.this.WorksheetDetailsCallBack);
                }
            });
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (MyTaskActivity.this.isSidemenuOpen()) {
                        Log.d(MyTaskActivity.this.TAG, "Sidemenu is open");
                        return;
                    }
                    UserHelper helper = new UserHelper(MyBaseAdapter.this.context);
                    MyTaskActivity.this.progressHelper.showDialog("Loading..");
                    helper.apiGetWorksheetDetails(currWorksheet, MyBaseAdapter.this.WorksheetDetailsCallBack);
                }
            });
            return convertView;
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
            view.setText(String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + MyTaskActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + MyTaskActivity.this.getResources().getString(R.string.txt_m));
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        updateMethod();
    }

    private void updateMethod() {
        setContentView(R.layout.activity_my_task);
        this.tasklist.clear();
        this.header.setTitle(getString(R.string.title_activity_my_task));
        this.header.setRightBtnImage(R.drawable.more);
        this.header.setRightBtnClickListener(new C07101());
        getViews();
        String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        this.status = getIntent().getStringExtra("Status");
        this.is_from_mng_jobber = getIntent().getBooleanExtra("is_from_mng_jobber", false);
        setRightSlideMenu(this.is_from_mng_jobber);
        if (this.status == null) {
            this.status = APPROVE_STATUS_PENDING;
        }
        if (this.status.equalsIgnoreCase(Constants.STATUS_PENDING)) {
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || this.is_from_mng_jobber) {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_hrs));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_task));
            }
        }
        if (this.status.equalsIgnoreCase("approve")) {
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || this.is_from_mng_jobber) {
                this.txtstatuslbl.setText(getResources().getString(R.string.approved_hrs));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.past_task_new));
            }
        }
        if (this.status.equalsIgnoreCase("reject")) {
            this.txtstatuslbl.setText(getResources().getString(R.string.reject_hrs));
        }
        if (this.status.equalsIgnoreCase(APPROVE_STATUS_NOT_PENDING)) {
            this.txtstatuslbl.setText(getResources().getString(R.string.past_task_new));
        }
        this.pageNo = 1;
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        this.progressHelper.showDialog("Loading..");
        this.apiCalled = true;
        this.userHelper.apiGetTaskList(this.MyTaskListOnlyCallback, this.is_from_mng_jobber, this.status, this.pageNo);
        this.PnameList = getListFromLocalDB("tblproject", "project_name");
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
        this.employeeList = getListFromLocalDB("tblemployee", "first_name");
        if (this.employeeList != null) {
            getEmployeeList(this.employeeList);
        }
        setTaskListView(this.context);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        this.header.setTitle(getString(R.string.title_activity_my_task));
        this.header.setRightBtnImage(R.drawable.more);
        this.header.setRightBtnClickListener(new C07112());
        getViews();
        String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        this.status = getIntent().getStringExtra("Status");
        this.is_from_mng_jobber = getIntent().getBooleanExtra("is_from_mng_jobber", false);
        setRightSlideMenu(this.is_from_mng_jobber);
        if (this.status == null) {
            this.status = APPROVE_STATUS_PENDING;
        }
        if (this.status.equalsIgnoreCase(Constants.STATUS_PENDING)) {
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || this.is_from_mng_jobber) {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_hrs));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.pendding_task));
            }
        }
        if (this.status.equalsIgnoreCase("approve")) {
            if (user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) || this.is_from_mng_jobber) {
                this.txtstatuslbl.setText(getResources().getString(R.string.approved_hrs));
            } else {
                this.txtstatuslbl.setText(getResources().getString(R.string.past_task_new));
            }
        }
        if (this.status.equalsIgnoreCase("reject")) {
            this.txtstatuslbl.setText(getResources().getString(R.string.reject_hrs));
        }
        if (this.status.equalsIgnoreCase(APPROVE_STATUS_NOT_PENDING)) {
            this.txtstatuslbl.setText(getResources().getString(R.string.past_task_new));
        }
        this.pageNo = 1;
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        this.progressHelper.showDialog("Loading..");
        this.apiCalled = true;
        this.userHelper.apiGetTaskList(this.MyTaskListOnlyCallback, this.is_from_mng_jobber, this.status, this.pageNo);
        this.PnameList = getListFromLocalDB("tblproject", "project_name");
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
        this.employeeList = getListFromLocalDB("tblemployee", "first_name");
        if (this.employeeList != null) {
            getEmployeeList(this.employeeList);
        }
        setTaskListView(this.context);
    }

    private void setTaskListView(Context context1) {
        try {
            this.tasklist.clear();
            this.Adapter = new MyBaseAdapter(context1, this.tasklist);
            this.mytaskListview.setAdapter(this.Adapter);
            this.mytaskListview.setOnScrollListener(new C07123());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEmployeeList(List item) {
        this.employeeList = item;
        this.autocopmleteEmployee = new ArrayAdapter(this, R.layout.auto_complete_textview, this.employeeList);
        this.autoEmployees.setAdapter(this.autocopmleteEmployee);
        this.autoEmployees.setOnItemClickListener(new C07134());
        this.autoEmployees.setOnTouchListener(new C07145());
    }

    private void getProjectListAdapter(List item) {
        this.PnameList = item;
        this.myAutoCompleteAdapter = new ArrayAdapter(this, R.layout.auto_complete_textview, this.PnameList);
        this.autoProject.setAdapter(this.myAutoCompleteAdapter);
        this.autoProject.init(this.context, this);
        this.autoProject.setOnItemClickListener(new C07156());
    }

    private void getBranchAdapter(List branchList) {
        this.BranchList = branchList;
        this.autocompleteAdapterB = new ArrayAdapter(this, R.layout.auto_complete_textview, this.BranchList);
        this.autoBranch.setAdapter(this.autocompleteAdapterB);
        this.autoBranch.setOnItemClickListener(new C07167());
        this.autoBranch.setOnTouchListener(new C07178());
    }

    private void getServiceAdapter(List slist) {
        this.serviceList = slist;
        this.autocompleteAdapterS = new ArrayAdapter(this, R.layout.auto_complete_textview, this.serviceList);
        this.autoService.setAdapter(this.autocompleteAdapterS);
        this.autoService.setOnItemClickListener(new C07189());
    }

    private List<String> getListFromLocalDB(String tblname, String list_name) {
        this.dbHelper = new DatabaseHelper(this.context);
        String qry = "select * from " + tblname;
        if (tblname.equalsIgnoreCase("tblproject") && !this.is_from_mng_jobber) {
            String role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
            String user_id = Preference.getSharedPref(Constants.PREF_USER_ID, "");
            if (!(role_id == null || !role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER) || this.is_from_mng_jobber)) {
                qry = qry + " where  ref_manager_id = '" + user_id + "'";
            }
        }
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        ArrayList<HashMap<String, String>> list = this.dbHelper.selectRecordsFromDBList(qry, null);
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

    private void setInvisibleManLayout() {
        this.ll_btns_manager.setVisibility(View.VISIBLE);
        this.ll_btns_Emp.setVisibility(View.GONE);
    }

    private void getViews() {
        this.mytaskListview = (ListView) findViewById(R.id.ls_mytask);
        this.progressHelper = new ProgressHelper(this.context);
        this.btnApprove = (ETechButton) findViewById(R.id.btnapprove);
        this.btnNotApprove = (ETechButton) findViewById(R.id.btnnotapprove);
        this.btnpending = (ETechButton) findViewById(R.id.btnpending);
        this.btnNewTask = (ETechButton) findViewById(R.id.btnnewtask);
        this.btnPastTask = (ETechButton) findViewById(R.id.btnpasttask);
        this.txt = (ETechTextView) findViewById(R.id.txtnodata);
        this.ll_btns_Emp = (LinearLayout) findViewById(R.id.ll_btns_emp);
        this.ll_btns_manager = (LinearLayout) findViewById(R.id.ll_btns_man);
        this.txtstatuslbl = (ETechTextView) findViewById(R.id.txtStatusofHours);
        this.btnApprove.setOnClickListener(this.approveBtnClicklistener);
        this.btnNotApprove.setOnClickListener(this.notApproveBtnClicklistener);
        this.btnpending.setOnClickListener(this.pendingBtnClicklistener);
        this.btnNewTask.setOnClickListener(this.newTaskclickListener);
        this.btnPastTask.setOnClickListener(this.pastTaskclickListener);
        this.btnsearch.setOnClickListener(this.searchBtnClickListener);
        this.btnclr.setOnClickListener(this.clearBtnClickListener);
        this.btncancel.setOnClickListener(this.cancelBtnClickListener);
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

    private void resetButtonView(Button btn) {
        this.btnApprove.setBackgroundResource(R.drawable.btn_round_corner);
        this.btnNotApprove.setBackgroundResource(R.drawable.btn_round_corner);
        this.btnpending.setBackgroundResource(R.drawable.btn_round_corner);
        this.btnNewTask.setBackgroundResource(R.drawable.btn_round_corner);
        this.btnPastTask.setBackgroundResource(R.drawable.btn_round_corner);
        this.btnApprove.setTextColor(getResources().getColor(R.color.dark_grey));
        this.btnNotApprove.setTextColor(getResources().getColor(R.color.dark_grey));
        this.btnpending.setTextColor(getResources().getColor(R.color.dark_grey));
        this.btnNewTask.setTextColor(getResources().getColor(R.color.dark_grey));
        this.btnPastTask.setTextColor(getResources().getColor(R.color.dark_grey));
        btn.setBackgroundResource(R.drawable.btn_round_corner_selected);
        btn.setTextColor(-1);
    }
}

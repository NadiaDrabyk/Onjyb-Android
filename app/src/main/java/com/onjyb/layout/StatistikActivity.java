package com.onjyb.layout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.onjyb.R;
import com.onjyb.adaptor.DropdownAdapter;
import com.onjyb.adaptor.overtimeListDialogAdapter;
import com.onjyb.Constants;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular.DropDownItemClickListener;
import com.onjyb.db.AttachmentMap;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.OvertimeRule;
import com.onjyb.db.Service;
import com.onjyb.db.SummaryTotal;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView.OnHeaderClickListener;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView.OnStickyHeaderChangedListener;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView.OnStickyHeaderOffsetChangedListener;

public class StatistikActivity extends BaseDrawerActivity implements OnItemClickListener, OnHeaderClickListener, OnStickyHeaderOffsetChangedListener, OnStickyHeaderChangedListener, DropDownItemClickListener {
    private ActionCallback MyTaskListOnlyCallback = new C07794();
    OnClickListener ShowOvertimeListClickListener = new C07805();
    private final String TAG = StatistikActivity.class.getName();
    EtechEditAutoCompleteTextRobotoRegular actvprojectList;
    boolean apiCalled = false;
    ArrayList<OvertimeRule> arrayOvertimeData = new ArrayList();
    private Context context = this;
    DropdownAdapter dAdapter;
    DatabaseHelper dbHelper;
    ETechEditText edtEndDate;
    ETechEditText edtStrDate;
    String emp_id;
    View footerView;
    boolean is_mng_jobber = false;
    private boolean isfilter = false;
    LinearLayout ll_TotalOvertimeView;
    private MyBaseAdapter mAdapter1;
    ArrayAdapter<String> myAutoCompleteAdapter;
    public overtimeListDialogAdapter overTimeLIstAdapter = null;
    Dialog overtimeListDialog = null;
    int pageNo = 1;
    List<String> pnameList;
    ProgressHelper progressHelper;
    HashMap<String, String> projectid;
    String status = "";
    private StickyListHeadersListView stickyList;
    ArrayList<SummaryTotal> summaryTotalArrayList = new ArrayList();
    ArrayList<WorkSheet> tasklist = new ArrayList();
    ETechTextView tvTotalOvertimes;
    ETechTextView tvTotalOvertimes1;
    ETechTextView tvTotalWorkHrs;
    ETechTextView tvTotalWorkOvertimeinList;
    ETechTextView tvTotalkmdrives;
    TextView tvnoworksheetdata;
    private String txtEndDatestk = "";
    private String txtStartDatestk = "";
    UserHelper userHelper;
    String user_role_id;

    class C07731 implements OnScrollListener {
        C07731() {
        }

        public void onScrollStateChanged(AbsListView arg0, int arg1) {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount > 0) {
                Boolean flag = Boolean.valueOf(Boolean.parseBoolean(Preference.getSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_TOTAL_SUMMARY, "")));
                if (firstVisibleItem + visibleItemCount == totalItemCount && visibleItemCount > 0 && flag.booleanValue()) {
                    StatistikActivity.this.userHelper = new UserHelper(StatistikActivity.this.context);
                    if (flag.booleanValue()) {
                        StatistikActivity.this.pageNo++;
                        StatistikActivity.this.progressHelper.showDialog("Loading..");
                        if (!StatistikActivity.this.apiCalled) {
                            StatistikActivity.this.apiCalled = true;
                            StatistikActivity.this.userHelper.apiGetTaskListWithTotal(StatistikActivity.this.MyTaskListOnlyCallback, StatistikActivity.this.status, StatistikActivity.this.pageNo, null, null, null, StatistikActivity.this.emp_id, StatistikActivity.this.edtStrDate.getText().toString(), StatistikActivity.this.edtEndDate.getText().toString(), true, StatistikActivity.this.is_mng_jobber);
                        }
                    }
                }
            }
        }
    }

    class C07742 implements OnItemClickListener {
        C07742() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String pName = StatistikActivity.this.actvprojectList.getText().toString();
            String str = pName;
            String qry = "select * from tblproject where project_name='" + str.substring(pName.indexOf("|") + 2) + "'";
            StatistikActivity.this.dbHelper = new DatabaseHelper(StatistikActivity.this.context);
            StatistikActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(StatistikActivity.this.context);
            StatistikActivity.this.projectid = StatistikActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            if (StatistikActivity.this.projectid != null) {
                StatistikActivity.this.isfilter = true;
                String pId = "";
                StatistikActivity.this.tasklist = new ArrayList();
                StatistikActivity.this.userHelper = new UserHelper(StatistikActivity.this.context);
                StatistikActivity.this.progressHelper.showDialog("Loading..");
                if (StatistikActivity.this.actvprojectList.getText().toString().equalsIgnoreCase("")) {
                    pId = "";
                } else if (StatistikActivity.this.actvprojectList.getText().toString().equalsIgnoreCase(((String) StatistikActivity.this.projectid.get("project_number")) + " | " + ((String) StatistikActivity.this.projectid.get("project_name")))) {
                    pId = (String) StatistikActivity.this.projectid.get("project_id");
                } else {
                    pId = "";
                }
                if (!StatistikActivity.this.apiCalled) {
                    StatistikActivity.this.apiCalled = true;
                    StatistikActivity.this.userHelper.apiGetTaskListWithTotal(StatistikActivity.this.MyTaskListOnlyCallback, StatistikActivity.this.status, 1, pId, null, null, StatistikActivity.this.emp_id, StatistikActivity.this.edtStrDate.getText().toString(), StatistikActivity.this.edtEndDate.getText().toString(), true, StatistikActivity.this.is_mng_jobber);
                }
            } else if (position == 0) {
                StatistikActivity.this.isfilter = true;
                StatistikActivity.this.tasklist = new ArrayList();
                StatistikActivity.this.userHelper = new UserHelper(StatistikActivity.this.context);
                StatistikActivity.this.progressHelper.showDialog("Loading..");
                if (!StatistikActivity.this.apiCalled) {
                    StatistikActivity.this.apiCalled = true;
                    StatistikActivity.this.userHelper.apiGetTaskListWithTotal(StatistikActivity.this.MyTaskListOnlyCallback, StatistikActivity.this.status, 1, null, null, null, StatistikActivity.this.emp_id, StatistikActivity.this.edtStrDate.getText().toString(), StatistikActivity.this.edtEndDate.getText().toString(), true, StatistikActivity.this.is_mng_jobber);
                }
            }
            AppUtils.hideKeyBoard(StatistikActivity.this);
        }
    }

    class C07753 implements OnGlobalLayoutListener {
        C07753() {
        }

        @TargetApi(16)
        public void onGlobalLayout() {
            StatistikActivity.this.actvprojectList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            StatistikActivity.this.actvprojectList.setDropDownWidth(StatistikActivity.this.actvprojectList.getWidth());
        }
    }

    class C07794 implements ActionCallback {

        class C07761 extends TypeReference<ArrayList<WorkSheet>> {
            C07761() {
            }
        }

        class C07772 extends TypeReference<ArrayList<SummaryTotal>> {
            C07772() {
            }
        }

        class C07783 extends TypeReference<ArrayList<OvertimeRule>> {
            C07783() {
            }
        }

        C07794() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (StatistikActivity.this.progressHelper != null) {
                StatistikActivity.this.progressHelper.dismissDialog();
            }
            StatistikActivity.this.apiCalled = false;
            if (statusCode == 1) {
                try {
                    JSONObject jsonObject = (JSONObject) res;
                    Preference.setSharedPref(Constants.PREF_HASE_RECORDS_AVAILABLE_TOTAL_SUMMARY, String.valueOf(jsonObject.getBoolean("has_more_records")));
                    ArrayList<WorkSheet> objWorkSheet = (ArrayList) new ObjectMapper().readValue(jsonObject.getJSONArray("work_details").toString(), new C07761());
                    ArrayList<SummaryTotal> objsummary = (ArrayList) new ObjectMapper().readValue(jsonObject.getJSONArray("summary").toString(), new C07772());
                    JSONArray overtimeData = jsonObject.getJSONArray("overtimeData");
                    ObjectMapper mapper2 = new ObjectMapper();
                    StatistikActivity.this.arrayOvertimeData.clear();
                    StatistikActivity.this.arrayOvertimeData = (ArrayList) mapper2.readValue(overtimeData.toString(), new C07783());
                    if (!(StatistikActivity.this.arrayOvertimeData == null || StatistikActivity.this.arrayOvertimeData.size() <= 0 || StatistikActivity.this.ll_TotalOvertimeView == null)) {
                        StatistikActivity.this.ll_TotalOvertimeView.setOnClickListener(StatistikActivity.this.ShowOvertimeListClickListener);
                    }
                    StatistikActivity.this.summaryTotalArrayList.addAll(objsummary);
                    StatistikActivity.this.tasklist.addAll(objWorkSheet);
                    if (StatistikActivity.this.footerView != null) {
                        StatistikActivity.this.stickyList.removeFooterView(StatistikActivity.this.footerView);
                        StatistikActivity.this.footerView = null;
                    }
                    if (StatistikActivity.this.tasklist == null || StatistikActivity.this.tasklist.size() <= 0) {
                        if (StatistikActivity.this.footerView == null) {
                            StatistikActivity.this.footerView = StatistikActivity.this.getLayoutInflater().inflate(R.layout.headerhomestatistikk, null);
                            StatistikActivity.this.stickyList.addFooterView(StatistikActivity.this.footerView);
                        }
                        StatistikActivity.this.mAdapter1.updateReceiptsList(StatistikActivity.this.tasklist);
                        return;
                    }
                    try {
                        StatistikActivity.this.tvnoworksheetdata.setVisibility(View.GONE);
                        if (StatistikActivity.this.isfilter) {
                            StatistikActivity.this.isfilter = false;
                            StatistikActivity.this.mAdapter1 = new MyBaseAdapter(StatistikActivity.this.context, StatistikActivity.this.tasklist);
                            StatistikActivity.this.stickyList.setAdapter(StatistikActivity.this.mAdapter1);
                            return;
                        }
                        StatistikActivity.this.mAdapter1.notifyDataSetChanged();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Log.e(StatistikActivity.this.TAG, "onActionComplete > Exception: " + e2.toString());
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(StatistikActivity.this.context, StatistikActivity.this.getString(R.string.app_name), res.toString());
            Toast.makeText(StatistikActivity.this.context, StatistikActivity.this.getResources().getString(R.string.response_error_msg), Toast.LENGTH_SHORT).show();
        }
    }

    class C07805 implements OnClickListener {
        C07805() {
        }

        public void onClick(View view) {
            if (StatistikActivity.this.arrayOvertimeData != null && StatistikActivity.this.arrayOvertimeData.size() > 0) {
                StatistikActivity.this.ShowOvertimeListDialog();
            }
        }
    }

    class C07816 implements OnItemClickListener {
        C07816() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        }
    }

    class C07827 implements OnClickListener {
        C07827() {
        }

        public void onClick(View view) {
            StatistikActivity.this.overtimeListDialog.dismiss();
        }
    }

    public class MyBaseAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private ActionCallback WorksheetDetailsCallBack = new C07882();
        Context context;
        LayoutInflater inflater;
        ArrayList<WorkSheet> myList;

        class C07882 implements ActionCallback {

            class C07841 extends TypeReference<ArrayList<WorkSheet>> {
                C07841() {
                }
            }

            class C07852 extends TypeReference<ArrayList<AttachmentMap>> {
                C07852() {
                }
            }

            class C07863 extends TypeReference<ArrayList<Service>> {
                C07863() {
                }
            }

            class C07874 extends TypeReference<ArrayList<OvertimeRule>> {
                C07874() {
                }
            }

            C07882() {
            }

            /* JADX WARNING: inconsistent code. */
            public void onActionComplete(int statusCode, String callbackString, Object res) {
                if (StatistikActivity.this.progressHelper != null) {
                    StatistikActivity.this.progressHelper.dismissDialog();
                }
                if (statusCode == 1) {
                    try {
                        //0925
                        JSONObject dic = ((JSONObject) res);
                        ArrayList<WorkSheet> items = new ObjectMapper().readValue(dic.getJSONArray("worksheet_details").toString(),
                                new StatistikActivity.MyBaseAdapter.C07882.C07841());
                        WorkSheet workSheet = items.get(0);

                        ArrayList<AttachmentMap> mapList = new ObjectMapper().readValue(dic.getJSONArray("workimage_details").toString(),
                                new StatistikActivity.MyBaseAdapter.C07882.C07852());
                        workSheet.setAttachementList(mapList);

                        ArrayList<Service> extraServices = new ObjectMapper().readValue(dic.getJSONArray("extraservice_details").toString(),
                                new StatistikActivity.MyBaseAdapter.C07882.C07863());
                        workSheet.setServiceObjectsArray(extraServices);

                        ArrayList<OvertimeRule> overtimeRules = new ObjectMapper().readValue(dic.getJSONArray("overtime_details").toString(),
                                new StatistikActivity.MyBaseAdapter.C07882.C07874());
                        workSheet.setOvertimeRuleArrayList(overtimeRules);

                        String isOvertimeautomatic = dic.getString("isworkovertimeautomatic");
                        workSheet.setIsworksheetAutomaticEditmode(isOvertimeautomatic);

                        String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
                        if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)){
                        }else {
                            if(!user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER)){
                                Intent intent = new Intent(StatistikActivity.this, RegisterTimeActivity.class);
                                intent.putExtra("WorksheetEditData",workSheet);
                                startActivity(intent);
                                return;
                            }
                        }
                        String status = workSheet.getApproveStatus();
                        if(status.equalsIgnoreCase("pennding")) {
                        }else{
                            if(!status.equalsIgnoreCase("reject")){
                                Intent intent = new Intent(StatistikActivity.this, DisplayMyTaskDetailActivity.class);
                                intent.putExtra("WorksheetEditData", workSheet);
                                startActivity(intent);
                                return;
                            }
                        }
                        Intent intent = new Intent(StatistikActivity.this, RegisterTimeActivity.class);
                        intent.putExtra("WorksheetEditData", workSheet);
                        if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER))
                            intent.putExtra("EditFlag", "approve");

                        startActivity(intent);
                        return;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                AlertDialogHelper.getNotificatonAlert(StatistikActivity.this.context, StatistikActivity.this.getString(R.string.app_name), res.toString());
            }
        }

        class HeaderViewHolder {
            TextView text;

            HeaderViewHolder() {
            }
        }

        private class MyViewHolder {
            TextView tvnoworksheet;
            TextView txtdate;
            TextView txthrs;
            TextView txtid;
            TextView txtkm;
            TextView txtpname;

            public MyViewHolder(View item) {
                this.txtid = (TextView) item.findViewById(R.id.tvpronono);
                this.txtpname = (TextView) item.findViewById(R.id.tvproname);
                this.txtdate = (TextView) item.findViewById(R.id.tvdatehome);
                this.txthrs = (TextView) item.findViewById(R.id.tvhours);
                this.txtkm = (TextView) item.findViewById(R.id.tvKmhome);
                this.tvnoworksheet = (TextView) item.findViewById(R.id.tvnoworksheet);
            }
        }

        class ViewHolder {
            TextView text;

            ViewHolder() {
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
            MyViewHolder mViewHolder;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.home_listview_items, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            if (getCount() > 0) {
                mViewHolder.tvnoworksheet.setVisibility(View.GONE);
            }
            final WorkSheet currentworksheet = getItem(position);
            mViewHolder.txtid.setText((position + 1) + ".");
            mViewHolder.txtpname.setText(currentworksheet.getProjectName());
            if (currentworksheet.getTotalWorkTime() == null) {
                mViewHolder.txthrs.setText("1");
            } else {
                mViewHolder.txthrs.setText(displayHoursfromMinutes1(Integer.parseInt(currentworksheet.getTotalWorkTime())));
            }
            mViewHolder.txtkm.setText(currentworksheet.getkMDrive());
            if (currentworksheet.getWorkDate() != null) {
                SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    mViewHolder.txtdate.setText(new SimpleDateFormat("dd MMM yy").format(input.parse(currentworksheet.getWorkDate())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            convertView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    new UserHelper(MyBaseAdapter.this.context).apiGetWorksheetDetails(currentworksheet, MyBaseAdapter.this.WorksheetDetailsCallBack);
                }
            });
            return convertView;
        }

        private String displayHoursfromMinutes1(int minutes) {
            int workHrs = 0;
            int workMn = 0;
            try {
                workHrs = minutes / 60;
                workMn = minutes % 60;
                if (workHrs < 0) {
                    workHrs = 0;
                }
                if (workMn < 0) {
                    workMn = 0;
                }
            } catch (Exception e) {
            }
            return String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + StatistikActivity.this.getResources().getString(R.string.txt_m);
        }

        public void updateReceiptsList(ArrayList<WorkSheet> messageList) {
            this.myList.clear();
            this.myList.addAll(messageList);
            notifyDataSetChanged();
        }

        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            int time;
            int i;
            int workMn;
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = this.inflater.inflate(R.layout.headerhomestatistikk, parent, false);
                StatistikActivity.this.tvTotalWorkHrs = (ETechTextView) convertView.findViewById(R.id.tvworkingHrs);
                StatistikActivity.this.tvTotalOvertimes = (ETechTextView) convertView.findViewById(R.id.tvworkOverTime);
                StatistikActivity.this.tvTotalOvertimes1 = (ETechTextView) convertView.findViewById(R.id.tvworkOverTime1);
                StatistikActivity.this.tvTotalkmdrives = (ETechTextView) convertView.findViewById(R.id.tvkmdrive);
                StatistikActivity.this.tvTotalWorkOvertimeinList = (ETechTextView) convertView.findViewById(R.id.txtTotalworkovertime);
                StatistikActivity.this.ll_TotalOvertimeView = (LinearLayout) convertView.findViewById(R.id.ll_TotalOvertimeView);
                ImageView ivOvertime = (ImageView)convertView.findViewById(R.id.imgvovettime);//jin
                if (StatistikActivity.this.arrayOvertimeData != null && StatistikActivity.this.arrayOvertimeData.size() > 0) {
                    StatistikActivity.this.ll_TotalOvertimeView.setOnClickListener(StatistikActivity.this.ShowOvertimeListClickListener);
                    ivOvertime.setOnClickListener(StatistikActivity.this.ShowOvertimeListClickListener);

                }
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            StatistikActivity.this.tvTotalWorkHrs.setText(String.format("%02d", new Object[]{Integer.valueOf(0)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(0)}) + StatistikActivity.this.getResources().getString(R.string.txt_m));
            StatistikActivity.this.tvTotalOvertimes.setText(String.format("%02d", new Object[]{Integer.valueOf(0)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(0)}) + StatistikActivity.this.getResources().getString(R.string.txt_m));
            StatistikActivity.this.tvTotalOvertimes1.setText(String.format("%02d", new Object[]{Integer.valueOf(0)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(0)}) + StatistikActivity.this.getResources().getString(R.string.txt_m));
            StatistikActivity.this.tvTotalkmdrives.setText("0 " + StatistikActivity.this.getResources().getString(R.string.Km));
            SummaryTotal summaryTotal = (SummaryTotal) StatistikActivity.this.summaryTotalArrayList.get(0);
            if (summaryTotal.getTotal_Work_Overtime() != null) {
                time = Integer.parseInt(summaryTotal.getTotal_Work_Overtime());
                i = 0;
                workMn = 0;
                try {
                    i = time / 60;
                    workMn = time % 60;
                    if (i < 0) {
                        i = 0;
                    }
                    if (workMn < 0) {
                        workMn = 0;
                    }
                } catch (Exception e) {
                }
                StatistikActivity.this.tvTotalWorkOvertimeinList.setText(String.format("%02d", new Object[]{Integer.valueOf(i)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + StatistikActivity.this.getResources().getString(R.string.txt_m));
            }
            if (summaryTotal.getTotal_work_hours() != null) {
                time = Integer.parseInt(summaryTotal.getTotal_work_hours());
                i = 0;
                workMn = 0;
                try {
                    i = time / 60;
                    workMn = time % 60;
                    if (i < 0) {
                        i = 0;
                    }
                    if (workMn < 0) {
                        workMn = 0;
                    }
                } catch (Exception e2) {
                }
                StatistikActivity.this.tvTotalWorkHrs.setText(String.format("%02d", new Object[]{Integer.valueOf(i)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + StatistikActivity.this.getResources().getString(R.string.txt_m));
            }
            if (summaryTotal.getTotal_overtime2() != null) {
                time = Integer.parseInt(summaryTotal.getTotal_overtime2());
                i = 0;
                workMn = 0;
                try {
                    i = time / 60;
                    workMn = time % 60;
                    if (i < 0) {
                        i = 0;
                    }
                    if (workMn < 0) {
                        workMn = 0;
                    }
                } catch (Exception e3) {
                }
                StatistikActivity.this.tvTotalOvertimes.setText(String.format("%02d", new Object[]{Integer.valueOf(i)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + StatistikActivity.this.getResources().getString(R.string.txt_m));
            }
            if (summaryTotal.getTotal_overtime1() != null) {
                time = Integer.parseInt(summaryTotal.getTotal_overtime1());
                i = 0;
                workMn = 0;
                try {
                    i = time / 60;
                    workMn = time % 60;
                    if (i < 0) {
                        i = 0;
                    }
                    if (workMn < 0) {
                        workMn = 0;
                    }
                } catch (Exception e4) {
                }
                StatistikActivity.this.tvTotalOvertimes1.setText(String.format("%02d", new Object[]{Integer.valueOf(i)}) + StatistikActivity.this.getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + StatistikActivity.this.getResources().getString(R.string.txt_m));
            }
            if (summaryTotal.getTotal_km_drive() != null) {
                StatistikActivity.this.tvTotalkmdrives.setText(summaryTotal.getTotal_km_drive() + " " + StatistikActivity.this.getResources().getString(R.string.Km));
            }
            return convertView;
        }

        public long getHeaderId(int position) {
            return 0;
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment1 extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dp = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dp.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            return dp;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((StatistikActivity)getActivity()).populateSetEndDatestatistikk(yy, mm + 1, dd);
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
            ((StatistikActivity)getActivity()).populateSetDatestatistikk(yy, mm + 1, dd);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistik);
        this.header.setTitle(getString(R.string.txtt_statistick));
        this.header.hideRightBtn();
        addStickHeader();
        getView();
        this.emp_id = Preference.getSharedPref(Constants.PREF_USER_ID, "");
        this.user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        if (this.user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER)) {
            this.is_mng_jobber = true;
        }
        setDefaultDate(this.edtEndDate);
        this.edtStrDate.setText(AppUtils.getIncrementByOne(this.edtEndDate.getText().toString(), -30));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        try {
            this.txtStartDatestk = simpleDateFormat.format(simpleDateFormat.parse(this.edtStrDate.getText().toString()));
            this.txtEndDatestk = simpleDateFormat.format(simpleDateFormat.parse(this.edtEndDate.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.pnameList = getListFromLocalDB("tblproject", "project_name");
        if (this.pnameList != null) {
            getProjectListAdapter(this.pnameList);
        }
        try {
            this.mAdapter1 = new MyBaseAdapter(this.context, this.tasklist);
            this.stickyList.setAdapter(this.mAdapter1);
            this.stickyList.setFocusable(true);
            this.stickyList.setOnScrollListener(new C07731());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.userHelper = new UserHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        this.apiCalled = true;
        this.userHelper.apiGetTaskListWithTotal(this.MyTaskListOnlyCallback, this.status, 1, null, null, null, this.emp_id, this.edtStrDate.getText().toString(), this.edtEndDate.getText().toString(), true, this.is_mng_jobber);
    }

    private void getProjectListAdapter(List item) {
        this.pnameList = item;
        this.pnameList.add(0, getResources().getString(R.string.all));
        this.myAutoCompleteAdapter = new ArrayAdapter(this, R.layout.auto_complete_textview, this.pnameList);
        this.dAdapter = new DropdownAdapter(this.context, R.layout.auto_complete_textview, R.layout.auto_complete_textview, this.pnameList);
        this.actvprojectList.setAdapter(this.dAdapter);
        this.actvprojectList.setOnItemClickListener(new C07742());
    }

    private List<String> getListFromLocalDB(String tblname, String list_name) {
        this.dbHelper = new DatabaseHelper(this.context);
        String qry = "select * from " + tblname;
        if (tblname.equalsIgnoreCase("tblproject")) {
            String role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
            Preference.getSharedPref(Constants.PREF_USER_ID, "");
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

    private void setDefaultDate(TextView view) {
        String formattedDate = new SimpleDateFormat("dd MMM yy").format(Calendar.getInstance().getTime());
        Log.d("current time>>>", formattedDate);
        view.setText(formattedDate);
    }

    public void selectEndDatestatistikk(View view) {
        new SelectDateFragment1().show(getSupportFragmentManager(), "DatePicker");
    }

    private void populateSetEndDatestatistikk(int year, int month, int day) {
        String time = day + "/" + month + "/" + year;
        this.txtEndDatestk = time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        try {
            Date date1 = simpleDateFormat.parse(time);
            String str1 = simpleDateFormat.format(date1);
            if (this.txtStartDatestk.equalsIgnoreCase("")) {
                AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.select_start_date_first));
                return;
            }
            if (Integer.parseInt(AppUtils.getDateDiffString(simpleDateFormat.parse(this.txtStartDatestk), date1)) < 0) {
                AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.start_must_greter));
                return;
            }
            this.isfilter = true;
            this.tasklist = new ArrayList();
            this.edtEndDate.setText(str1);
            this.userHelper = new UserHelper(this.context);
            this.progressHelper.showDialog("Loading..");
            if (!this.apiCalled) {
                this.apiCalled = true;
                this.userHelper.apiGetTaskListWithTotal(this.MyTaskListOnlyCallback, this.status, 1, null, null, null, this.emp_id, this.edtStrDate.getText().toString(), this.edtEndDate.getText().toString(), true, this.is_mng_jobber);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void selectDatestatistikk(View view) {
        new SelectDateFragment().show(getSupportFragmentManager(), "DatePicker");
    }

    private void populateSetDatestatistikk(int year, int month, int day) {
        String time = day + "/" + month + "/" + year;
        this.txtStartDatestk = time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat = new SimpleDateFormat("dd MMM yy");
        try {
            Date date1 = simpleDateFormat.parse(time);
            String str1 = simpleDateFormat.format(date1);
            if (!this.txtEndDatestk.equalsIgnoreCase("")) {
                if (Integer.parseInt(AppUtils.getDateDiffString(date1, simpleDateFormat.parse(this.txtEndDatestk))) < 0) {
                    AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.start_must_greter));
                    return;
                }
                this.edtStrDate.setText(str1);
                this.isfilter = true;
                this.tasklist = new ArrayList();
                this.userHelper = new UserHelper(this.context);
                this.progressHelper.showDialog("Loading..");
                if (!this.apiCalled) {
                    this.apiCalled = true;
                    this.userHelper.apiGetTaskListWithTotal(this.MyTaskListOnlyCallback, this.status, 1, null, null, null, this.emp_id, this.edtStrDate.getText().toString(), this.edtEndDate.getText().toString(), true, this.is_mng_jobber);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addStickHeader() {
        this.stickyList = (StickyListHeadersListView) findViewById(R.id.liststastikk);
        this.stickyList.setOnItemClickListener(this);
        this.stickyList.setOnHeaderClickListener(null);
        this.stickyList.setOnStickyHeaderChangedListener(this);
        this.stickyList.setOnStickyHeaderOffsetChangedListener(this);
        this.stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.list_header_statistikk, null));
        this.stickyList.setDrawingListUnderStickyHeader(true);
        this.stickyList.setAreHeadersSticky(true);
        this.stickyList.setStickyHeaderTopOffset(0);
        this.stickyList.setOnTouchListener(null);
        Log.d(this.TAG, this.stickyList.getHeaderViewsCount() + "CHILDs");
    }

    private void getView() {
        this.progressHelper = new ProgressHelper(this.context);
        this.actvprojectList = (EtechEditAutoCompleteTextRobotoRegular) findViewById(R.id.atvproject_statistikk);
        this.actvprojectList.init(this.context, this);
        this.edtStrDate = (ETechEditText) findViewById(R.id.edtstrtdtstk);
        this.edtEndDate = (ETechEditText) findViewById(R.id.edtenddtstk);
        this.tvnoworksheetdata = (TextView) findViewById(R.id.tvnoworksheet);
        this.actvprojectList.getViewTreeObserver().addOnGlobalLayoutListener(new C07753());
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
        view.setText(String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + " " + editString);
    }

    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
    }

    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
    }

    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    }

    public void onItemOptionClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (view.getId() == R.id.atvproject_statistikk) {
            AppUtils.hideKeyBoard(this);
            String pName = this.actvprojectList.getText().toString();
            String str = pName;
            String qry = "select * from tblproject where project_name='" + str.substring(pName.indexOf("|") + 2) + "'";
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            this.projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
            if (this.projectid != null) {
                this.isfilter = true;
                String pId = "";
                this.tasklist = new ArrayList();
                this.userHelper = new UserHelper(this.context);
                this.progressHelper.showDialog("Loading..");
                if (this.actvprojectList.getText().toString().equalsIgnoreCase("")) {
                    pId = "";
                } else if (this.actvprojectList.getText().toString().equalsIgnoreCase(((String) this.projectid.get("project_number")) + " | " + ((String) this.projectid.get("project_name")))) {
                    pId = (String) this.projectid.get("project_id");
                } else {
                    pId = "";
                }
                if (!this.apiCalled) {
                    this.apiCalled = true;
                    this.userHelper.apiGetTaskListWithTotal(this.MyTaskListOnlyCallback, this.status, 1, pId, null, null, this.emp_id, this.edtStrDate.getText().toString(), this.edtEndDate.getText().toString(), true, this.is_mng_jobber);
                }
            } else if (position == 0) {
                this.isfilter = true;
                this.tasklist = new ArrayList();
                this.userHelper = new UserHelper(this.context);
                this.progressHelper.showDialog("Loading..");
                if (!this.apiCalled) {
                    this.apiCalled = true;
                    this.userHelper.apiGetTaskListWithTotal(this.MyTaskListOnlyCallback, this.status, 1, null, null, null, this.emp_id, this.edtStrDate.getText().toString(), this.edtEndDate.getText().toString(), true, this.is_mng_jobber);
                }
            }
        }
    }

    private void ShowOvertimeListDialog() {
        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            int temp = (height * 2) / 3;
            if (this.overtimeListDialog == null) {
                this.overtimeListDialog = new Dialog(this);
                this.overtimeListDialog.requestWindowFeature(1);
                this.overtimeListDialog.setContentView(R.layout.overtimelist_statistikkdialog);
            }
            ListView lsovertimeLIstview = (ListView) this.overtimeListDialog.findViewById(R.id.lsovertimelist);
            if (this.overTimeLIstAdapter == null) {
                this.overTimeLIstAdapter = new overtimeListDialogAdapter(this, this.arrayOvertimeData);
            }
            lsovertimeLIstview.setAdapter(this.overTimeLIstAdapter);
            lsovertimeLIstview.setOnItemClickListener(new C07816());
            int lstotalh = AppUtils.getListViewHeightBasedOnChildrenLatest(lsovertimeLIstview) + AppUtils.dpToPixel(70.0f, this.context);
            if (lstotalh < temp) {
                this.overtimeListDialog.getWindow().setLayout(-2, lstotalh);
            } else {
                this.overtimeListDialog.getWindow().setLayout(-2, temp);
            }
            ((TextView) this.overtimeListDialog.findViewById(R.id.txtTitledialog)).setText(getResources().getString(R.string.tv_over_time));
            ((ImageView) this.overtimeListDialog.findViewById(R.id.lblcloseImg)).setOnClickListener(new C07827());
            this.overTimeLIstAdapter.notifyDataSetChanged();
            if (this.arrayOvertimeData != null && this.arrayOvertimeData.size() > 0) {
                this.overtimeListDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.onjyb.layout;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.ImageRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.onjyb.R;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular.DropDownItemClickListener;
import com.onjyb.db.AttachmentMap;
import com.onjyb.db.BarGraph;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.LineGraph;
import com.onjyb.db.OvertimeRule;
import com.onjyb.db.Service;
import com.onjyb.db.WorkSheet;
import com.onjyb.helper.HomeWatcher;
import com.onjyb.helper.HomeWatcher.OnHomePressedListener;
import com.onjyb.helper.MyBarValueFormatter;
import com.onjyb.helper.MyBroadcastReceiver;
import com.onjyb.helper.NotificationJobService;
//import com.onjyb.receiver.RegistrationIntentService;
import com.onjyb.receiver.RegistrationIntentService;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AlertDialogHelper.OnMyDialogResult;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import com.viewpagerindicator.CirclePageIndicator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView.OnHeaderClickListener;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView.OnStickyHeaderChangedListener;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView.OnStickyHeaderOffsetChangedListener;

public class EmpHomeActivity extends BaseDrawerActivity implements OnItemClickListener, OnHeaderClickListener, OnStickyHeaderOffsetChangedListener, OnStickyHeaderChangedListener, OnPageChangeListener, OnMyDialogResult, DropDownItemClickListener {
    private static final int REQUEST_JOBSCHEDULE_CODE = 101;
    private static String[] SDCARD_PERMISSIONS = new String[]{"android.permission.RECEIVE_BOOT_COMPLETED"};
    public static int kJobId = 1;
    private ActionCallback MyTaskListOnlyCallback = new C06177();
    private final String TAG = LoginActivity.class.getName();
    private ActionCallback WorksheetDetailsCallBack = new ActionCallback() {

        class C06041 extends TypeReference<ArrayList<WorkSheet>> {
            C06041() {
            }
        }

        class C06052 extends TypeReference<ArrayList<AttachmentMap>> {
            C06052() {
            }
        }

        class C06063 extends TypeReference<ArrayList<Service>> {
            C06063() {
            }
        }

        class C06074 extends TypeReference<ArrayList<OvertimeRule>> {
            C06074() {
            }
        }
        //WorksheetDetailsCallBack
        /* JADX WARNING: inconsistent code. */
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (EmpHomeActivity.this.progressHelper != null) {
                EmpHomeActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    //0925
                    JSONObject dic = ((JSONObject) res);
                    ArrayList<WorkSheet> items = new ObjectMapper().readValue(dic.getJSONArray("worksheet_details").toString(),
                            new C06041());
                    WorkSheet workSheet = items.get(0);

                    ArrayList<AttachmentMap> mapList = new ObjectMapper().readValue(dic.getJSONArray("workimage_details").toString(),
                            new C06052());
                    workSheet.setAttachementList(mapList);

                    ArrayList<Service> extraServices = new ObjectMapper().readValue(dic.getJSONArray("extraservice_details").toString(),
                            new C06063());
                    workSheet.setServiceObjectsArray(extraServices);

                    ArrayList<OvertimeRule> overtimeRules = new ObjectMapper().readValue(dic.getJSONArray("overtime_details").toString(),
                            new C06074());
                    workSheet.setOvertimeRuleArrayList(overtimeRules);

                    String isOvertimeautomatic = dic.getString("isworkovertimeautomatic");
                    workSheet.setIsworksheetAutomaticEditmode(isOvertimeautomatic);

                    String user_role_id = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
                    if(user_role_id.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)){
                    }else {
                        if(!user_role_id.equalsIgnoreCase(Constants.USER_ROLE_MANAGER)){
                            Intent intent = new Intent(EmpHomeActivity.this, RegisterTimeActivity.class);
                            intent.putExtra("WorksheetEditData",workSheet);
                            startActivity(intent);
                            return;
                        }
                    }
                    String status = workSheet.getApproveStatus();
                    if(status.equalsIgnoreCase("pennding")) {
                    }else{
                        if(!status.equalsIgnoreCase("reject")){
                            Intent intent = new Intent(EmpHomeActivity.this, DisplayMyTaskDetailActivity.class);
                            intent.putExtra("WorksheetEditData", workSheet);
                            startActivity(intent);
                            return;
                        }
                    }
                    Intent intent = new Intent(EmpHomeActivity.this, RegisterTimeActivity.class);
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
            AlertDialogHelper.getNotificatonAlert(EmpHomeActivity.this.context, EmpHomeActivity.this.getString(R.string.app_name), res.toString());
        }
    };
    ArrayAdapter<String> adapter;
    private ActionCallback apiCallBackUnReadCount = new C06219();
    EtechEditAutoCompleteTextRobotoRegular atvproject;
    BarChart barChart;
    ImageButton btnFinish;
    ImageButton btnNext;
    private Context context = this;
    DatabaseHelper dbHelper;
    private ImageView[] dots;
    private int dotsCount;
    EditText edtenddate;
    EditText edtstartdt;
    private boolean fadeHeader = true;
    View footerView;
    private ActionCallback graphDetailActionCallBack = new C06208();
    UserHelper helper;
    CirclePageIndicator indicator;
    boolean isBackPressed = false;
    boolean isFromSplash = false;
    boolean isfromPause = false;
    boolean isfromResume = false;
    boolean ismasterApisuccess = false;
    JobScheduler jobScheduler;
    LineChart lineChart;
    private ArrayList<String> list;
    private MyBaseAdapter mAdapter;
    HomeWatcher mHomeWatcher;
    private ActionCallback masterlistcallback = new C06156();
    ArrayAdapter<String> myAutoCompleteAdapter;
    OnPageChangeListener myOnPageChangeListener = new OnPageChangeListener() {
        public void onPageScrollStateChanged(int state) {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            EmpHomeActivity.this.indicator.setCurrentItem(position);
        }
    };
    MyPagerAdapter myPagerAdapter;
    TextView noDatainLinechart;
    ArrayList<BarGraph> objBarGraph;
    ArrayList<LineGraph> objLineGraph;
//    String pId = "";
    int pageNo = 1;
    LinearLayout pager_indicator;
    List<String> pnameList;
    ProgressHelper progressHelper;
//    String projectName = "";
//    HashMap<String, String> projectid;
    String status = "";
    private StickyListHeadersListView stickyList;
    ETechTextView tvnoProjectAvailable;
    ViewPager viewPager;
    ArrayList<WorkSheet> worksheetList = new ArrayList();

    class C06092 implements PageTransformer {
        C06092() {
        }

        public void transformPage(View page, float position) {
            float normalizedposition = Math.abs(Math.abs(position) - DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            page.setScaleX((normalizedposition / 2f) + 0.55f);
            page.setScaleY((normalizedposition / 2f) + 0.55f);
        }
    }

    class C06103 implements PageTransformer {
        C06103() {
        }

        public void transformPage(View page, float position) {
            float normalizedposition = Math.abs(Math.abs(position) - DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            page.setScaleX((normalizedposition / 2f) + 0.55f);
            page.setScaleY((normalizedposition / 2f) + 0.55f);
        }
    }

    class C06114 implements OnHomePressedListener {
        C06114() {
        }

        public void onHomePressed() {
            EmpHomeActivity.this.isfromPause = true;
        }

        public void onHomeLongPressed() {
        }
    }

    class C06125 implements OnGlobalLayoutListener {
        C06125() {
        }

        @TargetApi(16)
        public void onGlobalLayout() {
            EmpHomeActivity.this.atvproject.setDropDownWidth(EmpHomeActivity.this.atvproject.getWidth());
            EmpHomeActivity.this.atvproject.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
    }

    class C06156 implements ActionCallback {

        class C06131 implements OnClickListener {
            C06131() {
            }

            public void onClick(View v) {
            }
        }

        class C06142 implements OnClickListener {
            C06142() {
            }

            public void onClick(View v) {
            }
        }

        C06156() {
        }
        //masterlistcallback
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (statusCode == 1) {
                try {
                    EmpHomeActivity.this.ismasterApisuccess = true;
//                    if (EmpHomeActivity.this.isFromSplash) {
//                        EmpHomeActivity.this.isFromSplash = false;
//                        EmpHomeActivity.this.goRegisterScreen();
//                    }
                    EmpHomeActivity.this.helper = new UserHelper(EmpHomeActivity.this.context);
                    EmpHomeActivity.this.helper.apiGetTaskList(EmpHomeActivity.this.MyTaskListOnlyCallback, false, EmpHomeActivity.this.status, 1);
                    EmpHomeActivity.this.isfromResume = false;
                    EmpHomeActivity.this.pnameList = EmpHomeActivity.this.getListFromLocalDB("tblproject", "project_name");
                    if (EmpHomeActivity.this.pnameList != null) {
                        EmpHomeActivity.this.getProjectListAdapter(EmpHomeActivity.this.pnameList);
                    }
                    JSONObject jObj = new JSONObject((String) res.toString());
                    Preference.setSharedPref("project_service_branch_details", res.toString());

                    EmpHomeActivity.this.dbHelper = new DatabaseHelper(EmpHomeActivity.this.context);
                    EmpHomeActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(EmpHomeActivity.this.context);
                    ArrayList<HashMap<String, String>> projectNameMap = EmpHomeActivity.this.dbHelper.selectRecordsFromDBList("select * from tblproject limit 1", null);
                    ArrayList<String> projectnameList = new ArrayList();
                    if (projectNameMap != null && projectNameMap.size() > 0) {
                        for (int i = 0; i < projectNameMap.size(); i++) {
                            String pname = (String) ((HashMap) projectNameMap.get(i)).get("project_name");
                            String pnumber = (String) ((HashMap) projectNameMap.get(i)).get("project_number");
                            projectnameList.add(pname);
//                            if (i == 0) {
//                                String projId = (String) ((HashMap) projectNameMap.get(0)).get("project_id");
//                                if (projId != null && projId.length() > 0) {
//                                    EmpHomeActivity.this.pId = projId;
//                                    EmpHomeActivity.this.projectName = pname;
//                                    EmpHomeActivity.this.atvproject.setText(pnumber + " | " + pname);
//                                }
//                            }
                        }
                    }
                    EmpHomeActivity.this.pnameList = projectnameList;
                    if (EmpHomeActivity.this.pnameList != null && EmpHomeActivity.this.pnameList.size() > 0) {
                        EmpHomeActivity.this.myAutoCompleteAdapter.notifyDataSetChanged();
                    }
//                    if (EmpHomeActivity.this.pId.equalsIgnoreCase("")) {
//                        EmpHomeActivity.this.tvnoProjectAvailable.setVisibility(View.VISIBLE);
//                        EmpHomeActivity.this.viewPager.setVisibility(View.GONE);
//                        return;
//                    }
//                    EmpHomeActivity.this.helper = new UserHelper(EmpHomeActivity.this.context);
//                    EmpHomeActivity.this.helper.apigetGraphDetails(EmpHomeActivity.this.pId, EmpHomeActivity.this.graphDetailActionCallBack);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(EmpHomeActivity.this.TAG, e.toString());
                    return;
                }
            }
            if (EmpHomeActivity.this.progressHelper != null) {
                EmpHomeActivity.this.progressHelper.dismissDialog();
            }
            if (EmpHomeActivity.this.footerView == null) {
                EmpHomeActivity.this.footerView = EmpHomeActivity.this.getLayoutInflater().inflate(R.layout.headerhome, null);
                EmpHomeActivity.this.stickyList.addFooterView(EmpHomeActivity.this.footerView);
            }
            AlertDialogHelper.getConfirmationAlert(EmpHomeActivity.this.context, EmpHomeActivity.this.getString(R.string.app_name), new C06131(), new C06142(), res.toString(), 0, 1, false, EmpHomeActivity.this.getResources().getString(R.string.txt_retry), null, 2, 2);
        }
    }

    class C06177 implements ActionCallback {

        class C06161 extends TypeReference<ArrayList<WorkSheet>> {
            C06161() {
            }
        }

        C06177() {
        }
        //MyTaskListOnlyCallback
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (EmpHomeActivity.this.progressHelper != null) {
                EmpHomeActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    Log.e(EmpHomeActivity.this.TAG, "Footerviewscount() :" + EmpHomeActivity.this.stickyList.getFooterViewsCount());
                    EmpHomeActivity.this.worksheetList.clear();
                    ArrayList<WorkSheet> workSheets = (ArrayList) new ObjectMapper().readValue(((JSONObject) res).getJSONArray("work_details").toString(), new C06161());
                    String lastProjectID = null;
                    EmpHomeActivity.this.helper = new UserHelper(EmpHomeActivity.this.context);

                    if(workSheets.size() > 0){
                        WorkSheet last = workSheets.get(0);
                        String strProject = last.getProject_Number() + " | " + last.getProjectName();
                        Preference.setSharedPref("LastWorkedProject", strProject);

                        EmpHomeActivity.this.atvproject.setText(strProject);
                        lastProjectID = last.getProject_id();
                        EmpHomeActivity.this.helper.apigetGraphDetails(lastProjectID, EmpHomeActivity.this.graphDetailActionCallBack);

                    }

                    if (lastProjectID == null) {
                        EmpHomeActivity.this.tvnoProjectAvailable.setVisibility(View.VISIBLE);
                        EmpHomeActivity.this.viewPager.setVisibility(View.GONE);
                        return;
                    }
                    if (EmpHomeActivity.this.isFromSplash) {
                        EmpHomeActivity.this.isFromSplash = false;
                        EmpHomeActivity.this.goRegisterScreen();
                    }
                    EmpHomeActivity.this.myPagerAdapter.notifyDataSetChanged();
                    EmpHomeActivity.this.worksheetList.addAll(workSheets);
                    if (EmpHomeActivity.this.worksheetList == null || EmpHomeActivity.this.worksheetList.size() <= 0) {
                        if (EmpHomeActivity.this.footerView == null) {
                            EmpHomeActivity.this.footerView = EmpHomeActivity.this.getLayoutInflater().inflate(R.layout.headerhome, null);
                            EmpHomeActivity.this.stickyList.addFooterView(EmpHomeActivity.this.footerView);
                        }
                        Log.e(EmpHomeActivity.this.TAG, "Footerviewscount() :" + EmpHomeActivity.this.stickyList.getFooterViewsCount());
                        return;
                    }

                    try {
                        EmpHomeActivity.this.mAdapter.notifyDataSetChanged();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Log.e(EmpHomeActivity.this.TAG, "onActionComplete > Exception: " + e2.toString());
                    return;
                }
            }
            if (EmpHomeActivity.this.footerView == null) {
                EmpHomeActivity.this.footerView = EmpHomeActivity.this.getLayoutInflater().inflate(R.layout.headerhome, null);
                EmpHomeActivity.this.stickyList.addFooterView(EmpHomeActivity.this.footerView);
            }
            Log.e(EmpHomeActivity.this.TAG, "Footerviewscount() :" + EmpHomeActivity.this.stickyList.getFooterViewsCount());
            AlertDialogHelper.getNotificatonAlert(EmpHomeActivity.this.context, EmpHomeActivity.this.getString(R.string.app_name), res.toString());
            Toast.makeText(EmpHomeActivity.this.context, EmpHomeActivity.this.getResources().getString(R.string.response_error_msg), Toast.LENGTH_SHORT).show();
        }
    }

    class C06208 implements ActionCallback {

        class C06181 extends TypeReference<ArrayList<BarGraph>> {
            C06181() {
            }
        }

        class C06192 extends TypeReference<ArrayList<LineGraph>> {
            C06192() {
            }
        }

        C06208() {
        }
        //graphDetailActionCallBack
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (EmpHomeActivity.this.progressHelper != null) {
                EmpHomeActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    JSONObject jsonObject = (JSONObject) res;
                    JSONArray jArrWorsheets = jsonObject.getJSONArray("bar_graph");
                    EmpHomeActivity.this.myPagerAdapter = new MyPagerAdapter(EmpHomeActivity.this.context);
                    EmpHomeActivity.this.viewPager.setAdapter(EmpHomeActivity.this.myPagerAdapter);
                    ObjectMapper mapper = new ObjectMapper();
                    EmpHomeActivity.this.objBarGraph = (ArrayList) mapper.readValue(jArrWorsheets.toString(), new C06181());
                    EmpHomeActivity.this.setDataOfBarGraph(EmpHomeActivity.this.objBarGraph);
                    EmpHomeActivity.this.myPagerAdapter.notifyDataSetChanged();
                    //jin
//                    JSONArray jArrlineGraph = jsonObject.getJSONArray("line_graph");
//                    ObjectMapper mapper1 = new ObjectMapper();
//                    EmpHomeActivity.this.objLineGraph = (ArrayList) mapper1.readValue(jArrlineGraph.toString(), new C06192());
//                    EmpHomeActivity.this.displayLineChart1(EmpHomeActivity.this.objLineGraph);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(EmpHomeActivity.this.TAG, "onActionComplete > Exception: " + e.toString());
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(EmpHomeActivity.this.context, EmpHomeActivity.this.getString(R.string.app_name), res.toString());
        }
    }

    class C06219 implements ActionCallback {
        C06219() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (EmpHomeActivity.this.progressHelper != null) {
                EmpHomeActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                Intent i = new Intent("any string");
                i.setClass(EmpHomeActivity.this.context, MyBroadcastReceiver.class);
                EmpHomeActivity.this.sendBroadcast(i);
            }
        }
    }

    public class MyBaseAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        Context context;
        LayoutInflater inflater;
        ArrayList<WorkSheet> myList;

        class HeaderViewHolder {
            TextView text;

            HeaderViewHolder() {
            }
        }

        private class MyViewHolder {

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
            }
        }

        public MyBaseAdapter(Context context, ArrayList<WorkSheet> myList) {
            this.myList = myList;
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }
        @Override
        public int getCount() {
            return this.myList.size();
        }
        @Override
        public WorkSheet getItem(int position) {
            return (WorkSheet) this.myList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.home_listview_items, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }
            final WorkSheet currentworksheet = getItem(position);
            mViewHolder.txtid.setText((position + 1) + ".");
            mViewHolder.txtpname.setText(currentworksheet.getProjectName());
            if (currentworksheet.getTotalWorkTime() == null) {
                mViewHolder.txthrs.setText("1");
            } else {
                mViewHolder.txthrs.setText(EmpHomeActivity.this.displayHoursfromMinutes1(Integer.parseInt(currentworksheet.getTotalWorkTime())));
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
                    new UserHelper(MyBaseAdapter.this.context).apiGetWorksheetDetails(currentworksheet, EmpHomeActivity.this.WorksheetDetailsCallBack);
                }
            });
            return convertView;
        }
        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                convertView = this.inflater.inflate(R.layout.headerhome, parent, false);
                if (EmpHomeActivity.this.footerView != null) {
                    EmpHomeActivity.this.stickyList.removeFooterView(EmpHomeActivity.this.footerView);
                    EmpHomeActivity.this.footerView = convertView;
                }
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            return 0;
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        int NumberOfPages = 2;
        private Context context;

        MyPagerAdapter(Context context) {
            this.context = context;
            EmpHomeActivity.this.barChart = new BarChart(context);
            EmpHomeActivity.this.lineChart = new LineChart(context);
        }

        public int getCount() {
            return this.NumberOfPages;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            LinearLayout layout = new LinearLayout(EmpHomeActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LayoutParams layoutParams = new LayoutParams(-1, -1);
            layout.setBackgroundColor(EmpHomeActivity.this.getResources().getColor(R.color.White));
            layout.setPadding(20, 0, 20, 20);
            layout.setLayoutParams(layoutParams);
            if (position == 0) {
                EmpHomeActivity.this.barChart.setLayoutParams(layoutParams);
//                EmpHomeActivity.this.barChart.setNoDataTextColor(EmpHomeActivity.this.getResources().getColor(R.color.White));
                layout.addView(EmpHomeActivity.this.barChart);
            } else {
                EmpHomeActivity.this.lineChart.setLayoutParams(layoutParams);
//                EmpHomeActivity.this.lineChart.setNoDataTextColor(EmpHomeActivity.this.getResources().getColor(R.color.White));
                EmpHomeActivity.this.noDatainLinechart = new TextView(this.context);
                EmpHomeActivity.this.noDatainLinechart.setText(EmpHomeActivity.this.getResources().getString(R.string.no_linechart_data));
                EmpHomeActivity.this.noDatainLinechart.setVisibility(View.GONE);
                EmpHomeActivity.this.noDatainLinechart.setGravity(17);
                EmpHomeActivity.this.noDatainLinechart.setLayoutParams(layoutParams);
                layout.addView(EmpHomeActivity.this.noDatainLinechart);
                layout.addView(EmpHomeActivity.this.lineChart);
            }
            container.addView(layout);
            return layout;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment1 extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((EmpHomeActivity)getActivity()).populateSetDate1(yy, mm + 1, dd);
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((EmpHomeActivity)getActivity()).populateSetDate(yy, mm + 1, dd);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activity_e);
        sethomeWather();
        if (VERSION.SDK_INT > 19) {
            setBackgroundService();
        }
        AppUtils.setStatusbarColor(this);
        this.header.setTitle(getString(R.string.title_activity_home_activity_e));
        this.header.hideRightBtn();
        this.header.setRightBtnImage(R.drawable.register_time);
        final String role = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
        this.header.setRightBtnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(EmpHomeActivity.this.context, RegisterTimeActivity.class);
                intent.setFlags(131072);
                intent.putExtra("isFromMenu", true);
                if (!role.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                    intent.putExtra("is_from_mng_jobber", true);
                }
                EmpHomeActivity.this.context.startActivity(intent);
            }
        });
        this.isFromSplash = getIntent().getBooleanExtra("inFromSplash", false);
        addStickHeader();
        getViews();
        this.helper = new UserHelper(this.context);
        this.progressHelper.showDialog("Loading..");
        this.helper.apiGetMasterTablesDetail(this.masterlistcallback);
        startService(new Intent(this, RegistrationIntentService.class));
        this.myPagerAdapter = new MyPagerAdapter(this.context);
        this.viewPager.setAdapter(this.myPagerAdapter);
        this.viewPager.setOffscreenPageLimit(1);
        this.indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        this.indicator.setViewPager(this.viewPager);
        this.viewPager.setOnPageChangeListener(this.myOnPageChangeListener);
        this.viewPager.setPageTransformer(false, new C06092());
        this.viewPager.setPageTransformer(false, new C06103());
    }

    private void sethomeWather() {
        this.mHomeWatcher = new HomeWatcher(this.context);
        this.mHomeWatcher.setOnHomePressedListener(new C06114());
        this.mHomeWatcher.startWatch();
    }

    private void addStickHeader() {
        this.stickyList = (StickyListHeadersListView) findViewById(R.id.list);
        this.stickyList.setOnItemClickListener(this);
        this.stickyList.setOnHeaderClickListener(this);
        this.stickyList.setOnStickyHeaderChangedListener(this);
        this.stickyList.setOnStickyHeaderOffsetChangedListener(this);
        this.stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.list_header, null));
        this.stickyList.setDrawingListUnderStickyHeader(true);
        this.stickyList.setAreHeadersSticky(true);

        try {
            this.mAdapter = new MyBaseAdapter(this.context, this.worksheetList);
            this.stickyList.setAdapter(this.mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.stickyList.setStickyHeaderTopOffset(-10);
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
                fieldlist.add(((String) ((HashMap) list.get(i)).get("project_number")) + " | " + ((String) ((HashMap) list.get(i)).get(list_name)));
            }
        }
        return fieldlist;
    }

    private void getProjectListAdapter(List item) {
        this.pnameList = item;
        this.myAutoCompleteAdapter = new ArrayAdapter(this, R.layout.auto_complete_textview, this.pnameList);
        this.atvproject.setAdapter(this.myAutoCompleteAdapter);
    }

    private void getViews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.atvproject = (EtechEditAutoCompleteTextRobotoRegular) findViewById(R.id.atvproject_home);
        this.atvproject.init(this.context, this);
        this.viewPager = (ViewPager) findViewById(R.id.myviewpager);
        this.tvnoProjectAvailable = (ETechTextView) findViewById(R.id.tvnoProjectAvailable);
        this.atvproject.getViewTreeObserver().addOnGlobalLayoutListener(new C06125());
    }

    public void selectDate(View view) {
        new SelectDateFragment().show(getSupportFragmentManager(), "DatePicker");
    }

    public void selectDate1(View view) {
        new SelectDateFragment1().show(getSupportFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        this.edtstartdt.setText(month + "/" + day + "/" + year);
    }

    public void populateSetDate1(int year, int month, int day) {
        this.edtenddate.setText(month + "/" + day + "/" + year);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
    }

    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
    }

    @TargetApi(11)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (this.fadeHeader && VERSION.SDK_INT >= 11) {
            header.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT - (((float) offset) / ((float) header.getMeasuredHeight())));
        }
    }

    @TargetApi(11)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        for (int i = 0; i < this.dotsCount; i++) {
            this.dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        this.dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
        if (position + 1 == this.dotsCount) {
            this.btnNext.setVisibility(View.GONE);
            this.btnFinish.setVisibility(View.VISIBLE);
            return;
        }
        this.btnNext.setVisibility(View.VISIBLE);
        this.btnFinish.setVisibility(View.GONE);
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.isBackPressed = true;
    }
//jin
    /*
    private void displayLineChart1(ArrayList<LineGraph> lineGraphs) {
        int i;
        this.lineChart.setNoDataText(getResources().getString(R.string.no_chart_data_avilable));
        this.lineChart.setNoDataTextDescription("...");
        this.lineChart.invalidate();
        this.lineChart.setPinchZoom(false);
        this.lineChart.setTouchEnabled(false);
        this.lineChart.setMinimumHeight(Opcodes.FCMPG);
        this.lineChart.setDrawGridBackground(false);
        this.lineChart.setDrawBorders(false);
        this.lineChart.getAxisRight().setEnabled(false);
        this.lineChart.getXAxis().setDrawAxisLine(false);
        this.lineChart.getXAxis().setDrawGridLines(false);
        XAxis xAxis = this.lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setPosition(XAxisPosition.BOTTOM);
        YAxis yAxis = this.lineChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        yAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        List xVals = new ArrayList();
        if (lineGraphs == null || lineGraphs.size() <= 0) {
            this.lineChart.setVisibility(View.GONE);
            this.noDatainLinechart.setVisibility(View.VISIBLE);
        } else {
            for (i = 0; i < lineGraphs.size(); i++) {
                if (((LineGraph) lineGraphs.get(i)).getLabel() != null) {
                    if (lineGraphs.size() <= 1) {
                        this.lineChart.setVisibility(View.GONE);
                        this.noDatainLinechart.setVisibility(View.VISIBLE);
                        break;
                    }
                    this.noDatainLinechart.setVisibility(View.GONE);
                    xVals.add(((LineGraph) lineGraphs.get(i)).getLabel());
                }
            }
        }
        ArrayList<Entry> yVals1 = new ArrayList();
        ArrayList<Entry> yVals2 = new ArrayList();
        for (i = 0; i < lineGraphs.size(); i++) {
            yVals1.add(new Entry((float) Integer.parseInt(((LineGraph) lineGraphs.get(i)).getLine1Value()), i));
            yVals2.add(new Entry((float) Integer.parseInt(((LineGraph) lineGraphs.get(i)).getLine2Value()), i));
        }
        if (this.lineChart.getData() == null || ((LineData) this.lineChart.getData()).getDataSetCount() <= 0) {
            LineDataSet set1 = new LineDataSet(yVals1, "DataSet 1");
            set1.setAxisDependency(AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.green));
            set1.setCircleColor(-1);
            set1.setLineWidth(2f);
            set1.setCircleRadius(0.0f);
            set1.setDrawCircleHole(false);
            set1.setLabel(getString(R.string.line_data_lable));
            LineDataSet set2 = new LineDataSet(yVals2, "DataSet 2");
            set2.setAxisDependency(AxisDependency.LEFT);
            set2.setColor(getResources().getColor(R.color.Dark_blue));
            set2.setCircleColor(-1);
            set2.setLineWidth(2f);
            set2.setCircleRadius(0.0f);
            set2.setDrawCircleHole(false);
            set1.setLabel(getString(R.string.line2_data_lable));
            List dataSets = new ArrayList();
            dataSets.add(set1);
            dataSets.add(set2);
            LineData data = new LineData(xVals, dataSets);
            data.setValueTextColor(-1);
            data.setValueTextSize(11.0f);
            data.setDrawValues(false);
            this.lineChart.setBackgroundColor(getResources().getColor(R.color.White));
            this.lineChart.setData(data);
            this.lineChart.setDescriptionColor(-16711936);
            this.lineChart.setBorderWidth(0.0f);
            this.lineChart.setDrawGridBackground(false);
            this.lineChart.getLegend().setEnabled(false);
            this.lineChart.setHighlightFullBarEnabled(false);
            this.lineChart.invalidate();
            this.myPagerAdapter.notifyDataSetChanged();
            return;
        }
        set2 = (LineDataSet) ((LineData) this.lineChart.getData()).getDataSetByIndex(1);
        ((LineDataSet) ((LineData) this.lineChart.getData()).getDataSetByIndex(0)).setYVals(yVals1);
        set2.setYVals(yVals2);
        ((LineData) this.lineChart.getData()).setXVals(xVals);
        ((LineData) this.lineChart.getData()).notifyDataChanged();
        this.lineChart.notifyDataSetChanged();
    }
*/
    private void setDataOfBarGraph(ArrayList<BarGraph> objBarGraph) {
        try {
            int i;
            this.barChart.setNoDataText(getResources().getString(R.string.no_chart_data_avilable));
            this.barChart.setNoDataTextDescription("...");
            this.barChart.invalidate();
            this.barChart.setDescription("");
            this.barChart.setDrawHighlightArrow(false);
            this.barChart.setTouchEnabled(false);
            this.barChart.setDrawGridBackground(false);
            this.barChart.setDrawBarShadow(false);
            this.barChart.setDrawValueAboveBar(true);
            this.barChart.getAxisRight().setEnabled(false);
            this.barChart.getXAxis().setDrawAxisLine(true);
            this.barChart.getXAxis().setDrawGridLines(false);
            this.barChart.setGridBackgroundColor(getResources().getColor(R.color.White));
            this.barChart.getLegend().setEnabled(false);
            XAxis xAxis = this.barChart.getXAxis();
            xAxis.setDrawGridLines(false);
            xAxis.setTypeface(Typeface.DEFAULT_BOLD);
            xAxis.setTextColor(getResources().getColor(R.color.grey));
            xAxis.setAxisLineColor(getResources().getColor(R.color.grey));
            xAxis.setPosition(XAxisPosition.BOTTOM);
            xAxis.setTextSize(9.0f);
            YAxis y = this.barChart.getAxisLeft();
            y.setDrawTopYLabelEntry(true);
            y.setDrawLabels(false);
            y.setPosition(YAxisLabelPosition.INSIDE_CHART);
            y.setDrawGridLines(false);
            y.setAxisMinValue(0.0f);
            ArrayList<String> xVals = new ArrayList();
            List labels = new ArrayList();
            if (objBarGraph.size() > 0 && objBarGraph != null) {
                for (i = 0; i < objBarGraph.size(); i++) {
                    labels.add(((BarGraph) objBarGraph.get(i)).getLabel());
                }
            }
            ArrayList<BarEntry> yVals1 = new ArrayList();
            ValueFormatter custom = new MyBarValueFormatter(this.context);
            for (i = 0; i < objBarGraph.size(); i++) {
                float val1 = (float) displayHoursfromMinutes(Integer.parseInt(((BarGraph) objBarGraph.get(i)).getExpectedValue()));
                float val2 = (float) displayHoursfromMinutes(Integer.parseInt(((BarGraph) objBarGraph.get(i)).getDeviationValue()));
                yVals1.add(new BarEntry(new float[]{val1, val2}, i));
            }
            if (this.barChart.getData() == null || ((BarData) this.barChart.getData()).getDataSetCount() <= 0) {
                BarDataSet set1 = new BarDataSet(yVals1, "");
                set1.setColors(getColors1(objBarGraph));
                List<Integer> color = new ArrayList();
                color.add(Integer.valueOf(getResources().getColor(R.color.green)));
                color.add(Integer.valueOf(getResources().getColor(R.color.yellow)));
                color.add(Integer.valueOf(getResources().getColor(R.color.Dark_blue)));
                List dataSets = new ArrayList();
                dataSets.add(set1);
                BarData data1 = new BarData(labels, dataSets);
                data1.setValueTypeface(Typeface.DEFAULT_BOLD);
//                data1.setValueTextColors(color);
                data1.setValueFormatter(custom);
                data1.setValueTextSize(11.0f);
                this.barChart.getAxisLeft().setEnabled(false);
                this.barChart.setData(data1);
            } else {
                ((BarDataSet) ((BarData) this.barChart.getData()).getDataSetByIndex(0)).getYVals();
                this.barChart.notifyDataSetChanged();
            }
            this.barChart.invalidate();
            this.myPagerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int displayHoursfromMinutes(int minutes) {
        int workHrs = 0;
        try {
            workHrs = minutes / 60;
            int workMn = minutes % 60;
            if (workHrs < 0) {
                workHrs = 0;
            }
            if (workMn < 0) {
            }
        } catch (Exception e) {
        }
        return workHrs;
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
        return String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + getResources().getString(R.string.txt_h) + " " + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + getResources().getString(R.string.txt_m);
    }

    private int[] getColors1(ArrayList<BarGraph> barGraphs) {
        int i;
        int[] colors = new int[(barGraphs.size() * 2)];
        int i2 = 0;
        int j = 0;
        while (i2 < barGraphs.size()) {
            i = j + 1;
            try {
                colors[j] = Color.parseColor("#" + ((BarGraph) barGraphs.get(i2)).getDeviationValueColor());
                j = i + 1;
            } catch (Exception e) {
                Exception e2 = e;
            }
            try {
                colors[i] = Color.parseColor("#" + ((BarGraph) barGraphs.get(i2)).getExpectedValueColor());
                i2++;
            } catch (Exception e3) {
//                e2 = e3;
                i = j;
            }
        }
        i = j;
        return colors;
//        e2.printStackTrace();
//        return colors;
    }

    protected void onResume() {
        super.onResume();
        try {
            Log.d(this.TAG, "onResume() Before Watcher Start");
            this.mHomeWatcher.startWatch();
            Log.d(this.TAG, "onResume() After Watcher Start");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.d(this.TAG, "onResume() Finally Block");
        }
        this.isfromResume = true;
        this.helper = new UserHelper(this.context);
        this.helper.apiGetUnReadCount(this.apiCallBackUnReadCount);
        if (this.isfromPause && this.ismasterApisuccess) {
            goRegisterScreen();
            this.isfromPause = false;
        } else if (this.isfromPause) {
            this.progressHelper.showDialog("Loading..");
            this.helper = new UserHelper(this.context);
            this.helper.apiGetMasterTablesDetail(this.masterlistcallback);
        }
    }

    private void goRegisterScreen() {
        String role = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
        Intent intent = new Intent();
        intent.setClass(this.context, RegisterTimeActivity.class);
        intent.setFlags(131072);
        intent.putExtra("isFromMenu", true);
        if (!role.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
            intent.putExtra("is_from_mng_jobber", true);
        }
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
        Log.d(this.TAG, "onPAuse()::::isAppIsInBackground : " + isAppIsInBackground(this.context));
        if (isAppIsInBackground(this.context)) {
            this.isfromPause = true;
        } else {
            this.mHomeWatcher.stopWatch();
        }
    }

    public void onOkClick(int titleId1, int btnresId) {
        if (btnresId == 1) {
            this.progressHelper.showDialog("Loading..");
            this.helper = new UserHelper(this.context);
            this.helper.apiGetMasterTablesDetail(this.masterlistcallback);
        } else if (btnresId == 2) {
            finish();
        }
    }

    public void onItemOptionClick(AdapterView<?> adapterView, View view, int position, long id) {
        AppUtils.hideKeyBoard(EmpHomeActivity.this);
        if (view.getId() == R.id.atvproject_home) {
            String pName = this.atvproject.getText().toString();
            String qry = "select project_id from tblproject where project_name='" + pName.substring(pName.indexOf("|") + 2) + "'";
            this.dbHelper = new DatabaseHelper(this.context);
            this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
            HashMap<String, String> projectid = this.dbHelper.selSingleRecordFromDB(qry, null);
            if (projectid != null) {
                String pId = (String) projectid.get("project_id");
                this.helper = new UserHelper(this.context);
                this.helper.apigetGraphDetails(pId, this.graphDetailActionCallBack);
                this.myPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        if (VERSION.SDK_INT > 20) {
            for (RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
                if (processInfo.importance == 100) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
            return isInBackground;
        }
        try {
            if (((RunningTaskInfo) am.getRunningTasks(1).get(0)).topActivity.getPackageName().equals(context.getPackageName())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @TargetApi(21)
    private void setBackgroundService() {
        if (kJobId > 0 && this.jobScheduler != null) {
            this.jobScheduler.cancel(kJobId);
        }
        Builder builder = new Builder(kJobId, new ComponentName(this, NotificationJobService.class));
        builder.setMinimumLatency(3000);
        builder.setOverrideDeadline(30000);
        builder.setPersisted(true);
        builder.setRequiredNetworkType(2);
        builder.setRequiresDeviceIdle(true);
        builder.setRequiresCharging(false);
        this.jobScheduler = (JobScheduler) getApplication().getSystemService("jobscheduler");
        this.jobScheduler.schedule(builder.build());
    }
}

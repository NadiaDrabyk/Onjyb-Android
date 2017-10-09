package com.onjyb.layout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.EtechEditAutoCompleteTextRobotoRegular;
import com.onjyb.customview.Header;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.helper.MyBroadcastReceiver;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.time.DateUtils;

public class BaseDrawerActivity extends FragmentActivity {
    private ActionCallback apiLogoutCallback = new C05922();
    AutoCompleteTextView autoBranch;
    AutoCompleteTextView autoEmployees;
    Spinner autoLeaveType;
    EtechEditAutoCompleteTextRobotoRegular autoProject;
    AutoCompleteTextView autoService;
    Spinner autostatusList;
    Button btnAboutApp;
    Button btnApplyLeave;
    Button btnGroupChat;
    Button btnHome;
    Button btnLeave;
    Button btnLogout;
    Button btncancel;
    Button btnclr;
    Button btnmanager_minejobber;
    Button btnregister;
    Button btnsearch;
    ImageView comp_logo;
    Context context = this;
    DatabaseHelper dbHelper;
    ETechTextView endDate;
    private boolean flagLogout = false;
    protected DrawerLayout fullLayout;
    protected Header header;
    ImageView imgProfile;
    ImageView imgProfilePic;
    protected LinearLayout linMenuViewLeft;
    protected LinearLayout linMenuViewRight;
    LinearLayout llAboutApp;
    LinearLayout llApplyLeave;
    LinearLayout llMyLeave;
    LinearLayout llMyTask;
    LinearLayout llRegisterTime;
    LinearLayout llStatistick;
    LinearLayout llUnreadChatMsg;
    LinearLayout llUnreadLeave;
    LinearLayout llUnreadMngTask;
    LinearLayout llUnreadTask;
    LinearLayout llgroupChat;
    LinearLayout llhome;
    LinearLayout llmanager_llMyTask;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    OnClickListener menuOptionClick = new C05911();
    Button minejobber;
    private int newWidth;
    ProgressHelper progressHelper;
    MyBroadcastReceiver receiver = new C05977();
    RelativeLayout rlLeaveType;
    RelativeLayout rlStatus;
    String startDate = "";
    Button statistik;
    ETechTextView strDate;
    ETechTextView tvEmailid;
    ETechTextView tvStatusName;
    ETechTextView tvUnreadLeaveCount;
    ETechTextView tvUnreadMngTaskCount;
    ETechTextView tvUnreadMsgsCount;
    ETechTextView tvUnreadTaskCount;
    ETechTextView tvUserName;
    ETechTextView txtBranchlbl;
    TextView txtBuildName;
    ETechTextView txtEmployee;
    ETechTextView txtServicelbl;
    TextView txtVersion;
    ETechTextView txtleaveType;
    View vAboutApp;
    View vApplyLeave;
    View vManagerMytask;
    View vMyLeave;
    View vMyTask;
    View vRegister;
    View vStatistick;
    View vgroupChat;
    View vhome;

    class C05911 implements OnClickListener {

        class C05891 implements OnClickListener {
            C05891() {
            }

            public void onClick(View v) {
                UserHelper helper = new UserHelper(BaseDrawerActivity.this.context);
                BaseDrawerActivity.this.progressHelper.showDialog("Loading..");
                helper.apiLogOut(BaseDrawerActivity.this.apiLogoutCallback);
            }
        }

        class C05902 implements OnClickListener {
            C05902() {
            }

            public void onClick(View v) {
            }
        }

        C05911() {
        }

        public void onClick(View v) {
            String role = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, "");
            Intent intent;
            switch (v.getId()) {
                case R.id.btnProfileEdit:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, ProfileActivity.class);
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.btnhome:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, EmpHomeActivity.class);
                    intent.setFlags(67108864);
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.manager_minejobber:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, AllStatusTaskActivity.class);
                    intent.setFlags(131072);
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.minejobber:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, AllStatusTaskActivity.class);
                    intent.setFlags(131072);
                    if (!role.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                        intent.putExtra("is_from_mng_jobber", true);
                    }
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.registertime:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, RegisterTimeActivity.class);
                    intent.setFlags(131072);
                    intent.putExtra("isFromMenu", true);
                    if (!role.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                        intent.putExtra("is_from_mng_jobber", true);
                    }
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.btnLeave:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, LeaveLisingActivity.class);
                    intent.setFlags(131072);
                    if (!role.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                        intent.putExtra("is_from_mng_leave", true);
                    }
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.btnApplyLeave:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, LeaveFormActivity.class);
                    intent.setFlags(131072);
                    intent.putExtra("isFromMenu", true);
                    if (!role.equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
                        intent.putExtra("is_from_mng_leave", true);
                    }
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.statistik:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, StatistikActivity.class);
                    intent.setFlags(131072);
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.btnGChat:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, GroupChatActivity.class);
                    intent.setFlags(131072);
                    Preference.setSharedPref(Constants.PREF_UNREAD_MESSAGE_COUNT, "0");
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.btnAboutApp:
                    intent = new Intent();
                    intent.setClass(BaseDrawerActivity.this.context, AboutAppActivity.class);
                    intent.setFlags(131072);
                    BaseDrawerActivity.this.startActivity(intent);
                    break;
                case R.id.logout:
                    AlertDialogHelper.getConfirmationAlert(BaseDrawerActivity.this.context, BaseDrawerActivity.this.getString(R.string.app_name), new C05891(), new C05902(), BaseDrawerActivity.this.getResources().getString(R.string.are_you_sure_want_logout), 0, 0, true, BaseDrawerActivity.this.getResources().getString(R.string.txt_yes), BaseDrawerActivity.this.getResources().getString(R.string.txt_no), -1, -1);
                    break;
            }
            BaseDrawerActivity.this.mDrawerLayout.closeDrawer(BaseDrawerActivity.this.linMenuViewLeft);
        }
    }

    class C05922 implements ActionCallback {
        C05922() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (BaseDrawerActivity.this.progressHelper != null) {
                BaseDrawerActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                BaseDrawerActivity.this.btnLogout.setText(BaseDrawerActivity.this.getString(R.string.menu_login));
                Preference.clearSharedPrefFile();
                BaseDrawerActivity.this.tvUnreadTaskCount.setText("0");
                BaseDrawerActivity.this.tvUnreadMsgsCount.setText("0");
                BaseDrawerActivity.this.tvUnreadLeaveCount.setText("0");
                BaseDrawerActivity.this.tvUnreadMngTaskCount.setText("0");
                BaseDrawerActivity.this.dbHelper = new DatabaseHelper(BaseDrawerActivity.this.context);
                BaseDrawerActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(BaseDrawerActivity.this.context);
                BaseDrawerActivity.this.dbHelper.deleteDatabase();
                Intent intent = new Intent();
                intent = new Intent(BaseDrawerActivity.this.getBaseContext(), LoginActivity.class);
                intent.setFlags(67108864);
                BaseDrawerActivity.this.startActivity(intent);
                BaseDrawerActivity.this.finishAffinity();
                return;
            }
            AlertDialogHelper.getNotificatonAlert(BaseDrawerActivity.this.context, BaseDrawerActivity.this.getString(R.string.app_name), res.toString());
        }
    }

    class C05933 implements OnClickListener {
        C05933() {
        }

        public void onClick(View v) {
            BaseDrawerActivity.this.setUnReadCount();
            BaseDrawerActivity.this.mDrawerLayout.openDrawer(BaseDrawerActivity.this.linMenuViewLeft);
        }
    }

    class C05944 implements OnClickListener {
        C05944() {
        }

        public void onClick(View v) {
            BaseDrawerActivity.this.mDrawerLayout.openDrawer(BaseDrawerActivity.this.linMenuViewRight);
        }
    }

    class C05977 extends MyBroadcastReceiver {
        C05977() {
        }

        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            BaseDrawerActivity.this.setUnReadCount();
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment1 extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((BaseDrawerActivity)getActivity()).populateSetDate1(yy, mm + 1, dd);
        }
    }

    @SuppressLint({"ValidFragment"})
    public static class SelectDateFragment extends DialogFragment implements OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            return new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            ((BaseDrawerActivity)getActivity()).populateSetDate(yy, mm + 1, dd);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.newWidth = (AppUtils.getDeviceWidth(this.context) * 4) / 5;
    }

    public void setRightButtonImageRecourceId(int rightBtnImage) {
        this.header.setRightBtnImage(rightBtnImage);
    }

    public void setLeftBtnClickListner(OnClickListener btnLeftClickListener) {
        this.header.setLeftBtnClickListener(btnLeftClickListener);
    }

    public void setRightBtnClickListner(OnClickListener btnRightClickListener) {
        this.header.setRightBtnClickListener(btnRightClickListener);
    }

    public void setLeftButtonImageRecourceId(int leftBtnImage) {
        this.header.setLeftBtnImage(leftBtnImage);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTitle(String title) {
        this.header.setTitle(title);
    }

    public void setBackGroundColor(int resid) {
        this.header.setBackGroundColor(resid);
    }

    public void setContentView(int layoutResID) {
        setContentView(layoutResID, false);
        AppUtils.setStatusbarColor(this);
    }

    public void setContentView(int layoutResID, boolean hideLeftMenu) {
        this.fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
        LinearLayout actContent = (LinearLayout) this.fullLayout.findViewById(R.id.content);
        this.mDrawerLayout = (DrawerLayout) this.fullLayout.findViewById(R.id.drawer_layout);
        this.linMenuViewLeft = (LinearLayout) this.fullLayout.findViewById(R.id.nav_drawer);
        this.linMenuViewRight = (LinearLayout) this.fullLayout.findViewById(R.id.nav_drawer_right);
        this.linMenuViewLeft.getLayoutParams().width = this.newWidth;
        this.mDrawerLayout.setDrawerLockMode(1, this.linMenuViewRight);
        if (hideLeftMenu) {
            this.mDrawerLayout.setDrawerLockMode(1, this.linMenuViewLeft);
        }
        this.header = (Header) this.fullLayout.findViewById(R.id.header1);
        this.header.init(this, null, null, null, Boolean.valueOf(false));
        setButtons();
        String str = "";
        String username = Preference.getSharedPref(Constants.PREF_USER_FNAME, "");
        String userlastname = Preference.getSharedPref(Constants.PREF_USER_LNAME, "");
        String emailOrMobile = Preference.getSharedPref(Constants.PREF_MOBILE, "");
        if (emailOrMobile == null || emailOrMobile == "") {
            emailOrMobile = Preference.getSharedPref(Constants.PREF_USER_EMAIL, "");
        }
        if (username != null && !username.equalsIgnoreCase("") && userlastname != null && !userlastname.equalsIgnoreCase("") && !userlastname.equalsIgnoreCase("null")) {
            str = username + " " + userlastname;
        } else if (username != null && !username.equalsIgnoreCase("") && !username.equalsIgnoreCase("null")) {
            str = username;
        } else if (!(userlastname == null || userlastname.equalsIgnoreCase("") || userlastname.equalsIgnoreCase("null"))) {
            str = userlastname;
        }
        this.tvUserName.setText(AppUtils.UpperCaseWords(str));
        this.tvEmailid.setText(emailOrMobile);
        this.header.setLeftBtnClickListener(new C05933());
        this.header.setRightBtnClickListener(new C05944());
        this.mDrawerLayout.closeDrawer(this.linMenuViewRight);
        getLayoutInflater().inflate(layoutResID, actContent, true);
        setContentView(this.fullLayout);
        AppUtils.setStatusbarColor(this);
    }

    public void setRightSlideMenu(boolean is_manager) {
        this.mDrawerLayout.setDrawerLockMode(0, this.linMenuViewRight);
        if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && !is_manager) {
            this.autoEmployees.setVisibility(View.VISIBLE);
            this.txtEmployee.setVisibility(View.VISIBLE);
        }
    }

    public void setLeaveFilterMenu(boolean is_from_mng) {
        this.mDrawerLayout.setDrawerLockMode(0, this.linMenuViewRight);
        this.txtBranchlbl.setVisibility(View.GONE);
        this.txtServicelbl.setVisibility(View.GONE);
        this.autoService.setVisibility(View.GONE);
        this.autoBranch.setVisibility(View.GONE);
        this.rlLeaveType.setVisibility(View.VISIBLE);
        this.txtleaveType.setVisibility(View.VISIBLE);
        this.rlStatus.setVisibility(View.GONE);
        this.tvStatusName.setVisibility(View.GONE);
        if (!Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE) && !is_from_mng) {
            this.autoEmployees.setVisibility(View.VISIBLE);
            this.txtEmployee.setVisibility(View.VISIBLE);
        }
    }

    public boolean isSidemenuOpen() {
        if (this.mDrawerLayout.isDrawerOpen(3) || this.mDrawerLayout.isDrawerOpen(5)) {
            return true;
        }
        return false;
    }

    private void setButtons() {
        this.btnmanager_minejobber = (Button) this.fullLayout.findViewById(R.id.manager_minejobber);
        this.llmanager_llMyTask = (LinearLayout) this.fullLayout.findViewById(R.id.ll_manager_my_job);
        this.vManagerMytask = this.fullLayout.findViewById(R.id.vmanagerMyTask);
        this.progressHelper = new ProgressHelper(this.context);
        this.btnregister = (Button) this.fullLayout.findViewById(R.id.registertime);
        this.minejobber = (Button) this.fullLayout.findViewById(R.id.minejobber);
        this.statistik = (Button) this.fullLayout.findViewById(R.id.statistik);
        this.btnLogout = (Button) this.fullLayout.findViewById(R.id.logout);
        this.btnAboutApp = (Button) this.fullLayout.findViewById(R.id.btnAboutApp);
        this.btnGroupChat = (Button) this.fullLayout.findViewById(R.id.btnGChat);
        this.btnLeave = (Button) this.fullLayout.findViewById(R.id.btnLeave);
        this.btnApplyLeave = (Button) this.fullLayout.findViewById(R.id.btnApplyLeave);
        this.btnHome = (Button) this.fullLayout.findViewById(R.id.btnhome);
        this.llRegisterTime = (LinearLayout) this.fullLayout.findViewById(R.id.ll_registertime);
        this.llMyTask = (LinearLayout) this.fullLayout.findViewById(R.id.ll_myTask);
        this.llStatistick = (LinearLayout) this.fullLayout.findViewById(R.id.ll_Statistik);
        this.llAboutApp = (LinearLayout) this.fullLayout.findViewById(R.id.ll_AbboutApp);
        this.llApplyLeave = (LinearLayout) this.fullLayout.findViewById(R.id.ll_ApplyLeave);
        this.llMyLeave = (LinearLayout) this.fullLayout.findViewById(R.id.ll_Leave);
        this.llgroupChat = (LinearLayout) this.fullLayout.findViewById(R.id.ll_group_chat);
        this.llhome = (LinearLayout) this.fullLayout.findViewById(R.id.ll_home);
        this.llUnreadMngTask = (LinearLayout) this.fullLayout.findViewById(R.id.manager_ll_unread_task);
        this.tvUnreadMngTaskCount = (ETechTextView) this.fullLayout.findViewById(R.id.manager_txtBadgetask);
        this.llUnreadTask = (LinearLayout) this.fullLayout.findViewById(R.id.ll_unread_task);
        this.llUnreadLeave = (LinearLayout) this.fullLayout.findViewById(R.id.ll_unread_leave);
        this.llUnreadChatMsg = (LinearLayout) this.fullLayout.findViewById(R.id.ll_unread_chat);
        this.tvUnreadTaskCount = (ETechTextView) this.fullLayout.findViewById(R.id.txtBadgetask);
        this.tvUnreadLeaveCount = (ETechTextView) this.fullLayout.findViewById(R.id.txtBadgeleave);
        this.tvUnreadMsgsCount = (ETechTextView) this.fullLayout.findViewById(R.id.txtBadgeGroupchat);
        this.vRegister = this.fullLayout.findViewById(R.id.vRegister);
        this.vMyTask = this.fullLayout.findViewById(R.id.vMyTask);
        this.vStatistick = this.fullLayout.findViewById(R.id.vStatistik);
        this.vAboutApp = this.fullLayout.findViewById(R.id.vAbboutApp);
        this.vgroupChat = this.fullLayout.findViewById(R.id.vGChat);
        this.vApplyLeave = this.fullLayout.findViewById(R.id.vLeave1);
        this.vMyLeave = this.fullLayout.findViewById(R.id.vLeave);
        this.vhome = this.fullLayout.findViewById(R.id.vhome);
        this.imgProfile = (ImageView) this.fullLayout.findViewById(R.id.btnProfileEdit);
        this.imgProfilePic = (ImageView) this.fullLayout.findViewById(R.id.imageView1);
        String url = Constants.BASE_IMAGE_URL + Preference.getSharedPref(Constants.PREF_USER_PROFILE_PIC, "");
        Log.d("URL", url);
        String username = Preference.getSharedPref(Constants.PREF_FILE_DBUSER_USERNAME, "");
        if (!username.equalsIgnoreCase("")) {
            Glide.with(this.context).load(url).asBitmap().centerCrop().placeholder((int) R.drawable.profile_pic).error((int) R.drawable.profile_pic).into(new BitmapImageViewTarget(this.imgProfilePic) {
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(BaseDrawerActivity.this.context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    BaseDrawerActivity.this.imgProfilePic.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
        this.comp_logo = (ImageView) this.fullLayout.findViewById(R.id.comp_logo);
        String comp_logo_url = Constants.BASE_IMAGE_URL + Preference.getSharedPref(Constants.PREF_COMPANY_LOGO, "");
        if (!username.equalsIgnoreCase("")) {
            Glide.with(this.context).load(comp_logo_url).asBitmap().placeholder((int) R.drawable.onjyb_logo).error((int) R.drawable.onjyb_logo).into(new BitmapImageViewTarget(this.comp_logo) {
                protected void setResource(Bitmap resource) {

//                    if (resource.getWidth() > resource.getHeight()) {
//                        BaseDrawerActivity.this.comp_logo.setScaleType(ScaleType.FIT_END);
//                    } else {
//                        BaseDrawerActivity.this.comp_logo.setScaleType(ScaleType.FIT_START);
//                    }

                    BaseDrawerActivity.this.comp_logo.setImageDrawable(new BitmapDrawable(resource));
                }
            });
        }
        checkUserRole();
        this.btnmanager_minejobber.setOnClickListener(this.menuOptionClick);
        this.btnregister.setOnClickListener(this.menuOptionClick);
        this.minejobber.setOnClickListener(this.menuOptionClick);
        this.statistik.setOnClickListener(this.menuOptionClick);
        this.btnAboutApp.setOnClickListener(this.menuOptionClick);
        this.btnGroupChat.setOnClickListener(this.menuOptionClick);
        this.btnLogout.setOnClickListener(this.menuOptionClick);
        this.btnLeave.setOnClickListener(this.menuOptionClick);
        this.btnApplyLeave.setOnClickListener(this.menuOptionClick);
        this.imgProfile.setOnClickListener(this.menuOptionClick);
        this.btnHome.setOnClickListener(this.menuOptionClick);
        this.tvUserName = (ETechTextView) this.fullLayout.findViewById(R.id.txtUsername);
        this.tvEmailid = (ETechTextView) this.fullLayout.findViewById(R.id.txtEmailId);
        this.btnsearch = (Button) this.fullLayout.findViewById(R.id.btnsearch);
        this.btncancel = (Button) this.fullLayout.findViewById(R.id.btncancel);
        this.btnclr = (Button) this.fullLayout.findViewById(R.id.btnclear);
        this.rlLeaveType = (RelativeLayout) this.fullLayout.findViewById(R.id.rel_auto_srch1);
        this.rlStatus = (RelativeLayout) this.fullLayout.findViewById(R.id.rel_auto_status);
        this.autoProject = (EtechEditAutoCompleteTextRobotoRegular) this.fullLayout.findViewById(R.id.auto_proname);
        this.autoService = (AutoCompleteTextView) this.fullLayout.findViewById(R.id.auto_service);
        this.autoBranch = (AutoCompleteTextView) this.fullLayout.findViewById(R.id.auto_branch);
        this.autoLeaveType = (Spinner) this.fullLayout.findViewById(R.id.auto_leaveType);
        this.autostatusList = (Spinner) this.fullLayout.findViewById(R.id.auto_Status);
        this.autoEmployees = (AutoCompleteTextView) this.fullLayout.findViewById(R.id.auto_employee);
        this.strDate = (ETechTextView) this.fullLayout.findViewById(R.id.tvstrdate);
        this.endDate = (ETechTextView) this.fullLayout.findViewById(R.id.tvEndDt);
        this.txtleaveType = (ETechTextView) this.fullLayout.findViewById(R.id.txtLeavetype);
        this.txtServicelbl = (ETechTextView) this.fullLayout.findViewById(R.id.tvService);
        this.txtBranchlbl = (ETechTextView) this.fullLayout.findViewById(R.id.tvBranch);
        this.txtEmployee = (ETechTextView) this.fullLayout.findViewById(R.id.txtemployee);
        this.tvStatusName = (ETechTextView) this.fullLayout.findViewById(R.id.txtStatusName);
        this.txtVersion = (TextView) this.fullLayout.findViewById(R.id.txtAppVersion);
        this.txtVersion.setText("Version: " + getApkVersion(false));
        this.txtBuildName = (TextView) this.fullLayout.findViewById(R.id.buildVersion);
        String buildVar = Constants.BUILDVERSION;
        this.txtBuildName.setVisibility(View.GONE);
        this.txtVersion.setPadding(0, 0, 0, 20);
    }

    private String getApkVersion(boolean isdebug) {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (isdebug) {
            return String.valueOf(pInfo.versionCode);
        }
        return pInfo.versionName;
    }

    public boolean dispatchTouchEvent(MotionEvent e) {
        View view;
        if (e.getAction() == 2) {
            if (!this.mDrawerLayout.isDrawerOpen((int) GravityCompat.START)) {
                view = getCurrentFocus();
                if (view != null) {
                    ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                checkUserRole();
                setUnReadCount();
            }
        } else if (e.getAction() == 0 || e.getAction() == 4) {
            view = getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(e);
    }

    public void checkUserRole() {
        if (Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_MANAGER).equalsIgnoreCase(Constants.USER_ROLE_EMPLOYEE)) {
            this.vRegister.setVisibility(View.VISIBLE);
            this.llRegisterTime.setVisibility(View.VISIBLE);
            this.llApplyLeave.setVisibility(View.VISIBLE);
            this.llMyLeave.setVisibility(View.VISIBLE);
            this.vApplyLeave.setVisibility(View.VISIBLE);
            this.vMyLeave.setVisibility(View.VISIBLE);
            this.llmanager_llMyTask.setVisibility(View.GONE);
            this.vManagerMytask.setVisibility(View.GONE);
            return;
        }
        this.llmanager_llMyTask.setVisibility(View.VISIBLE);
        this.vManagerMytask.setVisibility(View.VISIBLE);
    }

    public void selectDatetoSearch(View view) {
        new SelectDateFragment().show(getSupportFragmentManager(), "DatePicker");
    }

    public void selectDatetoSearch1(View view) {
        new SelectDateFragment1().show(getSupportFragmentManager(), "DatePicker");
    }

    public void populateSetDate(int year, int month, int day) {
        String time = day + "/" + month + "/" + year;
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
        this.startDate = time;
        try {
            this.strDate.setText(output.format(input.parse(time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void populateSetDate1(int year, int month, int day) {
        String time = day + "/" + month + "/" + year;
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yy");
        try {
            Date date1 = input.parse(time);
            String str1 = output.format(date1);
            if (Integer.parseInt(getDateDiffString(input.parse(this.startDate), date1)) <= 0) {
                AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getResources().getString(R.string.start_must_greter));
            } else {
                this.endDate.setText(str1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getDateDiffString(Date dateOne, Date dateTwo) {
        long delta = (dateTwo.getTime() - dateOne.getTime()) / DateUtils.MILLIS_PER_DAY;
        if (delta > 0) {
            return String.valueOf(delta);
        }
        return String.valueOf(delta);
    }

    protected void onResume() {
        super.onResume();
        OnjybApp.activityResumed();
    }

    protected void onPause() {
        super.onPause();
        OnjybApp.activityPaused();
    }

    private void setUnReadCount() {
        String msg = Preference.getSharedPref(Constants.PREF_UNREAD_MESSAGE_COUNT, "");
        String task = Preference.getSharedPref(Constants.PREF_UNREAD_WORK_COUNT, "");
        String leave = Preference.getSharedPref(Constants.PREF_UNREAD_LEAVE_COUNT, "");
        String roleid = Preference.getSharedPref(Constants.PREF_KEY_USER_USER_ROLE_ID, Constants.USER_ROLE_EMPLOYEE);
        String managertask = Preference.getSharedPref(Constants.PREF_UNREAD_MANAGER_APPROVE_TASK, "");
        String managerleave = Preference.getSharedPref(Constants.PREF_UNREAD_MANAGER_APPROVE_leave, "");
        if (task == null || task.length() <= 0 || "0".equalsIgnoreCase(task)) {
            this.llUnreadTask.setVisibility(View.GONE);
        } else {
            this.llUnreadTask.setVisibility(View.VISIBLE);
            if (Integer.parseInt(task) > 99) {
                this.tvUnreadTaskCount.setText("99+");
            } else {
                this.tvUnreadTaskCount.setText(task);
            }
        }
        if (msg == null || msg.length() <= 0 || "0".equalsIgnoreCase(msg)) {
            this.llUnreadChatMsg.setVisibility(View.GONE);
        } else {
            this.llUnreadChatMsg.setVisibility(View.VISIBLE);
            if (Integer.parseInt(msg) > 99) {
                this.tvUnreadMsgsCount.setText("99+");
            } else {
                this.tvUnreadMsgsCount.setText(msg);
            }
        }
        if (leave == null || leave.length() <= 0 || "0".equalsIgnoreCase(leave)) {
            this.llUnreadLeave.setVisibility(View.GONE);
        } else {
            this.llUnreadLeave.setVisibility(View.VISIBLE);
            if (Integer.parseInt(task) > 99) {
                this.tvUnreadLeaveCount.setText("99+");
            } else {
                this.tvUnreadLeaveCount.setText(leave);
            }
        }
        String mngTodoOnLv = "";
        if (roleid.equalsIgnoreCase(Constants.USER_ROLE_MANAGER) || roleid.equalsIgnoreCase(Constants.USER_ROLE_CEO)) {
            this.tvUnreadMngTaskCount.setText("");
            if (!(managerleave == null || managerleave.length() <= 0 || managerleave.equalsIgnoreCase("0"))) {
                mngTodoOnLv = Integer.parseInt(managerleave) > 99 ? "99+" : managerleave;
            }
        }
        if (managertask != null && managertask.length() > 0 && !managertask.equalsIgnoreCase("0")) {
            this.llUnreadMngTask.setVisibility(View.VISIBLE);
            if (mngTodoOnLv == null || "".equalsIgnoreCase(mngTodoOnLv)) {
                if (Integer.parseInt(managertask) > 99) {
                    this.tvUnreadMngTaskCount.setText("99+ | 0");
                } else {
                    this.tvUnreadMngTaskCount.setText(managertask + " | " + 0);
                }
            } else if (Integer.parseInt(managertask) > 99) {
                this.tvUnreadMngTaskCount.setText("99+ | " + mngTodoOnLv);
            } else {
                this.tvUnreadMngTaskCount.setText(managertask + " | " + mngTodoOnLv);
            }
            if ("0".equalsIgnoreCase(managerleave) && "0".equalsIgnoreCase(managertask)) {
                this.llUnreadMngTask.setVisibility(View.GONE);
            }
        } else if (managertask != null && managertask.length() > 0) {
            if (!managertask.equalsIgnoreCase("0") || managerleave == null || managerleave.length() <= 0 || managerleave.equalsIgnoreCase("0")) {
                this.llUnreadMngTask.setVisibility(View.GONE);
                return;
            }
            this.llUnreadMngTask.setVisibility(View.VISIBLE);
            if (Integer.parseInt(managerleave) > 99) {
                mngTodoOnLv = "99+";
            } else {
                mngTodoOnLv = managerleave;
            }
            this.tvUnreadMngTaskCount.setText("0 | " + mngTodoOnLv);
        }
    }
}

package com.onjyb.layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.beans.User;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechEditText;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;

public class LoginActivity extends BaseDrawerActivity {
    private final String TAG = LoginActivity.class.getName();
    OnClickListener btnLoginClickListener = new C06881();
    OnClickListener btnforgotpassword = new C06892();
    Context context = this;
    DatabaseHelper dbHelper;
    private ETechEditText edtPwd;
    private ETechEditText edtUsername;
    ETechButton forgotpsw;
    Button login;
    private ActionCallback loginCallback = new C06903();
    ProgressHelper progressHelper;
    private String strPwd;
    private String strUname;
    private String strUserType;
    User user;
    private String userRole;

    class C06881 implements OnClickListener {
        C06881() {
        }

        public void onClick(View v) {
            try {
                AppUtils.hideKeyBoard(LoginActivity.this);
                String str = LoginActivity.this.validateData();
                if (str == null) {
                    LoginActivity.this.progressHelper.showDialog("Loading..");
                    LoginActivity.this.userRole = Constants.USER_TYPE_APP;
                    LoginActivity.this.apiLoginCall(LoginActivity.this.edtUsername.getText().toString(), LoginActivity.this.edtPwd.getText().toString(), LoginActivity.this.userRole);
                    return;
                }
                AlertDialogHelper.getNotificatonAlert(LoginActivity.this.context, LoginActivity.this.getString(R.string.app_name), str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C06892 implements OnClickListener {
        C06892() {
        }

        public void onClick(View v) {
            LoginActivity.this.startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        }
    }

    class C06903 implements ActionCallback {
        C06903() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            try {
                if (LoginActivity.this.progressHelper != null) {
                    LoginActivity.this.progressHelper.dismissDialog();
                }
                if (statusCode == 1) {
                    Preference.setSharedPref(Constants.PREF_FILE_DBUSER_USERNAME, "" + LoginActivity.this.user.getEmail());
                    Preference.setSharedPref(Constants.PREF_KEY_DBUSER_PASSWORD, "" + LoginActivity.this.user.getPassword());
                    LoginActivity.this.clearLoginFields();
                    Intent i = new Intent(LoginActivity.this, EmpHomeActivity.class);
                    i.putExtra("inFromSplash", true);
                    LoginActivity.this.startActivity(i);
                    LoginActivity.this.finish();
                    return;
                }
                AlertDialogHelper.getNotificatonAlert(LoginActivity.this.context, LoginActivity.this.getString(R.string.app_name), res.toString());
            } catch (Exception e) {
                if (LoginActivity.this.progressHelper != null) {
                    LoginActivity.this.progressHelper.dismissDialog();
                }
                e.printStackTrace();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login, true);
        AppUtils.setStatusbarColor(this);
        this.header.setTitle(getResources().getString(R.string.header_login_screen));
        this.header.hideLeftBtn();
        this.header.hideRightBtn();
        getViews();
        this.login.setOnClickListener(this.btnLoginClickListener);
        this.forgotpsw.setOnClickListener(this.btnforgotpassword);
    }

    private String validateData() {
        try {
            this.strUname = this.edtUsername.getText().toString().trim();
            this.strPwd = this.edtPwd.getText().toString().trim();
            if (this.strUname.length() == 0) {
                return getString(R.string.enter_username);
            }
            if (this.strPwd.length() == 0) {
                return getString(R.string.enter_password);
            }
            if (this.strUname.length() > 0 && !AppUtils.isEmailValid(this.strUname)) {
                return getString(R.string.enter_valid_email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getViews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.login = (Button) findViewById(R.id.btnlogin);
        this.forgotpsw = (ETechButton) findViewById(R.id.btnForgotPwd);
        this.edtUsername = (ETechEditText) findViewById(R.id.edtloginemail);
        this.edtPwd = (ETechEditText) findViewById(R.id.edtloginpsw);
    }

    public void onBackPressed() {
        Intent startMain = new Intent("android.intent.action.MAIN");
        startMain.addCategory("android.intent.category.HOME");
        startMain.setFlags(268435456);
        startActivity(startMain);
    }

    public void apiLoginCall(String stremail, String strPassword, String userType) {
        this.user = new User();
        this.user.setEmail(stremail);
        this.user.setPassword(strPassword);
        new UserHelper(this.context).apiUserLogin(this.user, this.loginCallback);
    }

    private void clearLoginFields() {
        this.edtUsername.setText("");
        this.edtPwd.setText("");
    }
}

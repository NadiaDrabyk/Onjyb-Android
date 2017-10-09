package com.onjyb.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.onjyb.R;
import com.onjyb.OnjybApp;
import com.onjyb.beans.User;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.Header;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.ProgressHelper;

public class ForgotPasswordActivity extends Activity {
    private final String TAG = ForgotPasswordActivity.class.getName();
    private OnClickListener btnSubmitOnClickListener = new C06302();
    private Button btnsubmit;
    Context context = this;
    private ETechEditText edtmail;
    private ActionCallback forgotPasswordCallback = new C06323();
    ProgressHelper progressHelper;
    private String userType;

    class C06291 implements OnClickListener {
        C06291() {
        }

        public void onClick(View v) {
            ForgotPasswordActivity.this.finish();
        }
    }

    class C06302 implements OnClickListener {
        C06302() {
        }

        public void onClick(View v) {
            try {
                String str = ForgotPasswordActivity.this.validateData();
                if (str == null) {
                    User userdata = new User();
                    userdata.setEmail(ForgotPasswordActivity.this.edtmail.getText().toString());
                    UserHelper helper = new UserHelper(ForgotPasswordActivity.this.context);
                    ForgotPasswordActivity.this.progressHelper.showDialog("Loading..");
                    helper.apiForgotPassword(userdata, ForgotPasswordActivity.this.forgotPasswordCallback);
                    return;
                }
                Toast.makeText(ForgotPasswordActivity.this.context, str, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C06323 implements ActionCallback {

        class C06311 implements OnClickListener {
            C06311() {
            }

            public void onClick(View v) {
                ForgotPasswordActivity.this.startActivity(new Intent(ForgotPasswordActivity.this.context, LoginActivity.class));
            }
        }

        C06323() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (ForgotPasswordActivity.this.progressHelper != null) {
                ForgotPasswordActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    if (ForgotPasswordActivity.this.progressHelper != null) {
                        ForgotPasswordActivity.this.progressHelper.dismissDialog();
                    }
                    AlertDialogHelper.getNotificatonAlert(ForgotPasswordActivity.this.context, ForgotPasswordActivity.this.getString(R.string.app_name), new C06311(), "sent mail", 0, 0, true);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(ForgotPasswordActivity.this.context, ForgotPasswordActivity.this.getString(R.string.app_name), res.toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        AppUtils.setStatusbarColor(this);
        setHeader();
        getviews();
        this.btnsubmit.setOnClickListener(this.btnSubmitOnClickListener);
    }

    private void setHeader() {
        try {
            Header header = (Header) findViewById(R.id.header1);
            header.setLeftBtnImage(R.drawable.ic_back_arrow);
            header.setLeftBtnClickListener(new C06291());
            header.setTitle(getString(R.string.forgot_password));
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

    private void getviews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.btnsubmit = (Button) findViewById(R.id.btnSubmit);
        this.edtmail = (ETechEditText) findViewById(R.id.edtmail);
    }

    private String validateData() {
        if (this.edtmail.getText().toString().trim().length() == 0) {
            return "Enter Email Address";
        }
        return null;
    }
}

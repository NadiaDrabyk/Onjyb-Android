package com.onjyb.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.customview.ETechButton;
import com.onjyb.customview.ETechTextView;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;

public class UpdateAvailableActivity extends Activity {
    ETechButton btnSkip;
    OnClickListener btnSkipClickListener = new C07902();
    ETechButton btnUpdate;
    OnClickListener btnUpdateClickListener = new C07891();
    Context context = this;
    ETechTextView tvupdateMsg;

    class C07891 implements OnClickListener {
        C07891() {
        }

        public void onClick(View v) {
            String url = Preference.getSharedPref(Constants.updateUrl, "");
            if (url != null && !url.equalsIgnoreCase("")) {
                UpdateAvailableActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                UpdateAvailableActivity.this.finish();
            }
        }
    }

    class C07902 implements OnClickListener {
        C07902() {
        }

        public void onClick(View v) {
            UpdateAvailableActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_available);
        AppUtils.setStatusbarColor(this);
        getViews();
        String msg = Preference.getSharedPref("message", "");
        String skiptitle = Preference.getSharedPref(Constants.skip_btn_name, "");
        String updatetitle = Preference.getSharedPref(Constants.update_btn_name, "");
        String forceupdate = Preference.getSharedPref(Constants.forceUpdateApp, "");
        if (!(msg == null || "".equalsIgnoreCase(msg))) {
            this.tvupdateMsg.setText(msg);
        }
        if (forceupdate != null && forceupdate.equalsIgnoreCase("yes")) {
            this.btnSkip.setVisibility(View.GONE);
            Constants.updateActivityCall = true;
        }
        if (!(skiptitle == null || "".equalsIgnoreCase(skiptitle))) {
            this.btnSkip.setText(skiptitle);
        }
        if (!(updatetitle == null || "".equalsIgnoreCase(updatetitle))) {
            this.btnUpdate.setText(updatetitle);
        }
        this.btnSkip.setOnClickListener(this.btnSkipClickListener);
        this.btnUpdate.setOnClickListener(this.btnUpdateClickListener);
    }

    private void getViews() {
        this.tvupdateMsg = (ETechTextView) findViewById(R.id.txtmessage);
        this.btnSkip = (ETechButton) findViewById(R.id.btnskip);
        this.btnUpdate = (ETechButton) findViewById(R.id.btnupdate1);
    }
}

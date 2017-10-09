package com.onjyb.layout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.onjyb.R;
import com.onjyb.customview.ETechTextView;

public class AboutAppActivity extends BaseDrawerActivity {
    ETechTextView tvsupportemail;
    String url = "http://www.onjyb.com";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        this.header.setTitle(getString(R.string.menu_about_app));
        this.header.hideRightBtn();
        this.tvsupportemail = (ETechTextView) findViewById(R.id.tvsupportmail);
    }

    public void RedierecttoUrl(View view) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(this.url)));
        finish();
    }

    public void RedierecttoUrlsupport(View view) {
        Intent i = new Intent("android.intent.action.SEND");
        i.setData(Uri.parse("mailto:" + this.tvsupportemail.getText().toString()));
        i.putExtra("android.intent.extra.EMAIL", new String[]{this.tvsupportemail.getText().toString()});
        i.setType("message/rfc822");
        startActivity(Intent.createChooser(i, "Send email"));
    }
}

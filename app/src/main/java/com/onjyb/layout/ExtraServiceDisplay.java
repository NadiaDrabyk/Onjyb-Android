package com.onjyb.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.onjyb.R;
import com.onjyb.OnjybApp;
import com.onjyb.customview.ETechTextView;
import com.onjyb.customview.Header;
import com.onjyb.customview.ImageSelectionView;
import com.onjyb.db.WorkServiceMap;
import com.onjyb.util.AppUtils;

public class ExtraServiceDisplay extends Activity {
    WorkServiceMap curWorkServiceMap;
    ImageSelectionView imgselectionView2;
    TextView tvNoComment;
    ETechTextView tvService;
    ETechTextView tvWorkHrs;
    ETechTextView tvdcomment;
    TextView txtnoImages;

    class C06281 implements OnClickListener {
        C06281() {
        }

        public void onClick(View v) {
            ExtraServiceDisplay.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_service_display);
        AppUtils.setStatusbarColor(this);
        setHeader();
        getViews();
        this.curWorkServiceMap = (WorkServiceMap) getIntent().getSerializableExtra("displayExtraService");
        if (!(this.curWorkServiceMap == null || this.curWorkServiceMap.getAttachmentList() == null || this.curWorkServiceMap.getAttachmentList().size() <= 0)) {
            this.imgselectionView2.setVisibility(View.VISIBLE);
            this.imgselectionView2.setEnabled(false);
            this.txtnoImages.setVisibility(View.GONE);
            this.imgselectionView2.hideAddImageBtn();
            this.imgselectionView2.setImageInView(this.curWorkServiceMap.getAttachmentList());
        }
        if (this.curWorkServiceMap.getRefServiceName() != null) {
            this.tvService.setText(this.curWorkServiceMap.getRefServiceName());
        }
        if (this.curWorkServiceMap.getService_comments() != null) {
            this.tvdcomment.setText(this.curWorkServiceMap.getService_comments());
        } else {
            this.tvdcomment.setVisibility(View.GONE);
            this.tvNoComment.setVisibility(View.VISIBLE);
        }
        if (this.curWorkServiceMap.getServiceTime() != null) {
            displayHoursInView(this.tvWorkHrs, Integer.parseInt(this.curWorkServiceMap.getServiceTime()));
        }
    }

    private void setHeader() {
        try {
            Header header = (Header) findViewById(R.id.headerDispltscreen);
            header.setLeftBtnImage(R.drawable.ic_back_arrow);
            header.setLeftBtnClickListener(new C06281());
            header.setTitle(getString(R.string.MyTaskDisplay));
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

    private void getViews() {
        this.tvService = (ETechTextView) findViewById(R.id.tvServiceDisply);
        this.tvWorkHrs = (ETechTextView) findViewById(R.id.tvworkHrsDisply);
        this.tvNoComment = (TextView) findViewById(R.id.txtNocomments);
        this.txtnoImages = (TextView) findViewById(R.id.txtnoImages);
        this.tvdcomment = (ETechTextView) findViewById(R.id.tvdcommentexservice);
        this.imgselectionView2 = (ImageSelectionView) findViewById(R.id.imgselectionView2);
    }

    private void displayHoursInView(ETechTextView tvtime, int time) {
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
        tvtime.setText(String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + getResources().getString(R.string.Hrs));
    }
}

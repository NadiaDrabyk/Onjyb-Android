package com.onjyb.layout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.onjyb.R;
import com.onjyb.adaptor.SpinnerListAdapter;
import com.onjyb.Constants;
import com.onjyb.customview.ETechEditText;
import com.onjyb.customview.ImageSelectionView;
import com.onjyb.db.AttachmentMap;
import com.onjyb.db.DatabaseHelper;
import com.onjyb.db.WorkServiceMap;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AlertDialogHelper.OnMyDialogResult;
import com.onjyb.util.AppUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExtraServiceActivity extends BaseDrawerActivity implements OnMyDialogResult {
    private static final int REQUEST_CONTACTS_CODE = 100;
    public final int CAMERA_CAPTURE = 1;
    public final int IMAGE_CROP = 3;
    public final int OPEN_GALLERY = 2;
    List<String> asServiceList;
    HashMap<String, String> assoServiceMap;
    AutoCompleteTextView atvproject;
    private Context context = this;
    WorkServiceMap curWorkServiceMap;
    private DatabaseHelper dbHelper;
    private Dialog dialog;
    ETechEditText edtComments;
    ImageSelectionView imageSelectionView;
    private Boolean isEditable = Boolean.valueOf(false);
    private Boolean isImgFromURL = Boolean.valueOf(false);
    ArrayAdapter<String> myAutoCompleteAdapter;
    TextView noImagetxt;
    TextView nocomments;
    HashMap<String, String> selServiceMap;
    String selectedHrsItem;
    String selectedminItem;
    private String serviceId;
    Spinner spexServiceHrsList;
    Spinner spexServiceMinList;

    class C06231 implements OnClickListener {
        C06231() {
        }

        public void onClick(View v) {
            ExtraServiceActivity.this.finish();
        }
    }

    class C06242 implements OnItemSelectedListener {
        C06242() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            ExtraServiceActivity.this.selectedminItem = ExtraServiceActivity.this.spexServiceMinList.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06253 implements OnItemSelectedListener {
        C06253() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            ExtraServiceActivity.this.selectedHrsItem = ExtraServiceActivity.this.spexServiceHrsList.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    class C06264 implements OnItemClickListener {
        C06264() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            String qry = "select * from tblservice where service_name = '" + ExtraServiceActivity.this.atvproject.getText().toString() + "'";
            ExtraServiceActivity.this.dbHelper = new DatabaseHelper(ExtraServiceActivity.this.context);
            ExtraServiceActivity.this.dbHelper = DatabaseHelper.getDBAdapterInstance(ExtraServiceActivity.this.context);
            ExtraServiceActivity.this.assoServiceMap = ExtraServiceActivity.this.dbHelper.selSingleRecordFromDB(qry, null);
            AppUtils.hideKeyBoard(ExtraServiceActivity.this);
        }
    }

    class C06275 implements OnTouchListener {
        C06275() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            ExtraServiceActivity.this.atvproject.showDropDown();
            return false;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_service);
        this.header.setTitle(getString(R.string.title_activity_register_time));
        this.header.hideRightBtn();
        this.header.setLeftBtnImage(R.drawable.ic_back_arrow);
        this.header.setLeftBtnClickListener(new C06231());
        getViews();
        getImageSelectionView();
        List<String> exServiceHrsList = new ArrayList();
        List<String> exServiceMInsList = new ArrayList();
        exServiceHrsList = Arrays.asList(getResources().getStringArray(R.array.hrs_array));
        exServiceMInsList = Arrays.asList(getResources().getStringArray(R.array.minits_array));
        this.spexServiceHrsList.setAdapter(new SpinnerListAdapter(this.context, exServiceHrsList));
        this.spexServiceMinList.setAdapter(new SpinnerListAdapter(this.context, exServiceMInsList));
        this.spexServiceMinList.setOnItemSelectedListener(new C06242());
        this.spexServiceHrsList.setOnItemSelectedListener(new C06253());
        this.isEditable = Boolean.valueOf(getIntent().getBooleanExtra("isEditable", false));
        this.isImgFromURL = Boolean.valueOf(getIntent().getBooleanExtra("isImgFromURL", false));
        if (this.isEditable.booleanValue()) {
            this.curWorkServiceMap = (WorkServiceMap) getIntent().getSerializableExtra("EditExtraService");
            this.serviceId = this.curWorkServiceMap.getBaseService();
            this.atvproject.setText(this.curWorkServiceMap.getRefServiceName());
            this.edtComments.setText(this.curWorkServiceMap.getService_comments());
            if (this.curWorkServiceMap.getServiceTime() != null) {
                displayHoursInView(this.spexServiceHrsList, this.spexServiceMinList, Integer.parseInt(this.curWorkServiceMap.getServiceTime()));
            }
        } else {
            this.curWorkServiceMap = (WorkServiceMap) getIntent().getSerializableExtra("ServiceDataObject");
            this.serviceId = (String) getIntent().getSerializableExtra("service_id");
        }
        this.asServiceList = getListFromLocalDB("tblservice", this.serviceId);
        if (this.asServiceList != null && this.asServiceList.size() > 0) {
            getAutocompleteListView(this.asServiceList);
        }
        if (this.curWorkServiceMap != null && this.curWorkServiceMap.getAttachmentList() != null) {
            this.imageSelectionView.setImageInView(this.curWorkServiceMap.getAttachmentList());
        }
    }

    private void displayHoursInView(Spinner spexServiceHrsList, Spinner spexServiceMinList, int time) {
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
        spexServiceHrsList.setSelection(getIndex(spexServiceHrsList, String.format("%02d", new Object[]{Integer.valueOf(workHrs)}) + " " + getResources().getString(R.string.Hrs)));
        spexServiceMinList.setSelection(getIndex(spexServiceMinList, String.format("%02d", new Object[]{Integer.valueOf(workMn)}) + " " + getResources().getString(R.string.Mins)));
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 3) {
            try {
                this.imageSelectionView.handleImageData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getImageSelectionView() {
        int deviceWidth = getResources().getDisplayMetrics().widthPixels - AppUtils.dpToPixel(30.0f, this);
        AppUtils.hideKeyBoard(this);
        this.dialog = new Dialog(this.context);
        this.dialog.requestWindowFeature(1);
        this.imageSelectionView = (ImageSelectionView) findViewById(R.id.imgselectionView1);
        this.imageSelectionView.setLayoutParams(new LayoutParams(deviceWidth, -2));
    }

    private void getViews() {
        this.atvproject = (AutoCompleteTextView) findViewById(R.id.atctv_asservice);
        this.edtComments = (ETechEditText) findViewById(R.id.edtextracomment);
        this.spexServiceHrsList = (Spinner) findViewById(R.id.exServiceHrsList);
        this.spexServiceMinList = (Spinner) findViewById(R.id.exServiceMinsList);
        this.noImagetxt = (TextView) findViewById(R.id.txtnoImages1);
        this.nocomments = (TextView) findViewById(R.id.txtnocomments1);
    }

    private void getAutocompleteListView(List item) {
        this.asServiceList = item;
        this.myAutoCompleteAdapter = new ArrayAdapter(this.context, R.layout.auto_complete_textview, this.asServiceList);
        this.atvproject.setAdapter(this.myAutoCompleteAdapter);
        this.atvproject.setOnItemClickListener(new C06264());
        this.atvproject.setOnTouchListener(new C06275());
    }

    private List<String> getListFromLocalDB(String tblname, String sId) {
        this.dbHelper = new DatabaseHelper(this.context);
        String qry1 = "select * from " + tblname;
        this.dbHelper = DatabaseHelper.getDBAdapterInstance(this.context);
        ArrayList<HashMap<String, String>> list = this.dbHelper.selectRecordsFromDBList(qry1, null);
        List<String> fieldlist = new ArrayList();
        if (list == null || list.size() <= 0) {
            return null;
        }
        for (int i = 0; i < list.size(); i++) {
            fieldlist.add((String) ((HashMap) list.get(i)).get("service_name"));
        }
        return fieldlist;
    }

    public void addService(View view) {
        String flag = validation();
        if (flag != null) {
            AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), flag);
        } else if (this.atvproject.getText().toString() == null || this.atvproject.getText().toString().length() <= 0) {
            Toast.makeText(this.context, "select Associate service", Toast.LENGTH_SHORT).show();
        } else {
            Intent finishIntent = new Intent(this, RegisterTimeActivity.class);
            WorkServiceMap workServiceMap = new WorkServiceMap();
            if (this.assoServiceMap != null) {
                workServiceMap.setRefServiceId((String) this.assoServiceMap.get("service_id"));
                workServiceMap.setRefServiceName((String) this.assoServiceMap.get("service_name"));
            } else {
                workServiceMap.setRefServiceId(this.curWorkServiceMap.getRefServiceId());
                workServiceMap.setRefServiceName(this.curWorkServiceMap.getRefServiceName());
            }
            try {
                workServiceMap.setServiceTime(String.valueOf(
                        (Integer.parseInt(this.selectedHrsItem.substring(0, (this.selectedHrsItem.length() - getResources().getString(R.string.Hrs).length()) - 1)) * 60) +
                                Integer.parseInt(this.selectedminItem.substring(0, (this.selectedminItem.length() - getResources().getString(R.string.Mins).length()) - 1))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<AttachmentMap> extServiceImages = this.imageSelectionView.getImagesList();
            int index = 0;
            while (index < extServiceImages.size()) {
                try {
                    ((AttachmentMap) extServiceImages.get(index)).setMapName("service");
                    ((AttachmentMap) extServiceImages.get(index)).setType(Constants.ATTACHEMENT_TYPE_BITMAP);
                    index++;
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (this.curWorkServiceMap != null) {
                ArrayList<AttachmentMap> totalImagesList = new ArrayList();
                totalImagesList.addAll(extServiceImages);
                workServiceMap.setAttachmentList(totalImagesList);
            } else {
                workServiceMap.setAttachmentList(extServiceImages);
            }
            if (this.isEditable.booleanValue()) {
                workServiceMap.setAttachementToDelete(this.imageSelectionView.getImagesListToDelete());
            }
            workServiceMap.setService_comments(this.edtComments.getText().toString());
            finishIntent.putExtra("ServiceDataObject", workServiceMap);
            setResult(-1, finishIntent);
            finish();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 100) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (!AppUtils.verifyAllPermissions(grantResults)) {
            Toast.makeText(this.context, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }

    private String validation() {
        try {
            String project = this.atvproject.getText().toString().trim();
            String comments = this.edtComments.getText().toString().trim();
            String workhrs = this.selectedHrsItem;
            String workmin = this.selectedminItem;
            if (project.length() == 0) {
                return getString(R.string.txt_select_any_service);
            }
            String zeroHrs = "00 " + getString(R.string.Hrs);
            String zeroMin = "00 " + getString(R.string.Min);
            if (workhrs.equalsIgnoreCase(zeroHrs) && workmin.equalsIgnoreCase(zeroMin)) {
                return getString(R.string.txt_Enter_service_time);
            }
            if (comments.length() == 0) {
                return getString(R.string.txt_Enter_comment);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onOkClick(int resId1, int titleResId1) {
        this.imageSelectionView.removeImage(this.context);
    }
}

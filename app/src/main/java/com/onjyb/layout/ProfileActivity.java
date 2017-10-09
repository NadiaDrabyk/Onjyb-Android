package com.onjyb.layout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.beans.User;
import com.onjyb.beans.UserHelper;
import com.onjyb.customview.ETechEditText;
import com.onjyb.reqreshelper.ActionCallback;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import com.onjyb.util.ProgressHelper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

public class ProfileActivity extends BaseDrawerActivity {
    private static final int REQUEST_CONTACTS_CODE = 100;
    private static String[] SDCARD_PERMISSIONS = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"};
    public final int CAMERA_CAPTURE = 1;
    public final int IMAGE_CROP = 3;
    public final int OPEN_GALLERY = 2;
    private String absolutepath = "";
    ETechEditText address;
    Button btnChangePassword;
    OnClickListener btnChangePasswordClickListener = new C07373();
    Button btnEditProfile;
    private ActionCallback changePasswordApiCallBack = new C07459();
    String chngNewPsw;
    String chngNewPsw1;
    String chngPsw;
    Context context = this;
    private Dialog dialog;
    OnClickListener displayProfilePicClickListener = new C07362();
    private ActionCallback editProfileApiClickListener = new ActionCallback() {
        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (ProfileActivity.this.progressHelper != null) {
                ProfileActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    ProfileActivity.this.clearText();
                    AlertDialogHelper.getNotificatonAlert(ProfileActivity.this.context, ProfileActivity.this.getString(R.string.app_name), ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(ProfileActivity.this.context, ProfileActivity.this.getString(R.string.app_name), res.toString());
        }
    };
    OnClickListener editProfilePicClickListener = new C07351();
    ETechEditText edtNewPsw;
    ETechEditText edtOldPsw;
    ETechEditText edtReSetPsw;
    ETechEditText email;
    private ImageView imgProfPic;
    boolean isUploadNewImg = false;
    Activity mactivity = this;
    ETechEditText mobile;
    private Uri picUri;
    User proDetail;
    private ActionCallback profileApiActionCallBack = new C07448();
    ArrayList<User> profileObj = new ArrayList();
    ProgressHelper progressHelper;
    ETechEditText userFName;
    String user_id;

    class C07351 implements OnClickListener {
        C07351() {
        }

        public void onClick(View v) {
            User user = new User();
            if (ProfileActivity.this.email.getText().toString().equalsIgnoreCase("") || ProfileActivity.this.mobile.getText().toString().equalsIgnoreCase("") || ProfileActivity.this.userFName.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(ProfileActivity.this.context, "Fill All Field Then Update", Toast.LENGTH_SHORT).show();
                return;
            }
            String str = ProfileActivity.this.validateData();
            if (str == null) {
                user.setEmail(ProfileActivity.this.email.getText().toString());
                user.setMobile(ProfileActivity.this.mobile.getText().toString());
                user.setUserId(ProfileActivity.this.user_id);
                user.setFirstName(ProfileActivity.this.userFName.getText().toString());
                if (!ProfileActivity.this.absolutepath.equalsIgnoreCase("")) {
                    user.setProfile_image(ProfileActivity.this.absolutepath);
                }
                if (!ProfileActivity.this.address.getText().toString().equalsIgnoreCase("")) {
                    user.setAddress(ProfileActivity.this.address.getText().toString());
                }
                if (ProfileActivity.this.checkUpdation(ProfileActivity.this.proDetail, user)) {
                    UserHelper helper = new UserHelper(ProfileActivity.this.context);
                    ProfileActivity.this.progressHelper.showDialog("Loading..");
                    helper.apiEditProfile(user, ProfileActivity.this.editProfileApiClickListener);
                    return;
                }
                AlertDialogHelper.getNotificatonAlert(ProfileActivity.this.context, ProfileActivity.this.getString(R.string.app_name), ProfileActivity.this.getResources().getString(R.string.txt_no_chng_profile));
                return;
            }
            AlertDialogHelper.getNotificatonAlert(ProfileActivity.this.context, ProfileActivity.this.getString(R.string.app_name), str);
        }
    }

    class C07362 implements OnClickListener {
        C07362() {
        }

        public void onClick(View v) {
            if (VERSION.SDK_INT <= 22) {
                ProfileActivity.this.setBottomDialog();
            } else if (AppUtils.hasSelfPermission(ProfileActivity.this.mactivity, ProfileActivity.SDCARD_PERMISSIONS)) {
                ProfileActivity.this.setBottomDialog();
            } else {
                ProfileActivity.this.requestPermissions(ProfileActivity.SDCARD_PERMISSIONS, 100);
            }
        }
    }

    class C07373 implements OnClickListener {
        C07373() {
        }

        public void onClick(View v) {
            String str = ProfileActivity.this.validateChangepassword();
            if (str == null) {
                UserHelper helper = new UserHelper(ProfileActivity.this.context);
                ProfileActivity.this.progressHelper.showDialog("Loading..");
                helper.apiChangePassword(ProfileActivity.this.user_id, ProfileActivity.this.chngPsw, ProfileActivity.this.chngNewPsw, ProfileActivity.this.chngNewPsw1, ProfileActivity.this.changePasswordApiCallBack);
                return;
            }
            AlertDialogHelper.getNotificatonAlert(ProfileActivity.this.context, ProfileActivity.this.getString(R.string.app_name), str);
        }
    }

    class C07384 implements OnClickListener {
        C07384() {
        }

        public void onClick(View v) {
            ProfileActivity.this.dialog.dismiss();
            ProfileActivity.this.startCropActivity(2);
        }
    }

    class C07395 implements OnClickListener {
        C07395() {
        }

        public void onClick(View v) {
            try {
                ProfileActivity.this.dialog.dismiss();
                ProfileActivity.this.startCropActivity(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C07406 implements OnClickListener {
        C07406() {
        }

        public void onClick(View v) {
            ProfileActivity.this.dialog.dismiss();
        }
    }

    class C07417 implements OnClickListener {
        C07417() {
        }

        public void onClick(View v) {
            Bitmap bitMap = BitmapFactory.decodeResource(ProfileActivity.this.getResources(), R.drawable.profile_pic);
            File mFile1 = Environment.getExternalStorageDirectory();
            String fileName = "profile_pic.png";
            try {
                FileOutputStream outStream = new FileOutputStream(new File(mFile1, fileName));
                bitMap.compress(CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            ProfileActivity.this.absolutepath = mFile1.getAbsolutePath().toString() + "/" + fileName;
            Log.i("absolutepath", "Your IMAGE ABSOLUTE PATH:-" + ProfileActivity.this.absolutepath);
            File temp = new File(ProfileActivity.this.absolutepath);
            try {
                if (!temp.exists()) {
                    temp.createNewFile();
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            ProfileActivity.this.imgProfPic.setImageResource(R.drawable.profile_pic);
            ProfileActivity.this.dialog.dismiss();
        }
    }

    class C07448 implements ActionCallback {

        class C07421 extends TypeReference<ArrayList<User>> {
            C07421() {
            }
        }

        C07448() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (ProfileActivity.this.progressHelper != null) {
                ProfileActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    ProfileActivity.this.profileObj = (ArrayList) new ObjectMapper().configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(((JSONObject) res).getJSONArray(Constants.RESPONSE_KEY_Profile_DETAIL).toString(), new C07421());
                    if (ProfileActivity.this.profileObj != null && ProfileActivity.this.profileObj.size() > 0) {
                        ProfileActivity.this.proDetail = new User();
                        ProfileActivity.this.proDetail = (User) ProfileActivity.this.profileObj.get(0);
                        ProfileActivity.this.email.setText(ProfileActivity.this.proDetail.getEmail());
                        ProfileActivity.this.mobile.setText(ProfileActivity.this.proDetail.getMobile());
                        ProfileActivity.this.address.setText(ProfileActivity.this.proDetail.getAddress());
                        String str = "";
                        ProfileActivity.this.userFName.setText(AppUtils.UpperCaseWords(ProfileActivity.this.proDetail.getFirstName() + " " + ProfileActivity.this.proDetail.getLastName()));
                        String url = Constants.BASE_IMAGE_URL + ProfileActivity.this.proDetail.getProfile_image();
                        if (ProfileActivity.this.proDetail.getProfile_image() != null && !ProfileActivity.this.proDetail.getProfile_image().equalsIgnoreCase("")) {
                            Glide.with(ProfileActivity.this.context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(ProfileActivity.this.imgProfPic) {
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ProfileActivity.this.context.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ProfileActivity.this.imgProfPic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                            return;
                        }
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Toast.makeText(ProfileActivity.this.context, res.toString(), Toast.LENGTH_LONG).show();
        }
    }

    class C07459 implements ActionCallback {
        C07459() {
        }

        public void onActionComplete(int statusCode, String callbackString, Object res) {
            if (ProfileActivity.this.progressHelper != null) {
                ProfileActivity.this.progressHelper.dismissDialog();
            }
            if (statusCode == 1) {
                try {
                    ProfileActivity.this.clearText();
                    AlertDialogHelper.getNotificatonAlert(ProfileActivity.this.context, ProfileActivity.this.getString(R.string.app_name), ((JSONObject) res).getString(Constants.RESPONSE_KEY_MSG));
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            AlertDialogHelper.getNotificatonAlert(ProfileActivity.this.context, ProfileActivity.this.getString(R.string.app_name), res.toString());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.header.setTitle(getString(R.string.title_activity_profile));
        getViews();
        this.header.hideRightBtn();
        UserHelper helper = new UserHelper(this.context);
        this.user_id = Preference.getSharedPref(Constants.PREF_USER_ID, Constants.USER_ROLE_EMPLOYEE);
        this.progressHelper.showDialog("Loading..");
        helper.apiProfiledisplay(this.user_id, this.profileApiActionCallBack);
        this.imgProfPic.setOnClickListener(this.displayProfilePicClickListener);
        this.btnChangePassword.setOnClickListener(this.btnChangePasswordClickListener);
        this.btnEditProfile.setOnClickListener(this.editProfilePicClickListener);
    }

    protected void onPause() {
        super.onPause();
    }

    private boolean checkUpdation(User proDetail, User user) {
        if (proDetail == null || user == null) {
            return false;
        }
        if (user.getFirstName().equalsIgnoreCase(proDetail.getFirstName() + " " + proDetail.getLastName()) && user.getMobile().equalsIgnoreCase(proDetail.getMobile()) && user.getAddress().equalsIgnoreCase(proDetail.getAddress()) && !this.isUploadNewImg) {
            return false;
        }
        return true;
    }

    private String validateChangepassword() {
        try {
            this.chngPsw = this.edtOldPsw.getText().toString().trim();
            this.chngNewPsw = this.edtNewPsw.getText().toString().trim();
            this.chngNewPsw1 = this.edtReSetPsw.getText().toString().trim();
            if (this.chngPsw.length() == 0) {
                return getString(R.string.enter_old_password);
            }
            if (this.chngNewPsw.length() == 0) {
                return getString(R.string.enter_new_password);
            }
            if (this.chngNewPsw1.length() == 0) {
                return getString(R.string.enter_conform_password);
            }
            if (!this.chngNewPsw.equalsIgnoreCase(this.chngNewPsw1)) {
                return getString(R.string.password_not_match);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setBottomDialog() {
        AppUtils.displayBottomDialog(this.dialog, getString(R.string.choose_photo), getString(R.string.from_library), getString(R.string.from_camera), getString(R.string.btn_remove_photo), getString(R.string.cancel), new C07384(), new C07395(), new C07406(), null, new C07417());
    }

    private void startCropActivity(int type) {
        try {
            Intent intent = new Intent(this, PhotoCropActivity.class);
            intent.putExtra("type", type);
            startActivityForResult(intent, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 3) {
            try {
                this.absolutepath = data.getStringExtra("path");
                setImage();
                Log.v("absolutepath", this.absolutepath);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private void setImage() {
        if (new File(this.absolutepath).exists()) {
            AppUtils.compressImagePNG(this.context, this.absolutepath, this.absolutepath);
            Bitmap bm = AppUtils.getCircularBitmap(BitmapFactory.decodeFile(this.absolutepath, new Options()));
            this.absolutepath = Constants.DIR_IMAGES + File.separator + "Pic" + System.currentTimeMillis() + ".png";
            AppUtils.createFileFromBitmap(this.absolutepath, bm);
            this.imgProfPic.invalidate();
            this.imgProfPic.setImageBitmap(bm);
            this.isUploadNewImg = true;
            return;
        }
        AlertDialogHelper.getNotificatonAlert(this.context, getString(R.string.app_name), getString(R.string.msg_error_image_select));
    }

    private void getViews() {
        this.progressHelper = new ProgressHelper(this.context);
        this.dialog = new Dialog(this.context);
        this.dialog.requestWindowFeature(1);
        this.email = (ETechEditText) findViewById(R.id.edtemail);
        this.mobile = (ETechEditText) findViewById(R.id.edtmobile);
        this.address = (ETechEditText) findViewById(R.id.edtAddress);
        this.edtOldPsw = (ETechEditText) findViewById(R.id.edtoldPsw);
        this.edtNewPsw = (ETechEditText) findViewById(R.id.edtnewPsw);
        this.edtReSetPsw = (ETechEditText) findViewById(R.id.edtnewPsw1);
        this.userFName = (ETechEditText) findViewById(R.id.txtUsername);
        this.imgProfPic = (ImageView) findViewById(R.id.imgprofPic22);
        this.btnEditProfile = (Button) findViewById(R.id.btnupdateDetail);
        this.btnChangePassword = (Button) findViewById(R.id.btnchangePsw);
    }

    private String validateData() {
        try {
            String name = this.userFName.getText().toString().trim();
            String mobile1 = this.mobile.getText().toString().trim();
            String address1 = this.address.getText().toString().trim();
            if (name.length() == 0) {
                return getString(R.string.enter_profile_name);
            }
            if (mobile1.length() == 0) {
                return getString(R.string.enter_mobile_no);
            }
            if (address1.length() == 0) {
                return getString(R.string.enter_address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void clearText() {
        this.edtOldPsw.setText("");
        this.edtReSetPsw.setText("");
        this.edtNewPsw.setText("");
    }
}

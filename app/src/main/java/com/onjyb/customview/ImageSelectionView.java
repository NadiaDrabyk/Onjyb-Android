package com.onjyb.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import com.onjyb.Constants;
import com.onjyb.R;
import com.onjyb.db.AttachmentMap;
import com.onjyb.helper.SlideShowActivity;
import com.onjyb.layout.PhotoCropActivity;
import com.onjyb.util.AlertDialogHelper;
import com.onjyb.util.AppUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImageSelectionView extends LinearLayout {
    private static final int REQUEST_CONTACTS_CODE = 100;
    private static String[] SDCARD_PERMISSIONS = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};
    static final int maximg = 10;
    OnClickListener BtnClickListener = new C05651();
    public final int CAMERA_CAPTURE = 1;
    public final int IMAGE_CROP = 3;
    public final int OPEN_GALLERY = 2;
    OnClickListener ViewImageListClickLIstener = new C05706();
    private String absolutepath = "";
    Activity activity;
    ArrayList<AttachmentMap> attachmentToDelete = new ArrayList();
    ArrayList<AttachmentMap> attachmentsList = new ArrayList();
    Button btn1;
    Button btn2;
    Context context;
    int count = 0;
    int dHeight;
    int dWidth;
    OnClickListener deleteBtnClickListener = new C05737();
    private Dialog dialog;
    boolean isNotDeletable = false;
    LinearLayout linearLayout;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    private String path;
    View view = null;

    class C05651 implements OnClickListener {
        C05651() {
        }

        public void onClick(View v) {
            if (VERSION.SDK_INT <= 22) {
                ImageSelectionView.this.setBottomDialog();
            } else if (AppUtils.hasSelfPermission(ImageSelectionView.this.activity, ImageSelectionView.SDCARD_PERMISSIONS)) {
                ImageSelectionView.this.setBottomDialog();
            } else {
                ImageSelectionView.this.activity.requestPermissions(ImageSelectionView.SDCARD_PERMISSIONS, 100);
            }
        }
    }

    class C05662 implements OnClickListener {
        C05662() {
        }

        public void onClick(View v) {
            ImageSelectionView.this.dialog.dismiss();
            ImageSelectionView.this.startCropActivity(2);
        }
    }

    class C05673 implements OnClickListener {
        C05673() {
        }

        public void onClick(View v) {
            try {
                ImageSelectionView.this.dialog.dismiss();
                ImageSelectionView.this.startCropActivity(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C05684 implements OnClickListener {
        C05684() {
        }

        public void onClick(View v) {
            ImageSelectionView.this.dialog.dismiss();
        }
    }

    class C05695 implements OnClickListener {
        C05695() {
        }

        public void onClick(View v) {
            Bitmap bitMap = BitmapFactory.decodeResource(ImageSelectionView.this.getResources(), R.drawable.profile_pic);
            File mFile1 = Environment.getExternalStorageDirectory();
            String fileName = "profile_pic.png";
            try {
                FileOutputStream outStream = new FileOutputStream(new File(mFile1, fileName));
                bitMap.compress(CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageSelectionView.this.absolutepath = mFile1.getAbsolutePath().toString() + "/" + fileName;
            Log.i("absolutepath", "Your IMAGE ABSOLUTE PATH:-" + ImageSelectionView.this.absolutepath);
            File temp = new File(ImageSelectionView.this.absolutepath);
            try {
                if (!temp.exists()) {
                    temp.createNewFile();
                    Log.e("absolutepath", "no image file at location :" + ImageSelectionView.this.absolutepath);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            ImageSelectionView.this.dialog.dismiss();
        }
    }

    class C05706 implements OnClickListener {
        C05706() {
        }

        public void onClick(View v) {
            try {
                if (ImageSelectionView.this.attachmentsList != null && ImageSelectionView.this.attachmentsList.size() > 0) {
                    Intent intent = new Intent(ImageSelectionView.this.context, SlideShowActivity.class);
                    intent.putExtra("ImageList", ImageSelectionView.this.attachmentsList);
                    ImageSelectionView.this.context.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C05737 implements OnClickListener {

        class C05722 implements OnClickListener {
            C05722() {
            }

            public void onClick(View v) {
            }
        }

        C05737() {
        }

        public void onClick(View v1) {
            try {
                final ViewGroup parentView = (ViewGroup) v1.getParent();
                ImageSelectionView.this.view = parentView;
                AlertDialogHelper.getConfirmationAlert(ImageSelectionView.this.context, ImageSelectionView.this.context.getString(R.string.app_name), new OnClickListener() {
                    public void onClick(View v) {
                        ImageHolder imageHolder = (ImageHolder) parentView.getParent();
                        ImageSelectionView.this.linearLayout1.removeView(imageHolder);
                        ImageSelectionView.this.linearLayout2.removeView(imageHolder);
                        ImageSelectionView imageSelectionView = ImageSelectionView.this;
                        imageSelectionView.count--;
                        AttachmentMap attachmentMap = (AttachmentMap) ImageSelectionView.this.attachmentsList.get(imageHolder.getIndex());
                        if (attachmentMap.getType() == Constants.ATTACHEMENT_TYPE_URL) {
                            ImageSelectionView.this.attachmentToDelete.add(attachmentMap);
                        }
                        ImageSelectionView.this.attachmentsList.remove(imageHolder.getIndex());
                    }
                }, new C05722(), ImageSelectionView.this.getResources().getString(R.string.str_delete_image), 1, 1, false, ImageSelectionView.this.getResources().getString(R.string.txt_yes), ImageSelectionView.this.getResources().getString(R.string.txt_no), -1, -1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ImageSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = (Activity) context;
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.imageviewselection, this, true);
        this.linearLayout1 = (LinearLayout) findViewById(R.id.ll_img_select1);
        this.linearLayout2 = (LinearLayout) findViewById(R.id.ll_img_select2);
        this.linearLayout = (LinearLayout) findViewById(R.id.ll_of_imgView);
        this.btn1 = (Button) findViewById(R.id.btnaddimg1);
        this.btn2 = (Button) findViewById(R.id.btnaddimg2);
        this.btn1.setOnClickListener(this.BtnClickListener);
        this.btn2.setOnClickListener(this.BtnClickListener);
        this.path = Constants.PREF_KEY_USER_IMG_PATH;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int deviceWidth = metrics.widthPixels;
        int deviceHeight = metrics.heightPixels;
        this.dWidth = deviceWidth - AppUtils.dpToPixel(30.0f, context);
        this.dHeight = deviceHeight;
        this.linearLayout1.setWeightSum(5.0f);
        this.linearLayout2.setWeightSum(5.0f);
        LayoutParams layoutParams = new LayoutParams(deviceWidth, -2);
        this.linearLayout1.setLayoutParams(layoutParams);
        this.linearLayout2.setLayoutParams(layoutParams);
        AppUtils.hideKeyBoard(this.activity);
        this.dialog = new Dialog(context);
        this.dialog.requestWindowFeature(1);
    }

    private void setBottomDialog() {
        AppUtils.checkOrCreateAppDirectories(this.context);
        AppUtils.displayBottomDialog(this.dialog, this.context.getString(R.string.choose_photo), this.context.getString(R.string.from_library), this.context.getString(R.string.from_camera), this.context.getString(R.string.btn_remove_photo), this.context.getString(R.string.cancel), new C05662(), new C05673(), new C05684(), null, new C05695());
    }

    public void startCropActivity(int type) {
        try {
            Intent intent = new Intent(this.context, PhotoCropActivity.class);
            intent.putExtra("type", type);
            this.activity.startActivityForResult(intent, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addImageOnView(Bitmap bitmap) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(this.dWidth / 5, this.dWidth / 5);
        ImageHolder obj = new ImageHolder(this.context, this.deleteBtnClickListener, this.ViewImageListClickLIstener);
        obj.setIndex(this.count);
        obj.setLayoutParams(lp);
        obj.setImage(bitmap);
        if (this.count < 0 || this.count >= 10) {
            Toast.makeText(this.context, "Maximum 10 image can upload", Toast.LENGTH_SHORT).show();
            return;
        }
        if (this.count < 5) {
            this.linearLayout1.addView(obj, 0);
        } else {
            this.linearLayout2.addView(obj, 0);
        }
        this.count++;
        if (this.count == 5) {
            this.btn1.setVisibility(GONE);
            this.btn2.setVisibility(View.VISIBLE);
        }
        if (this.count == 10) {
            this.btn2.setVisibility(GONE);
        }
    }

    public void handleImageData(Intent data) {
        try {
            this.absolutepath = data.getStringExtra("path");
            setImage(this.absolutepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImage(String absolutepath) {
        this.absolutepath = absolutepath;
        AttachmentMap attachmentMap = new AttachmentMap();
        if (new File(this.absolutepath).exists()) {
            AppUtils.compressImagePNG(this.context, this.absolutepath, this.absolutepath);
            Bitmap bm = BitmapFactory.decodeFile(this.absolutepath, new Options());
            this.absolutepath = Constants.DIR_IMAGES + File.separator + "Pic" + System.currentTimeMillis() + ".png";
            AppUtils.createFileFromBitmap(this.absolutepath, bm);
            addImageOnView(bm);
            attachmentMap.setAttachmentName(this.absolutepath);
            attachmentMap.setMapName("worksheet");
            this.attachmentsList.add(attachmentMap);
            return;
        }
        AlertDialogHelper.getNotificatonAlert(this.context, this.context.getString(R.string.app_name), this.context.getString(R.string.msg_error_image_select));
    }

    public void setImageViewFromLocal(ArrayList<AttachmentMap> attachmentMaps) {
        if (attachmentMaps != null) {
            for (int i = 0; i < attachmentMaps.size(); i++) {
                AttachmentMap attachmentMap = (AttachmentMap) attachmentMaps.get(i);
                String imgpath = attachmentMap.getAttachmentName();
                if (new File(imgpath).exists()) {
                    AppUtils.compressImagePNG(this.context, imgpath, imgpath);
                    addImageOnView(BitmapFactory.decodeFile(imgpath, new Options()));
                    this.attachmentsList.add(attachmentMap);
                }
            }
        }
    }

    public void hideAddImageBtn() {
        this.btn1.setVisibility(GONE);
        this.btn2.setVisibility(GONE);
        this.isNotDeletable = true;
    }

    public void setImageInView(ArrayList<AttachmentMap> attachmentMaps) {
        if (attachmentMaps != null) {
            for (int i = 0; i < attachmentMaps.size(); i++) {
                ImageHolder obj;
                AttachmentMap attachmentMap = (AttachmentMap) attachmentMaps.get(i);
                this.attachmentsList.add(attachmentMap);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(this.dWidth / 5, this.dWidth / 5);
                if (this.isNotDeletable) {
                    obj = new ImageHolder(this.context, true);
                } else {
                    obj = new ImageHolder(this.context, this.deleteBtnClickListener, this.ViewImageListClickLIstener);
                }
                obj.setIndex(this.count);
                obj.setLayoutParams(lp);
                if (attachmentMap.getAttachmentId() == null) {
                    attachmentMap.setType(Constants.ATTACHEMENT_TYPE_BITMAP);
                    obj.setImage(BitmapFactory.decodeFile(attachmentMap.getAttachmentName(), new Options()));
                } else {
                    attachmentMap.setType(Constants.ATTACHEMENT_TYPE_URL);
                    obj.setImage(attachmentMap);
                }
                if (this.count < 0 || this.count >= 10) {
                    Toast.makeText(this.context, "Maximum 10 image can upload", Toast.LENGTH_SHORT).show();
                } else {
                    if (this.count < 5) {
                        this.linearLayout1.addView(obj, 0);
                    } else {
                        this.linearLayout2.addView(obj, 0);
                    }
                    this.count++;
                    if (this.count == 5) {
                        this.btn1.setVisibility(GONE);
                        this.btn2.setVisibility(View.VISIBLE);
                    }
                    if (this.count == 10) {
                        this.btn2.setVisibility(GONE);
                    }
                }
            }
        }
    }

    public ArrayList<AttachmentMap> getImagesList() {
        for (int i = 0; i < this.attachmentsList.size(); i++) {
            AttachmentMap attachmentMap = (AttachmentMap) this.attachmentsList.get(0);
        }
        return this.attachmentsList;
    }

    public ArrayList<AttachmentMap> getImagesListToDelete() {
        return this.attachmentToDelete;
    }

    public void removeImage(Context context) {
        if (this.view != null) {
            ImageHolder imageHolder = (ImageHolder) this.view.getParent();
            this.linearLayout1.removeView(imageHolder);
            this.linearLayout2.removeView(imageHolder);
            this.count--;
            AttachmentMap attachmentMap = (AttachmentMap) this.attachmentsList.get(imageHolder.getIndex());
            if (attachmentMap.getType() == Constants.ATTACHEMENT_TYPE_URL) {
                this.attachmentToDelete.add(attachmentMap);
            }
            this.attachmentsList.remove(imageHolder.getIndex());
        }
    }
}

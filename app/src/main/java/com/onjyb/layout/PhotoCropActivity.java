package com.onjyb.layout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.ImageRequest;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.onjyb.R;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.OnjybApp;
import com.onjyb.util.AppUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PhotoCropActivity extends Activity {
    private static int SPLASH_TIME_OUT = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;
    private static float density = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    public static Point displaySize = new Point();
    private static final int done_button = 1;
    private static Boolean isTablet = null;
    public final int CAMERA_CAPTURE = 1;
    public final int OPEN_GALLERY = 2;
    String absolutepath = "";
    private String bitmapKey;
    private TextView btnDone;
    private String cameraabsolutepath;
    Context context = this;
    private boolean doneButtonPressed = false;
    private BitmapDrawable drawable;
    private Bitmap imageToCrop;
    boolean isSuccess = false;
    private View lytBack;
    Activity mActivity = null;
    private ViewGroup rootView;
    private boolean sameBitmap = false;
    String temppath = "";
    private int type = 0;
    private PhotoCropView view;
    private Uri mImageUri;//0924

    class C07291 implements OnClickListener {
        C07291() {
        }

        public void onClick(View v) {
            PhotoCropActivity.this.finish();
        }
    }

    class C07302 implements OnClickListener {
        C07302() {
        }

        public void onClick(View v) {
            Bitmap bitmap = PhotoCropActivity.this.view.getBitmap();
            String absolutepath = Constants.DIR_IMAGES + File.separator + "croppedPhotoTmp.png";
            AppUtils.writeBmpToFile(bitmap, absolutepath);
            if (bitmap == PhotoCropActivity.this.imageToCrop) {
                PhotoCropActivity.this.sameBitmap = true;
            } else if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
            }
            Intent intent = new Intent();
            intent.putExtra("path", absolutepath);
            PhotoCropActivity.this.setResult(-1, intent);
            PhotoCropActivity.this.finish();
        }
    }

    class C07334 implements ImageLoadingListener {
        C07334() {
        }

        public void onLoadingStarted(String imageUri, View view) {
        }

        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            PhotoCropActivity.this.finish();
        }

        public void onLoadingComplete(String imageUri, View imageView, Bitmap loadedImage) {
            PhotoCropActivity.this.imageToCrop = loadedImage;
            if (PhotoCropActivity.this.imageToCrop == null) {
                PhotoCropActivity.this.finish();
            } else {
                PhotoCropActivity.this.initImage();
            }
        }

        public void onLoadingCancelled(String imageUri, View view) {
            PhotoCropActivity.this.finish();
        }
    }

    private class PhotoCropView extends FrameLayout {
        int bitmapHeight;
        int bitmapWidth;
        int bitmapX;
        int bitmapY;
        Paint circlePaint = null;
        int draggingState = 0;
        boolean freeform;
        Paint halfPaint = null;
        float oldX = 0.0f;
        float oldY = 0.0f;
        float ratio = (this.rectSizeX / this.rectSizeY);
        Paint rectPaint = null;
        float rectSizeX = 640.0f;
        float rectSizeY = 368.0f;
        float rectX = -1.0f;
        float rectY = -1.0f;
        int viewHeight;
        int viewWidth;

        class C07341 implements OnTouchListener {
            C07341() {
            }

            public boolean onTouch(View view, MotionEvent motionEvent) {
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                int cornerSide = PhotoCropActivity.dp(14);
                if (motionEvent.getAction() == 0) {
                    if (PhotoCropView.this.rectX - ((float) cornerSide) < x && PhotoCropView.this.rectX + ((float) cornerSide) > x && PhotoCropView.this.rectY - ((float) cornerSide) < y && PhotoCropView.this.rectY + ((float) cornerSide) > y) {
                        PhotoCropView.this.draggingState = 1;
                    } else if ((PhotoCropView.this.rectX - ((float) cornerSide)) + PhotoCropView.this.rectSizeX < x && (PhotoCropView.this.rectX + ((float) cornerSide)) + PhotoCropView.this.rectSizeX > x && PhotoCropView.this.rectY - ((float) cornerSide) < y && PhotoCropView.this.rectY + ((float) cornerSide) > y) {
                        PhotoCropView.this.draggingState = 2;
                    } else if (PhotoCropView.this.rectX - ((float) cornerSide) < x && PhotoCropView.this.rectX + ((float) cornerSide) > x && (PhotoCropView.this.rectY - ((float) cornerSide)) + PhotoCropView.this.rectSizeY < y && (PhotoCropView.this.rectY + ((float) cornerSide)) + PhotoCropView.this.rectSizeY > y) {
                        PhotoCropView.this.draggingState = 3;
                    } else if ((PhotoCropView.this.rectX - ((float) cornerSide)) + PhotoCropView.this.rectSizeX < x && (PhotoCropView.this.rectX + ((float) cornerSide)) + PhotoCropView.this.rectSizeX > x && (PhotoCropView.this.rectY - ((float) cornerSide)) + PhotoCropView.this.rectSizeY < y && (PhotoCropView.this.rectY + ((float) cornerSide)) + PhotoCropView.this.rectSizeY > y) {
                        PhotoCropView.this.draggingState = 4;
                    } else if (PhotoCropView.this.rectX >= x || PhotoCropView.this.rectX + PhotoCropView.this.rectSizeX <= x || PhotoCropView.this.rectY >= y || PhotoCropView.this.rectY + PhotoCropView.this.rectSizeY <= y) {
                        PhotoCropView.this.draggingState = 0;
                    } else {
                        PhotoCropView.this.draggingState = 5;
                    }
                    if (PhotoCropView.this.draggingState != 0) {
                        PhotoCropView.this.requestDisallowInterceptTouchEvent(true);
                    }
                    PhotoCropView.this.oldX = x;
                    PhotoCropView.this.oldY = y;
                } else if (motionEvent.getAction() == 1) {
                    PhotoCropView.this.draggingState = 0;
                } else if (motionEvent.getAction() == 2 && PhotoCropView.this.draggingState != 0) {
                    float diffX = x - PhotoCropView.this.oldX;
                    float diffY = y - PhotoCropView.this.oldY;
                    PhotoCropView photoCropView;
                    if (PhotoCropView.this.draggingState == 5) {
                        photoCropView = PhotoCropView.this;
                        photoCropView.rectX += diffX;
                        photoCropView = PhotoCropView.this;
                        photoCropView.rectY += diffY;
                        if (PhotoCropView.this.rectX < ((float) PhotoCropView.this.bitmapX)) {
                            PhotoCropView.this.rectX = (float) PhotoCropView.this.bitmapX;
                        } else if (PhotoCropView.this.rectX + PhotoCropView.this.rectSizeX > ((float) (PhotoCropView.this.bitmapX + PhotoCropView.this.bitmapWidth))) {
                            PhotoCropView.this.rectX = ((float) (PhotoCropView.this.bitmapX + PhotoCropView.this.bitmapWidth)) - PhotoCropView.this.rectSizeX;
                        }
                        if (PhotoCropView.this.rectY < ((float) PhotoCropView.this.bitmapY)) {
                            PhotoCropView.this.rectY = (float) PhotoCropView.this.bitmapY;
                        } else if (PhotoCropView.this.rectY + PhotoCropView.this.rectSizeY > ((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight))) {
                            PhotoCropView.this.rectY = ((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight)) - PhotoCropView.this.rectSizeY;
                        }
                    } else if (PhotoCropView.this.draggingState == 1) {
                        if (PhotoCropView.this.rectSizeX - diffX < 160.0f) {
                            diffX = PhotoCropView.this.rectSizeX - 160.0f;
                        }
                        if (PhotoCropView.this.rectX + diffX < ((float) PhotoCropView.this.bitmapX)) {
                            diffX = ((float) PhotoCropView.this.bitmapX) - PhotoCropView.this.rectX;
                        }
                        if (PhotoCropView.this.freeform) {
                            if (PhotoCropView.this.rectSizeY - diffY < 160.0f) {
                                diffY = PhotoCropView.this.rectSizeY - 160.0f;
                            }
                            if (PhotoCropView.this.rectY + diffY < ((float) PhotoCropView.this.bitmapY)) {
                                diffY = ((float) PhotoCropView.this.bitmapY) - PhotoCropView.this.rectY;
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectY += diffY;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX -= diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY -= diffY;
                        } else {
                            if (PhotoCropView.this.rectY + diffX < ((float) PhotoCropView.this.bitmapY)) {
                                diffX = ((float) PhotoCropView.this.bitmapY) - PhotoCropView.this.rectY;
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectY += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX -= diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY -= diffX;
                        }
                    } else if (PhotoCropView.this.draggingState == 2) {
                        if (PhotoCropView.this.rectSizeX + diffX < 160.0f) {
                            diffX = -(PhotoCropView.this.rectSizeX - 160.0f);
                        }
                        if ((PhotoCropView.this.rectX + PhotoCropView.this.rectSizeX) + diffX > ((float) (PhotoCropView.this.bitmapX + PhotoCropView.this.bitmapWidth))) {
                            diffX = (((float) (PhotoCropView.this.bitmapX + PhotoCropView.this.bitmapWidth)) - PhotoCropView.this.rectX) - PhotoCropView.this.rectSizeX;
                        }
                        if (PhotoCropView.this.freeform) {
                            if (PhotoCropView.this.rectSizeY - diffY < 160.0f) {
                                diffY = PhotoCropView.this.rectSizeY - 160.0f;
                            }
                            if (PhotoCropView.this.rectY + diffY < ((float) PhotoCropView.this.bitmapY)) {
                                diffY = ((float) PhotoCropView.this.bitmapY) - PhotoCropView.this.rectY;
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectY += diffY;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY -= diffY;
                        } else {
                            if (PhotoCropView.this.rectY - diffX < ((float) PhotoCropView.this.bitmapY)) {
                                diffX = PhotoCropView.this.rectY - ((float) PhotoCropView.this.bitmapY);
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectY -= diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY += diffX;
                        }
                    } else if (PhotoCropView.this.draggingState == 3) {
                        if (PhotoCropView.this.rectSizeX - diffX < 160.0f) {
                            diffX = PhotoCropView.this.rectSizeX - 160.0f;
                        }
                        if (PhotoCropView.this.rectX + diffX < ((float) PhotoCropView.this.bitmapX)) {
                            diffX = ((float) PhotoCropView.this.bitmapX) - PhotoCropView.this.rectX;
                        }
                        if (PhotoCropView.this.freeform) {
                            if ((PhotoCropView.this.rectY + PhotoCropView.this.rectSizeY) + diffY > ((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight))) {
                                diffY = (((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight)) - PhotoCropView.this.rectY) - PhotoCropView.this.rectSizeY;
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX -= diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY += diffY;
                            if (PhotoCropView.this.rectSizeY < 160.0f) {
                                PhotoCropView.this.rectSizeY = 160.0f;
                            }
                        } else {
                            if ((PhotoCropView.this.rectY + PhotoCropView.this.rectSizeX) - diffX > ((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight))) {
                                diffX = ((PhotoCropView.this.rectY + PhotoCropView.this.rectSizeX) - ((float) PhotoCropView.this.bitmapY)) - ((float) PhotoCropView.this.bitmapHeight);
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX -= diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY -= diffX;
                        }
                    } else if (PhotoCropView.this.draggingState == 4) {
                        if ((PhotoCropView.this.rectX + PhotoCropView.this.rectSizeX) + diffX > ((float) (PhotoCropView.this.bitmapX + PhotoCropView.this.bitmapWidth))) {
                            diffX = (((float) (PhotoCropView.this.bitmapX + PhotoCropView.this.bitmapWidth)) - PhotoCropView.this.rectX) - PhotoCropView.this.rectSizeX;
                        }
                        if (PhotoCropView.this.freeform) {
                            if ((PhotoCropView.this.rectY + PhotoCropView.this.rectSizeY) + diffY > ((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight))) {
                                diffY = (((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight)) - PhotoCropView.this.rectY) - PhotoCropView.this.rectSizeY;
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY += diffY;
                        } else {
                            if ((PhotoCropView.this.rectY + PhotoCropView.this.rectSizeX) + diffX > ((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight))) {
                                diffX = (((float) (PhotoCropView.this.bitmapY + PhotoCropView.this.bitmapHeight)) - PhotoCropView.this.rectY) - PhotoCropView.this.rectSizeX;
                            }
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeX += diffX;
                            photoCropView = PhotoCropView.this;
                            photoCropView.rectSizeY += diffX;
                        }
                        if (PhotoCropView.this.rectSizeX < 160.0f) {
                            PhotoCropView.this.rectSizeX = 160.0f;
                        }
                        if (PhotoCropView.this.rectSizeY < 160.0f) {
                            PhotoCropView.this.rectSizeY = 160.0f;
                        }
                    }
                    PhotoCropView.this.oldX = x;
                    PhotoCropView.this.oldY = y;
                    PhotoCropView.this.invalidate();
                }
                return true;
            }
        }

        public PhotoCropView(Context context) {
            super(context);
            init();
        }

        public PhotoCropView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public PhotoCropView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        private void init() {
            this.rectPaint = new Paint();
            this.rectPaint.setColor(1073412858);
            this.rectPaint.setStrokeWidth((float) PhotoCropActivity.dp(2));
            this.rectPaint.setStyle(Style.STROKE);
            this.circlePaint = new Paint();
            this.circlePaint.setColor(-1);
            this.halfPaint = new Paint();
            //jin
//            this.halfPaint.setColor(-939524096);
//            setBackgroundColor(-13421773);
            setOnTouchListener(new C07341());
        }

        private void updateBitmapSize() {
            if (this.viewWidth != 0 && this.viewHeight != 0 && PhotoCropActivity.this.imageToCrop != null) {
                float percX = (this.rectX - ((float) this.bitmapX)) / ((float) this.bitmapWidth);
                float percY = (this.rectY - ((float) this.bitmapY)) / ((float) this.bitmapHeight);
                float percSizeX = this.rectSizeX / ((float) this.bitmapWidth);
                float percSizeY = this.rectSizeY / ((float) this.bitmapHeight);
                float w = (float) PhotoCropActivity.this.imageToCrop.getWidth();
                float h = (float) PhotoCropActivity.this.imageToCrop.getHeight();
                float scaleX = ((float) this.viewWidth) / w;
                float scaleY = ((float) this.viewHeight) / h;
                if (scaleX > scaleY) {
                    this.bitmapHeight = this.viewHeight;
                    this.bitmapWidth = (int) Math.ceil((double) (w * scaleY));
                } else {
                    this.bitmapWidth = this.viewWidth;
                    this.bitmapHeight = (int) Math.ceil((double) (h * scaleX));
                }
                this.bitmapX = ((this.viewWidth - this.bitmapWidth) / 2) + PhotoCropActivity.dp(14);
                this.bitmapY = ((this.viewHeight - this.bitmapHeight) / 2) + PhotoCropActivity.dp(14);
                if (this.rectX != -1.0f || this.rectY != -1.0f) {
                    this.rectX = (((float) this.bitmapWidth) * percX) + ((float) this.bitmapX);
                    this.rectY = (((float) this.bitmapHeight) * percY) + ((float) this.bitmapY);
                    this.rectSizeX = ((float) this.bitmapWidth) * percSizeX;
                    this.rectSizeY = ((float) this.bitmapHeight) * percSizeY;
                } else if (this.freeform) {
                    if (((float) (this.bitmapWidth / this.bitmapHeight)) > this.ratio) {
                        this.rectY = (float) this.bitmapY;
                        this.rectSizeX = ((float) this.bitmapHeight) * this.ratio;
                        this.rectSizeY = (float) this.bitmapHeight;
                        this.rectX = ((((float) this.viewWidth) - this.rectSizeX) / 2f) + ((float) PhotoCropActivity.dp(14));
                    } else {
                        this.rectX = (float) this.bitmapX;
                        this.rectSizeX = (float) this.bitmapWidth;
                        this.rectSizeY = ((float) this.bitmapWidth) / this.ratio;
                        this.rectY = ((((float) this.viewHeight) - this.rectSizeY) / 2f) + ((float) PhotoCropActivity.dp(14));
                    }
                } else if (this.bitmapWidth > this.bitmapHeight) {
                    this.rectY = (float) this.bitmapY;
                    this.rectX = (float) (((this.viewWidth - this.bitmapHeight) / 2) + PhotoCropActivity.dp(14));
                    this.rectSizeX = (float) this.bitmapHeight;
                    this.rectSizeY = (float) this.bitmapHeight;
                } else {
                    this.rectX = (float) this.bitmapX;
                    this.rectY = (float) (((this.viewHeight - this.bitmapWidth) / 2) + PhotoCropActivity.dp(14));
                    this.rectSizeX = (float) this.bitmapWidth;
                    this.rectSizeY = (float) this.bitmapWidth;
                }
                invalidate();
            }
        }

        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            this.viewWidth = (right - left) - PhotoCropActivity.dp(28);
            this.viewHeight = (bottom - top) - PhotoCropActivity.dp(28);
            updateBitmapSize();
        }

        public Bitmap getBitmap() {
            int x = (int) (((float) PhotoCropActivity.this.imageToCrop.getWidth()) * ((this.rectX - ((float) this.bitmapX)) / ((float) this.bitmapWidth)));
            int y = (int) (((float) PhotoCropActivity.this.imageToCrop.getHeight()) * ((this.rectY - ((float) this.bitmapY)) / ((float) this.bitmapHeight)));
            int sizeX = (int) (((float) PhotoCropActivity.this.imageToCrop.getWidth()) * (this.rectSizeX / ((float) this.bitmapWidth)));
            int sizeY = (int) (((float) PhotoCropActivity.this.imageToCrop.getWidth()) * (this.rectSizeY / ((float) this.bitmapWidth)));
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (x + sizeX > PhotoCropActivity.this.imageToCrop.getWidth()) {
                sizeX = PhotoCropActivity.this.imageToCrop.getWidth() - x;
            }
            if (y + sizeY > PhotoCropActivity.this.imageToCrop.getHeight()) {
                sizeY = PhotoCropActivity.this.imageToCrop.getHeight() - y;
            }
            try {
                return Bitmap.createBitmap(PhotoCropActivity.this.imageToCrop, x, y, sizeX, sizeY);
            } catch (Throwable th) {
                return null;
            }
        }

        protected void onDraw(Canvas canvas) {
            if (PhotoCropActivity.this.drawable != null) {
                PhotoCropActivity.this.drawable.setBounds(this.bitmapX, this.bitmapY, this.bitmapX + this.bitmapWidth, this.bitmapY + this.bitmapHeight);
                PhotoCropActivity.this.drawable.draw(canvas);
            }
            canvas.drawRect((float) this.bitmapX, (float) this.bitmapY, (float) (this.bitmapX + this.bitmapWidth), this.rectY, this.halfPaint);
            Canvas canvas2 = canvas;
            canvas2.drawRect((float) this.bitmapX, this.rectY, this.rectX, this.rectSizeY + this.rectY, this.halfPaint);
            canvas2 = canvas;
            canvas2.drawRect(this.rectSizeX + this.rectX, this.rectY, (float) (this.bitmapX + this.bitmapWidth), this.rectSizeY + this.rectY, this.halfPaint);
            canvas2 = canvas;
            canvas2.drawRect((float) this.bitmapX, this.rectSizeY + this.rectY, (float) (this.bitmapX + this.bitmapWidth), (float) (this.bitmapY + this.bitmapHeight), this.halfPaint);
            canvas2 = canvas;
            canvas2.drawRect(this.rectX, this.rectY, this.rectSizeX + this.rectX, this.rectSizeY + this.rectY, this.rectPaint);
            int side = PhotoCropActivity.dp(1);
            canvas2 = canvas;
            canvas2.drawRect(((float) side) + this.rectX, ((float) side) + this.rectY, ((float) PhotoCropActivity.dp(20)) + (this.rectX + ((float) side)), ((float) (side * 3)) + this.rectY, this.circlePaint);
            canvas2 = canvas;
            canvas2.drawRect(((float) side) + this.rectX, ((float) side) + this.rectY, ((float) (side * 3)) + this.rectX, ((float) PhotoCropActivity.dp(20)) + (this.rectY + ((float) side)), this.circlePaint);
            canvas2 = canvas;
            canvas2.drawRect(((this.rectX + this.rectSizeX) - ((float) side)) - ((float) PhotoCropActivity.dp(20)), ((float) side) + this.rectY, (this.rectX + this.rectSizeX) - ((float) side), ((float) (side * 3)) + this.rectY, this.circlePaint);
            canvas2 = canvas;
            canvas2.drawRect((this.rectX + this.rectSizeX) - ((float) (side * 3)), ((float) side) + this.rectY, (this.rectX + this.rectSizeX) - ((float) side), ((float) PhotoCropActivity.dp(20)) + (this.rectY + ((float) side)), this.circlePaint);
            canvas2 = canvas;
            canvas2.drawRect(((float) side) + this.rectX, ((this.rectY + this.rectSizeY) - ((float) side)) - ((float) PhotoCropActivity.dp(20)), ((float) (side * 3)) + this.rectX, (this.rectY + this.rectSizeY) - ((float) side), this.circlePaint);
            canvas2 = canvas;
            canvas2.drawRect(((float) side) + this.rectX, (this.rectY + this.rectSizeY) - ((float) (side * 3)), ((float) PhotoCropActivity.dp(20)) + (this.rectX + ((float) side)), (this.rectY + this.rectSizeY) - ((float) side), this.circlePaint);
            canvas.drawRect(((this.rectX + this.rectSizeX) - ((float) side)) - ((float) PhotoCropActivity.dp(20)), (this.rectY + this.rectSizeY) - ((float) (side * 3)), (this.rectX + this.rectSizeX) - ((float) side), (this.rectY + this.rectSizeY) - ((float) side), this.circlePaint);
            canvas.drawRect((this.rectX + this.rectSizeX) - ((float) (side * 3)), ((this.rectY + this.rectSizeY) - ((float) side)) - ((float) PhotoCropActivity.dp(20)), (this.rectX + this.rectSizeX) - ((float) side), (this.rectY + this.rectSizeY) - ((float) side), this.circlePaint);
            for (int a = 1; a < 3; a++) {
                canvas2 = canvas;
                canvas2.drawRect(((this.rectSizeX / 3.0f) * ((float) a)) + this.rectX, ((float) side) + this.rectY, ((this.rectSizeX / 3.0f) * ((float) a)) + (this.rectX + ((float) side)), (this.rectY + this.rectSizeY) - ((float) side), this.circlePaint);
                canvas2 = canvas;
                canvas2.drawRect(((float) side) + this.rectX, ((this.rectSizeY / 3.0f) * ((float) a)) + this.rectY, this.rectSizeX + (this.rectX - ((float) side)), ((float) side) + (this.rectY + ((this.rectSizeY / 3.0f) * ((float) a))), this.circlePaint);
            }
        }
    }

    public static int dp(int value) {
        return (int) Math.max(DefaultRetryPolicy.DEFAULT_BACKOFF_MULT, density * ((float) value));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        this.mActivity = this;
        OnjybApp.initImageLoader(this.mActivity);
        density = getResources().getDisplayMetrics().density;
        isTablet = false;//Boolean.valueOf(this.mActivity.getResources().getBoolean(R.bool.isTablet));
        try {
            WindowManager manager = (WindowManager) getSystemService("window");
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    if (VERSION.SDK_INT < 13) {
                        displaySize.set(display.getWidth(), display.getHeight());
                    } else {
                        display.getSize(displaySize);
                    }
                }
            }
        } catch (Exception e) {
        }
        setContentView(R.layout.photocropactivity);
        AppUtils.setStatusbarColor(this);
        this.rootView = (ViewGroup) findViewById(R.id.rootView);
        this.lytBack = findViewById(R.id.btnbck);
        this.lytBack.setOnClickListener(new C07291());
        this.btnDone = (TextView) findViewById(R.id.txtDone);
        this.btnDone.setOnClickListener(new C07302());
        this.type = getIntent().getIntExtra("type", 0);
        if (this.type == 0) {
            finish();
        }
        if (this.type == 1) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            File individualDir = new File( Constants.DIR_IMAGES);   //0923
            if (!individualDir.exists())
                individualDir.mkdir();
            //end
            this.cameraabsolutepath = Constants.DIR_IMAGES + File.separator + "croppedPhotoTmp.png";
            mImageUri = Uri.fromFile(new File(this.cameraabsolutepath));
            intent.putExtra("output", mImageUri);
            startActivityForResult(intent, 1);
            return;
        }
        Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
    }

    private void initImage() {
        Bitmap scaled = Bitmap.createScaledBitmap(this.imageToCrop, 640, (int) (((double) this.imageToCrop.getHeight()) * (640.0d / ((double) this.imageToCrop.getWidth()))), true);
        if (scaled != this.imageToCrop) {
            if (!(this.imageToCrop == null || this.imageToCrop.isRecycled())) {
                this.imageToCrop.recycle();
            }
            this.imageToCrop = null;
        }
        this.imageToCrop = scaled;
        this.drawable = new BitmapDrawable(this.imageToCrop);
        this.view = new PhotoCropView(this);
        String group = getIntent().getStringExtra("group");
        if (group != null && group.equals("group")) {
            this.view.freeform = true;
        }
        this.rootView.addView(this.view, new LayoutParams(-1, -1));
    }

    protected void onDestroy() {
        super.onDestroy();
        this.drawable = null;
        if (this.imageToCrop != null && !this.sameBitmap) {
            this.imageToCrop.recycle();
            this.imageToCrop = null;
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //0924
        if(resultCode != -1)
            finish();
        else{
            if(requestCode == 1){
                //from camera
                this.imageToCrop = grabImage();
            }else if (requestCode == 2){
                //from gallery
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    this.imageToCrop = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    finish();
                }
            }
//            initImage();
            Bitmap bitmap = this.imageToCrop;//PhotoCropActivity.this.view.getBitmap();
            String absolutepath = Constants.DIR_IMAGES + File.separator + "croppedPhotoTmp.png";
            AppUtils.writeBmpToFile(bitmap, absolutepath);
            if (bitmap == PhotoCropActivity.this.imageToCrop) {
                PhotoCropActivity.this.sameBitmap = true;
            } else if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
            }
            Intent intent = new Intent();
            intent.putExtra("path", absolutepath);
            PhotoCropActivity.this.setResult(-1, intent);
            PhotoCropActivity.this.finish();
        }
        //end

    /*
        if (resultCode != -1) {
            finish();
        } else if (requestCode == 1) {
            try {
                settImage(this.cameraabsolutepath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2) {
            try {
                Uri picUri = data.getData();
                this.absolutepath = AppUtils.getRealPathFromURI(this.context, picUri);
                Log.v("absolutepath", this.absolutepath);
                if (this.absolutepath.startsWith("http")) {
                    try {
                        File file = new File(Constants.DIR_IMAGES);
                        file.mkdirs();
                        final File file2 = new File(file, "croppedPhotoTmp.png");
                        if (!file2.exists()) {
                            file2.createNewFile();
                        }
                        Thread thread = new Thread(new Runnable() {

                            class C07311 implements DownloadListener {
                                C07311() {
                                }

                                public void onDownloadSuccessful() {
                                    PhotoCropActivity.this.absolutepath = file2.getAbsolutePath();
                                    Log.v("temppath", PhotoCropActivity.this.temppath);
                                    PhotoCropActivity.this.absolutepath = PhotoCropActivity.this.temppath;
                                    Log.d("onDownloadSuccessful->", PhotoCropActivity.this.isSuccess + "");
                                }

                                public void onDownloadFailed(String errorMessage) {
                                    Log.d("onDownloadFailed()->", PhotoCropActivity.this.isSuccess + "");
                                }
                            }

                            public void run() {
                                PhotoCropActivity.this.isSuccess = FileDownloader.downloadFile(PhotoCropActivity.this.absolutepath, file2, new C07311());
                                Log.d("FileDownloader status", PhotoCropActivity.this.isSuccess + "");
                            }
                        });
                        thread.start();
                        thread.join();
                        if (this.isSuccess) {
                            this.absolutepath = file2.getAbsolutePath();
                            settImage(this.absolutepath);
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                } else if (this.absolutepath == null || !this.absolutepath.isEmpty()) {
                    settImage(this.absolutepath);
                } else {
                    AppUtils.getPath(this.context, picUri);
                    settImage(this.absolutepath);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        */
    }

    private void settImage(String photoPath) {
        if (photoPath == null) {
            finish();
            return;
        }
        int size;
        if (isTablet.booleanValue()) {
            size = dp(520);
        } else {
            size = Math.max(displaySize.x, displaySize.y);
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new SimpleBitmapDisplayer()).build();
        if (photoPath == null || photoPath.length() <= 0) {
            ImageLoader.getInstance().loadImage(photoPath, options, new C07334());
            return;
        }
        try {
            MemoryCacheUtils.removeFromCache(photoPath, ImageLoader.getInstance().getMemoryCache());
//            DiscCacheUtils.removeFromCache(photoPath, ImageLoader.getInstance().getDiskCache());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.imageToCrop = BitmapFactory.decodeFile(photoPath, new Options());
        if (this.imageToCrop == null) {
            finish();
        } else {
            initImage();
        }
    }
    //0924
    public Bitmap grabImage()
    {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = null;
        try
        {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            ExifInterface ei = new ExifInterface(this.cameraabsolutepath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            return  rotatedBitmap;
        }
        catch (Exception e)
        {
        }

        return bitmap;
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    //end
}

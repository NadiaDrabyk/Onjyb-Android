package com.onjyb.helper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.db.AttachmentMap;
import com.onjyb.util.AppUtils;
import java.util.ArrayList;

public class SlideShowActivity extends Activity {
    private String TAG = "SlideShowActivity";
    private ImageButton btnClose;
    OnClickListener closeSliderCliclListener = new C05801();
    private ArrayList<AttachmentMap> images;
    private TextView lblCount;
    private TextView lblDate;
    private TextView lblTitle;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int selectedPosition = 0;
    private ViewPager viewPager;
    OnPageChangeListener viewPagerPageChangeListener = new C05812();

    class C05801 implements OnClickListener {
        C05801() {
        }

        public void onClick(View v) {
            SlideShowActivity.this.finish();
        }
    }

    class C05812 implements OnPageChangeListener {
        C05812() {
        }

        public void onPageSelected(int position) {
            SlideShowActivity.this.displayMetaInfo(position);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public Object instantiateItem(ViewGroup container, int position) {
            this.layoutInflater = (LayoutInflater) SlideShowActivity.this.getSystemService("layout_inflater");
            View view = this.layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);
            TouchImageView imageViewPreview = (TouchImageView) view.findViewById(R.id.image_preview);
            AttachmentMap image = (AttachmentMap) SlideShowActivity.this.images.get(position);
            String url_static = "http://api.androidhive.info/images/glide/small/deadpool.jpg";
            String path = "";
            if (image.getAttachmentId() != null) {
                path = Constants.BASE_IMAGE_URL + image.getAttachmentName();
            } else {
                path = image.getAttachmentName();
            }
            Glide.with(SlideShowActivity.this).load(path).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageViewPreview);
            container.addView(view);
            return view;
        }

        public int getCount() {
            return SlideShowActivity.this.images.size();
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image_slider);
        AppUtils.setStatusbarColor(this);
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.lblCount = (TextView) findViewById(R.id.lbl_count);
        this.lblTitle = (TextView) findViewById(R.id.title);
        this.lblDate = (TextView) findViewById(R.id.date);
        this.btnClose = (ImageButton) findViewById(R.id.lbl_close);
        this.images = (ArrayList) getIntent().getSerializableExtra("ImageList");
        Log.d(this.TAG, "position: " + this.selectedPosition);
        Log.d(this.TAG, "images size: " + this.images.size());
        this.myViewPagerAdapter = new MyViewPagerAdapter();
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(this.viewPagerPageChangeListener);
        setCurrentItem(this.selectedPosition);
        this.btnClose.setOnClickListener(this.closeSliderCliclListener);
    }

    private void setCurrentItem(int position) {
        this.viewPager.setCurrentItem(position, false);
        displayMetaInfo(this.selectedPosition);
    }

    private void displayMetaInfo(int position) {
        this.lblCount.setText((position + 1) + " of " + this.images.size());
        this.lblTitle.setText(((AttachmentMap) this.images.get(position)).getMapName());
    }
}

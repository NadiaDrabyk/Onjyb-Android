package com.onjyb.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.onjyb.R;
import com.onjyb.Constants;
import com.onjyb.db.AttachmentMap;

public class ImageHolder extends FrameLayout {
    private ImageButton btndelete;
    Context context;
    private ImageView imageView;
    int index = -1;
    boolean isNotEditable = false;

    public ImageHolder(Context context, OnClickListener deleteBtnClickListener, OnClickListener viewImageListClickListener) {
        super(context);
        this.context = context;
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.imageholder, this, true);
        this.imageView = (ImageView) findViewById(R.id.imgview1);
        this.btndelete = (ImageButton) findViewById(R.id.imgcancel);
        this.btndelete.setOnClickListener(deleteBtnClickListener);
        this.imageView.setOnClickListener(viewImageListClickListener);
    }

    public ImageHolder(Context context, boolean isNotDeletable) {
        super(context);
        this.context = context;
        this.isNotEditable = isNotDeletable;
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.imageholder, this, true);
        this.imageView = (ImageView) findViewById(R.id.imgview1);
        this.btndelete = (ImageButton) findViewById(R.id.imgcancel);
        if (this.isNotEditable) {
            this.btndelete.setVisibility(GONE);
            this.btndelete.setClickable(false);
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public void setImage(Bitmap bitmap) {
        this.imageView.setImageBitmap(bitmap);
    }

    public void setImage(AttachmentMap attachment) {
        ImageLoader.getInstance().displayImage(Constants.BASE_IMAGE_URL + attachment.getAttachmentName(), this.imageView);
    }
}

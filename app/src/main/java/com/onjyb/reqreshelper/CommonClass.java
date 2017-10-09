package com.onjyb.reqreshelper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;

public class CommonClass {
    private Context context;

    public CommonClass(Context context) {
        this.context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) this.context.getSystemService("connectivity");
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo state : info) {
                    if (state.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = this.context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("_data"));
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, new String[]{"_data"}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow("_data");
            cursor.moveToFirst();
            String string = cursor.getString(column_index);
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void customToastMsg(Context context, String message) {
        Toast toast = new Toast(context);
        toast.setGravity(80, 0, 0);
        toast.setDuration(1);
        toast.setText(message);
        toast.show();
    }
}

package com.onjyb.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.onjyb.R;
import com.onjyb.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.apache.commons.lang3.time.DateUtils;

import dalvik.bytecode.Opcodes;
//import org.codehaus.jackson.org.objectweb.asm.Opcodes;

public class AppUtils {
    static final int MAX_IMAGE_DIMENSION = 400;
    public static final String SIM_ABSENT = "Absent";
    public static final String SIM_READY = "Ready";
    public static final String SIM_UNKNOWN = "Unknown";
    public static final String TAG = "AppUtils";

    static class C07971 implements OnScanCompletedListener {
        C07971() {
        }

        public void onScanCompleted(String path, Uri uri) {
            Log.e("MediaScannerConnection", "onScanCompleted()");
        }
    }

    public static long getMinutesFromHours(String hrs, String min) {
        try {
            if (!"".equalsIgnoreCase(hrs) && !"".equalsIgnoreCase(min)) {
                return (long) ((Integer.parseInt(hrs) * 60) + Integer.parseInt(min));
            }
            if (!"".equalsIgnoreCase(min)) {
                return (long) Integer.parseInt(min);
            }
            if ("".equalsIgnoreCase(hrs)) {
                return 0;
            }
            return (long) (Integer.parseInt(hrs) * 60);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDateDifference(String dateOne, String dateTwo, String formate) {
        SimpleDateFormat input = new SimpleDateFormat(formate);
        try {
            return getDateDiffString(input.parse(dateOne), input.parse(dateTwo));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDateDiffString(Date dateOne, Date dateTwo) {
        long delta = (dateTwo.getTime() - dateOne.getTime()) / DateUtils.MILLIS_PER_DAY;
        if (delta > 0) {
            return String.valueOf(delta);
        }
        return String.valueOf(delta);
    }

    public static double roundTwoDecimals(double d) {
        return Double.valueOf(new DecimalFormat("#.##").format(d)).doubleValue();
    }

    private static String getIncrementByOne(Date date, int count) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yy");
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(5, count);
            return format.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getIncrementByOne(String date, int count) {
        try {
            return getIncrementByOne(new SimpleDateFormat("dd MMM yy").parse(date), count);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFormattedDate(String date) {
        Exception e;
        try {
            Date ndate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat simpleDateFormat;
            try {
                String str = dateformat.format(ndate);
                if (str != null) {
                    simpleDateFormat = dateformat;
                    return str;
                }
                return "";
            } catch (Exception e2) {
                e = e2;
                simpleDateFormat = dateformat;
                e.printStackTrace();
                return "";
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return "";
        }
    }

    public static String getFormattedDate(String date, String cuurentform, String requireform) {
        SimpleDateFormat simpleDateFormat;
        Exception e;
        try {
            Date ndate = new SimpleDateFormat(cuurentform).parse(date);
            SimpleDateFormat dateformat = new SimpleDateFormat(requireform);
            try {
                String str = dateformat.format(ndate);
                if (str != null) {
                    simpleDateFormat = dateformat;
                    return str;
                }
                return "";
            } catch (Exception e2) {
                e = e2;
                simpleDateFormat = dateformat;
                e.printStackTrace();
                return "";
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return "";
        }
    }

    public static String UpperCaseWords(String line) {
        String[] data = line.trim().toLowerCase().split("\\s");
        line = "";
        for (int i = 0; i < data.length; i++) {
            if (data[i].length() > 1) {
                line = line + data[i].substring(0, 1).toUpperCase() + data[i].substring(1) + " ";
            } else {
                line = line + data[i].toUpperCase();
            }
        }
        return line.trim();
    }

    public static String getFullFormattedDate(String date) {
        try {
            String str = DateFormat.format("dd-MM-yyyy hh:mm:ss", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date)).toString();
            if (str != null) {
                return str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void runMediaScan(Context context, String dir) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{dir}, new String[]{"image/*"}, new C07971());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStatusbarColor(Context context, int resId) {
        if (VERSION.SDK_INT > 19) {
            Window window = ((Activity) context).getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
        }
    }

    public static String convertLocalTimeZone(String timeZone, String createDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date date = formatter.parse(createDate);
            formatter.setTimeZone(TimeZone.getDefault());
            return formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void clearApplicationData(Context context) {
        File appDir = new File(context.getCacheDir().getParent());
        if (appDir.exists()) {
            for (String s : appDir.list()) {
                if (deleteDir(new File(appDir, s))) {
                    Log.i(TAG, String.format("**************** DELETED -> (%s) *******************", new Object[]{new File(appDir, s).getAbsolutePath()}));
                }
            }
        }
    }

    public static boolean isEmailValid(String email) {
        if (Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", 2).matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static void compressImagePNG(Context context, String filePath, String destfilepath) {
        FileNotFoundException e;
        Bitmap scaledBitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) (actualWidth / actualHeight);
        float maxRatio = 612.0f / 816.0f;
        if (((float) actualHeight) > 816.0f || ((float) actualWidth) > 612.0f) {
            if (imgRatio < maxRatio) {
                actualWidth = (int) (((float) actualWidth) * (816.0f / ((float) actualHeight)));
                actualHeight = (int) 816.0f;
            } else if (imgRatio > maxRatio) {
                actualHeight = (int) (((float) actualHeight) * (612.0f / ((float) actualWidth)));
                actualWidth = (int) 612.0f;
            } else {
                actualHeight = (int) 816.0f;
                actualWidth = (int) 612.0f;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
        } catch (OutOfMemoryError exception2) {
            exception2.printStackTrace();
        }
        float ratioX = ((float) actualWidth) / ((float) options.outWidth);
        float ratioY = ((float) actualHeight) / ((float) options.outHeight);
        float middleX = ((float) actualWidth) / 2.0f;
        float middleY = ((float) actualHeight) / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
        try {
            int orientation = new ExifInterface(filePath).getAttributeInt("Orientation", 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90.0f);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180.0f);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270.0f);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            OutputStream fileOutputStream = new FileOutputStream(destfilepath);
            OutputStream outputStream;
            try {
                scaledBitmap.compress(CompressFormat.PNG, 80, fileOutputStream);
                outputStream = fileOutputStream;
            } catch (Exception e3) {
                //e = e3;
                //outputStream = fileOutputStream;
                //e.printStackTrace();
            }
        } catch (FileNotFoundException e4) {
            e = e4;
            e.printStackTrace();
        }
    }

    public static void compressImageJPG(Context context, String filePath, String destfilepath) {
        FileNotFoundException e;
        Bitmap scaledBitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = (float) (actualWidth / actualHeight);
        float maxRatio = 612.0f / 816.0f;
        if (((float) actualHeight) > 816.0f || ((float) actualWidth) > 612.0f) {
            if (imgRatio < maxRatio) {
                actualWidth = (int) (((float) actualWidth) * (816.0f / ((float) actualHeight)));
                actualHeight = (int) 816.0f;
            } else if (imgRatio > maxRatio) {
                actualHeight = (int) (((float) actualHeight) * (612.0f / ((float) actualWidth)));
                actualWidth = (int) 612.0f;
            } else {
                actualHeight = (int) 816.0f;
                actualWidth = (int) 612.0f;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
        } catch (OutOfMemoryError exception2) {
            exception2.printStackTrace();
        }
        float ratioX = ((float) actualWidth) / ((float) options.outWidth);
        float ratioY = ((float) actualHeight) / ((float) options.outHeight);
        float middleX = ((float) actualWidth) / /*ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT*/2f;
        float middleY = ((float) actualHeight) / /*ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT*/2f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
        try {
            int orientation = new ExifInterface(filePath).getAttributeInt("Orientation", 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90.0f);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180.0f);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270.0f);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            OutputStream fileOutputStream = new FileOutputStream(destfilepath);
            OutputStream outputStream;
            try {
                scaledBitmap.compress(CompressFormat.JPEG, 80, fileOutputStream);
                outputStream = fileOutputStream;
            } catch (Exception e3) {
                //e = e3;
                outputStream = fileOutputStream;
                //e.printStackTrace();
            }
        } catch (FileNotFoundException e4) {
            e = e4;
            e.printStackTrace();
        }
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        if (height <= reqHeight && width <= reqWidth) {
            return 1;
        }
        int heightRatio = Math.round(((float) height) / ((float) reqHeight));
        int widthRatio = Math.round(((float) width) / ((float) reqWidth));
        if (heightRatio < widthRatio) {
            return heightRatio;
        }
        return widthRatio;
    }

    public static void createFileFromBitmap(String filepath, Bitmap bitmap) {
        try {
            File f = new File(filepath);
            if (f.exists()) {
                f.delete();
                f = new File(filepath);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.close();
        } catch (Exception e) {
            Log.e("erro in file write", e.toString());
        }
    }

    public static void hideKeyBoard(Activity context) {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null)
        {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setListViewHeightBasedOnChildrenLatest(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    public static int getListViewHeightBasedOnChildrenLatest(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        LayoutParams params = listView.getLayoutParams();
        return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    public static void runMediaScan(Context context, File file) {
        try {
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openAttachmentFile(Context context, String filePath) {
        String url = filePath;
        try {
            String type;
            if (url.lastIndexOf(".") != -1) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(url.substring(url.lastIndexOf(".") + 1));
                Log.e("Mime type", type);
            } else {
                type = null;
            }
            if (type != null) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setDataAndType(Uri.parse("file://" + url), type);
                context.startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    public static String getFormattedDateFromTimestamp(long timestampInMilliSeconds, String format) {
        try {
            Date date = new Date();
            date.setTime(timestampInMilliSeconds);
            return new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTimeFromTimestamp(long timestampInMilliSeconds) {
        try {
            long days = TimeUnit.MILLISECONDS.toDays(timestampInMilliSeconds);
            timestampInMilliSeconds -= TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(timestampInMilliSeconds);
            timestampInMilliSeconds -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timestampInMilliSeconds);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timestampInMilliSeconds - TimeUnit.MINUTES.toMillis(minutes));
            String dhms = "";
            if (days > 0) {
                return days + " days " + hours + " hrs " + minutes + " mins";
            }
            if (hours > 0) {
                return hours + " hrs " + minutes + " mins " + seconds + " secs";
            }
            if (minutes > 0) {
                return minutes + " mins " + seconds + " secs";
            }
            return seconds + " secs";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isToday(long timestampInMilliSeconds) {
        try {
            Date date = new Date();
            date.setTime(timestampInMilliSeconds);
            return new SimpleDateFormat("yyyy/MM/dd").format(date).equalsIgnoreCase(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void checkOrCreateAppDirectories(Context context) {
        Log.e(AppUtils.class + "", " : checkOrCreateAppDirectories()");
        /*Storage.verifyDirPath(Constants.APP_HOME);
        Storage.verifyDirPath(Constants.DIR_MEDIA);
        Storage.verifyDirPath(Constants.DIR_IMAGES);
        Storage.verifyDirPath(Constants.DIR_AUDIO);
        Storage.verifyDirPath(Constants.DIR_VIDEO);
    */}

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, context.getResources().getDisplayMetrics());
    }

    public static void hideSoftKeyboard(InputMethodManager imm, View view) {
        try {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public static boolean isConnectedToInternet(Context _context) {
        boolean z = false;
        if (_context != null) {
            try {
                ConnectivityManager cm = (ConnectivityManager) _context.getSystemService("connectivity");
                if (cm != null) {
                    z = cm.getActiveNetworkInfo().isConnected();
                }
            } catch (Exception e) {
            }
        }
        return z;
    }

    public static boolean verifyAllPermissions(int[] permissions) {
        for (int result : permissions) {
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    public static void setStatusbarColor(Activity activity) {
        try {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            if (VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(ContextCompat.getColor(activity, R.color.grey));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasSelfPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != 0) {
                return false;
            }
        }
        return true;
    }

    public static HashMap<String, Object> getDefaultParameterMap(Context context) {
        HashMap<String, Object> map = new HashMap();
        try {
            String appVersionName = "";
            map.put("app_version", context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
            map.put("language", Locale.getDefault().getDisplayLanguage());
            map.put("os_version", VERSION.RELEASE);
            map.put("device_os", "android");
            map.put("os", "android");
            map.put("device_id", Constants.DEVICE_ID);
            map.put("appIdentifier", "com.etechmavens.buildingapp");
            map.put("debug", "0");
            map.put("device_name", Build.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String getCurrentAppVersion(Context context) {
        String appVersionName = "";
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return appVersionName;
        }
    }

    public static HashMap<String, Object> getDefaultPerameters(Context context) {
        HashMap<String, Object> map = new HashMap();
        try {
            String appVersionName = "";
            appVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            map.put("language", Locale.getDefault().getDisplayLanguage());
            map.put("os", "android");
            map.put("app_version", appVersionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String removeSpecialCharactersFromString(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }
        return text.replaceAll("[^0-9]+", "");
    }

    public static boolean validatePhone(String phone) {
        if (phone == null || phone.length() == 0) {
            return false;
        }
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static boolean validateEmail(String email) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static byte[] readFile(File file) {
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(file, "r");
            long longlength = f.length();
            int length = (int) longlength;
            if (((long) length) != longlength) {
                throw new IOException("File size >= 2 GB");
            }
            byte[] data = new byte[length];
            f.readFully(data);
            f.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }/* catch (Throwable th) {
            f.close();
            return null;
        }*/
    }


    public static int indexOfStringArray(String[] strArray, String strFind) {
        for (int index = 0; index < strArray.length; index++) {
            if (strArray[index].toUpperCase().equals(strFind.toUpperCase())) {
                return index;
            }
        }
        return -1;
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static boolean IsFreeMemoryAvailableForFile(long filesize) {
        if (filesize < getAvailableExternalMemorySize()) {
            return true;
        }
        return false;
    }

    public static long getAvailableInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static long getTotalInternalMemorySize() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public static long getAvailableExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return 0;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static long getTotalExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return 0;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public static long getFileSize(String dir, String filename) {
        try {
            return new File(dir, filename).length();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String formatSize(long size) {
        String suffix;
        float fSize = (float) size;
        if (fSize >= 1024.0f) {
            suffix = " KB";
            fSize /= 1024.0f;
            if (fSize >= 1024.0f) {
                suffix = " MB";
                fSize /= 1024.0f;
            }
        } else {
            suffix = " Bytes";
        }
        StringBuilder resultBuffer = new StringBuilder(String.format("%.2f", new Object[]{Float.valueOf(fSize)}));
        if (suffix != null) {
            resultBuffer.append(suffix);
        }
        return resultBuffer.toString();
    }

    public static boolean writeBmpToFile(Bitmap bm, String filePath) {
        File file = new File(filePath);
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(CompressFormat.JPEG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? /*ImageRequest.DEFAULT_IMAGE_TIMEOUT_MS*/1000 : 1024;
        if (bytes < ((long) unit)) {
            return bytes + " B";
        }
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(((int) (Math.log((double) bytes) / Math.log((double) unit))) - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", new Object[]{Double.valueOf(((double) bytes) / Math.pow((double) unit, (double) ((int) (Math.log((double) bytes) / Math.log((double) unit))))), pre});
    }

    public static int dpToPixel(float dp, Context context) {
        return (int) TypedValue.applyDimension(1, dp, context.getResources().getDisplayMetrics());
    }

    public static float pixelToDp(Context context, float px) {
        return TypedValue.applyDimension(0, px, context.getResources().getDisplayMetrics());
    }

    public static int getDeviceWidth(Context mContext) {
        if (VERSION.SDK_INT < 13) {
            return ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        }
        Display display = ((WindowManager) mContext.getSystemService("window")).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static void displayBottomDialog(Dialog dialog, String diaTitle, String btntext1, String btntext2, String btntext4, String btntext3, OnClickListener btnListener1, OnClickListener btnListener2, OnClickListener btnListener3, OnClickListener btnListener4, OnClickListener btnRemoveListener) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = 48;
        window.setAttributes(wlp);
        dialog.getWindow().addFlags(2);
        window.setBackgroundDrawableResource(R.color.WhiteSmoke);
        dialog.setContentView(R.layout.photo_options);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(-1, -2);
        ((TextView) dialog.findViewById(R.id.txtDiaTitle)).setText(diaTitle);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.button1);
        dialogButton1.setText(btntext1);
        dialogButton1.setOnClickListener(btnListener1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.button2);
        dialogButton2.setText(btntext2);
        dialogButton2.setOnClickListener(btnListener2);
        Button dialogButton3 = (Button) dialog.findViewById(R.id.button3);
        dialogButton3.setText(btntext3);
        dialogButton3.setOnClickListener(btnListener3);
        Button removePhoto = (Button) dialog.findViewById(R.id.btnRemovePhoto);
        removePhoto.setText(btntext4);
        removePhoto.setOnClickListener(btnRemoveListener);
        Button dialogButton4 = (Button) dialog.findViewById(R.id.button4);
        if (btnListener4 == null) {
            dialogButton4.setVisibility(View.GONE);
        } else {
            dialogButton4.setVisibility(View.VISIBLE);
            dialogButton4.setOnClickListener(btnListener4);
        }
        dialog.show();

    }

    public static void displayBottomDialogWithDiffColor(Dialog dialog, String diaTitle, String btntext1, String btntext2, String btntext3, OnClickListener btnListener1, OnClickListener btnListener2, OnClickListener btnListener3, OnClickListener btnListener4) {
    }

    public static int getDeviceHeight(Context mContext) {
        if (VERSION.SDK_INT < 13) {
            return ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        }
        Display display = ((WindowManager) mContext.getSystemService("window")).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;
        float r;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = (float) (bitmap.getHeight() / 2);
        } else {
            r = (float) (bitmap.getWidth() / 2);
        }
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        //   paint.setColor(-12434878);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap getThumbnailBitmap(String path, int thumbnailSize) {
        try {
            Options bounds = new Options();
            bounds.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, bounds);
            if (bounds.outWidth == -1 || bounds.outHeight == -1) {
            }
            int originalSize = bounds.outHeight > bounds.outWidth ? bounds.outHeight : bounds.outWidth;
            Options opts = new Options();
            opts.inSampleSize = originalSize / thumbnailSize;
            return BitmapFactory.decodeFile(path, opts);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeFile(File f, int WIDTH, int HIGHT) {
        Bitmap bitmap = null;
        try {
            Options o = new Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int REQUIRED_WIDTH = WIDTH;
            int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while ((o.outWidth / scale) / 2 >= REQUIRED_WIDTH && (o.outHeight / scale) / 2 >= REQUIRED_HIGHT) {
                scale *= 2;
            }
            Options o2 = new Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeFile(File f) {
        Bitmap bitmap = null;
        if (f != null) {
            try {
                if (f.exists()) {
                    bitmap = BitmapFactory.decodeFile(f.getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static boolean rotateBitmap(File inFile, File outFile, int angle) throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        FileOutputStream outStream = null;
        Options options = new Options();
        Matrix matrix = new Matrix();
        matrix.postRotate((float) angle);
        options.inSampleSize = 1;
        while (options.inSampleSize <= 32) {
            try {
                FileInputStream inStream = new FileInputStream(inFile);
                try {
                    Bitmap originalBitmap = BitmapFactory.decodeStream(inStream, null, options);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, true);
                    FileOutputStream outStream2 = new FileOutputStream(outFile);
                    try {
                        rotatedBitmap.compress(CompressFormat.JPEG, 100, outStream2);
                        outStream2.close();
                        originalBitmap.recycle();
                        rotatedBitmap.recycle();
                        if (outStream2 != null) {
                            try {
                                outStream2.close();
                            } catch (IOException e) {
                            }
                        }
                        outStream = outStream2;
                        fileInputStream = inStream;
                        return true;
                    } catch (OutOfMemoryError e2) {
                        outStream = outStream2;
                        fileInputStream = inStream;
                    } catch (Throwable th2) {
                        th = th2;
                        outStream = outStream2;
                        fileInputStream = inStream;
                    }
                } catch (OutOfMemoryError e3) {
                    fileInputStream = inStream;
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e4) {
                        }
                    }
                    options.inSampleSize++;
                } catch (Throwable th3) {
                    th = th3;
                    fileInputStream = inStream;
                }
            } catch (OutOfMemoryError e5) {
                if (outStream != null) {
                    outStream.close();
                }
                options.inSampleSize++;
            } catch (Throwable th4) {
                th = th4;
            }
        }
        return false;
      /*  if (outStream != null) {
            try {
                outStream.close();
            } catch (IOException e6) {
            }
        }
        */
        // throw th;
        //  throw th;
    }

    public static Bitmap rotateBitmap(Bitmap b, int degrees) {
        if (degrees == 0 || b == null) {
            return b;
        }
        try {
            Matrix m = new Matrix();
            m.setRotate((float) degrees, ((float) b.getWidth()) / /*ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT*/2f, ((float) b.getHeight()) / /*ImageRequest.DEFAULT_IMAGE_BACKOFF_MULT*/2f);
            System.gc();
            Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, false);
            if (b == b2) {
                return b;
            }
            b.recycle();
            return b2;
        } catch (OutOfMemoryError ex) {
            throw ex;
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
    }

    public static Bitmap decodeFileNew(File f) {
        try {
            Log.e("getImageLoader", "decodeFileNew : " + f.getAbsolutePath());
            Options o = new Options();
            o.inJustDecodeBounds = true;
            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            int scale = 1;
            if (o.outHeight > MAX_IMAGE_DIMENSION || o.outWidth > MAX_IMAGE_DIMENSION) {
                scale = (int) Math.pow(2.0d, (double) ((int) Math.ceil(Math.log(400.0d / ((double) Math.max(o.outHeight, o.outWidth))) / Math.log(0.5d))));
            }
            Options o2 = new Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            Bitmap b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap loadBitmap(File f) {
        Options btmapOptions = new Options();
        btmapOptions.inJustDecodeBounds = true;
        try {
            int rotatedWidth;
            int rotatedHeight;
            BitmapFactory.decodeStream(new FileInputStream(f), null, btmapOptions);
            int orientation = new ExifInterface(f.getPath()).getAttributeInt("Orientation", 1);
            int angle = 0;
            if (orientation == 6) {
                angle = 90;
                rotatedWidth = btmapOptions.outHeight;
                rotatedHeight = btmapOptions.outWidth;
            } else if (orientation == 3) {
                //   angle = Opcodes.GETFIELD;
                rotatedWidth = btmapOptions.outWidth;
                rotatedHeight = btmapOptions.outHeight;
            } else if (orientation == 8) {
                angle = 270;
                rotatedWidth = btmapOptions.outHeight;
                rotatedHeight = btmapOptions.outWidth;
            } else {
                rotatedWidth = btmapOptions.outWidth;
                rotatedHeight = btmapOptions.outHeight;
            }
            Matrix mat = new Matrix();
            mat.postRotate((float) angle);
            Options options = new Options();
            if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
                options.inSampleSize = (int) Math.max(((float) rotatedWidth) / 400.0f, ((float) rotatedHeight) / 400.0f);
            }
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", "loadBitmap() " + e, e);
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String convertStreamToString(java.io.InputStream r6) {
        /*
        r2 = new java.io.BufferedReader;
        r4 = new java.io.InputStreamReader;
        r4.<init>(r6);
        r2.<init>(r4);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = 0;
    L_0x0010:
        r1 = r2.readLine();	 Catch:{ IOException -> 0x002d }
        if (r1 == 0) goto L_0x0039;
    L_0x0016:
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x002d }
        r4.<init>();	 Catch:{ IOException -> 0x002d }
        r4 = r4.append(r1);	 Catch:{ IOException -> 0x002d }
        r5 = "\n";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x002d }
        r4 = r4.toString();	 Catch:{ IOException -> 0x002d }
        r3.append(r4);	 Catch:{ IOException -> 0x002d }
        goto L_0x0010;
    L_0x002d:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0047 }
        r6.close();	 Catch:{ IOException -> 0x0042 }
    L_0x0034:
        r4 = r3.toString();
        return r4;
    L_0x0039:
        r6.close();	 Catch:{ IOException -> 0x003d }
        goto L_0x0034;
    L_0x003d:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0034;
    L_0x0042:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0034;
    L_0x0047:
        r4 = move-exception;
        r6.close();	 Catch:{ IOException -> 0x004c }
    L_0x004b:
        throw r4;
    L_0x004c:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x004b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.onjyba.util.AppUtils.convertStreamToString(java.io.InputStream):java.lang.String");
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & 15) >= 3;
    }

    public static Bitmap resize(Bitmap bitMap, int width, int height) {
        int bitWidth = bitMap.getWidth();
        int bitHeight = bitMap.getHeight();
        int per;
        if (bitHeight < bitWidth) {
            per = (height * 100) / bitHeight;
            bitHeight = height;
            bitWidth = (bitWidth * per) / 100;
        } else {
            per = (width * 100) / bitWidth;
            bitWidth = width;
            bitHeight = (bitHeight * per) / 100;
        }
        return Bitmap.createScaledBitmap(bitMap, bitWidth, bitHeight, false);
    }

    public static Bitmap cropImage(Bitmap bitMap, int Width, int Height) {
        int bitWidth = bitMap.getWidth();
        int bitHeight = bitMap.getHeight();
        int X = 0;
        int Y = 0;
        if (bitHeight < bitWidth) {
            X = (bitWidth / 2) - (Width / 2);
        } else {
            Y = (bitHeight / 2) - (Height / 2);
        }
        if (X + Width > bitWidth || Y + Height > bitHeight) {
            return bitMap;
        }
        return Bitmap.createBitmap(bitMap, X, Y, Width, Height);
    }

    public static void saveBitmap(Bitmap bitmap, String fileName) {
        Exception e;
        Throwable th;
        FileOutputStream outStream = null;
        try {
            FileOutputStream outStream2 = new FileOutputStream(fileName);
            try {
                bitmap.compress(CompressFormat.PNG, 100, outStream2);
                outStream2.close();
                if (outStream2 != null) {
                    try {
                        outStream2.close();
                        outStream = outStream2;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        outStream = outStream2;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                outStream = outStream2;
                try {
                    e.printStackTrace();
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                   /* th = th2;
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                    */
                }
            } catch (Throwable th3) {
             /*   th = th3;
                outStream = outStream2;
                if (outStream != null) {
                    outStream.close();
                }
                throw th;
                */
            }
        } catch (Exception e4) {
           /* e = e4;
            e.printStackTrace();
            if (outStream != null) {
                outStream.close();
            }
            */
        }
    }

    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        return 6371.0d * (2.0d * Math.asin(Math.sqrt((Math.sin(dLat / 2.0d) * Math.sin(dLat / 2.0d)) + (((Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))) * Math.sin(dLon / 2.0d)) * Math.sin(dLon / 2.0d)))));
    }

    public static String getDBStr(String str) {
        str = str != null ? str.replaceAll("'", "''") : "";
        str = str != null ? str.replaceAll("&#039;", "''") : "";
        return str != null ? str.replaceAll("&amp;", "&") : "";
    }

    public static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return null;
    }

    public static String SimState(int simState) {
        switch (simState) {
            case 0:
                return SIM_UNKNOWN;
            case 1:
                return SIM_ABSENT;
            case 5:
                return SIM_READY;
            default:
                return null;
        }
    }

    public static boolean isAppRunning(Context context) {
       /* if (((RunningTaskInfo) ((ActivityManager) context.getSystemService("activity")).getRunningTasks(ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED).get(0)).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
            return true;
        }*/
        return false;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = { "_data" };
            cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
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

    public static String getRealPathFromURI(Activity activity, Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("_data"));
    }

    public static int compareDates(String license_expiry_date, String prch_date) {
        int days = 0;
        try {
            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            days = Long.valueOf(Math.abs(dates.parse(license_expiry_date).getTime() - dates.parse(prch_date).getTime()) / DateUtils.MILLIS_PER_DAY).intValue();
        } catch (Exception e) {
        }
        return days;
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static String getPath(Context context, Uri uri) {
        boolean isKitKat;
        if (VERSION.SDK_INT >= 19) {
            isKitKat = true;
        } else {
            isKitKat = false;
        }
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            String[] split = null;
            if (isExternalStorageDocument(uri)) {
                split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                return null;
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else if (!isMediaDocument(uri)) {
                return null;
            } else {
                String type = DocumentsContract.getDocumentId(uri).split(":")[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = "_id=?";
                return getDataColumn(context, contentUri, "_id=?", new String[]{split[1]});
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            return null;
        }
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static int getBuissnesDays(Date startDate, Date endDate) {
        int bDays = 0;
        while (endDate.compareTo(startDate) >= 0) {
            try {
                if (!(startDate.getDay() == 0 || startDate.getDay() == 6)) {
                    bDays++;
                }
                startDate.setDate(startDate.getDate() + 1);
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }
        Log.d(TAG, "Business Day: " + bDays);
        return bDays;
    }
}

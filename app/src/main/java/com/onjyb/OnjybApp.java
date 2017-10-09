package com.onjyb;

import android.app.Application;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.onjyb.db.LeaveType;
import com.onjyb.util.AppUtils;
import com.onjyb.util.Preference;
import java.util.ArrayList;

public class OnjybApp extends MultiDexApplication {
    protected static boolean activityVisible;
    private static boolean imageLoaderInited = false;
    public static ArrayList<LeaveType> leaveTypesArray = new ArrayList();

    @Override
    protected void attachBaseContext(Context newBase) {
        MultiDex.install(newBase);
        super.attachBaseContext(newBase);
    }
    public void onCreate() {
        super.onCreate();
        Preference.setAppContext(this);
        AppUtils.checkOrCreateAppDirectories(getApplicationContext());
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(480, 800).diskCacheExtraOptions(480, 800, null).threadPoolSize(3).threadPriority(3).tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(2097152)).memoryCacheSize(2097152).memoryCacheSizePercentage(13).diskCacheSize(52428800).diskCacheFileCount(100).diskCacheFileNameGenerator(new HashCodeFileNameGenerator());
        ImageLoader.getInstance().init(builder.imageDownloader(new BaseImageDownloader(this)).defaultDisplayImageOptions(DisplayImageOptions.createSimple()).writeDebugLogs().build());
        //jin
        StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder2.build());
    }

    public static void initImageLoader(Context context) {
        if (!imageLoaderInited) {
            ImageLoaderConfiguration config;
            imageLoaderInited = true;
            if (VERSION.SDK_INT < 11) {
                config = new ImageLoaderConfiguration.Builder(context).threadPriority(3).threadPoolSize(1).memoryCache(new WeakMemoryCache()).build();
            } else {
                config = new ImageLoaderConfiguration.Builder(context).threadPriority(3).threadPoolSize(3).build();
            }
            if (config != null) {
                ImageLoader.getInstance().init(config);
            }
        }
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }
}

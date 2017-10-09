package com.onjyb.helper;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import com.onjyb.layout.EmpHomeActivity;

public class ReconnectionService extends Service {
    private JobScheduler jobScheduler;
    private final IBinder mBinder = new MyBinder();

    public class MyBinder extends Binder {
        public ReconnectionService getService() {
            return ReconnectionService.this;
        }
    }

    @TargetApi(21)
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (VERSION.SDK_INT > 19) {
            Builder builder = new Builder(EmpHomeActivity.kJobId, new ComponentName(this, NotificationJobService.class));
            builder.setMinimumLatency(3000);
            builder.setOverrideDeadline(30000);
            builder.setRequiredNetworkType(2);
            builder.setRequiresDeviceIdle(true);
            builder.setRequiresCharging(false);
            this.jobScheduler = (JobScheduler) getApplication().getSystemService("jobscheduler");
            this.jobScheduler.schedule(builder.build());
        }
        return 1;
    }

    public IBinder onBind(Intent arg0) {
        return this.mBinder;
    }
}

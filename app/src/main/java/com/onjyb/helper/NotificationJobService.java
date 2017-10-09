package com.onjyb.helper;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

@TargetApi(21)
public class NotificationJobService extends JobService {
    private static final String TAG = "SyncService";

    public boolean onStartJob(JobParameters params) {
        startService(new Intent(getApplicationContext(), ReconnectionService.class));
        Log.i(TAG, "on start job: " + params.getJobId());
        return true;
    }

    public boolean onStopJob(JobParameters params) {
        return true;
    }
}

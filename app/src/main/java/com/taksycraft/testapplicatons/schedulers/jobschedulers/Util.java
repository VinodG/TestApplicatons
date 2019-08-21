package com.taksycraft.testapplicatons.schedulers.jobschedulers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
//https://www.vogella.com/tutorials/AndroidTaskScheduling/article.html
public class Util {
    public static   void scheduleJob(Context context )
    {
        ComponentName serviceComponent = new ComponentName(context, NotificationJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(10 * 1000); // wait at least
        builder.setOverrideDeadline(15 * 1000); // maximum delay
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            jobScheduler = context.getSystemService(JobScheduler.class);
            jobScheduler.schedule(builder.build());
        }


    }
}

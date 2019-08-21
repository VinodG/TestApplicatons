package com.taksycraft.testapplicatons.schedulers.jobschedulers;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

public class JobSchedulerApp extends AppCompatActivity {
    private JobScheduler mScheduler;
    private static final int JOB_ID = 0;
    //Switches for setting job options
    private Switch mDeviceIdleSwitch;
    private Switch mDeviceChargingSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Job Scheduling");
        setContentView(R.layout.activity_job_scheduler_app);
        mDeviceIdleSwitch = findViewById(R.id.idleSwitch);
        mDeviceChargingSwitch = findViewById(R.id.chargingSwitch);


    }

    public void scheduleJob(View view) {
//        anotherWayToSchedule();
        Util.scheduleJob(getApplicationContext());

    }

    private void anotherWayToSchedule() {
        RadioGroup networkOptions = findViewById(R.id.networkOptions);
        int selectedNetworkID = networkOptions.getCheckedRadioButtonId();
        int selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
        switch(selectedNetworkID){
            case R.id.noNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE;
                break;
            case R.id.anyNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY;
                break;
            case R.id.wifiNetwork:
                selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED;
                break;
        }

        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getPackageName(),
                NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, serviceName);

//        boolean constraintSet = selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE;
        boolean constraintSet = (selectedNetworkOption != JobInfo.NETWORK_TYPE_NONE)
                || mDeviceChargingSwitch.isChecked() || mDeviceIdleSwitch.isChecked();
        if(constraintSet) {
            //Schedule the job and notify the user
            builder
//            .setRequiredNetworkType(selectedNetworkOption)
//                    .setRequiresDeviceIdle(mDeviceIdleSwitch.isChecked())
//                    .setRequiresCharging(mDeviceChargingSwitch.isChecked())
//                    .setOverrideDeadline(15*1000)
                    .setPeriodic(15*1000)
            ;
            JobInfo myJobInfo = builder.build();
            mScheduler.schedule(myJobInfo);
            Toast.makeText(this, "Job Scheduled, job will run when " +
                    "the constraints are met.", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please set at least one constraint",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelJobs(View view) {
        if (mScheduler!=null){
            mScheduler.cancelAll();
            mScheduler = null;
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}

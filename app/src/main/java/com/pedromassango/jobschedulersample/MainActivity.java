package com.pedromassango.jobschedulersample;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.JobIntentService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ComponentName serviceComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceComponentName = new ComponentName(this, MyJobService.class);
    }

    // To stop job
    public void onStartJob(View v){
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponentName);
        builder.setOverrideDeadline( 1000); // delay time after scheduled the job

        // Start the job
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        // start and get the result
        int jobResult = scheduler.schedule(builder.build());

        if(jobResult == JobScheduler.RESULT_FAILURE) {
            showStatus("Job failed to start");
        }else if(jobResult == JobScheduler.RESULT_SUCCESS){
            showStatus("Job Running");
        }
    }

    // To stop the job
    public void onStopJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //scheduler.cancel(0);
        List<JobInfo> jobs = scheduler.getAllPendingJobs();

        if(jobs.isEmpty()){
            showStatus("No Job to cancel");
        }else{
            int id = jobs.get(0).getId();

            scheduler.cancel(id);
            showStatus("Job stopped");
        }
    }

    private void showStatus(String state){
        TextView textView = findViewById(R.id.tv_job_state);
        textView.setText( state);
    }
}

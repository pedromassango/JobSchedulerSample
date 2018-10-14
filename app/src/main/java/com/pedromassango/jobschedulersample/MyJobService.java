package com.pedromassango.jobschedulersample;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Pedro Massango on 5/16/18.
 */
public class MyJobService extends JobService {

    // JobService thread
    private JobThread jobThread;

    private void log(String msg) {
        Log.e("MyJobService", msg);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate();");

        jobThread = new JobThread();
        jobThread.start(); // start the thread when Service is created
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("onStartCommand();");

        // since onStartCommand is called only if the thread is already running
        // we just ignore this method, since we already started the thread in onCreate()
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        log("onStartJob();");

        // we just ignore this method,
        // since we already started the thread in onCreate()
        return true; // return true to let the service do the job
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        log("onStopJob();");

        // change the thread state to cancel the execution
        jobThread.stopThread = true;
        return false;
    }

    /**
     * Thread to run on this JobService
     */
    class JobThread extends Thread{

        boolean stopThread = false;

        @Override
        public void run() {
            int count = 0;

            // this will block the UI because JobService run on mainThread.
            // Now it is safe to execute because we are inside of a background
            // Thread
            while (count != 100 && !stopThread){
                count++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    stopSelf();
                    log("Thread interrupted");
                }

                log("count: " +count);
            }
        }
    }

}

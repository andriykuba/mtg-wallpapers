package com.github.andriykuba.mtgwallpapers.schedule;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;

import com.github.andriykuba.mtgwallpapers.Persist;
import com.github.andriykuba.mtgwallpapers.Screen;
import com.github.andriykuba.mtgwallpapers.download.ResentDownloader;

import java.util.Calendar;

public class Scheduler extends JobService {
    public static void updateSchedule(final Context context) {
        final JobScheduler jobScheduler =
                (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        final Persist persist = new Persist(context);
        if (persist.isUpdateAutomatically()) {
            final long nextTimeStartMin = getMillisecondsUntilMidnight();
            final long nextTimeStartMax = nextTimeStartMin + 10800000; // + 3 hours

            final ComponentName serviceComponent = new ComponentName(context, Scheduler.class);
            final JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

            builder.setMinimumLatency(nextTimeStartMin); // wait at least
            builder.setOverrideDeadline(nextTimeStartMax); // maximum delay

            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
            builder.setRequiresDeviceIdle(true);
            builder.setRequiresCharging(false);
            jobScheduler.schedule(builder.build());
        } else {
            // Just stop all.
            jobScheduler.cancelAll();
        }
    }

    private static long getMillisecondsUntilMidnight() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis() - System.currentTimeMillis();
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        final JobState state = new JobState(this, params);
        new ResentDownloader(this, new Screen(this), null, state).execute();
        updateSchedule(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters params) {
        return false;
    }
}

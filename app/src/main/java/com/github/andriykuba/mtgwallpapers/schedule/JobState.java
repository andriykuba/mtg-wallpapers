package com.github.andriykuba.mtgwallpapers.schedule;

import android.app.job.JobParameters;
import android.app.job.JobService;


public class JobState {
    private final JobService service;
    private final JobParameters parameters;
    private int callCounter = 0;

    JobState(final JobService service, final JobParameters parameters) {
        this.service = service;
        this.parameters = parameters;
    }

    // Must be called only once !
    public void finish() {
        if (callCounter > 0) {
            throw new RuntimeException("trying to finish job more that one time");
        }
        service.jobFinished(parameters, false);
        callCounter++;
    }
}

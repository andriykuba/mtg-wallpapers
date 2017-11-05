package com.github.andriykuba.mtgwallpapers.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class StartServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Scheduler.updateSchedule(context);
    }
}

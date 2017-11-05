package com.github.andriykuba.mtgwallpapers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


public class Toolbar {

    public static void initMain(AppCompatActivity activity) {
        android.support.v7.widget.Toolbar myToolbar =
                (android.support.v7.widget.Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(myToolbar);
    }

    public static void initChild(AppCompatActivity activity) {
        initMain(activity);

        ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }
    }
}

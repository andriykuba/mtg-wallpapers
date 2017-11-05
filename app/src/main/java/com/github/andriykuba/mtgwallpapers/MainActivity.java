package com.github.andriykuba.mtgwallpapers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.github.andriykuba.mtgwallpapers.change.Changer;
import com.github.andriykuba.mtgwallpapers.download.ResentDownloader;
import com.github.andriykuba.mtgwallpapers.gallery.GalleryActivity;
import com.github.andriykuba.mtgwallpapers.schedule.Scheduler;

public class MainActivity extends AppCompatActivity {

    Button buttonUpdate;
    Button buttonSet;
    Button buttonGallery;
    Switch switchBlur;
    Switch switchAutomatically;

    private Persist persist;
    private Screen screen;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = new Screen(this);
        persist = new Persist(this);

        Toolbar.initMain(this);
        initElements();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final MetaData data = persist.getCurrentWallpaperMetadata();
        if (data.url.isEmpty()) {
            downloadLastWallpaper();
        }
        Wallpaper.show(this, getRootView(), data);
    }

    private void initElements() {
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadLastWallpaper();
            }
        });

        buttonSet = (Button) findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper();
            }
        });

        buttonGallery = (Button) findViewById(R.id.buttonGallery);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        switchBlur = (Switch) findViewById(R.id.switchBlur);
        switchBlur.setChecked(persist.isBlur());
        switchBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persist.saveSwitchBlur(switchBlur.isChecked());
            }
        });

        final Context context = this;
        switchAutomatically = (Switch) findViewById(R.id.switchAutomatically);
        switchAutomatically.setChecked(persist.isUpdateAutomatically());
        switchAutomatically.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                persist.saveSwitchAutomatically(switchAutomatically.isChecked());
                Scheduler.updateSchedule(context);
            }
        });

        Scheduler.updateSchedule(this);
    }

    private void downloadLastWallpaper() {
        new ResentDownloader(this, screen, getRootView()).execute();
    }

    private View getRootView() {
        return findViewById(R.id.activity_main);
    }

    private void setWallpaper() {
        final MetaData data = persist.getCurrentWallpaperMetadata();
        new Changer(this, screen, data).execute();
    }

    private void openGallery() {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_info) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

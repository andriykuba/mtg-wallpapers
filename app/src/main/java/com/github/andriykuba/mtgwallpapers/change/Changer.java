package com.github.andriykuba.mtgwallpapers.change;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.github.andriykuba.mtgwallpapers.Dialog;
import com.github.andriykuba.mtgwallpapers.GlideApp;
import com.github.andriykuba.mtgwallpapers.GlideRequest;
import com.github.andriykuba.mtgwallpapers.MetaData;
import com.github.andriykuba.mtgwallpapers.Persist;
import com.github.andriykuba.mtgwallpapers.R;
import com.github.andriykuba.mtgwallpapers.Screen;
import com.github.andriykuba.mtgwallpapers.schedule.JobState;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class Changer extends AsyncTask<String, Void, String> {
    private final Context context;
    private final ProgressDialog dialog;
    private final JobState jobState;
    private final Screen screen;
    private final MetaData data;

    public Changer(final Context context, final Screen screen, final MetaData data) {
        this(context, screen, data, null);
    }

    public Changer(final Context context, final Screen screen, final MetaData data, final JobState jobState) {
        this.context = context;
        this.jobState = jobState;
        this.screen = screen;
        this.data = data;

        dialog = new ProgressDialog(context);
    }

    private boolean isJob() {
        return jobState != null;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            setLockWallpaper(wallpaperManager);
            setHomeWallpaper(wallpaperManager);

            return context.getString(
                    R.string.changer_result,
                    data.name.toUpperCase(),
                    data.series.toUpperCase(),
                    data.getDateAsString(context).toUpperCase(),
                    data.author.toUpperCase());
        } catch (IOException | InterruptedException | ExecutionException e) {
            return context.getString(R.string.changer_exception, e.getMessage());
        }
    }

    private void setLockWallpaper(WallpaperManager wallpaperManager) throws ExecutionException, InterruptedException, IOException {
        final Transformation<Bitmap> centerInside = new CenterInside();

        Bitmap bitmap = GlideApp
                .with(context.getApplicationContext())
                .asBitmap()
                .load(data.url)
                .transform(centerInside)
                .submit(screen.widthPixels, screen.heightPixels)
                .get();

        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
    }

    private void setHomeWallpaper(WallpaperManager wallpaperManager) throws ExecutionException, InterruptedException, IOException {
        final Persist persist = new Persist(context);

        GlideRequest<Bitmap> bitmapGlideRequest = GlideApp
                .with(context.getApplicationContext())
                .asBitmap()
                .load(data.url);

        final Transformation<Bitmap> centerInside = new CenterInside();
        if (persist.isBlur()) {
            bitmapGlideRequest.transform(new MultiTransformation(new BlurTransformation(12), centerInside));
        } else {
            bitmapGlideRequest.transform(centerInside);
        }

        Bitmap bitmap = bitmapGlideRequest
                .submit(screen.widthPixels, screen.heightPixels)
                .get();

        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
    }

    @Override
    protected void onPreExecute() {
        if (isJob()) return;

        dialog.setMessage(context.getString(R.string.changer_progress));
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        if (isJob()) {
            Log.i("ChangerService", result);
            jobState.finish();
        } else {
            Dialog.show(context, result);

            dialog.dismiss();
        }
    }
}

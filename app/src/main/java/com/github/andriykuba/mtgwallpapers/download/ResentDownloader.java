package com.github.andriykuba.mtgwallpapers.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.github.andriykuba.mtgwallpapers.Dialog;
import com.github.andriykuba.mtgwallpapers.MetaData;
import com.github.andriykuba.mtgwallpapers.Persist;
import com.github.andriykuba.mtgwallpapers.R;
import com.github.andriykuba.mtgwallpapers.Screen;
import com.github.andriykuba.mtgwallpapers.Wallpaper;
import com.github.andriykuba.mtgwallpapers.change.Changer;
import com.github.andriykuba.mtgwallpapers.schedule.JobState;

import java.util.Locale;

public class ResentDownloader extends AsyncTask<String, Void, DownloadResult> {
    private final Context context;
    private final View rootView;
    private final JobState jobState;
    private final ProgressDialog dialog;
    private final Screen screen;

    public ResentDownloader(final Context context, final Screen screen, final View rootView) {
        this(context, screen, rootView, null);
    }

    public ResentDownloader(final Context context, final Screen screen, final View rootView, final JobState jobState) {
        this.context = context;
        this.rootView = rootView;
        this.jobState = jobState;
        this.screen = screen;

        dialog = new ProgressDialog(context);
    }

    private boolean isJob() {
        return jobState != null;
    }

    @Override
    protected DownloadResult doInBackground(final String... params) {
        final String url = String.format(Locale.getDefault(), Downloader.SOURCE_URL, 0);
        return Downloader.downloadMetaDataList(context, screen, url);
    }

    @Override
    protected void onPreExecute() {
        if (!isJob()) {
            dialog.setMessage(context.getString(R.string.downloader_progress));
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(final DownloadResult result) {
        if (isJob()) {
            onPostExecuteService(result);
        } else {
            onPostExecuteForeground(result);
        }
    }

    private void onPostExecuteService(final DownloadResult result) {
        if (result.isError()) {
            Log.e("DownloaderService", result.getExceptionMessage());
        } else {
            final MetaData newer = persistIfNewer(result);
            if (newer == null) {
                Log.i("DownloaderService", "The same wallpapers");
            } else {
                // Set the wallpaper
                new Changer(context, screen, newer, jobState).execute();
            }
        }
    }

    private void onPostExecuteForeground(final DownloadResult result) {
        if (result.isError()) {
            Dialog.show(context, result.getExceptionMessage());
        } else {
            if (result.isEmpty()) {
                Dialog.show(context, context.getString(R.string.downloader_no_results));
            } else {
                final MetaData newer = persistIfNewer(result);

                if (newer == null) {
                    Dialog.show(context, context.getString(R.string.downloader_no_newer));
                } else {
                    Wallpaper.show(context, rootView, newer);
                }
            }
        }

        dialog.dismiss();
    }

    private MetaData persistIfNewer(final DownloadResult result) {
        if (result.isEmpty()) return null;

        final Persist persist = new Persist(context);

        final MetaData recent = result.getWallpapers().get(0);
        final MetaData current = persist.getCurrentWallpaperMetadata();

        if (current.url.equals(recent.url)) {
            return null;
        } else {
            persist.saveCurrent(recent);
            return recent;
        }
    }

}
package com.github.andriykuba.mtgwallpapers.download;

import android.app.Activity;
import android.os.AsyncTask;

import com.github.andriykuba.mtgwallpapers.Dialog;
import com.github.andriykuba.mtgwallpapers.R;
import com.github.andriykuba.mtgwallpapers.Screen;
import com.github.andriykuba.mtgwallpapers.gallery.GalleryAdapter;

import java.util.Locale;

public class ListDownloader extends AsyncTask<Void, Void, DownloadResult> {

    private final Activity context;
    private final Screen screen;
    private final GalleryAdapter adapter;

    public ListDownloader(final Activity context, final Screen screen, final GalleryAdapter adapter) {
        this.context = context;
        this.screen = screen;
        this.adapter = adapter;
    }

    @Override
    protected DownloadResult doInBackground(Void... params) {
        final String url = String.format(Locale.getDefault(), Downloader.SOURCE_URL, adapter.getPage());
        return Downloader.downloadMetaDataList(context, screen, url);
    }

    @Override
    protected void onPostExecute(final DownloadResult result) {
        if (result.isError()) {
            Dialog.show(context, result.getExceptionMessage());
        } else if (result.isEmpty()) {
            Dialog.show(context, context.getString(R.string.downloader_no_more));
        } else {
            adapter.appendData(result.getWallpapers());
            adapter.notifyDataSetChanged();
        }
    }
}

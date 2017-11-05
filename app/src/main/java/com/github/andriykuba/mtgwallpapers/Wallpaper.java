package com.github.andriykuba.mtgwallpapers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

public class Wallpaper {

    public static void show(final Context context, final View rootView, final MetaData data) {
        final TextView info = (TextView) rootView.findViewById(R.id.wallpaperInfo);
        final ImageView wallpaperView = (ImageView) rootView.findViewById(R.id.wallpaperImage);

        final String infoText = data.name +
                System.lineSeparator() +
                data.series +
                System.lineSeparator() +
                data.getDateAsString(context) +
                System.lineSeparator() +
                data.author;

        info.setText(infoText.trim().isEmpty() ? context.getString(R.string.default_wallpaper_description) : infoText);

        GlideApp.with(context)
                .load(data.url)
                .into(wallpaperView);

        wallpaperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Wallpaper.openInGallery(context, data);
            }
        });

        wallpaperView.setClickable(true);
    }

    private static void openInGallery(final Context context, final MetaData data) {
        GlideApp
                .with(context.getApplicationContext())
                .asFile()
                .load(data.url)
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, Transition<? super File> transition) {
                        open(context, resource);
                    }
                });
    }

    private static void open(final Context context, final File resource) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        final Uri imgUri = Uri.fromFile(resource);
        intent.setDataAndType(imgUri, "image/*");
        context.startActivity(intent);
    }

}

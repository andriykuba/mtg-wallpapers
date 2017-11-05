package com.github.andriykuba.mtgwallpapers;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public final class WallpapersAppGlideModule extends AppGlideModule {
    @Override
    public void applyOptions(final Context context, final GlideBuilder builder) {
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context));
    }
}

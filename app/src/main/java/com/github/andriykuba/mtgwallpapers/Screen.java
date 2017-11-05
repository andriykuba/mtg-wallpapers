package com.github.andriykuba.mtgwallpapers;

import android.content.Context;
import android.util.DisplayMetrics;

public class Screen {

    public final int widthPixels;
    public final int heightPixels;

    public final boolean isTablet;

    public Screen(final Context context) {
        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;

        final float widthInch = widthPixels / metrics.ydpi;
        final float heightInch = heightPixels / metrics.xdpi;
        final double diagonal = Math.sqrt(heightInch * heightInch + widthInch * widthInch);

        isTablet = diagonal > 6.5;
    }

}

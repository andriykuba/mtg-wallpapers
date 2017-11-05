package com.github.andriykuba.mtgwallpapers;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Dialog {
    public static void show(final Context context, final String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        View toastView = toast.getView();

        int paddingInDp = paddingToPixels(context, 10);
        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(18);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setGravity(Gravity.CENTER);
        toastView.setBackgroundColor(Color.BLACK);
        toastView.setPadding(paddingInDp, paddingInDp, paddingInDp, paddingInDp);
        toast.show();
    }

    private static int paddingToPixels(final Context context, final int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}

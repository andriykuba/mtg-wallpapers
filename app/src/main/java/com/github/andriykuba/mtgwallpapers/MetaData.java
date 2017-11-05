package com.github.andriykuba.mtgwallpapers;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

public class MetaData {
    public String name;
    public String series;
    public String author;
    public Date date;
    public String url;

    public String getDateAsString(final Context context) {
        if (date == null || date.getTime() == 0) {
            return "";
        } else {
            final DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
            return dateFormat.format(date);
        }
    }
}

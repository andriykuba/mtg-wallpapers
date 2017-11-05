package com.github.andriykuba.mtgwallpapers.download;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.github.andriykuba.mtgwallpapers.MetaData;
import com.github.andriykuba.mtgwallpapers.R;
import com.github.andriykuba.mtgwallpapers.Screen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Downloader {
    public static final String SOURCE_URL = "https://magic.wizards.com/en/see-more-wallpaper?page=%d&filter_by=DESC&artist=-1&expansion=&title=";
    private static final Pattern PATTERN_ROW = Pattern.compile("<li(.+?)</li>", Pattern.DOTALL);
    private static final Pattern PATTERN_NAME = Pattern.compile("<h3>(.+?)</h3>", Pattern.DOTALL);
    private static final Pattern PATTERN_SERIES = Pattern.compile("<span>(.+?)</span>", Pattern.DOTALL);
    private static final Pattern PATTERN_DATE = Pattern.compile("</span>(.+?)</p>", Pattern.DOTALL);
    private static final Pattern PATTERN_AUTHOR = Pattern.compile("<p class=\"author\">(.+?)</p>", Pattern.DOTALL);
    private static final Pattern PATTERN_URL = Pattern.compile("download=\"(.+?)\"", Pattern.DOTALL);
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public static DownloadResult downloadMetaDataList(
            final Context context,
            final Screen screen,
            final String url) {

        final StringBuilder resourceAsStringBuffer = new StringBuilder();
        InputStream resourceStream = null;
        final DownloadResult result = new DownloadResult();

        try {
            resourceStream = new URL(url).openStream();
            final InputStreamReader resourceInputStream = new InputStreamReader(resourceStream);
            final BufferedReader resourceReader = new BufferedReader(resourceInputStream);

            String str;
            while ((str = resourceReader.readLine()) != null) {
                resourceAsStringBuffer.append(str);
            }

        } catch (MalformedURLException e) {
            result.setExceptionMessage(
                    context.getString(R.string.downloader_exception_url,
                            url,
                            e.getMessage()));
        } catch (IOException e) {
            result.setExceptionMessage(
                    context.getString(R.string.downloader_exception_url_open,
                            url,
                            e.getMessage()));
        } finally {
            try {
                if (resourceStream != null) resourceStream.close();
            } catch (IOException ex) {
                result.setExceptionMessage(
                        context.getString(R.string.downloader_exception_connection_close,
                                url,
                                ex.getMessage()));
            }
        }

        if (!result.isError()) {
            String data = "";
            try {
                JSONObject json = new JSONObject(resourceAsStringBuffer.toString());
                data = json.getString("data");
            } catch (JSONException e) {
                result.setExceptionMessage(
                        context.getString(R.string.downloader_exception_json,
                                url,
                                e.getMessage()));
            }
            result.setWallpapers(Downloader.parseToMetaList(screen, data));
        }

        return result;
    }

    @SuppressLint("SimpleDateFormat")
    private static List<MetaData> parseToMetaList(Screen screen, final String rawList) {
        final Matcher matcherRow = PATTERN_ROW.matcher(rawList);

        final List<MetaData> metaDataList = new ArrayList<>();

        final String mark = screen.isTablet ? "Tablet_Wallpaper" : "iPhone_Wallpaper";
        while (matcherRow.find()) {
            final String rawMetaData = matcherRow.group(1);
            final MetaData metaData = new MetaData();

            metaData.name = match(PATTERN_NAME, rawMetaData);
            metaData.series = match(PATTERN_SERIES, rawMetaData);
            metaData.author = match(PATTERN_AUTHOR, rawMetaData);

            final String dateString = match(PATTERN_DATE, rawMetaData);
            try {
                metaData.date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                Log.e("WallpaperParseException", e.getMessage());
            }

            final Matcher matcherUrl = PATTERN_URL.matcher(rawMetaData);
            while (matcherUrl.find()) {
                final String url = matcherUrl.group(1);
                if (url.contains(mark)) {
                    metaData.url = url.trim();
                    break;
                }
            }

            // Add only if url exist
            if (metaData.url != null) {
                metaDataList.add(metaData);
            }
        }

        return metaDataList;
    }

    private static String match(final Pattern pattern, final String data) {
        final Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return "";
        }
    }
}

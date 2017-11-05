package com.github.andriykuba.mtgwallpapers.download;

import com.github.andriykuba.mtgwallpapers.MetaData;

import java.util.List;

public class DownloadResult {
    private List<MetaData> wallpapers;
    private String exceptionMessage;

    String getExceptionMessage() {
        return exceptionMessage;
    }

    void setExceptionMessage(final String message) {
        exceptionMessage = message;
    }

    boolean isError() {
        return exceptionMessage != null;
    }

    List<MetaData> getWallpapers() {
        return wallpapers;
    }

    void setWallpapers(final List<MetaData> list) {
        wallpapers = list;
    }

    boolean isEmpty() {
        return wallpapers == null || wallpapers.isEmpty();
    }
}

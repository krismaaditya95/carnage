package com.adit.carnage.classes;

import android.net.Uri;

public class Pictures {
    private final Uri uri;
    private final String name;
    private final int size;


    public Pictures(Uri uri, String name, int size) {
        this.uri = uri;
        this.name = name;
        this.size = size;
    }

    public Uri getUri() {
        return uri;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}

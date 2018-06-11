package com.ajou.android.lab5;

import android.graphics.Path;

public class ColoredPath {

    private Path mPath;
    private int mColor;

    public ColoredPath(Path path, int color) {
        mPath = path;
        mColor = color;
    }

    public Path getPath() {
        return mPath;
    }

    public int getColor() {
        return mColor;
    }
}

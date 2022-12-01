package com.example.doodler;

import android.graphics.Path;

public class Stroke {
    public int color;
    public int size;
    public int opacity;
    public Path path;

    public Stroke(int color, int size, int opacity, Path path) {
        this.color = color;
        this.size = size;
        this.opacity = opacity;
        this.path = path;
    }
}

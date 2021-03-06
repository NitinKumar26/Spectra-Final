package com.vidya.spectra.model;

import java.io.Serializable;

/**
 * Created by Nitin Kumar on 19/07/18.
 */
public class Image implements Serializable{
    private String name;
    private String url;
    private int height;
    private int width;

    public Image() {
    }

    public Image(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}


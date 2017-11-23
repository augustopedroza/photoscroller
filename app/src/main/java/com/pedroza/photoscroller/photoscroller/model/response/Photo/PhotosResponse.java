package com.pedroza.photoscroller.photoscroller.model.response.Photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pedroza on 7/25/17.
 */

public class PhotosResponse {

    @SerializedName("photos")
    @Expose
    private PhotoQueryInfo photoQueryInfo;
    @SerializedName("stat")
    @Expose
    private String stat;

    public PhotoQueryInfo getPhotoQueryInfo() {
        return photoQueryInfo;
    }

    public void setPhotoQueryInfo(PhotoQueryInfo photos) {
        this.photoQueryInfo = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
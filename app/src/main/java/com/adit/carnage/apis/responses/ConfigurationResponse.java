package com.adit.carnage.apis.responses;

import com.adit.carnage.apis.classes.Images;
import com.google.gson.annotations.SerializedName;

public class ConfigurationResponse {

    @SerializedName("images")
    private Images images;

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}

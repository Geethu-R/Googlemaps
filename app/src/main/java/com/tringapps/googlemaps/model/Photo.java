
package com.tringapps.googlemaps.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Photo {

    @SerializedName("height")
    private Long mHeight;
    @SerializedName("html_attributions")
    private List<String> mHtmlAttributions;
    @SerializedName("photo_reference")
    private String mPhotoReference;
    @SerializedName("width")
    private Long mWidth;

    public Long getHeight() {
        return mHeight;
    }

    public void setHeight(Long height) {
        mHeight = height;
    }

    public List<String> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<String> html_attributions) {
        mHtmlAttributions = html_attributions;
    }

    public String getPhotoReference() {
        return mPhotoReference;
    }

    public void setPhotoReference(String photo_reference) {
        mPhotoReference = photo_reference;
    }

    public Long getWidth() {
        return mWidth;
    }

    public void setWidth(Long width) {
        mWidth = width;
    }

}

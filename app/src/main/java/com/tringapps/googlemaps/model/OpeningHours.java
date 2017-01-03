
package com.tringapps.googlemaps.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class OpeningHours {

    @SerializedName("open_now")
    private Boolean mOpenNow;
    @SerializedName("weekday_text")
    private List<Object> mWeekdayText;

    public Boolean getOpenNow() {
        return mOpenNow;
    }

    public void setOpenNow(Boolean open_now) {
        mOpenNow = open_now;
    }

    public List<Object> getWeekdayText() {
        return mWeekdayText;
    }

    public void setWeekdayText(List<Object> weekday_text) {
        mWeekdayText = weekday_text;
    }

}

package com.officialshopwala.app;

import android.graphics.Bitmap;
import android.media.Image;

public class OnboardItem {
    int onboardImage;
    String Title;
    String description;

    public int getOnboardImage() {
        return onboardImage;
    }

    public void setOnboardImage(int onboardImage) {
        this.onboardImage = onboardImage;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

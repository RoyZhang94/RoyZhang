package com.bignerdranch.andriod.tot;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Roy on 2017/4/16.
 */

public class NewsItem {
    private String mTitle;
    private UUID mId;
    private String mTextUrl;
    private String mPhotoUrl;
    private Bitmap mPhotoBitmap;
    private String mDate;
    private String mAuthorName;
    private String mType;

    public NewsItem(){
        mId = UUID.randomUUID();
    }

    @Override
    public  String toString(){
        return mTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTextUrl() {
        return mTextUrl;
    }

    public void setTextUrl(String textUrl) {
        mTextUrl = textUrl;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public Bitmap getPhotoBitmap() {
        return mPhotoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        mPhotoBitmap = photoBitmap;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

}

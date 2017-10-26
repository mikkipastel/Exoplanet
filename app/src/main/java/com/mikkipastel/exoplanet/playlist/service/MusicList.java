package com.mikkipastel.exoplanet.playlist.service;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicList implements Parcelable {

    private String cover;
    private String duration;
    private String filename;
    private String url;

    public MusicList(Parcel in) {
        cover = in.readString();
        duration = in.readString();
        filename = in.readString();
        url = in.readString();
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cover);
        dest.writeString(duration);
        dest.writeString(filename);
        dest.writeString(url);
    }

    public static final Creator<MusicList> CREATOR = new Creator<MusicList>() {
        @Override
        public MusicList createFromParcel(Parcel in) {
            return new MusicList(in);
        }

        @Override
        public MusicList[] newArray(int size) {
            return new MusicList[size];
        }
    };
}

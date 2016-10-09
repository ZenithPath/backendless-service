package com.example.scame.backendlessservice.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class HistoryAdapterItem implements Parcelable {

    private String registrationType;

    private String name;

    private Date date;

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.registrationType);
        dest.writeString(this.name);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    public HistoryAdapterItem() {
    }

    protected HistoryAdapterItem(Parcel in) {
        this.registrationType = in.readString();
        this.name = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Parcelable.Creator<HistoryAdapterItem> CREATOR = new Parcelable.Creator<HistoryAdapterItem>() {
        @Override
        public HistoryAdapterItem createFromParcel(Parcel source) {
            return new HistoryAdapterItem(source);
        }

        @Override
        public HistoryAdapterItem[] newArray(int size) {
            return new HistoryAdapterItem[size];
        }
    };
}

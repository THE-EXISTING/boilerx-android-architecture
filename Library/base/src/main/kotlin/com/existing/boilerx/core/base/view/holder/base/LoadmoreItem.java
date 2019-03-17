package com.existing.boilerx.core.base.view.holder.base;

import android.os.Parcel;

/**
 * Created by「 The Khaeng 」on 13 Jun 2018 :)
 */
public class LoadmoreItem extends BaseItem {

    public static final int TYPE = 100000;

    public LoadmoreItem(int id) {
        super(id, TYPE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoadmoreItem that = (LoadmoreItem) o;

        return true;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected LoadmoreItem(Parcel in) {
        super(in);
    }

    public static final Creator<LoadmoreItem> CREATOR = new Creator<LoadmoreItem>() {
        @Override
        public LoadmoreItem createFromParcel(Parcel source) {
            return new LoadmoreItem(source);
        }

        @Override
        public LoadmoreItem[] newArray(int size) {
            return new LoadmoreItem[size];
        }
    };
}

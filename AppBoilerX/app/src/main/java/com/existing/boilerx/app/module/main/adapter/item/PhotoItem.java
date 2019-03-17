package com.existing.boilerx.app.module.main.adapter.item;

import android.os.Parcel;

import com.existing.boilerx.app.module.main.MainActivity;
import com.existing.boilerx.app.repository.model.PhotoModel;
import com.existing.boilerx.core.base.view.holder.base.BaseItem;


/**
 * Created by「 The Khaeng 」on 17 Oct 2017 :)
 */

public class PhotoItem extends BaseItem{

    private PhotoModel model;

    public PhotoItem(int id){
        super(id, MainActivity.TYPE_PICTURE );
    }

    public PhotoModel getModel(){
        return model;
    }

    public PhotoItem setModel( PhotoModel model ){
        this.model = model;
        return this;
    }

    @Override
    public boolean isItemTheSame( Object baseItem ){
        if( baseItem instanceof PhotoItem ){
            return getId() == ( (PhotoItem) baseItem ).getId();
        }
        return false;
    }

    @Override
    public boolean isContentTheSame( Object baseItem ){
        return equals( baseItem );
    }


    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoItem photoItem = (PhotoItem) o;

        return getModel() != null ? getModel().equals(photoItem.getModel()) : photoItem.getModel() == null;
    }

    @Override
    public int hashCode() {
        return getModel() != null ? getModel().hashCode() : 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ){
        super.writeToParcel( dest, flags );
        dest.writeParcelable( this.model, flags );
    }

    protected PhotoItem( Parcel in ){
        super( in );
        this.model = in.readParcelable( PhotoModel.class.getClassLoader() );
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>(){
        @Override
        public PhotoItem createFromParcel( Parcel source ){
            return new PhotoItem( source );
        }

        @Override
        public PhotoItem[] newArray( int size ){
            return new PhotoItem[size];
        }
    };
}

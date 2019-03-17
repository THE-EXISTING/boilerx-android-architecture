package com.existing.boilerx.common.base.repository.base.database.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PhotoRealmObject extends RealmObject {

    public static final String FIELD_ID_NAME = "id";

    @PrimaryKey
    private String id;
    private int imageWidthPortrait = 0;
    private int imageHeightPortrait = 0;
    private int imageWidthLandScape = 0;
    private int imageHeightLandScape = 0;
    private String imageUrl = null;
    private String caption = null;
    private String profilePicture = null;
    private String camera = null;
    private String lens = null;
    private String focalLength = null;
    private String iso = null;
    private String shutterSpeed = null;
    private String aperture = null;


    public PhotoRealmObject(){
    }

    public String getId(){
        return id;
    }

    public void setId( String id ){
        this.id = id;
    }

    public int getImageWidthPortrait(){
        return imageWidthPortrait;
    }

    public void setImageWidthPortrait( int imageWidthPortrait ){
        this.imageWidthPortrait = imageWidthPortrait;
    }

    public int getImageHeightPortrait(){
        return imageHeightPortrait;
    }

    public void setImageHeightPortrait( int imageHeightPortrait ){
        this.imageHeightPortrait = imageHeightPortrait;
    }

    public int getImageWidthLandScape(){
        return imageWidthLandScape;
    }

    public void setImageWidthLandScape( int imageWidthLandScape ){
        this.imageWidthLandScape = imageWidthLandScape;
    }

    public int getImageHeightLandScape(){
        return imageHeightLandScape;
    }

    public void setImageHeightLandScape( int imageHeightLandScape ){
        this.imageHeightLandScape = imageHeightLandScape;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl( String imageUrl ){
        this.imageUrl = imageUrl;
    }

    public String getCaption(){
        return caption;
    }

    public void setCaption( String caption ){
        this.caption = caption;
    }

    public String getProfilePicture(){
        return profilePicture;
    }

    public void setProfilePicture( String profilePicture ){
        this.profilePicture = profilePicture;
    }

    public String getCamera(){
        return camera;
    }

    public void setCamera( String camera ){
        this.camera = camera;
    }

    public String getLens(){
        return lens;
    }

    public void setLens( String lens ){
        this.lens = lens;
    }

    public String getFocalLength(){
        return focalLength;
    }

    public void setFocalLength( String focalLength ){
        this.focalLength = focalLength;
    }

    public String getIso(){
        return iso;
    }

    public void setIso( String iso ){
        this.iso = iso;
    }

    public String getShutterSpeed(){
        return shutterSpeed;
    }

    public void setShutterSpeed( String shutterSpeed ){
        this.shutterSpeed = shutterSpeed;
    }

    public String getAperture(){
        return aperture;
    }

    public void setAperture( String aperture ){
        this.aperture = aperture;
    }
}

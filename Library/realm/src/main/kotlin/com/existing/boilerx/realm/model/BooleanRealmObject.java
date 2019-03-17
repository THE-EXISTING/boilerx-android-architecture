package com.existing.boilerx.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BooleanRealmObject extends RealmObject{
    public static final String FIELD_ID_NAME = "id";

    @PrimaryKey
    private String id;
    private boolean value;

    public BooleanRealmObject(){
    }


    public BooleanRealmObject( String id,
                               boolean value ){
        this.id = id;
        this.value = value;
    }


    public String getId(){
        return id;
    }

    public void setId( String id ){
        this.id = id;
    }

    public boolean getValue(){
        return value;
    }

    public void setValue( boolean value ){
        this.value = value;
    }
}

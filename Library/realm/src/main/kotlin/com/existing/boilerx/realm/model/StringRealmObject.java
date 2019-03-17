package com.existing.boilerx.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StringRealmObject extends RealmObject{
    public static final String FIELD_ID_NAME = "id";

    @PrimaryKey
    private String id;
    private String value;

    public StringRealmObject(){
    }


    public StringRealmObject( String id,
                              String value ){
        this.id = id;
        this.value = value;
    }


    public String getId(){
        return id;
    }

    public void setId( String id ){
        this.id = id;
    }

    public String getValue(){
        return value;
    }

    public void setValue( String value ){
        this.value = value;
    }
}

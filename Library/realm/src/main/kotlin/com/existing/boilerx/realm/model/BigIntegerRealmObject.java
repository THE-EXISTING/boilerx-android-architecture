package com.existing.boilerx.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import timber.log.Timber;

import java.math.BigInteger;

public class BigIntegerRealmObject extends RealmObject{
    public static final String FIELD_ID_NAME = "id";

    @PrimaryKey
    private String id;
    private String valueString;

    public BigIntegerRealmObject(){
    }


    public BigIntegerRealmObject( String id,
                                  String value ){
        this.id = id;
        this.valueString = value;
    }

    public BigIntegerRealmObject( String id,
                                  BigInteger value ){
        this.id = id;
        setValue( value );
    }

    public BigIntegerRealmObject( String id,
                                  Long value ){
        this.id = id;
        setValue( value );
    }

    public BigIntegerRealmObject( String id,
                                  Integer value ){
        this.id = id;
        setValue( value );
    }


    public String getId(){
        return id;
    }

    public void setId( String id ){
        this.id = id;
    }

    public int getIntValue(){
        String balanceString = "0";
        if( !balanceString.isEmpty() ){
            balanceString = this.valueString;
        }
        try{
            return Integer.parseInt( balanceString );
        }catch( Exception e ){
            Timber.e( e );
            return 0;
        }
    }

    public long getLongValue(){
        String balanceString = "0";
        if( !balanceString.isEmpty() ){
            balanceString = this.valueString;
        }
        try{
            return Long.parseLong( balanceString );
        }catch( Exception e ){
            Timber.e( e );
            return 0L;
        }
    }

    public BigInteger getValue(){
        String balanceString = "0";
        if( !balanceString.isEmpty() ){
            balanceString = this.valueString;
        }
        try{
            return new BigInteger( balanceString );
        }catch( Exception e ){
            Timber.e( e );
            return BigInteger.ZERO;
        }
    }

    public void setValue( BigInteger value ){
        this.valueString = value.toString();
    }

    public void setValue( Long value ){
        this.valueString = value.toString();
    }

    public void setValue( Integer value ){
        this.valueString = value.toString();
    }

    public String getValueString(){
        return valueString;
    }

    public void setValueString( String value ){
        this.valueString = value;
    }
}

package com.existing.boilerx.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import timber.log.Timber;

import java.math.BigDecimal;

public class BigDecimalRealmObject extends RealmObject{
    public static final String FIELD_ID_NAME = "id";

    @PrimaryKey
    private String id;
    private String valueString;

    public BigDecimalRealmObject(){
    }


    public BigDecimalRealmObject( String id,
                                  String value ){
        this.id = id;
        this.valueString = value;
    }

    public BigDecimalRealmObject( String id,
                                  BigDecimal value ){
        this.id = id;
        setValue( value );
    }

    public BigDecimalRealmObject( String id,
                                  Double value ){
        this.id = id;
        setValue( value );
    }

    public BigDecimalRealmObject( String id,
                                  Float value ){
        this.id = id;
        setValue( value );
    }


    public String getId(){
        return id;
    }

    public void setId( String id ){
        this.id = id;
    }

    public float getFloatValue(){
        String balanceString = "0.0";
        if( !balanceString.isEmpty() ){
            balanceString = this.valueString;
        }
        try{
            return Float.parseFloat( balanceString );
        }catch( Exception e ){
            Timber.e( e );
            return 0.0f;
        }
    }

    public double getDoubleValue(){
        String balanceString = "0.0";
        if( !balanceString.isEmpty() ){
            balanceString = this.valueString;
        }
        try{
            return Double.parseDouble( balanceString );
        }catch( Exception e ){
            Timber.e( e );
            return 0.0;
        }
    }

    public BigDecimal getValue(){
        String balanceString = "0.0";
        if( !balanceString.isEmpty() ){
            balanceString = this.valueString;
        }
        try{
            return new BigDecimal( balanceString );
        }catch( Exception e ){
            Timber.e( e );
            return BigDecimal.ZERO;
        }
    }

    public void setValue( BigDecimal value ){
        this.valueString = value.toString();
    }

    public void setValue( Double value ){
        this.valueString = value.toString();
    }

    public void setValue( Float value ){
        this.valueString = value.toString();
    }

    public String getValueString(){
        return valueString;
    }

    public void setValueString( String value ){
        this.valueString = value;
    }
}

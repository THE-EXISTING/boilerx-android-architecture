package com.existing.boilerx.realm.model;


import io.realm.annotations.RealmModule;

/**
 * Created by「 The Khaeng 」on 31 Aug 2017 :)
 */

@RealmModule( library = true,
              classes = {
                      BigDecimalRealmObject.class,
                      BigIntegerRealmObject.class,
                      BooleanRealmObject.class,
                      StringRealmObject.class
              } )
public class PrimitiveTypeDatabaseModule {
}

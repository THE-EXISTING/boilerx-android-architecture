package com.existing.boilerx.common.base.repository.base.database.realm;


import com.existing.boilerx.common.base.repository.base.database.realm.model.PhotoRealmObject;
import io.realm.annotations.RealmModule;

/**
 * Created by「 The Khaeng 」on 31 Aug 2017 :)
 */

@RealmModule( library = true,
              classes = {
//                      BigDecimalRealmObject.class,
//                      BigIntegerRealmObject.class,
//                      BooleanRealmObject.class,
//                      StringRealmObject.class,
                      PhotoRealmObject.class
              } )
public class AppDatabaseModule{
}

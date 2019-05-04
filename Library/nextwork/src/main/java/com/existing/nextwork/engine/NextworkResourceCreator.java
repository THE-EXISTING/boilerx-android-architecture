package com.existing.nextwork.engine;

import com.existing.nextwork.engine.model.ResultWrapper;

import org.jetbrains.annotations.Nullable;

/**
 * Created by「 The Khaeng 」on 13 Oct 2017 :)
 */

public interface NextworkResourceCreator<T, R extends ResultWrapper<T>> {
    R loadingFromNetwork( @Nullable Object payload );

    R loadingFromDatabase(@Nullable Object payload);

    R completed( @Nullable Object payload);

    R next( T newData, @Nullable Object payload, boolean isCache );

    R error( Throwable error, @Nullable Object payload );
}

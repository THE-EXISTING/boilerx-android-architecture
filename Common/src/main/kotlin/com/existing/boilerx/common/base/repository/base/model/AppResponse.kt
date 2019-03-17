package com.existing.boilerx.common.base.repository.base.model

import com.existing.nextwork.engine.model.ResponseWrapper
import retrofit2.Response

/**
 * Default class used by API responses.
 *
 * @param <T>
</T> */
class AppResponse<T> : ResponseWrapper<T> {

    constructor(code: Int, body: T?, error: Throwable?) : super(code, body, error) {}

    constructor(error: Throwable) : super(error) {}

    constructor(response: Response<T>) : super(response) {
    }

    constructor(response: T) : super(response) {
    }
}

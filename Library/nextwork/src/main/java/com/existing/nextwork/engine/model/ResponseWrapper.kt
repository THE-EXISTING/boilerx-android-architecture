package com.existing.nextwork.engine.model

import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 * Common class used by API responses.
 *
 * @param <T>
</T> */
open class ResponseWrapper<T> {

    companion object {
        private val TAG = ResponseWrapper::class.java.simpleName
    }

    val code: Int

    val body: T?

    val error: Throwable?

    val method: String?

    val url: String?

    constructor(code: Int, body: T?, error: Throwable?) {
        this.code = code
        this.body = body
        this.error = error
        this.method = null
        this.url = null
    }

    constructor(error: Throwable?) {
        this.code = 500
        this.body = null
        this.error = error
        this.method = null
        this.url = null
    }

    constructor(response: T?) {
        if (response != null) {
            this.code = 200
            this.body = response
            this.error = null
            this.method = null
            this.url = null
        } else {
            this.code = 500
            this.body = null
            this.error = NullPointerException()
            this.method = null
            this.url = null
        }
    }

    constructor(response: Response<T>) {
        code = response.code()
        method = response.raw().request().method()
        url = response.raw().request().url().toString()
        if (response.isSuccessful) {
            body = response.body()
            error = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                    Timber.e(TAG, "error while parsing response")
                }

            }
            if (message == null || message.trim { it <= ' ' }.isEmpty()) {
                message = response.message()
            }
            error = IllegalArgumentException(message)
            body = null
        }
    }


    fun isSuccessful(): Boolean = code in 200..299

    override
    fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResponseWrapper<*>

        if (code != other.code) return false
        if (body != other.body) return false
        if (error != other.error) return false

        return true
    }

    override
    fun hashCode(): Int {
        var result = code
        result = 31 * result + (body?.hashCode() ?: 0)
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ResponseWrapper(code=$code, body=$body, error=$error, method=$method, url=$url)"
    }


}

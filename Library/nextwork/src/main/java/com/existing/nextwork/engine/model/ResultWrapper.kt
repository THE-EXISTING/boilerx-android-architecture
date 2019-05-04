package com.existing.nextwork.engine.model

/**
 * A generic class that holds a value with its loadingFromNetwork status.
 *
 * @param <T>
</T> */
abstract class ResultWrapper<T>(@param:Status @field:Status
                                var status: Int,
                                val data: T?,
                                val exception: Throwable?,
                                val payload: Any?,
                                val isCached: Boolean) {

    override
    fun toString(): String {
        return "ResultWrapper{" +
                "status=" + status +
                ", message='" + exception +
                ", data=" + data +
                ", payload=" + payload +
                ", isCached=" + isCached +
                '}'.toString()
    }

    override
    fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as ResultWrapper<*>?

        if (status != that!!.status) return false
        if (isCached != that.isCached) return false
        if (if (exception != null) exception != that.exception else that.exception != null)
            return false
        if (if (data != null) data != that.data else that.data != null) return false
        return if (payload != null) payload == that.payload else that.payload == null
    }

    override
    fun hashCode(): Int {
        var result = status
        result = 31 * result + (exception?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        result = 31 * result + (payload?.hashCode() ?: 0)
        result = 31 * result + if (isCached) 1 else 0
        return result
    }


}

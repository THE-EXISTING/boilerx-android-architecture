package com.existing.nextwork.engine.model


import androidx.annotation.IntDef
import com.existing.nextwork.engine.model.NextworkStatus.COMPLETED
import com.existing.nextwork.engine.model.NextworkStatus.CONSUME
import com.existing.nextwork.engine.model.NextworkStatus.ERROR
import com.existing.nextwork.engine.model.NextworkStatus.LOADING_FROM_DATABASE
import com.existing.nextwork.engine.model.NextworkStatus.LOADING_FROM_NETWORK
import com.existing.nextwork.engine.model.NextworkStatus.NEXT

/**
 * Status of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<ResultWrapper<T>>` to pass back the latest data to the UI with its fetch status.
 */

object NextworkStatus {
    const val CONSUME = -1
    const val NEXT = 10
    const val COMPLETED = 20
    const val ERROR = 30
    const val LOADING_FROM_DATABASE = 40
    const val LOADING_FROM_NETWORK = 50
}


@Retention(AnnotationRetention.SOURCE)
@IntDef(NEXT, COMPLETED, ERROR, LOADING_FROM_DATABASE, LOADING_FROM_NETWORK, CONSUME)
annotation class Status

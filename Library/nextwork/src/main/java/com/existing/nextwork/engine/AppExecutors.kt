package com.existing.nextwork.engine

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


/**
 * Global executor pools for the whole application.
 *
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */

class AppExecutors private constructor(
     val diskIO: Executor,
     val networkIO: Executor,
     val mainThread: Executor
) {

    companion object {
        @Volatile
        private var INSTANCE: AppExecutors? = null

        fun getInstance(): AppExecutors =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AppExecutors(
                    Executors.newSingleThreadExecutor(),
                    Executors.newFixedThreadPool(4),
                    MainThreadExecutor()
                ).also { INSTANCE = it }
            }
    }



    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override
        fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }


}

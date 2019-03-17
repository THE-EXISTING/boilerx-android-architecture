package com.existing.boilerx.ktx

import androidx.arch.core.util.Function
import androidx.lifecycle.*
import io.reactivex.*
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription
import timber.log.Timber

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

fun <T, R> LiveData<T>.then(liveData: MediatorLiveData<R>, block: (T) -> Unit): MediatorLiveData<R> {
    liveData.addSource(this) { t -> block.invoke(t) }
    return liveData
}

fun <T> T.toLiveData(): LiveData<T> {
    return object : LiveData<T>() {
        init {
            value = this@toLiveData
        }
    }
}

fun <T> Flowable<T>.toLiveData(isBindLifecycle: Boolean = false): LiveData<T> {
    return if (isBindLifecycle) {
        LiveDataReactiveStreams.fromPublisher(this)
    } else {
        val liveData: MutableLiveData<T> = MutableLiveData()
        this.subscribe(flowableSubscriber(liveData))
        liveData
    }
}

fun <T> Observable<T>.toLiveData(
        backPressureStrategy: BackpressureStrategy, isBindLifecycle: Boolean = false
                                ): LiveData<T> {
    return if (isBindLifecycle) {
        LiveDataReactiveStreams.fromPublisher(this.toFlowable(backPressureStrategy))
    } else {
        val liveData: MutableLiveData<T> = MutableLiveData()
        this.subscribe(observer(liveData))
        liveData
    }
}


fun <T> Single<T>.toLiveData(isBindLifecycle: Boolean = false): LiveData<T> {
    return if (isBindLifecycle) {
        LiveDataReactiveStreams.fromPublisher(this.toFlowable())
    } else {
        val liveData: MutableLiveData<T> = MutableLiveData()
        this.subscribe(singleObserver(liveData))
        liveData
    }
}

fun <T> Maybe<T>.toLiveData(isBindLifecycle: Boolean = false): LiveData<T> {
    return if (isBindLifecycle) {
        LiveDataReactiveStreams.fromPublisher(this.toFlowable())
    } else {
        val liveData: MutableLiveData<T> = MutableLiveData()
        this.subscribe(maybeObserver(liveData))
        liveData
    }
}

fun <T> Completable.toLiveData(isBindLifecycle: Boolean = false): LiveData<T> {
    return if (isBindLifecycle) {
        LiveDataReactiveStreams.fromPublisher(this.toFlowable())
    } else {
        val liveData: MutableLiveData<T> = MutableLiveData()
        this.subscribe(completableObserver(liveData))
        liveData
    }
}


fun <T> LiveData<Boolean>.fetchSwitchMap(func: Function<Boolean, LiveData<T>>): LiveData<T> {
    return Transformations.switchMap<Boolean, T>(this) { forceFetch ->
        if (forceFetch == null) {
            AbsentLiveData.create<T>()
        } else {
            func.apply(forceFetch)
        }
    }
}

fun <A, B> zipLiveData(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        fun update() {
            val localLastA = lastA
            val localLastB = lastB
            if (localLastA != null && localLastB != null)
                this.value = Pair(localLastA, localLastB)
        }

        addSource(a) {
            lastA = it
            update()
        }
        addSource(b) {
            lastB = it
            update()
        }
    }
}

/**
 * This is merely an extension function for [zipLiveData].
 *
 * @see zipLiveData
 * @author Mitchell Skaggs
 */
fun <A, B> LiveData<A>.zip(b: LiveData<B>): LiveData<Pair<A, B>> = zipLiveData(this, b)

/**
 * This is an extension function that calls to [Transformations.map].
 *
 * @see Transformations.map
 * @author Mitchell Skaggs
 */
fun <A, B> LiveData<A>.map(function: (A) -> B): LiveData<B> = Transformations.map(this, function)

/**
 * This is an extension function that calls to [Transformations.switchMap].
 *
 * @see Transformations.switchMap
 * @author Mitchell Skaggs
 */
fun <A, B> LiveData<A>.switchMap(function: (A) -> LiveData<B>): LiveData<B> =
        Transformations.switchMap(this, function)


private fun <T> observer(liveData: MutableLiveData<T>): Observer<T> {
    return object : Observer<T> {
        override
        fun onComplete() {
        }

        override
        fun onSubscribe(d: Disposable) {
        }

        override
        fun onNext(t: T) {
            liveData.postValue(t)
        }

        override
        fun onError(e: Throwable) {
            if (e is NullPointerException) {
                Timber.e(e, "Null value")
                liveData.postValue(null)
            }
        }

    }
}

private fun <T> maybeObserver(liveData: MutableLiveData<T>): MaybeObserver<T> {
    return object : MaybeObserver<T> {

        override
        fun onSuccess(t: T) {
            liveData.postValue(t)
        }

        override
        fun onComplete() {
        }

        override
        fun onSubscribe(d: Disposable) {
        }

        override
        fun onError(e: Throwable) {
            if (e is NullPointerException) {
                Timber.e(e, "Null value")
                liveData.postValue(null)
            }
        }

    }
}

private fun <T> completableObserver(liveData: MutableLiveData<T>): CompletableObserver {
    return object : CompletableObserver {
        override
        fun onComplete() {
        }

        override
        fun onSubscribe(d: Disposable) {
        }

        override
        fun onError(e: Throwable) {
            if (e is NullPointerException) {
                Timber.e(e, "Null value")
                liveData.postValue(null)
            }
        }

    }
}


private fun <T> singleObserver(liveData: MutableLiveData<T>): SingleObserver<T?> {
    return object : SingleObserver<T?> {
        override
        fun onSuccess(t: T) {
            liveData.postValue(t)
        }


        override
        fun onSubscribe(d: Disposable) {
        }

        override
        fun onError(e: Throwable) {
            if (e is NullPointerException) {
                Timber.e(e, "Null value")
                liveData.postValue(null)
            }
        }

    }
}

private fun <T> flowableSubscriber(liveData: MutableLiveData<T>): FlowableSubscriber<T> {
    return object : FlowableSubscriber<T> {
        override
        fun onSubscribe(s: Subscription) {
            s.request(java.lang.Long.MAX_VALUE)
        }


        override
        fun onComplete() {
        }

        override
        fun onNext(t: T) {
            liveData.postValue(t)
        }

        override
        fun onError(e: Throwable) {
            if (e is NullPointerException) {
                Timber.e(e, "Null value")
                liveData.postValue(null)
            }
        }

    }
}

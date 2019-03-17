package com.existing.boilerx.ktx

import io.reactivex.*
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscription

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */

fun <T> Single<T>.subscribeOnSuccess(block:(T) -> Unit){
    this.subscribe(object : SingleObserver<T>{
        override
        fun onSuccess(t: T) {
            block(t)
        }

        override
        fun onSubscribe(d: Disposable) {
        }

        override
        fun onError(e: Throwable) {
        }

    })
}

fun <T> Observable<T>.subscribeOnNext(block:(T) -> Unit){
    this.subscribe(object : Observer<T>{
        override
        fun onComplete() {
        }

        override
        fun onSubscribe(d: Disposable) {
        }

        override
        fun onNext(t: T) {
            block(t)
        }

        override
        fun onError(e: Throwable) {
        }
    })
}

fun <T> Flowable<T>.subscribeOnNext(block:(T) -> Unit){
    this.subscribe(object : FlowableSubscriber<T>{
        override
        fun onComplete() {
        }

        override
        fun onSubscribe(s: Subscription) {
            s.request(java.lang.Long.MAX_VALUE)
        }

        override
        fun onNext(t: T) {
            block(t)
        }

        override
        fun onError(t: Throwable?) {
        }
    })
}


abstract class FlowableSubscriberAbstract<T> : FlowableSubscriber<T> {
    override
    fun onComplete() {
    }

    override
    fun onSubscribe(s: Subscription) {
        s.request(java.lang.Long.MAX_VALUE)
    }

}


abstract class SingleObserverAbstract<T> : SingleObserver<T> {

    override
    fun onSubscribe(d: Disposable) {
    }


    override
    fun onSuccess(t: T) {
    }



    override
    fun onError(e: Throwable) {
    }

}

abstract class ObserverAbstract<T> : Observer<T> {

    override
    fun onComplete() {
    }

    override
    fun onSubscribe(d: Disposable) {
    }

    override
    fun onNext(t: T) {
    }

    override
    fun onError(e: Throwable) {
    }

}


fun <T> Flowable<T>.subscribeMap(
    onNext: (FlowableEmitter<T>, T) -> Unit,
    onError: (FlowableEmitter<T>, Throwable) -> Unit = { e, t -> }
): Flowable<T> {
    return Flowable.create<T>({ emitter: FlowableEmitter<T> ->
        this.subscribe(object : FlowableSubscriber<T> {
            override
            fun onSubscribe(s: Subscription) {
                s.request(java.lang.Long.MAX_VALUE)
            }

            override
            fun onComplete() {
            }

            override
            fun onNext(data: T) {
                onNext(emitter, data)
            }

            override
            fun onError(e: Throwable) {
                onError(emitter, e)
            }

        })
    }, BackpressureStrategy.LATEST)
}



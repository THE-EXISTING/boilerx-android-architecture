package com.existing.nextwork.engine.checker

import com.existing.nextwork.exception.EmptyDataListException
import com.existing.nextwork.exception.NullDataException

/**
 * Created by「 The Khaeng 」on 08 Sep 2018 :)
 */


fun Throwable?.isNoDataException(): Boolean {
    return this is NullDataException || this is EmptyDataListException
}

fun Throwable?.getNoDataMessageException(): String {
    if (this.isNoDataException()) {
        return this?.message ?: ""
    }
    return ""
}


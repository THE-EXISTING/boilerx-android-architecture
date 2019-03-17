package com.existing.nextwork.exception

/**
 * Created by「 The Khaeng 」on 24 Apr 2018 :)
 */
class InternalErrorException(code: Int, urlLog: String = "") : StatusCodeException(code, "An internal server error occurred", urlLog)

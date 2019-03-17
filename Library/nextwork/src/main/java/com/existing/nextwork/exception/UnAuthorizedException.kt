package com.existing.nextwork.exception


/**
 * Created by「 The Khaeng 」on 03 Oct 2017 :)
 */
class UnAuthorizedException(code: Int, urlLog: String = "") : StatusCodeException(
    code,
    "for Unauthorized requests, when a request requires authentication " + "but it isn't provided",
    urlLog
)

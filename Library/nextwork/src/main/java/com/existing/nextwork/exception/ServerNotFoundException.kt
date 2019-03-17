package com.existing.nextwork.exception

/**
 * Created by「 The Khaeng 」on 02 Oct 2017 :)
 */
class ServerNotFoundException(code: Int, urlLog: String = "") : StatusCodeException(code, "for Not found requests, when a resource can't be found to fulfill the request")

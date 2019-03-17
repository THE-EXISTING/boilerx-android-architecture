package com.existing.boilerx.common.base.mvvm.usecase

import com.existing.boilerx.core.base.usecase.UseCaseLiveData

/**
 * Created by「 The Khaeng 」on 15 Jan 2019 :)
 */
abstract class AppUseCaseLiveData<PARAMETER, RESULT > : UseCaseLiveData<PARAMETER, RESULT>() {

//    override
//    fun onPostExecute(result: RESULT?) {
//        super.onPostExecute(result)
//        if (TriggerDataSteam.isClearTrigger(result)) {
//            clearParameter()
//        }
//    }

}
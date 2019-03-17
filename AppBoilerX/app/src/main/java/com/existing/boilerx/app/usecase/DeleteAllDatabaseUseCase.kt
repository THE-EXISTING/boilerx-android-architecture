package com.existing.boilerx.app.usecase

import com.existing.boilerx.app.repository.PhotoRepository
import com.existing.boilerx.common.base.mvvm.usecase.AppUseCase

/**
 * Created by「 The Khaeng 」on 07 Jan 2019 :)
 */
class DeleteAllDatabaseUseCase(
    private var repository: PhotoRepository = PhotoRepository.getInstance()
) : AppUseCase<Unit, Boolean>() {

    override
    fun execute(parameter: Unit): Boolean = repository.deleteAllDatabase()

}
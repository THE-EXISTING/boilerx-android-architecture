package com.existing.boilerx.app.module.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.existing.nextwork.engine.model.NextworkStatus
import com.existing.boilerx.app.module.main.adapter.item.PhotoItem
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.app.usecase.DeleteAllDatabaseUseCase
import com.existing.boilerx.app.usecase.LoadPhotoListAfterIdUseCase
import com.existing.boilerx.app.usecase.LoadPhotoListBeforeIdUseCase
import com.existing.boilerx.app.usecase.LoadPhotoListUseCase
import com.existing.boilerx.common.base.mvvm.AppViewModel
import com.existing.boilerx.common.base.repository.base.model.AppResult
import com.existing.boilerx.core.base.usecase.invoke
import com.existing.boilerx.core.base.view.holder.base.BaseItem

class MainViewModel(
    private val loadPhotoListUseCase: LoadPhotoListUseCase = LoadPhotoListUseCase(),
    private val loadPhotoListBeforeIdUseCase: LoadPhotoListBeforeIdUseCase = LoadPhotoListBeforeIdUseCase(),
    private val loadPhotoListAfterIdUseCase: LoadPhotoListAfterIdUseCase = LoadPhotoListAfterIdUseCase(),
    private val deleteAllDatabaseUseCase: DeleteAllDatabaseUseCase = DeleteAllDatabaseUseCase(),
    var baseItemList: MutableList<BaseItem> = mutableListOf()
) : AppViewModel() {

    val loadPhotoList: LiveData<AppResult<List<PhotoModel>>> get() = _loadPhotoList
    private val _loadPhotoList = loadPhotoListUseCase.observe()

    val loadPhotoListAfterId: LiveData<AppResult<List<PhotoModel>>> get() = _loadPhotoListAfterId
    private val _loadPhotoListAfterId = loadPhotoListAfterIdUseCase.observe()

    val loadPhotoListBeforeId: LiveData<AppResult<List<PhotoModel>>> get() = _loadPhotoListBeforeId
    private val _loadPhotoListBeforeId = loadPhotoListBeforeIdUseCase.observe()

    val errorMessage: LiveData<Throwable> get() = _errorMessage
    private val _errorMessage = MediatorLiveData<Throwable>()

    init {
        _errorMessage.addSource(_loadPhotoList){ result ->
            if (result.status == NextworkStatus.ERROR) {
                result.exception?.let{ _errorMessage.value = it }
            }
        }

        _errorMessage.addSource(_loadPhotoListAfterId){ result ->
            if (result.status == NextworkStatus.ERROR) {
                result.exception?.let{ _errorMessage.value = it }
            }
        }

        _errorMessage.addSource(_loadPhotoListBeforeId){ result ->
            if (result.status == NextworkStatus.ERROR) {
                result.exception?.let{ _errorMessage.value = it }
            }
        }
    }


    fun getMaxPhotoId(): Int = loadPhotoListAfterIdUseCase.getMaximumFromDatabase()


    fun requestPhotoList(forceFetch: Boolean = false) {
        loadPhotoListUseCase.execute(LoadPhotoListUseCase.Parameter(forceFetch))
    }

    fun requestPhotoListBeforeId(id: Int) {
        loadPhotoListBeforeIdUseCase.execute(LoadPhotoListBeforeIdUseCase.Parameter(id))
    }

    fun requestPhotoListAfterId(id: Int) {
        loadPhotoListAfterIdUseCase.execute(LoadPhotoListAfterIdUseCase.Parameter(id))
    }

    fun getMinimumPhotoId(): Int {
        if (baseItemList.size == 0) return 0

        var minId = baseItemList[0].id

        baseItemList.forEach { pictureItem ->
            minId = Math.min(minId, pictureItem.id)
        }

        return minId.toInt()
    }

    fun getItem(pos: Int): BaseItem = baseItemList[pos]

    fun getItemType(pos: Int): Int = baseItemList[pos].type

    fun getItemListSize(): Int = baseItemList.size


    fun initialLoadPhoto() {
        requestPhotoList()
        val id = getMaxPhotoId()
        if (id > 0) {
            requestPhotoListAfterId(id)
        }
    }

    fun loadMoreData() {
        val minId = getMinimumPhotoId()
        requestPhotoListBeforeId(minId)
    }


    fun refreshData() {
        baseItemList.clear()
        deleteAllDatabaseUseCase()
        requestPhotoList(true)
    }


    fun getNewDataList(newData: List<PhotoModel>?): MutableList<BaseItem> {
        return newData
            ?.map {
                PhotoItem(it.databaseId.toInt())
                    .setModel(it) as BaseItem
            }?.toMutableList() ?: mutableListOf()
    }

    fun getAfterNewDataList(newData: List<PhotoModel>?): MutableList<BaseItem>? {
        val newDataList = getNewDataList(newData)
        val oldItemList = baseItemList.map { it }.toMutableList()
        newDataList.addAll(oldItemList)
        return newDataList
    }

    fun getBeforeNewDataList(newData: List<PhotoModel>?): MutableList<BaseItem>? {
        val newDataList = getNewDataList(newData)
        val oldItemList = baseItemList.map { it }.toMutableList()
        newDataList.addAll(0, oldItemList)
        return newDataList
    }


    fun updatePhotoItemList(newDataList: MutableList<BaseItem>?) {
        baseItemList = newDataList ?: mutableListOf()
    }


}
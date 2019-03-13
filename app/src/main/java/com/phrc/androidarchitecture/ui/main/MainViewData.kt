package com.phrc.androidarchitecture.ui.main

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.phrc.androidarchitecture.BR
import com.phrc.androidarchitecture.database.entity.RepositoriesEntity
import com.phrc.androidarchitecture.ui.base.AdapterItem

data class RepositoriesViewData constructor(
        val id : Int,
        val name : String?,
        val description : String?,
        val create : String?) : AdapterItem<Int>
{

    override val adapterId: Int
        get() = id

    constructor(entity: RepositoriesEntity) : this(
            entity.id,
            entity.name,
            entity.description,
            entity.create
    )
}

data class RepositoriesListViewData constructor(val items: List<RepositoriesViewData>)

class MainViewData : BaseObservable() {


    internal var search = ""

    @Bindable
    fun getSearch(): String {
        return search
    }

    fun setSearch(search: String) {
        this.search = search
        notifyPropertyChanged(BR.search)
    }
}
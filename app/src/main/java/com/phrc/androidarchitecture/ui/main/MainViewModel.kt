package com.phrc.androidarchitecture.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.phrc.androidarchitecture.repository.DataRequestModel
import com.phrc.androidarchitecture.repository.RepositoriesRepository
import javax.inject.Inject

class MainViewModel
    @Inject internal
    constructor(
        val repositoriesRepository: RepositoriesRepository
    ) : ViewModel() {

    val respositories : LiveData<DataRequestModel<RepositoriesListViewData>>
    val params = MutableLiveData<String>()

    val viewData = MainViewData()

    init {
        respositories =
                Transformations
                        .switchMap(
                                params
                        ) { params ->
                            Transformations.map(
                                    repositoriesRepository.getRepositories(params)
                            ) { result ->
                                result.convert(RepositoriesListViewData(if (result.content == null) {
                                    emptyList()
                                } else {
                                    result.content.map { RepositoriesViewData(it) }
                                }))
                            }
                        }
    }

}
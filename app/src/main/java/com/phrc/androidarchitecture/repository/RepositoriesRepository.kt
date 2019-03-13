package com.phrc.androidarchitecture.repository

import androidx.lifecycle.LiveData
import com.phrc.androidarchitecture.api.requester.RepositoryRequester
import com.phrc.androidarchitecture.api.responseEntity.GetRepositoriesResponse
import com.phrc.androidarchitecture.database.dao.RepositoriesDao
import com.phrc.androidarchitecture.database.entity.RepositoriesEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoriesRepository
    @Inject constructor(private val requester: RepositoryRequester, private val dao: RepositoriesDao) {

    fun getRepositories(user : String) : LiveData<DataRequestModel<List<RepositoriesEntity>>>{
        return DataRequestManager.build<List<RepositoriesEntity>, List<GetRepositoriesResponse>>(
                DataRequestManager.DbQuery{dao.getAllFlowable()},
                DataRequestManager.ApiRequest{requester.getRepositories(user)},
                DataRequestManager.SaveOnDb {response ->
                    dao.replaceTable(response.map{ RepositoriesEntity(it) })
                }
        ).asLiveData()
    }
}
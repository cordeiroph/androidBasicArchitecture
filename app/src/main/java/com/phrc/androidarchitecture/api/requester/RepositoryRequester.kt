package com.phrc.androidarchitecture.api.requester

import com.phrc.androidarchitecture.api.responseEntity.GetRepositoriesResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RepositoryRequester {

    @GET("/users/{username}/repos")
    fun getRepositories(@Path("username") username : String): Single<Response<List<GetRepositoriesResponse>>>

}
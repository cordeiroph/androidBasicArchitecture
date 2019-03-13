package com.phrc.androidarchitecture.api.responseEntity

import com.google.gson.annotations.SerializedName

data class GetRepositoriesResponse
    constructor (
            val id : Int,
            @SerializedName("node_id") val nodeId: String,
            val name : String,
            val owner : GetRepositoriesOwnerResponse,
            val description : String,
            @SerializedName("created_at") val create : String,
            @SerializedName("updated_at") val updated : String,
            @SerializedName("pushed_at") val pushed : String,
            @SerializedName("stargazers_count") val stars : Int,
            val language : String)

data class GetRepositoriesOwnerResponse
    constructor (
            val id : Int,
            val login: String,
            @SerializedName("avatar_url") val image : String)
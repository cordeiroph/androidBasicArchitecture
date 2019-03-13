package com.phrc.androidarchitecture.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.phrc.androidarchitecture.api.responseEntity.GetRepositoriesResponse

@Entity(tableName = "RepositoriesEntity")
data class RepositoriesEntity constructor(

        @PrimaryKey val id : Int,
        val nodeId: String?,
        val name : String?,
//        val owner : GetRepositoriesOwnerResponse,
        val description : String?,
        val create : String,
        val updated : String?,
        val pushed : String?,
        val stars : Int,
        val language : String
){
    @Ignore
    constructor(repositoriesResponse: GetRepositoriesResponse) : this(
            repositoriesResponse.id,
            repositoriesResponse.nodeId,
            repositoriesResponse.name,
            repositoriesResponse.description,
            repositoriesResponse.create,
            repositoriesResponse.updated,
            repositoriesResponse.pushed,
            repositoriesResponse.stars,
            repositoriesResponse.language
    )

}
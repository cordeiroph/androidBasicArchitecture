package com.phrc.androidarchitecture.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.phrc.androidarchitecture.api.responseEntity.GetRepositoriesOwnerResponse

@Entity
data class OwnerEntity  constructor(
        @PrimaryKey val id : Int,
        val name: String,
        val image : String
){
    @Ignore
    constructor(ownerResponse: GetRepositoriesOwnerResponse) : this (
           ownerResponse.id,
           ownerResponse.login,
           ownerResponse.image
    )
}
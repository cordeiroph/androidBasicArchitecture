package com.phrc.androidarchitecture.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.phrc.androidarchitecture.database.entity.OwnerEntity
import io.reactivex.Flowable

@Dao
abstract class OwnerDao : BaseDao<OwnerEntity>
{

    @Query("Select * from OwnerEntity")
    abstract fun getAllFlowable() : Flowable<List<OwnerEntity>>

    @Query("Select * from OwnerEntity")
    abstract fun getAllLiveData() : LiveData<List<OwnerEntity>>
}
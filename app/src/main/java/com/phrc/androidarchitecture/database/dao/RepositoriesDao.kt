package com.phrc.androidarchitecture.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.phrc.androidarchitecture.database.entity.RepositoriesEntity
import io.reactivex.Flowable

@Dao
interface RepositoriesDao: BaseDao<RepositoriesEntity>
{

    @Query("Select * from RepositoriesEntity")
    fun getAllFlowable() : Flowable<List<RepositoriesEntity>>

    @Query("Select * from RepositoriesEntity")
    fun getAllLiveData() : LiveData<List<RepositoriesEntity>>

    @Transaction
    fun replaceTable(newValues : List<RepositoriesEntity>){
        insertAll(newValues)
        deleteAllExcept(newValues.map { it.id })
    }

    @Query("DELETE from RepositoriesEntity where id not in (:ids)")
    fun deleteAllExcept(ids: List<Int>)
}
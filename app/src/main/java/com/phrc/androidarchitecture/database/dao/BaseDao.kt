package com.phrc.androidarchitecture.database.dao

import androidx.room.*

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t : T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(t : List<T>)

    @Update
    fun update(t: T)

    @Update
    fun updateAll(t: List<T>)

    @Delete
    fun delete(t: T)

    @Delete
    fun deleteAll(t: List<T>)

}
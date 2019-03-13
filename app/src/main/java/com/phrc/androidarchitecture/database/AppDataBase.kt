package com.phrc.androidarchitecture.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.phrc.androidarchitecture.database.dao.OwnerDao
import com.phrc.androidarchitecture.database.dao.RepositoriesDao
import com.phrc.androidarchitecture.database.entity.OwnerEntity
import com.phrc.androidarchitecture.database.entity.RepositoriesEntity

@Database(entities = [
    RepositoriesEntity::class,
    OwnerEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "androidArchitectureDB.db"
    }

    abstract fun repositoriesDao() : RepositoriesDao
    abstract fun ownerDao() : OwnerDao
}
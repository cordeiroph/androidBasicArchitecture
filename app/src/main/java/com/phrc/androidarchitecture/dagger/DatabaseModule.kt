package com.phrc.androidarchitecture.dagger

import android.content.Context
import com.phrc.androidarchitecture.database.AppDataBase
import com.phrc.androidarchitecture.database.DataBaseCreator
import com.phrc.androidarchitecture.database.dao.OwnerDao
import com.phrc.androidarchitecture.database.dao.RepositoriesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(context: Context): AppDataBase {
        return DataBaseCreator(context).database
    }

    @Singleton
    @Provides
    fun provideOwnerDao(db: AppDataBase): OwnerDao {
        return db.ownerDao()
    }

    @Singleton
    @Provides
    fun provideRepositoriesDao(db: AppDataBase): RepositoriesDao {
        return db.repositoriesDao()
    }



}
package com.fusetech.mobilleltarkotlin.di

import android.content.Context
import com.fusetech.mobilleltarkotlin.BaseApplication
import com.fusetech.mobilleltarkotlin.repositories.Sql
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication{
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun getSql(): Sql{
        return Sql()
    }

}

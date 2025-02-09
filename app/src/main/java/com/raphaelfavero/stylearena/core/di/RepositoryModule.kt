package com.raphaelfavero.stylearena.core.di

import com.raphaelfavero.stylearena.ranking.DailyWinnersRepository
import com.raphaelfavero.stylearena.stylearena.StyleArenaRepository
import com.raphaelfavero.stylearena.styleupload.StyleUploadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStyleUploadRepository(repository: StyleUploadRepository.Impl): StyleUploadRepository

    @Binds
    @Singleton
    abstract fun bindStyleArenaRepository(repository: StyleArenaRepository.Impl): StyleArenaRepository


    @Binds
    @Singleton
    abstract fun bindDailyWinnersRepository(repository: DailyWinnersRepository.Impl): DailyWinnersRepository

}
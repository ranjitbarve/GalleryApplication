package com.ranjit.data.logger.di

import com.ranjit.data.BuildConfig
import com.ranjit.data.logger.utils.DebugLogger
import com.ranjit.data.logger.utils.Logger
import com.ranjit.data.logger.utils.ProductionLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        var logger: Logger?
        logger = if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            DebugLogger()
        } else {
            ProductionLogger()
        }
        return logger
    }

}
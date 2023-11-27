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

/*
Key points:

LoggerModule is a Dagger Hilt module.

@Provides: Annotates methods inside Dagger modules to specify that they provide instances of certain types.

@Singleton: Indicates that the provided instance should be treated as a singleton.

fun provideLogger(): Logger: Provides a Logger instance.

BuildConfig.DEBUG: Checks whether the build is in DEBUG mode.

If in DEBUG mode:

Timber.plant(Timber.DebugTree()): Plants a Timber DebugTree for logging in the Debug mode.
DebugLogger(): Creates an instance of the DebugLogger implementation.
If not in DEBUG mode:

ProductionLogger(): Creates an instance of the ProductionLogger implementation.
Returns the selected Logger implementation based on the build configuration.
*/


@Module
@InstallIn(SingletonComponent::class)
object LoggerModule {

    // This module provides a Logger instance based on the build configuration.

    @Provides
    @Singleton
    fun provideLogger(): Logger {
        var logger: Logger?

        // Declare a Logger variable.

        logger = if (BuildConfig.DEBUG) {
            // If the build is in DEBUG mode:

            Timber.plant(Timber.DebugTree())
            // Plant a Timber DebugTree for logging.

            DebugLogger()
            // Use the DebugLogger implementation.

        } else {
            // If the build is not in DEBUG mode:

            ProductionLogger()
            // Use the ProductionLogger implementation.

        }

        return logger
        // Return the selected Logger implementation.
    }

}

package com.ranjit.galleryapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Gallery : Application() {

    // This class represents the application. It is annotated with @HiltAndroidApp
    // to enable Hilt for dependency injection.

    override fun onCreate() {
        super.onCreate()

        // The onCreate() method is called when the application is first created.

        if (BuildConfig.DEBUG) {
            // Check if the application is in debug mode.

            // If it is, plant a Timber.DebugTree() for logging. Timber is a logging library.
            Timber.plant(Timber.DebugTree())
        }
    }
}
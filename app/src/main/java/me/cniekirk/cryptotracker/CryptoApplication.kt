package me.cniekirk.cryptotracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * [Application] class that initialises logging and has the required Hilt annotation
 *
 * @author Charlie Niekirk
 */
@HiltAndroidApp
class CryptoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            TODO("Plant a production logging tree that logs to Crashlytics or similar")
        }
    }
}
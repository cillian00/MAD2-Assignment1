package org.wit.gym.main

import android.app.Application
import org.wit.gym.models.GymCloudStore
import org.wit.gym.models.GymStore
import timber.log.Timber
import timber.log.Timber.Forest.i

class MainApp : Application() {

    lateinit var gyms: GymStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        gyms = GymCloudStore(applicationContext)
        i("gyms started")
    }
}
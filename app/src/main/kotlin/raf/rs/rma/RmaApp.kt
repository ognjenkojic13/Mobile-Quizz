package raf.rs.rma

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RmaApp : Application() {

    override fun onCreate() {
        super.onCreate()
//        RmaDatabase.initDatabase(context = this)
    }
}
package com.unware.mediaapp

import android.app.Application

class MediaApp: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MediaApp? = null

        fun applicationContext() : MediaApp {
            return instance as MediaApp
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
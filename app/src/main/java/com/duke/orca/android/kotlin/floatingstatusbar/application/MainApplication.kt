package com.duke.orca.android.kotlin.floatingstatusbar.application

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.duke.orca.android.kotlin.floatingstatusbar.floating_status_bar.FloatingStatusBarService
import com.duke.orca.android.kotlin.floatingstatusbar.penguin.PenguinIconService
import timber.log.Timber

class MainApplication: Application(), LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Timber.d("onAppBackground")

        LocalBroadcastManager.getInstance(this).also {
            it.sendBroadcast(Intent(FloatingStatusBarService.ACTION_ON_APPLICATION_BACKGROUNDED))
            it.sendBroadcast(Intent(PenguinIconService.ACTION_ON_APPLICATION_BACKGROUNDED))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        Timber.d("onAppForegrounded")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, FloatingStatusBarService::class.java))
            startForegroundService(Intent(this, PenguinIconService::class.java))
        } else {
            startService(Intent(this, FloatingStatusBarService::class.java))
            startService(Intent(this, PenguinIconService::class.java))
        }
    }
}
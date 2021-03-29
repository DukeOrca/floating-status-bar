package com.duke.orca.android.kotlin.floatingstatusbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.duke.orca.android.kotlin.floatingstatusbar.floating_status_bar.FloatingStatusBarService

class ConfigurationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        findViewById<Button>(R.id.button).setOnClickListener {
            hideFloatingStatusBar()
        }
    }

    private fun hideFloatingStatusBar() {
        LocalBroadcastManager.getInstance(this).also {
            it.sendBroadcast(Intent(FloatingStatusBarService.ACTION_ON_APPLICATION_BACKGROUNDED))
        }
    }
}
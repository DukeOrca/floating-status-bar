package com.duke.orca.android.kotlin.floatingstatusbar.penguin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.duke.orca.android.kotlin.floatingstatusbar.R
import timber.log.Timber

class PenguinIconService: Service() {
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_ON_APPLICATION_BACKGROUNDED -> {
                    stopForeground(true)
                    stopSelf()
                }
                else -> { }
            }
        }
    }

    private val windowManager: WindowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private var imageView: ImageView? = null

    override fun onCreate() {
        super.onCreate()

        imageView = ImageView(this).apply {
            setImageResource(R.drawable.ic_penguin_96px)
        }

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE

        val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.BOTTOM

        windowManager.addView(imageView, layoutParams)

        showNotification()

        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver,
                IntentFilter(ACTION_ON_APPLICATION_BACKGROUNDED)
        )
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

        imageView?.let {
            try {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
                windowManager.removeView(it)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_MIN
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.description = getString(R.string.notification_channel_description)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_orca_50px)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_content_text))
                .setPriority(NotificationCompat.PRIORITY_MIN)

        val notification: Notification = builder.build()

        startForeground(1, notification)
    }

    companion object {
        @Suppress("SpellCheckingInspection")
        const val ACTION_ON_APPLICATION_BACKGROUNDED = "com.duke.orca.android.kotlin.floatingstatusbar.penguin" +
                ".penguin_icon_service.action_on_application_backgrounded"
        @Suppress("SpellCheckingInspection")
        const val CHANNEL_ID = "com.duke.orca.android.kotlin.floatingstatusbar" +
                ".penguin.penguin_icon_service.channel_id"
        const val CHANNEL_NAME = "PenguinIcon"
    }
}
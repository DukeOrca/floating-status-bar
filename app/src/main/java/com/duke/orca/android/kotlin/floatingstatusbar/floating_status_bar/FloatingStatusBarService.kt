package com.duke.orca.android.kotlin.floatingstatusbar.floating_status_bar

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
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.duke.orca.android.kotlin.floatingstatusbar.R
import timber.log.Timber

class FloatingStatusBarService : Service() {

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_ON_APPLICATION_BACKGROUNDED -> {
                    stopForeground(true)  // 의문. 다른 노티도 지우면 안되는데? 서비스에서 발행한 것만 지울수 있는건가?? // 플로우서 확인.
                    stopSelf()
                }
                else -> { }
            }
        }
    }

    private val windowManager: WindowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private var textView: TextView? = null

    override fun onCreate() {
        super.onCreate()

        val text = "flow.team"

        textView = TextView(this).apply {
            setBackgroundColor(ContextCompat.getColor(this@FloatingStatusBarService,
                R.color.red
            ))
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            this.text = text
        }

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.gravity = Gravity.TOP

        windowManager.addView(textView, layoutParams)

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

        textView?.let {
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
        const val ACTION_ON_APPLICATION_BACKGROUNDED = "com.duke.orca.android.kotlin.floatingstatusbar.floating_status_bar" +
                ".floating_status_bar_service.action_on_application_backgrounded"
        @Suppress("SpellCheckingInspection")
        const val CHANNEL_ID = "com.duke.orca.android.kotlin.floatingstatusbar" +
            ".floating_status_bar.floating_status_bar_service.channel_id"
        const val CHANNEL_NAME = "FloatingStatusBar"
    }
}
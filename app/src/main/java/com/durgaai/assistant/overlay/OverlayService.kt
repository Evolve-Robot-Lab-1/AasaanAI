package com.durgaai.assistant.overlay

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import com.durgaai.assistant.DurgaAIApplication
import com.durgaai.assistant.MainActivity
import com.durgaai.assistant.R

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private var floatingBubble: FloatingBubble? = null
    private lateinit var prefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Mark service as enabled
        prefs.edit().putBoolean(PREF_OVERLAY_ENABLED, true).apply()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start as foreground service (Android 12+ compliance)
        startForeground(NOTIFICATION_ID, createNotification())

        // Create and show floating bubble
        if (floatingBubble == null) {
            floatingBubble = FloatingBubble(this, windowManager)
            floatingBubble?.show()
        }

        // Return START_STICKY for auto-restart
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        floatingBubble?.hide()
        floatingBubble = null

        // Mark service as disabled
        prefs.edit().putBoolean(PREF_OVERLAY_ENABLED, false).apply()
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, DurgaAIApplication.OVERLAY_CHANNEL_ID)
            .setContentTitle("Durga AI Assistant")
            .setContentText("Overlay is active and ready")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        const val PREFS_NAME = "DurgaAIPrefs"
        const val PREF_OVERLAY_ENABLED = "overlay_enabled"

        fun isRunning(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getBoolean(PREF_OVERLAY_ENABLED, false)
        }
    }
}

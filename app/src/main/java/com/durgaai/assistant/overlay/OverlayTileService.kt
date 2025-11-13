package com.durgaai.assistant.overlay

import android.content.Intent
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi

/**
 * Quick Settings Tile for toggling the overlay service on/off.
 * Provides easy access to start/stop the assistant from the notification shade.
 */
@RequiresApi(Build.VERSION_CODES.N)
class OverlayTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()

        val isRunning = OverlayService.isRunning(this)
        val intent = Intent(this, OverlayService::class.java)

        if (isRunning) {
            // Stop the service
            stopService(intent)
        } else {
            // Start the service
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }

        // Update tile state after a short delay to reflect the change
        android.os.Handler(mainLooper).postDelayed({
            updateTileState()
        }, 500)
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        val isRunning = OverlayService.isRunning(this)

        tile.state = if (isRunning) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.label = if (isRunning) "AI Assistant ON" else "AI Assistant OFF"
        tile.updateTile()
    }
}

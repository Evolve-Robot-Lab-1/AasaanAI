package com.durgaai.assistant.accessibility_bridge

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 * Utility class for inserting AI-generated replies into chat apps.
 * Provides multiple insertion methods with fallbacks.
 */
object ReplyInserter {

    /**
     * Insert text using the best available method.
     * Primary: Accessibility ACTION_SET_TEXT
     * Fallback: Clipboard + paste
     */
    fun insertText(context: Context, text: String, useClipboardFallback: Boolean = true): Boolean {
        // Try accessibility service first
        val accessibilityService = DurgaAccessibilityService.instance
        if (accessibilityService != null && accessibilityService.insertReply(text)) {
            return true
        }

        // Fallback to clipboard method
        if (useClipboardFallback) {
            return insertViaClipboard(context, text)
        }

        return false
    }

    /**
     * Insert text by copying to clipboard.
     * User will need to manually paste.
     */
    private fun insertViaClipboard(context: Context, text: String): Boolean {
        return try {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Durga AI Reply", text)
            clipboardManager.setPrimaryClip(clipData)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Insert text with auto-send (Power Mode).
     * Phase 4-5 feature: automatically sends the message.
     */
    fun insertAndSend(context: Context, text: String): Boolean {
        if (insertText(context, text, useClipboardFallback = false)) {
            // Add delay and trigger send action
            Handler(Looper.getMainLooper()).postDelayed({
                // Trigger send button click via accessibility
                // Implementation in Phase 4-5
            }, 300)
            return true
        }
        return false
    }
}

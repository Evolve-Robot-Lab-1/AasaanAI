package com.durgaai.assistant.accessibility_bridge

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Accessibility service for reading chat context and inserting replies.
 * Phase 4-5 implementation will include:
 * - Reading chat messages from various apps
 * - Inserting AI-generated replies using ACTION_SET_TEXT
 * - Fallback to clipboard + paste if needed
 */
class DurgaAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()

        serviceInfo = AccessibilityServiceInfo().apply {
            // Configure to listen to all apps
            eventTypes = AccessibilityEvent.TYPE_VIEW_FOCUSED or
                    AccessibilityEvent.TYPE_VIEW_CLICKED or
                    AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED

            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                    AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY
            notificationTimeout = 100
        }

        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Phase 4-5: Extract chat messages from accessibility nodes
        // For now, this is a placeholder
    }

    override fun onInterrupt() {
        // Handle service interruption
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    /**
     * Insert text into the currently focused input field.
     * Uses ACTION_SET_TEXT for direct insertion.
     */
    fun insertReply(text: String): Boolean {
        val rootNode = rootInActiveWindow ?: return false

        try {
            val focusedNode = findFocusedEditText(rootNode)
            if (focusedNode != null) {
                val arguments = android.os.Bundle()
                arguments.putCharSequence(
                    AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    text
                )
                return focusedNode.performAction(
                    AccessibilityNodeInfo.ACTION_SET_TEXT,
                    arguments
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            rootNode.recycle()
        }

        return false
    }

    /**
     * Recursively find the focused EditText node.
     */
    private fun findFocusedEditText(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        if (node == null) return null

        if (node.isFocused && node.isEditable) {
            return node
        }

        for (i in 0 until node.childCount) {
            val result = findFocusedEditText(node.getChild(i))
            if (result != null) return result
        }

        return null
    }

    companion object {
        var instance: DurgaAccessibilityService? = null
            private set

        fun isEnabled(): Boolean = instance != null
    }
}

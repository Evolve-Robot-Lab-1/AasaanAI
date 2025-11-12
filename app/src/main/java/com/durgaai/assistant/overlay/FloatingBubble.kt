package com.durgaai.assistant.overlay

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.durgaai.assistant.ui.theme.DurgaAITheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ClickableViewAccessibility")
class FloatingBubble(
    private val context: Context,
    private val windowManager: WindowManager
) {
    private var bubbleView: View? = null
    private var params: WindowManager.LayoutParams? = null

    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0f
    private var initialTouchY: Float = 0f
    private var isDragging = false

    fun show() {
        if (bubbleView != null) return

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 100
        }

        val container = FrameLayout(context)
        val composeView = ComposeView(context).apply {
            setContent {
                DurgaAITheme {
                    BubbleContent()
                }
            }
        }

        container.addView(composeView)
        bubbleView = container

        // Handle touch events for dragging
        container.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params?.x ?: 0
                    initialY = params?.y ?: 0
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isDragging = false
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - initialTouchX
                    val deltaY = event.rawY - initialTouchY

                    if (Math.abs(deltaX) > 10 || Math.abs(deltaY) > 10) {
                        isDragging = true
                        params?.x = initialX + deltaX.toInt()
                        params?.y = initialY + deltaY.toInt()
                        params?.let { windowManager.updateViewLayout(bubbleView, it) }
                    }
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (!isDragging) {
                        // Handle click
                        (composeView.getChildAt(0) as? View)?.performClick()
                    }
                    true
                }
                else -> false
            }
        }

        try {
            windowManager.addView(bubbleView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide() {
        bubbleView?.let {
            try {
                windowManager.removeView(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        bubbleView = null
    }

    @Composable
    private fun BubbleContent() {
        var isPulsing by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            while (true) {
                isPulsing = true
                delay(1000)
                isPulsing = false
                delay(1000)
            }
        }

        Box(
            modifier = Modifier
                .size(if (isPulsing) 64.dp else 60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    // Handle bubble click - will expand to show reply options in later phases
                    scope.launch {
                        // Placeholder for future functionality
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "AI",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
        }
    }
}

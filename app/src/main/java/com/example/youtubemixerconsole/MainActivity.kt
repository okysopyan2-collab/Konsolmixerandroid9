package com.example.youtubemixerconsole

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.graphics.Typeface
import kotlin.math.max
import kotlin.math.min

class MainActivity : Activity() {
    private val bg = Color.rgb(19, 22, 27)
    private val panel = Color.rgb(34, 39, 46)
    private val accent = Color.rgb(42, 190, 126)
    private val mutedColor = Color.rgb(220, 64, 64)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 14, 18)
        window.navigationBarColor = Color.rgb(12, 14, 18)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }
        val header = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(12), 0, dp(12), 0)
            setBackgroundColor(Color.rgb(12, 14, 18))
        }
        header.addView(label("KONSOL MIXER", 16f, Color.WHITE, true),
            LinearLayout.LayoutParams(0, dp(42), 1f))
        header.addView(label("SIMULASI • Android 9", 11f, Color.LTGRAY, false))
        root.addView(header, LinearLayout.LayoutParams(-1, dp(42)))

        val body = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(dp(6), dp(5), dp(6), dp(5))
        }

        val youtubePanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(4), dp(4), dp(4), dp(4))
            setBackgroundColor(panel)
        }
        youtubePanel.addView(label("YOUTUBE", 12f, Color.WHITE, true),
            LinearLayout.LayoutParams(-1, dp(28)))
        val web = WebView(this)
        web.setBackgroundColor(Color.BLACK)
        web.webViewClient = WebViewClient()
        web.webChromeClient = WebChromeClient()
        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.mediaPlaybackRequiresUserGesture = true
        web.settings.cacheMode = WebSettings.LOAD_DEFAULT
        web.loadUrl("https://m.youtube.com/")
        youtubePanel.addView(web, LinearLayout.LayoutParams(-1, 0, 1f))
        youtubePanel.addView(label("Putar video dengan mengetuknya. YouTube memerlukan internet.", 9f,
            Color.LTGRAY, false), LinearLayout.LayoutParams(-1, dp(28)))
        body.addView(youtubePanel, LinearLayout.LayoutParams(0, -1, 0.43f))

        val mixerPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(6), dp(4), dp(6), dp(4))
            setBackgroundColor(panel)
        }
        val mixerTitle = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        mixerTitle.addView(label("INPUT CHANNELS", 12f, Color.WHITE, true),
            LinearLayout.LayoutParams(0, dp(26), 1f))
        mixerTitle.addView(label("L  ▮▮▮     R  ▮▮▮", 10f, accent, true))
        mixerPanel.addView(mixerTitle, LinearLayout.LayoutParams(-1, dp(28)))

        val channels = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        for (i in 1..4) channels.addView(channelStrip(i),
            LinearLayout.LayoutParams(0, 0, 1f).apply { height = 0; weight = 1f })
        mixerPanel.addView(channels, LinearLayout.LayoutParams(-1, 0, 1f))

        val lower = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(4), dp(2), dp(4), dp(2))
            setBackgroundColor(Color.rgb(25, 29, 34))
        }
        val eqBox = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        eqBox.addView(label("EQ", 11f, Color.WHITE, true), LinearLayout.LayoutParams(dp(30), -1))
        listOf("80", "250", "1k", "4k", "12k").forEach { hz ->
            val eq = Button(this).apply {
                text = hz
                textSize = 9f
                setTextColor(Color.WHITE)
                setPadding(0, 0, 0, 0)
                setBackgroundColor(Color.rgb(55, 62, 70))
                setOnClickListener {
                    isSelected = !isSelected
                    setBackgroundColor(if (isSelected) accent else Color.rgb(55, 62, 70))
                }
            }
            eqBox.addView(eq, LinearLayout.LayoutParams(dp(42), dp(34)).apply { setMargins(dp(2), 0, dp(2), 0) })
        }
        lower.addView(eqBox, LinearLayout.LayoutParams(0, dp(48), 1f))

        val master = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(8), 0, dp(4), 0)
        }
        master.addView(label("MASTER", 10f, Color.WHITE, true))
        val masterFader = FaderView(this, 0.78f)
        master.addView(masterFader, LinearLayout.LayoutParams(dp(42), dp(48)))
        val fx = Button(this).apply {
            text = "FX ON"
            textSize = 10f
            setTextColor(Color.WHITE)
            setBackgroundColor(accent)
            setOnClickListener {
                isSelected = !isSelected
                text = if (isSelected) "FX OFF" else "FX ON"
                setBackgroundColor(if (isSelected) mutedColor else accent)
            }
        }
        master.addView(fx, LinearLayout.LayoutParams(dp(72), dp(38)))
        lower.addView(master, LinearLayout.LayoutParams(-2, dp(48)))
        mixerPanel.addView(lower, LinearLayout.LayoutParams(-1, dp(52)))

        body.addView(mixerPanel, LinearLayout.LayoutParams(0, -1, 0.57f))
        root.addView(body, LinearLayout.LayoutParams(-1, 0, 1f))
        root.addView(label("Catatan: fader/EQ/FX adalah simulasi visual dan tidak memproses audio YouTube di Android.",
            9f, Color.rgb(255, 205, 90), false).apply {
            gravity = Gravity.CENTER
            setPadding(dp(4), 0, dp(4), 0)
        }, LinearLayout.LayoutParams(-1, dp(20)))
        setContentView(root)
    }

    private fun channelStrip(number: Int): View {
        val strip = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(dp(3), dp(3), dp(3), dp(3))
            setBackgroundColor(if (number % 2 == 0) Color.rgb(42, 48, 56) else Color.rgb(38, 44, 51))
        }
        strip.addView(label("CH $number", 11f, Color.WHITE, true),
            LinearLayout.LayoutParams(-1, dp(24)))
        val meter = MeterView(this)
        val faderArea = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        faderArea.addView(meter, LinearLayout.LayoutParams(dp(10), -1))
        faderArea.addView(FaderView(this, 0.68f), LinearLayout.LayoutParams(0, -1, 1f))
        strip.addView(faderArea, LinearLayout.LayoutParams(-1, 0, 1f))
        val buttons = LinearLayout(this).apply { gravity = Gravity.CENTER }
        listOf("MUTE", "SOLO").forEach { title ->
            val button = Button(this).apply {
                text = title
                textSize = 8f
                setTextColor(Color.WHITE)
                setPadding(0, 0, 0, 0)
                setBackgroundColor(Color.rgb(69, 76, 84))
                setOnClickListener {
                    isSelected = !isSelected
                    setBackgroundColor(if (isSelected) (if (title == "MUTE") mutedColor else Color.rgb(201, 151, 42)) else Color.rgb(69, 76, 84))
                }
            }
            buttons.addView(button, LinearLayout.LayoutParams(0, dp(34), 1f).apply {
                setMargins(dp(2), dp(3), dp(2), 0)
            })
        }
        strip.addView(buttons, LinearLayout.LayoutParams(-1, dp(38)))
        return strip
    }

    private fun label(textValue: String, size: Float, color: Int, bold: Boolean): TextView =
        TextView(this).apply {
            text = textValue
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            if (bold) typeface = Typeface.DEFAULT_BOLD
            maxLines = 1
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    private inner class FaderView(context: android.content.Context, initial: Float) : View(context) {
        private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(13, 16, 20); strokeWidth = dp(4).toFloat(); strokeCap = Paint.Cap.ROUND }
        private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.rgb(220, 224, 228) }
        private val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = accent; strokeWidth = dp(2).toFloat() }
        private var value = initial.coerceIn(0f, 1f)

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val x = width / 2f
            val top = dp(12).toFloat()
            val bottom = max(top + 1f, height - dp(12).toFloat())
            canvas.drawLine(x, top, x, bottom, trackPaint)
            val y = bottom - value * (bottom - top)
            canvas.drawLine(x, y, x, bottom, accentPaint)
            canvas.drawRoundRect(RectF(x - dp(12), y - dp(5), x + dp(12), y + dp(5)), dp(3).toFloat(), dp(3).toFloat(), thumbPaint)
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val top = dp(12).toFloat()
                    val bottom = max(top + 1f, height - dp(12).toFloat())
                    value = (1f - (event.y - top) / (bottom - top)).coerceIn(0f, 1f)
                    invalidate()
                    return true
                }
            }
            return true
        }
    }

    private inner class MeterView(context: android.content.Context) : View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val segments = 12
            val gap = dp(2).toFloat()
            val h = (height - gap * (segments - 1)) / segments
            for (i in 0 until segments) {
                paint.color = when {
                    i >= 10 -> Color.rgb(220, 65, 55)
                    i >= 7 -> Color.rgb(230, 177, 52)
                    else -> Color.rgb(46, 194, 119)
                }
                val y = height - (i + 1) * h - i * gap
                canvas.drawRoundRect(RectF(0f, y, width.toFloat(), y + h), dp(2).toFloat(), dp(2).toFloat(), paint)
            }
        }
    }
}

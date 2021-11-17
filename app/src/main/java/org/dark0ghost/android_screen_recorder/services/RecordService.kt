package org.dark0ghost.android_screen_recorder.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.*
import android.util.Log
import android.widget.Toast
import org.dark0ghost.android_screen_recorder.R
import org.dark0ghost.android_screen_recorder.interfaces.GetIntent
import org.dark0ghost.android_screen_recorder.interfaces.GetsDirectory
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.AUDIO_ENCODER
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.AUDIO_SOURCE
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.BIT_RATE
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.HEIGHT
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.NAME_DIR_VIDEO
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.OUTPUT_FORMAT
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.SERVICE_THREAD_NAME
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.VIDEO_ENCODER
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.VIDEO_FRAME_RATE
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.VIDEO_SOURCE
import org.dark0ghost.android_screen_recorder.utils.Settings.MediaRecordSettings.WIDTH
import org.dark0ghost.android_screen_recorder.utils.Settings.NotificationSettings.CHANNEL_ID
import org.dark0ghost.android_screen_recorder.utils.Settings.NotificationSettings.CONTENT_TEXT
import org.dark0ghost.android_screen_recorder.utils.Settings.NotificationSettings.CONTENT_TITTLE
import org.dark0ghost.android_screen_recorder.utils.Settings.NotificationSettings.FOREGROUND_ID
import java.io.File
import java.io.IOException


class RecordService: GetsDirectory, Service() {
    private val binder = RecordBinder()

    private var virtualDisplay: VirtualDisplay? = null

    private var dpi: Int = 0

    private lateinit var notification: Notification
    private lateinit var notificationManager: NotificationManager
    private lateinit var mediaRecorder: MediaRecorder

    private fun createVirtualDisplay() {
        try {
            mediaProjection?.let {
                virtualDisplay = it.createVirtualDisplay(
                    "MainScreen",
                    WIDTH,
                    HEIGHT,
                    dpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mediaRecorder.surface,
                    null,
                    null
                )
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun initNotification(): Notification {
        val notificationBuilder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Notification.Builder(this, CHANNEL_ID).apply {
                    setContentTitle(CONTENT_TITTLE)
                    setContentText(CONTENT_TEXT)
                    setSmallIcon(R.drawable.ic_notification_custom)
                }
            } else {
                Notification.Builder(this).apply {
                    setContentTitle(CONTENT_TITTLE)
                    setContentText(CONTENT_TEXT)
                    setSmallIcon(R.drawable.ic_stat_cast_connected)
                }
            }
        return notificationBuilder.build()
    }

    private fun initRecorder() {
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            MediaRecorder()
        }

        mediaRecorder.apply {
            setAudioSource(AUDIO_SOURCE)
            setVideoSource(VIDEO_SOURCE)
            setOutputFormat(OUTPUT_FORMAT)
            setOutputFile("${getsDirectory()}${System.currentTimeMillis()}.mp4")
            setVideoSize(WIDTH, HEIGHT)
            setVideoEncoder(VIDEO_ENCODER)
            setAudioEncoder(AUDIO_ENCODER)
            setVideoEncodingBitRate(BIT_RATE)
            setVideoFrameRate(VIDEO_FRAME_RATE)
            try {
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    private fun stopNotification() = notificationManager.cancel(FOREGROUND_ID)

    private fun notifyMassage() =  notificationManager.notify(FOREGROUND_ID, notification)


    var running: Boolean = false
        private set

    @Volatile
    var mediaProjection: MediaProjection? = null

    fun setDpi(dpi1: Int) {
        dpi = dpi1
    }

    fun startRecord(): Boolean {
        if (mediaProjection == null || running) {
            return false
        }
        initRecorder()
        createVirtualDisplay()
        notifyMassage()
        mediaRecorder.start()
        running = true
        return true
    }

    fun stopRecord(): Boolean {
        if (!running) {
            return false
        }
        stopNotification()
        running = false
        mediaRecorder.apply {
            stop()
            reset()
            release()
        }
        virtualDisplay?.release()
        mediaProjection?.stop()
        return true
    }

    // GetsDirectory

    override fun getsDirectory(): String {
        val rootDir = "${getExternalFilesDir("media")!!.absolutePath}/${NAME_DIR_VIDEO}/"
        val file = File(rootDir)
        if (!file.exists()) {
            Log.e(
                "getsDirectory/mkdirs", if (file.mkdirs()) {
                    "path is created"
                } else {
                    "path isn't create"
                }
            )
        }
        Toast.makeText(applicationContext, rootDir, Toast.LENGTH_SHORT).show()
        Log.e("getsDirectory", rootDir)
        return rootDir
    }

    // End GetsDirectory

    override fun onCreate() {
        super.onCreate()
        val serviceThread = HandlerThread(
            SERVICE_THREAD_NAME,
            Process.THREAD_PRIORITY_BACKGROUND
        )
        serviceThread.start()
        running = false
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notification = initNotification()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CONTENT_TEXT, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        startForeground(FOREGROUND_ID, notification)
        return START_NOT_STICKY
    }


    inner class RecordBinder : Binder() {
        fun getRecordService(): RecordService = this@RecordService
    }

    companion object : GetIntent {
        override fun intent(context: Context): Intent = Intent(context, RecordService::class.java)
    }
}
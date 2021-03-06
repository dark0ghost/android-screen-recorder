package org.dark0ghost.android_screen_recorder.utils

import android.Manifest
import android.media.MediaRecorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.dark0ghost.android_screen_recorder.R
import org.dark0ghost.android_screen_recorder.states.ClickState
import androidx.compose.runtime.saveable.rememberSaveable

object Settings {
   object DebugSettings {
      const val DEBUG_MODE: Boolean = false
   }

   object AudioRecordSettings {
      const val PERMISSIONS_REQUEST_RECORD_AUDIO: Int = 1
      const val SIMPLE_RATE: Float = 16000.0f
   }

   object MediaRecordSettings {
      const val BIT_RATE: Int = 5 * 1024 * 1024
      const val VIDEO_FRAME_RATE: Int = 60
      const val NAME_DIR_VIDEO: String = "ScreenRecord"
      const val NAME_DIR_SUBTITLE: String = "Subtitle"
      const val SERVICE_THREAD_NAME: String = "service_thread"
      const val WIDTH: Int = 1080
      const val HEIGHT: Int = 1920
      const val AUDIO_ENCODER: Int = MediaRecorder.AudioEncoder.AMR_NB
      const val VIDEO_ENCODER: Int = MediaRecorder.VideoEncoder.H264
      const val OUTPUT_FORMAT: Int = MediaRecorder.OutputFormat.THREE_GPP
      const val VIDEO_SOURCE: Int = MediaRecorder.VideoSource.SURFACE
      const val AUDIO_SOURCE: Int = MediaRecorder.AudioSource.MIC
      const val EXTRA_COMMAND_KEY: String = "COMMAND_KEY"
      const val ACTION_START_SERVICE: String = "ACTION_START_SERVICE"
      const val ACTION_START_RECORDING: String = "ACTION_START_RECORDING"
      const val ACTION_STOP_SERVICE: String = "ACTION_STOP_SERVICE"
      const val COMMAND_START_SERVICE: Int = 4
      const val COMMAND_STOP_SERVICE: Int = 5
   }

   object NotificationSettings {
      const val CHANNEL_ID: String = "recorder"
      const val CONTENT_TITTLE: Int = R.string.content_tittle
      const val CONTENT_TEXT: Int = R.string.content_text
      const val NOTIFICATION_FOREGROUND_ID: Int = 1
   }

   object InlineButtonSettings {
      const val WIDTH: Int = 200
      const val HEIGHT: Int = 200
      var callbackForStartRecord: () -> ClickState =
         {
            android.util.Log.e("InlineButtonSettings", "fn not init")
            ClickState.NotUsed
         }

      var isStartButton: Boolean = false
   }

   object MainActivitySettings {
      const val FILE_NAME_FORMAT: String = "yyyy-MM-dd-HH-mm-ss-SSS"
   }

   object CustomSubtitlesTimerSettings {
      const val SUBTITLES_FORMAT_PATTERN: String = "HH:mm:ss,SSS"
   }

   object RecorderControllerSettings {
      const val SERVICE_STARTING_TIMEOUT_MS = 50L
   }

   object PermissionsSettings {
      val RECORD_AUDIO_PERMISSIONS: Array<String> = arrayOf(
         Manifest.permission.RECORD_AUDIO
      )
   }

   object Model {
      lateinit var model: org.vosk.Model
      const val MILLIS_WAIT_INIT_MODEL: Long = 1000
   }

   object ButtonText {
      const val START_RECORD_TEXT_ID: Int = R.string.start_record_button_text
      const val STOP_RECORD_TEXT_ID: Int = R.string.stop_record_button_text
   }

   object ComposeSettings {
      val GRADIENT = Brush.horizontalGradient(listOf(Color(0xff2faf8f), Color(0xFF8cd97f),  Color(0xFFc1eb74)))

      var isClicked = mutableStateOf(ClickState.NotUsed)
   }
}
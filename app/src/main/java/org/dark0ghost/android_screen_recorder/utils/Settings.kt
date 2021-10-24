package org.dark0ghost.android_screen_recorder.utils

object Settings {
   object AudioRecordSettings {
      const val PERMISSIONS_REQUEST_RECORD_AUDIO = 1
   }

   object MediaRecordSettings{
      const val BIT_RATE = 5 * 1024 * 1024
      const val VIDEO_FRAME_RATE = 30
      const val NAME_DIR = "ScreenRecord"
      const val SERVICE_THREAD_NAME = "service_thread"
   }

}
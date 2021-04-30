package com.mindorks.framework.rtm8

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast


const val ACTION_PLAY = "PLAY"
const val ACTION_STOP = "STOP"
const val ACTION_CREATE = "CREATE"

class AudioPlayerService : Service(),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener {

    private var myMediaPlayer: MediaPlayer? = null
    fun init() {
        myMediaPlayer = MediaPlayer()
        myMediaPlayer?.setOnPreparedListener(this)
        myMediaPlayer?.setOnCompletionListener(this)
        myMediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            var actionIntent = intent.action
            when (actionIntent) {
                ACTION_CREATE -> init()
                ACTION_PLAY -> {
                    if (!myMediaPlayer!!.isPlaying) {

//                        val assetFileDescriptor = this.resources.openRawResourceFd(R.raw.love)
//                        myMediaPlayer?.run {
//                            reset()
//                            setDataSource(
//                                assetFileDescriptor.fileDescriptor,
//                                assetFileDescriptor.startOffset,
//                                assetFileDescriptor.declaredLength
//                            )
//                            prepareAsync()
//                        }

                        myMediaPlayer?.run {
                            reset()
                            setDataSource("http://128.199.174.165:8081/audio/Inuyasha-FutariNoKimochi-822964372.mp3")
                            prepareAsync()
                        }
                    }
                }
                ACTION_STOP -> myMediaPlayer?.stop()
            }
        }
        return flags
    }

    override fun onPrepared(mp: MediaPlayer?) {
        myMediaPlayer?.start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Toast.makeText(this, "Error Read File", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Toast.makeText(this, "Player Stop", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        myMediaPlayer?.release()
    }
}
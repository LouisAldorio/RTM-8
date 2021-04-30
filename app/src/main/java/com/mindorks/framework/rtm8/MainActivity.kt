package com.mindorks.framework.rtm8

import android.annotation.TargetApi
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper.prepare
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

var sp : SoundPool? = null
var soundID  : Int = 0

var myIntentService : Intent? = null

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //anda bisa cek datanya di Device Explorer >> data >> data >> {nama package anda di baris 1} >> shared_prefs
    private val PrefFileName = "MYFILEPREF01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_submit.setOnClickListener(this)
        btn_find.setOnClickListener(this)
        btn_reset.setOnClickListener(this)
        btn_play.setOnClickListener(this)

        val url = "http://128.199.174.165:8081/audio/Inuyasha-FutariNoKimochi-822964372.mp3" // your URL here
//        var url = "https://pl.meln.top/mr/34e004c10445a2885c94207768ab99a8.mp3?session_key=865b495cfefce32ca15c89faf730e7ef"
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }
    }

    override fun onClick(p0: View?) {
        var mySharedHelper = SharePrefHelper(this,PrefFileName)
        when(p0?.id){
            R.id.btn_submit -> {
                mySharedHelper.nama = edit_text_name.text.toString()
                mySharedHelper.email = edit_text_email.text.toString()
                Toast.makeText(this,"Simpan berhasil", Toast.LENGTH_SHORT).show()
                clearEditText()

                //play sound
                if(soundID!=0) {
                    sp?.play(soundID,.99f,.99f,1,0,.99f)
                }
            }
            R.id.btn_reset -> {
                mySharedHelper.clearValues()
                clearEditText()
            }
            R.id.btn_find -> {
                edit_text_name.setText(mySharedHelper.nama)
                edit_text_email.setText(mySharedHelper.email)
            }
            R.id.btn_play-> {
                if(btn_play.text.toString().toUpperCase().equals("PLAY")){
                    btn_play.text = "STOP"
                    myIntentService?.setAction(ACTION_PLAY)
                    startService(myIntentService)
                }
                else{
                    btn_play.text = "PLAY"
                    myIntentService?.setAction(ACTION_STOP)
                    startService(myIntentService)
                }
            }
        }
    }
    private fun clearEditText(){
        edit_text_name.text.clear()
        edit_text_email.text.clear()
    }

    override fun onStart() {
        super.onStart()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            createNewSoundPool()
        else
            createOldSoundPool()

        sp?.setOnLoadCompleteListener{soundPool, id, status ->
            if(status != 0)
                Toast.makeText(this,"Gagal Load",Toast.LENGTH_SHORT)
                        .show()
            else
                Toast.makeText(this,"Load Sukses",Toast.LENGTH_SHORT)
                        .show()
        }
        soundID = sp?.load(this, R.raw.hoodini,1) ?: 0

        //media player
        if(myIntentService==null){
            myIntentService = Intent(this,AudioPlayerService::class.java)
            myIntentService?.setAction(ACTION_CREATE)
            startService(myIntentService)
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNewSoundPool() {
        sp = SoundPool.Builder()
                .setMaxStreams(15)
                .build()
    }

    @Suppress("DEPRECATION")
    private fun createOldSoundPool() {
        sp = SoundPool(15, AudioManager.STREAM_MUSIC,0)
    }

    override fun onStop() {
        super.onStop()
        sp?.release()
        sp = null
    }
}
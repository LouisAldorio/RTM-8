package com.mindorks.framework.rtm8

import android.annotation.TargetApi
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

var sp : SoundPool? = null
var soundID  : Int = 0

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //anda bisa cek datanya di Device Explorer >> data >> data >> {nama package anda di baris 1} >> shared_prefs
    private val PrefFileName = "MYFILEPREF01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_submit.setOnClickListener(this)
        btn_find.setOnClickListener(this)
        btn_reset.setOnClickListener(this)
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
package com.mindorks.framework.rtm8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

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
}
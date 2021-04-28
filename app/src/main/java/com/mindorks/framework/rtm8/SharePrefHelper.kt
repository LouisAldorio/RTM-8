package com.mindorks.framework.rtm8

import android.content.Context
import android.content.SharedPreferences

class SharePrefHelper(context: Context,name: String){
    val USER_NAME = "NAMA"
    val USER_EMAIL = "EMAIL"

    private var myPreferences : SharedPreferences

    init {
        myPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE)
    }

    inline fun SharedPreferences.editMe(opertion:(SharedPreferences.Editor)->Unit){
        val editMe = edit()
        opertion(editMe)
        editMe.apply()
    }

    //inline function yang lebih menghemat penggunaan memort
    var nama : String?
        get() = myPreferences.getString(USER_NAME, "Kosong")
        set(value) {
            myPreferences.editMe {
                it.putString(USER_NAME, value)
            }
        }
    var email : String?
        get() = myPreferences.getString(USER_EMAIL, "Kosong")
        set(value) {
            myPreferences.editMe {
                it.putString(USER_EMAIL, value)
            }
        }
    fun clearValues() {
        myPreferences.editMe {
            it.clear()
        }
    }


    //cara biasa
//    var nama : String?
//        get() = myPreferences.getString(USER_NAME,"Kosong")
//        set(value) {
//            myPreferences.edit().putString(USER_NAME,value).apply()
//        }
//    var email : String?
//        get() = myPreferences.getString(USER_EMAIL,"Kosong")
//        set(value) {
//            myPreferences.edit().putString(USER_EMAIL, value).apply()
//        }
//
//    fun clearValues(){
//        myPreferences.edit().clear().apply()
//
//    }
}
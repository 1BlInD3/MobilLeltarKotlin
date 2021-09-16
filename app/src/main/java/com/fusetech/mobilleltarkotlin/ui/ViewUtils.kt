package com.fusetech.mobilleltarkotlin

import android.app.AlertDialog
import android.content.Context

fun showMe(message: String,context: Context){
    AlertDialog.Builder(context).setTitle("Figyelem").setMessage(message).create().show()
}
package com.fusetech.mobilleltarkotlin

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.fusetech.mobilleltarkotlin.ui.interfaces.UpdateInterface

fun showMe(message: String, context: Context) {
    val dialog = AlertDialog.Builder(context)
    dialog.setTitle("Figyelem")
    dialog.setMessage(message)
    dialog.setPositiveButton("OK") { _, _ -> }
    dialog.create()
    dialog.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus()
}
fun closeBin(listener: UpdateInterface, code: String,context: Context){
    val dialog =  AlertDialog.Builder(context,R.style.AlertDialogTheme)
    dialog.setTitle("Leltározás vége?")
    dialog.setMessage("Lezárod a $code polcot?")
    dialog.setPositiveButton("Igen"){_,_->
        listener.update(code)
    }
    dialog.setNegativeButton("Nem"){_,_->
        listener.clear()
    }
    dialog.setOnCancelListener {
        listener.onCancel()
    }
    dialog.create()
    dialog.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus()
    //dialog.getButton(DialogInterface.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN)
}
package com.fusetech.mobilleltarkotlin.ui.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.repositories.Sql
import com.fusetech.mobilleltarkotlin.ui.interfaces.CikkPolcListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CikkPolcViewModel
@Inject
constructor(
    private val sql: Sql
) : ViewModel() {
    var binOrItemText: String? = ""
    var cikkPolcListener: CikkPolcListener? = null
    fun onBarcodeRecieved(code: String) {
        binOrItemText = code
        cikkPolcListener?.setText(binOrItemText!!)
    }

    fun loadData(code: String) {
        cikkPolcListener?.onProgressOn()
        CoroutineScope(IO).launch {
            if (sql.isPolc(code)) {
                cikkPolcListener?.sendBundle(code)
            } else if (Sql().isCikk(code)) {
                cikkPolcListener?.sendCikk(code)
            } else {
                cikkPolcListener?.errorCode("Nem polc, nem cikk")
            }
            CoroutineScope(Main).launch {
                cikkPolcListener?.onProgressOff()
            }
        }
    }

    fun dataClick(view: View) {
        if (binOrItemText?.isNotEmpty()!!) {
            cikkPolcListener?.onProgressOn()
            CoroutineScope(IO).launch {
                if (sql.isPolc(binOrItemText!!)) {
                    cikkPolcListener?.sendBundle(binOrItemText!!)
                    cikkPolcListener?.setFocus()
                } else if (Sql().isCikk(binOrItemText!!)) {
                    cikkPolcListener?.sendCikk(binOrItemText!!)
                    cikkPolcListener?.setFocus()
                } else {
                    cikkPolcListener?.errorCode("Nem polc, nem cikk")
                    cikkPolcListener?.setFocus()
                }
                CoroutineScope(Main).launch {
                    cikkPolcListener?.onProgressOff()
                }
            }
        }
    }
}
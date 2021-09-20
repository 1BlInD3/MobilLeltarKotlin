package com.fusetech.mobilleltarkotlin.ui.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.bundle
import com.fusetech.mobilleltarkotlin.repositories.Sql
import com.fusetech.mobilleltarkotlin.ui.interfaces.LeltarListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeltarViewModel
@Inject
constructor(
    val sql: Sql
) : ViewModel() {
    var leltarListener: LeltarListener? = null
    var rakhely: String? = ""
    var cikkszam = ""
    var mennyiseg = ""
    var desc1 = ""
    var desc2 = ""
    var unit = ""
    var internalName = ""

    fun buttonClick(view: View){
        leltarListener?.clearAll()
    }
    fun cikkTextSet(code: String){
        bundle.clear()
        leltarListener?.setProgressOn()
        CoroutineScope(IO).launch {
            if(sql.isPolc(code)){
                if(sql.isPolcOpen(code)){
                    CoroutineScope(Main).launch {
                        leltarListener?.setRaktarText(code)
                        leltarListener?.setProgressOff()
                        leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                        leltarListener?.errorCode("A $code polc meg van kezdve, jelenleg ezt a polcot leltározod $rakhely")
                    }
                }else if(sql.isPolcEmpty(code)){
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("A $code polc üres")
                        leltarListener?.setProgressOff()
                    }
                }else if(sql.isPolcClosed(code)){
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("A $code polc lezárt státuszú")
                        leltarListener?.setProgressOff()
                    }
                }else if (sql.uploadPolc(code)){
                    CoroutineScope(Main).launch {
                        leltarListener?.setRaktarText(code)
                        leltarListener?.setProgressOff()
                        leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                        leltarListener?.errorCode("A $code polcot felvettem")
                    }
                }
            }else if(sql.isCikk(code)){
                CoroutineScope(Main).launch {
                    leltarListener?.setCikkText(code)
                    leltarListener?.setProgressOff()
                    leltarListener?.cikkAdatok(bundle.getString("MEG1"),bundle.getString("MEG2"),bundle.getString("UNIT")!!)
                }
            }
            else{
                CoroutineScope(Main).launch {
                    leltarListener?.errorCode("Egyik sem az")
                    leltarListener?.setProgressOff()
                }
            }
        }
    }
}
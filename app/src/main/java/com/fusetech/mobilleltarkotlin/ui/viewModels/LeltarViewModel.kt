package com.fusetech.mobilleltarkotlin.ui.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.activity.MainActivity
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.bundle
import com.fusetech.mobilleltarkotlin.repositories.Sql
import com.fusetech.mobilleltarkotlin.ui.interfaces.LeltarListener
import com.fusetech.mobilleltarkotlin.ui.interfaces.UpdateInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LeltarViewModel"
@HiltViewModel
class LeltarViewModel
@Inject
constructor(
    val sql: Sql
) : ViewModel(), UpdateInterface {
    var leltarListener: LeltarListener? = null
    var rakhely: String = ""
    var cikkszam = ""
    var mennyiseg: String? = ""
    var desc1 = ""
    var desc2 = ""
    var unit = ""
    var internalName = ""
    var megjegyzes: String? = ""
    private var warehouseID = ""
    private var updateListener: UpdateInterface = this
    var isUpdate = false
    var bizszam = 0

    fun raktarClick(view: View) {
        //leltarListener?.deleteRakhely()
        cikkTextSet(rakhely)
    }

    fun buttonClick(view: View) {
        leltarListener?.isClicked()
        if (rakhely.isNotEmpty()) {
            leltarListener?.showData(updateListener, rakhely)
        }
    }

    fun mennyisegClick(view: View) {
        if (mennyiseg?.isNotEmpty()!!) {
            leltarListener?.mennyisegListener(mennyiseg?.toBigDecimal()!!)
        }
    }

    fun insertLeltarData(view: View) {
        if (rakhely.isNotEmpty() && cikkszam.isNotEmpty() && mennyiseg?.isNotEmpty()!! && !isUpdate) {
            CoroutineScope(IO).launch {
                CoroutineScope(Main).launch {
                    leltarListener?.setProgressOn()
                }
                if (sql.insertData(
                        cikkszam, mennyiseg?.toDouble()!!,
                        MainActivity.dolgKod, warehouseID, rakhely, megjegyzes?.trim()
                    )
                ) {
                    CoroutineScope(Main).launch {
                        //leltarListener?.errorCode("Sikeres felt??lt??s")
                        leltarListener?.afterUpload()
                        leltarListener?.setNewBinOn()
                        leltarListener?.setProgressOff()
                    }
                } else {
                    CoroutineScope(Main).launch {
                        leltarListener?.setProgressOff()
                        leltarListener?.errorCode("Nem siker??lt az elemet felvenni")
                    }
                }
            }
        } else if (rakhely.isNotEmpty() && cikkszam.isNotEmpty() && mennyiseg?.isNotEmpty()!! && isUpdate) {
            leltarListener?.setProgressOn()
            CoroutineScope(IO).launch {
                if (sql.updateData(bizszam, mennyiseg?.toDouble()!!, megjegyzes)) {
                    CoroutineScope(Main).launch {
                        //leltarListener?.errorCode("Sikeres friss??t??s")
                        leltarListener?.afterUpload()
                        leltarListener?.setProgressOff()
                        leltarListener?.setNewBinOn()
                    }
                } else {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("Nem siker??lt az elemet friss??teni")
                        leltarListener?.setProgressOff()
                    }
                }
            }
            isUpdate = false
        } else {
            when {
                rakhely.isEmpty() -> {
                    leltarListener?.errorCode("A rakhely nem lehet ??res")
                }
                cikkszam.isEmpty() -> {
                    leltarListener?.errorCode("A cikksz??m nem lehet ??res")
                }
                else -> {
                    leltarListener?.errorCode("A mennyis??g nem lehet ??res")
                }
            }
        }
    }

    fun cikkClikk(view: View) {
        if (cikkszam.isNotEmpty()) {
            leltarListener?.setProgressOn()
            CoroutineScope(IO).launch {
                if (sql.isCikk(cikkszam)) {
                    CoroutineScope(Main).launch {
                        leltarListener?.setCikkText(cikkszam)
                        leltarListener?.setProgressOff()
                        leltarListener?.cikkAdatok(
                            bundle.getString("MEG1"),
                            bundle.getString("MEG2"),
                            bundle.getString("UNIT")!!
                        )
                        leltarListener?.setNewBinOff()
                    }
                } else {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("Nem cikk")
                        leltarListener?.cikkSelect()
                        leltarListener?.setProgressOff()
                    }
                }
            }
        }
    }

    fun cikkTextSet(code: String) {
        bundle.clear()
        leltarListener?.setProgressOn()
        Log.d(TAG, "cikkTextSet: $rakhely")
        CoroutineScope(IO).launch {
            if (sql.isPolc(code)){
                if(rakhely.isEmpty()){
                   // leltarListener?.deleteRakhely()
                    warehouseID = bundle.getString("RAKKOD")!!
                    if (sql.isPolcOpen(code)) {
                        if (sql.hasPolcItems(code)) {
                            CoroutineScope(Main).launch {
                                leltarListener?.setRaktarText(code)
                                leltarListener?.setProgressOff()
                                leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                                leltarListener?.errorCode("A $code polc meg van kezdve ??s cikkek vannak rajta")
                                leltarListener?.tabSwitch()
                            }
                        } else {
                            CoroutineScope(Main).launch {
                                leltarListener?.setRaktarText(code)
                                leltarListener?.setProgressOff()
                                leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                                leltarListener?.errorCode("A $code polc meg van kezdve, jelenleg ezt a polcot lelt??rozod $rakhely")
                            }
                        }
                    } else if (sql.isPolcEmpty(code)) {
                        CoroutineScope(Main).launch {
                            leltarListener?.errorCode("A $code polc ??res")
                            leltarListener?.setProgressOff()
                        }
                    } else if (sql.isPolcClosed(code)) {
                        CoroutineScope(Main).launch {
                            leltarListener?.errorCode("A $code polc lez??rt st??tusz??")
                            leltarListener?.setProgressOff()
                        }
                    } else if (sql.uploadPolc(code)) {
                        CoroutineScope(Main).launch {
                            leltarListener?.setRaktarText(code)
                            leltarListener?.setProgressOff()
                            leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                            //leltarListener?.errorCode("A $code polcot felvettem")
                        }
                    } else {
                        leltarListener?.errorCode("Nem siker??lt a polcot leellen??rizni!")
                    }
                }else{
                    CoroutineScope(Main).launch {
                        leltarListener?.setProgressOff()
                        leltarListener?.errorCode("Cikket vigy??l fel!")
                    }
                }
            } else if (sql.isCikk(code)) {
                CoroutineScope(Main).launch {
                    if(rakhely.isNotEmpty()){
                        if(cikkszam.isEmpty()){
                            leltarListener?.setCikkText(code)
                            leltarListener?.setProgressOff()
                            leltarListener?.cikkAdatok(
                                bundle.getString("MEG1"),
                                bundle.getString("MEG2"),
                                bundle.getString("UNIT")!!
                            )
                            leltarListener?.setNewBinOff()
                        }else{
                            leltarListener?.errorCode("M??r vett??l fel cikket!")
                            leltarListener?.setProgressOff()
                        }
                    }else{
                        leltarListener?.errorCode("Polcot vigy??l el??bb fel!")
                        leltarListener?.setProgressOff()
                    }
                }
            } else if(rakhely.isNotEmpty() && code == "EMPTY"){
                if(sql.hasPolcItemsInAdat(rakhely)){
                    CoroutineScope(Main).launch {
                        Log.d(TAG, "cikkTextSet: Nem j??")
                        leltarListener?.errorCode("Nem lehet ??res, vannak rajta t??telek")
                        leltarListener?.setProgressOff()
                    }
                }else{
                    sql.closePolcLeltar(rakhely,0)
                    CoroutineScope(Main).launch {
                        Log.d(TAG, "cikkTextSet: J??")
                        leltarListener?.clearAll()
                        leltarListener?.errorCode("A polc st??tusza ??resre v??ltozott")
                        leltarListener?.setProgressOff()
                    }
                }
            }else {
                CoroutineScope(Main).launch {
                    leltarListener?.errorCode("Hiba t??rt??nt ezzel a vonalk??ddal $code")
                    leltarListener?.setProgressOff()
                }
            }
        }
    }

    override fun update(code: String) {
        leltarListener?.setProgressOn()
        CoroutineScope(IO).launch {
            sql.closePolcLeltar(code,2)
            CoroutineScope(Main).launch {
                leltarListener?.clearAll()
               // leltarListener?.errorCode("A $code polcot lez??rtam")
                leltarListener?.setProgressOff()
            }
        }
    }

    override fun clear() {
        leltarListener?.clearAll()
        leltarListener?.setProgressOff()
    }

    override fun onCancel() {
        leltarListener?.setProgressOff()
    }
}
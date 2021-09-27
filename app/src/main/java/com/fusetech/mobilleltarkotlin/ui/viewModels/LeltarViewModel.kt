package com.fusetech.mobilleltarkotlin.ui.viewModels

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
    var warehouseID = ""
    var updateListener: UpdateInterface = this
    var isUpdate = false
    var bizszam = 0

    fun buttonClick(view: View) {
        leltarListener?.setProgressOn()
        if (rakhely.isNotEmpty()) {
            leltarListener?.showData(updateListener, rakhely)
        }
        leltarListener?.setProgressOff()
    }

    fun mennyisegClick(view: View) {
        if (mennyiseg?.isNotEmpty()!!) {
            leltarListener?.mennyisegListener(mennyiseg?.toDouble()!!)
        }
    }

    fun insertLeltarData(view: View) {
        if (rakhely.isNotEmpty() && cikkszam.isNotEmpty() && mennyiseg?.isNotEmpty()!! && !isUpdate) {
            leltarListener?.setProgressOn()
            CoroutineScope(IO).launch {
                if (sql.insertData(
                        cikkszam, mennyiseg?.toDouble()!!,
                        MainActivity.dolgKod, warehouseID, rakhely, megjegyzes
                    )
                ) {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("Sikeres feltöltés")
                        leltarListener?.afterUpload()
                        leltarListener?.setProgressOff()
                    }
                } else {
                    CoroutineScope(Main).launch {
                        leltarListener?.setProgressOff()
                        leltarListener?.errorCode("Nem sikerült az elemet felvenni")
                    }
                }
            }
        } else if (rakhely.isNotEmpty() && cikkszam.isNotEmpty() && mennyiseg?.isNotEmpty()!! && isUpdate) {
            leltarListener?.setProgressOn()
            CoroutineScope(IO).launch {
                if (sql.updateData(bizszam, mennyiseg?.toDouble()!!, megjegyzes)) {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("Sikeres frissítés")
                        leltarListener?.afterUpload()
                        leltarListener?.setProgressOff()
                    }
                } else {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("Nem sikerült az elemet frissíteni")
                        leltarListener?.setProgressOff()
                    }
                }
            }
            isUpdate = false
        } else {
            if (rakhely.isEmpty()) {
                leltarListener?.errorCode("A rakhely nem lehet üres")
            } else if (cikkszam.isEmpty()) {
                leltarListener?.errorCode("A cikkszám nem lehet üres")
            } else {
                leltarListener?.errorCode("A mennyiség nem lehet üres")
            }
        }
    }

    fun cikkTextSet(code: String) {
        bundle.clear()
        leltarListener?.setProgressOn()
        CoroutineScope(IO).launch {
            if (sql.isPolc(code)) {
                leltarListener?.deleteRakhely()
                warehouseID = bundle.getString("RAKKOD")!!
                if (sql.isPolcOpen(code)) {
                    if (sql.hasPolcItems(code)) {
                        CoroutineScope(Main).launch {
                            leltarListener?.setRaktarText(code)
                            leltarListener?.setProgressOff()
                            leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                            leltarListener?.errorCode("A $code polc meg van kezdve és cikkek vannak rajta")
                            leltarListener?.tabSwitch()
                        }
                    } else {
                        leltarListener?.setRaktarText(code)
                        leltarListener?.setProgressOff()
                        leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                        leltarListener?.errorCode("A $code polc meg van kezdve, jelenleg ezt a polcot leltározod $rakhely")
                    }
                } else if (sql.isPolcEmpty(code)) {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("A $code polc üres")
                        leltarListener?.setProgressOff()
                    }
                } else if (sql.isPolcClosed(code)) {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("A $code polc lezárt státuszú")
                        leltarListener?.setProgressOff()
                    }
                } else if (sql.uploadPolc(code)) {
                    CoroutineScope(Main).launch {
                        leltarListener?.setRaktarText(code)
                        leltarListener?.setProgressOff()
                        leltarListener?.raktarAdat(bundle.getString("RAK")!!)
                        leltarListener?.errorCode("A $code polcot felvettem")
                    }
                } else {
                    leltarListener?.errorCode("Nem sikerült a polcot leellenőrizni!")
                }
            } else if (sql.isCikk(code)) {
                CoroutineScope(Main).launch {
                    leltarListener?.setCikkText(code)
                    leltarListener?.setProgressOff()
                    leltarListener?.cikkAdatok(
                        bundle.getString("MEG1"),
                        bundle.getString("MEG2"),
                        bundle.getString("UNIT")!!
                    )
                }
            } else {
                CoroutineScope(Main).launch {
                    leltarListener?.errorCode("Egyik sem az")
                    leltarListener?.setProgressOff()
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
                    }
                } else {
                    CoroutineScope(Main).launch {
                        leltarListener?.errorCode("Nem cikk")
                        leltarListener?.setProgressOff()
                    }
                }
            }
        }
    }

    override fun update(code: String) {
        CoroutineScope(IO).launch {
            sql.closePolcLeltar(code)
            CoroutineScope(Main).launch {
                leltarListener?.clearAll()
                leltarListener?.errorCode("A $code polcot lezártam")
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
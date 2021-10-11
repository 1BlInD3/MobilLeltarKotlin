package com.fusetech.mobilleltarkotlin.ui.interfaces

import java.math.BigDecimal

interface LeltarListener {
    fun setRaktarText(code: String)
    fun setCikkText(code: String)
    fun setProgressOn()
    fun setProgressOff()
    fun cikkAdatok(des1: String?, desc2: String?, unit: String)
    fun raktarAdat(terulet: String)
    fun errorCode(code: String)
    fun clearAll()
    fun mennyisegListener(quantity: BigDecimal)
    fun afterUpload()
    fun showData(listener: UpdateInterface, code: String)
    fun tabSwitch()
    fun deleteRakhely()
    fun cikkSelect()
    fun setNewBinOn()
    fun setNewBinOff()
    fun isClicked()
}
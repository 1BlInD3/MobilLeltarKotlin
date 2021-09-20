package com.fusetech.mobilleltarkotlin.ui.interfaces

interface LeltarListener {
    fun setRaktarText(code: String)
    fun setCikkText(code: String)
    fun setProgressOn()
    fun setProgressOff()
    fun cikkAdatok(des1: String?, desc2: String?, unit: String)
    fun raktarAdat(terulet: String)
    fun errorCode(code: String)
    fun clearAll()
}
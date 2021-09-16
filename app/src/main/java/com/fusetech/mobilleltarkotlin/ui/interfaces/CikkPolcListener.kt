package com.fusetech.mobilleltarkotlin.ui.interfaces

interface CikkPolcListener {
    fun setText(code: String)
    fun onProgressOn()
    fun onProgressOff()
    fun sendBundle(code: String)
    fun sendCikk(code: String)
    fun errorCode(code: String)
}
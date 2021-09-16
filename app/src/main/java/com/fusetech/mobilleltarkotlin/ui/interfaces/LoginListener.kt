package com.fusetech.mobilleltarkotlin.ui.interfaces

interface LoginListener {
    fun onCancelClicked()
    fun onRequestFailed(code : String)
    fun onRequestSuccess()
    fun onProgressOn()
    fun onProgressOff()
}
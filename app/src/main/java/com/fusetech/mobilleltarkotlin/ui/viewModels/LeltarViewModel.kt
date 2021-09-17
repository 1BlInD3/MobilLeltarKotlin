package com.fusetech.mobilleltarkotlin.ui.viewModels

import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.repositories.Sql
import com.fusetech.mobilleltarkotlin.ui.interfaces.LeltarListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeltarViewModel
@Inject
constructor(
    val sql: Sql
) : ViewModel() {
    var leltarListener: LeltarListener? = null
    var rakhely: String = "BRAZING"
    var cikkszam = ""
    fun cikkTextSet(code: String){
        leltarListener?.setCikkText(code)
    }
}
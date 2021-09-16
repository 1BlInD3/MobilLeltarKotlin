package com.fusetech.mobilleltarkotlin.ui.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.ui.interfaces.MenuLstener

class MenuViewModel: ViewModel() {
    var menuListener: MenuLstener? = null
    fun onLeltarozasClick(view: View){
        menuListener?.loadLeltar()
    }
    fun onLekerdezesClick(view: View){
        menuListener?.loadLekerdezes()
    }
    fun onKilepClick(view: View){
        menuListener?.loadKilep()
    }
}
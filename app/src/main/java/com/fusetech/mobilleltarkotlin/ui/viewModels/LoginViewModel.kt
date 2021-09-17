package com.fusetech.mobilleltarkotlin.ui.viewModels

import android.view.View
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.repositories.Sql
import com.fusetech.mobilleltarkotlin.ui.interfaces.LoginListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel
@Inject
    constructor(
        private val sql: Sql
    ): ViewModel() {
    var barCode: String? = "Kérem a vonalkódot : "
    var loginListener: LoginListener? = null

    fun onLoginCancelClick(view: View){
        loginListener?.onCancelClicked()
    }
    fun onLoginCode(code: String){
        loginListener?.onProgressOn()
        CoroutineScope(IO).launch {
            val right = sql.userLogin(code)
            if(right){
                CoroutineScope(Main).launch {
                    loginListener?.onRequestSuccess()
                    loginListener?.onProgressOff()
                }
            }else{
                CoroutineScope(Main).launch {
                    loginListener?.onRequestFailed(code)
                    loginListener?.onProgressOff()
                }
            }
        }
    }
}
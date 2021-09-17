package com.fusetech.mobilleltarkotlin.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.activity.MainActivity
import com.fusetech.mobilleltarkotlin.dataItems.CikkItems
import com.fusetech.mobilleltarkotlin.repositories.Sql
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CikkResultViewModel
@Inject
    constructor(
        private val sql: Sql
    ): ViewModel() {
    private lateinit var cikkItems: MutableLiveData<ArrayList<CikkItems>>
    fun getItems():LiveData<ArrayList<CikkItems>>{
        return cikkItems
    }
    fun initItems(){
        cikkItems = sql.cikkResultQuery(MainActivity.cikkCode)
    }
}
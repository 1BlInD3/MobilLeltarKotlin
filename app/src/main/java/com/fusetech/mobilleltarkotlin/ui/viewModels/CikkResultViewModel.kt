package com.fusetech.mobilleltarkotlin.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.activity.MainActivity
import com.fusetech.mobilleltarkotlin.dataItems.CikkItems
import com.fusetech.mobilleltarkotlin.repositories.Sql

class CikkResultViewModel: ViewModel() {
    private lateinit var cikkItems: MutableLiveData<ArrayList<CikkItems>>
    fun getItems():LiveData<ArrayList<CikkItems>>{
        return cikkItems
    }
    fun initItems(){
        cikkItems = Sql().cikkResultQuery(MainActivity.cikkCode)
    }
}
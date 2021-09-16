package com.fusetech.mobilleltarkotlin.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.activity.MainActivity
import com.fusetech.mobilleltarkotlin.dataItems.PolcItems
import com.fusetech.mobilleltarkotlin.repositories.Sql

class PolcResultViewModel: ViewModel() {
    private lateinit var polcItems : MutableLiveData<ArrayList<PolcItems>> //= getPolcitems()
    fun getItems() : LiveData<ArrayList<PolcItems>>{
        return polcItems
    }
    fun getListItmes(){
        polcItems = Sql().polcResultQuery(MainActivity.containerCode)
    }
}
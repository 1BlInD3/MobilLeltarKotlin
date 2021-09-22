package com.fusetech.mobilleltarkotlin.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fusetech.mobilleltarkotlin.activity.MainActivity
import com.fusetech.mobilleltarkotlin.dataItems.RaktarAdat
import com.fusetech.mobilleltarkotlin.repositories.Sql
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TetelViewModel
@Inject
constructor(val sql: Sql): ViewModel() {
    private lateinit var adatok: MutableLiveData<ArrayList<RaktarAdat>>
    fun getItems(): LiveData<ArrayList<RaktarAdat>>{
        return adatok
    }
    fun onSeleced(){

    }
    fun onListLoad(){
           adatok =  sql.loadBinItems(MainActivity.rakthely)
    }
}
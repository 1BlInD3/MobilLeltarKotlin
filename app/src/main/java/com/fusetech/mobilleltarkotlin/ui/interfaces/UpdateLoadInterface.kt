package com.fusetech.mobilleltarkotlin.ui.interfaces

interface UpdateLoadInterface {
    fun setData(cikkszam: String,
                meg1: String,
                meg2: String?,
                qty: Double,
                megjegyzes: String?,
                bizszam: Int)
}
package com.fusetech.mobilleltarkotlin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fusetech.mobilleltarkotlin.fragments.LeltarFragment
import com.fusetech.mobilleltarkotlin.fragments.TetelFragment


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                LeltarFragment()
            }
            1 -> {
                TetelFragment()
            }
            else -> createFragment(position)
        }
    }
    override fun getItemCount(): Int = 2

    fun addNames(): ArrayList<String>{
        val list :ArrayList<String> = ArrayList()
        list.add("Leltár")
        list.add("Tételek")
       return list
    }
}


package com.fusetech.mobilleltarkotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.adapters.ViewPagerAdapter
import com.fusetech.mobilleltarkotlin.databinding.FragmentTabbedBinding
import com.fusetech.mobilleltarkotlin.ui.viewModels.TabbedViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG = "TabbedFragment"
class TabbedFragment : Fragment() {
    private val viewModel: TabbedViewModel by viewModels()
    private lateinit var  binding: FragmentTabbedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tabbed, container, false)
        binding.viewModel = viewModel
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        binding.mViewPager.adapter = viewPagerAdapter
        binding.tabbedLayout.tabGravity = TabLayout.GRAVITY_FILL
        val list : ArrayList<String> = ArrayList()
        list.add("Leltár")
        list.add("Tételek")
        TabLayoutMediator(binding.tabbedLayout, binding.mViewPager) { tab, position ->
            tab.text = list[position]
        }.attach()

        binding.tabbedLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabSelected: ${tab?.position!!}")
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabUnselected: ")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabReselected: ")
            }

        })

        return binding.root
    }
}
package com.fusetech.mobilleltarkotlin.fragments

import android.content.Context
import android.content.res.Resources
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
    private lateinit var with: With
    interface With{
        fun getString()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tabbed, container, false)
        binding.viewModel = viewModel
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        binding.mViewPager.adapter = viewPagerAdapter
        binding.tabbedLayout.tabGravity = TabLayout.GRAVITY_FILL
       // binding.tabbedLayout.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
       // binding.tabbedLayout.clearFocus()
        //binding.mViewPager.descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
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
                with.getString()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabUnselected: ${tab?.position!!}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabReselected: ${tab?.position!!}")
            }
        })


        Thread(Runnable {
            var oldId = -1
            while (true) {
                val newView: View? = getView()?.findFocus()
                if (newView != null && newView.id != oldId) {
                    oldId = newView.id
                    var idName: String = try {
                        resources.getResourceEntryName(newView.id)
                    } catch (e: Resources.NotFoundException) {
                        newView.id.toString()
                    }
                    Log.i(TAG, "Focused Id: \t" + idName + "\tClass: \t" + newView.javaClass)
                }
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }).start()



        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        with = if(context is With){
            context
        }else{
            throw Exception("must implement")
        }
    }
    fun changeTab(){
        binding.mViewPager.currentItem = 0
    }
    fun changeTetelTab(){
        binding.mViewPager.currentItem = 1
    }

    override fun onResume() {
        super.onResume()
        binding.mViewPager.clearFocus()
        binding.tabbedLayout.clearFocus()
        //binding.a.clearFocus()
        //binding.b.clearFocus()
    }
}
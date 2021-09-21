package com.fusetech.mobilleltarkotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.rakhelyInfo
import com.fusetech.mobilleltarkotlin.adapters.RaktarAdatAdapter
import com.fusetech.mobilleltarkotlin.dataItems.RaktarAdat
import com.fusetech.mobilleltarkotlin.databinding.FragmentTetelBinding
import com.fusetech.mobilleltarkotlin.showMe
import com.fusetech.mobilleltarkotlin.ui.viewModels.TetelViewModel

class TetelFragment : Fragment(),RaktarAdatAdapter.CurrentSelection {
    private val viewModel : TetelViewModel by viewModels()
    private lateinit var binding: FragmentTetelBinding
    private val adat: ArrayList<RaktarAdat> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tetel, container, false)
        binding.viewModel = viewModel
        initRecycler()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        binding.itemRecycler.adapter = RaktarAdatAdapter(rakhelyInfo,this)
        binding.itemRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.itemRecycler.setHasFixedSize(true)
        binding.itemRecycler.adapter?.notifyDataSetChanged()
    }

    override fun onCurrentClick(position: Int) {
        showMe("${position+1}",requireContext())
    }
}
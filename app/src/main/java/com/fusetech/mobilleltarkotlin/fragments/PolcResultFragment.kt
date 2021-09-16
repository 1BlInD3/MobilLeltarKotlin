package com.fusetech.mobilleltarkotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.adapters.PolcItemAdapter
import com.fusetech.mobilleltarkotlin.dataItems.PolcItems
import com.fusetech.mobilleltarkotlin.databinding.FragmentPolcResultBinding
import com.fusetech.mobilleltarkotlin.ui.viewModels.PolcResultViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class PolcResultFragment : Fragment(){
    private lateinit var binding: FragmentPolcResultBinding
    private lateinit var viewModel: PolcResultViewModel
    private var myItems: ArrayList<PolcItems> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[PolcResultViewModel::class.java]
        } ?: throw Exception("Invalid activity")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_polc_result, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    private fun initRecycler(){
        binding.recycler.adapter = PolcItemAdapter(viewModel.getItems().value!!)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.setHasFixedSize(true)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        CoroutineScope(IO).launch {
            viewModel.getListItmes()
            CoroutineScope(Main).launch {
                initRecycler()
                viewModel.getItems().observe(viewLifecycleOwner, {
                    binding.recycler.adapter?.notifyDataSetChanged()
                })
            }
        }
    }

    override fun onStop() {
        super.onStop()
        myItems.clear()
    }

    override fun onPause() {
        super.onPause()
        Log.d("MYTAG", "onPause: ")
        myItems.clear()
    }
}
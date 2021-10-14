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
import com.fusetech.mobilleltarkotlin.adapters.PolcItemAdapter
import com.fusetech.mobilleltarkotlin.databinding.FragmentPolcResultBinding
import com.fusetech.mobilleltarkotlin.showMe
import com.fusetech.mobilleltarkotlin.ui.viewModels.PolcResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PolcResultFragment : Fragment(){
    private val viewModel: PolcResultViewModel by viewModels()
    private lateinit var binding: FragmentPolcResultBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_polc_result, container, false)
        binding.viewModel = viewModel
        binding.polcProgress.visibility = View.GONE
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
        binding.polcProgress.visibility = View.VISIBLE
        CoroutineScope(IO).launch {
            if(viewModel.hasData()){
                viewModel.getListItmes()
                CoroutineScope(Main).launch {
                    binding.polcProgress.visibility = View.GONE
                    initRecycler()
                    viewModel.getItems().observe(viewLifecycleOwner, {
                        binding.recycler.adapter?.notifyDataSetChanged()
                    })
                }
            }else{
                CoroutineScope(Main).launch {
                    showMe("A polc üres",requireContext())
                    binding.polcProgress.visibility = View.GONE
                    binding.constraintLayout.visibility = View.GONE
                }
            }
        }
    }
}
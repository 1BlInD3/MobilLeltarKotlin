package com.fusetech.mobilleltarkotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.bundle
import com.fusetech.mobilleltarkotlin.adapters.CikkItemAdapter
import com.fusetech.mobilleltarkotlin.databinding.FragmentCikkResultBinding
import com.fusetech.mobilleltarkotlin.ui.viewModels.CikkResultViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class CikkResultFragment : Fragment() {
    private lateinit var viewModel: CikkResultViewModel
    private lateinit var binding: FragmentCikkResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[CikkResultViewModel::class.java]
        }?:throw Exception("Invalid activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cikk_result,container,false)
        binding.viewmodel = viewModel
        return binding.root
    }
    private fun initRecycler(){
        binding.cikkRecycler.adapter = CikkItemAdapter(viewModel.getItems().value!!)
        binding.cikkRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.cikkRecycler.setHasFixedSize(true)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.megjegyzes1.text = arguments?.getString("MEG1")
        binding.megjegyzes2.text = arguments?.getString("MEG2")
        binding.intrem.text = arguments?.getString("INTREM")
        binding.until.text = arguments?.getString("UNIT")
        bundle.clear()
        CoroutineScope(IO).launch {
            viewModel.initItems()
            CoroutineScope(Main).launch {
                initRecycler()
                viewModel.getItems().observe(viewLifecycleOwner,{
                    binding.cikkRecycler.adapter?.notifyDataSetChanged()
                })
            }
        }
    }
}
package com.fusetech.mobilleltarkotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.rakthely
import com.fusetech.mobilleltarkotlin.adapters.RaktarAdatAdapter
import com.fusetech.mobilleltarkotlin.dataItems.RaktarAdat
import com.fusetech.mobilleltarkotlin.databinding.FragmentTetelBinding
import com.fusetech.mobilleltarkotlin.showMe
import com.fusetech.mobilleltarkotlin.ui.interfaces.TetelListener
import com.fusetech.mobilleltarkotlin.ui.viewModels.TetelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TetelFragment : Fragment(),RaktarAdatAdapter.CurrentSelection,TetelListener {
    private val TAG = "TetelFragment"
    private val viewModel : TetelViewModel by viewModels()
    private lateinit var binding: FragmentTetelBinding
    private val adat: ArrayList<RaktarAdat> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_tetel, container, false)
        binding.viewModel = viewModel
        binding.tetelProgress.visibility = View.GONE
        viewModel.tetelListener = this
        return binding.root
    }

    private fun initRecycler() {
        binding.itemRecycler.adapter = RaktarAdatAdapter(viewModel.getItems().value!!,this)
        binding.itemRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.itemRecycler.setHasFixedSize(true)
    }

    override fun onCurrentClick(position: Int) {
        showMe("${position+1}",requireContext())
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        CoroutineScope(IO).launch {
            Log.d(TAG, "1")
            Log.d(TAG, "onResume: $rakthely")
            if(rakthely.isNotEmpty()){
                viewModel.onListLoad()
                Log.d(TAG, "2")
                CoroutineScope(Main).launch {
                    initRecycler()
                    Log.d(TAG, "3")
                    viewModel.getItems().observe(viewLifecycleOwner,{
                        binding.itemRecycler.adapter?.notifyDataSetChanged()
                        Log.d(TAG, "4")
                    })
                }
            }
        }
    }

    override fun setProgressOn() {
       binding.tetelProgress.visibility = View.VISIBLE
    }

    override fun setProgressOff() {
        binding.tetelProgress.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshRecycler() {
        initRecycler()
    }

}
package com.fusetech.mobilleltarkotlin.fragments

import android.annotation.SuppressLint
import android.content.Context
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
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.rakhelyInfo
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.rakthely
import com.fusetech.mobilleltarkotlin.adapters.RaktarAdatAdapter
import com.fusetech.mobilleltarkotlin.databinding.FragmentTetelBinding
import com.fusetech.mobilleltarkotlin.ui.viewModels.TetelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

private const val TAG = "TetelFragment"

@AndroidEntryPoint
class TetelFragment : Fragment(), RaktarAdatAdapter.CurrentSelection {
    private val viewModel: TetelViewModel by viewModels()
    private lateinit var binding: FragmentTetelBinding
    private lateinit var changeTab: ChangeTab

    interface ChangeTab {
        fun changeTab()
        fun setData(cikkszam: String,
                    meg1: String,
                    meg2: String?,
                    qty: Double,
                    megjegyzes: String?,
                    bizszam: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tetel, container, false)
        binding.viewModel = viewModel
        binding.tetelProgress.visibility = View.GONE
        return binding.root
    }

    private fun initRecycler() {
        binding.itemRecycler.adapter = RaktarAdatAdapter(viewModel.getItems().value!!, this)
        binding.itemRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.itemRecycler.setHasFixedSize(true)
    }

    override fun onCurrentClick(position: Int) {
        changeTab.changeTab()
        changeTab.setData(viewModel.getItems().value!![position].cikkszam,
            viewModel.getItems().value!![position].megnevezes1,
            viewModel.getItems().value!![position].megnevezes2,
            viewModel.getItems().value!![position].mennyiseg,
            viewModel.getItems().value!![position].megjegyzes,
            viewModel.getItems().value!![position].bizszam)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        binding.tetelProgress.visibility = View.VISIBLE
        CoroutineScope(IO).launch {
            Log.d(TAG, "1")
            Log.d(TAG, "onResume: $rakthely")
            if (rakthely.isNotEmpty()) {
                viewModel.onListLoad()
                Log.d(TAG, "2")
                CoroutineScope(Main).launch {
                    initRecycler()
                    binding.tetelProgress.visibility = View.GONE
                    Log.d(TAG, "3")
                    viewModel.getItems().observe(viewLifecycleOwner, {
                        binding.itemRecycler.adapter?.notifyDataSetChanged()
                        Log.d(TAG, "4")
                    })
                }
            } else {
                CoroutineScope(Main).launch {
                    binding.tetelProgress.visibility = View.VISIBLE
                    rakhelyInfo.clear()
                    binding.itemRecycler.adapter?.notifyDataSetChanged()
                    binding.tetelProgress.visibility = View.GONE
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        changeTab = if (context is ChangeTab) {
            context
        } else {
            throw Exception("Must implement")
        }
    }
}
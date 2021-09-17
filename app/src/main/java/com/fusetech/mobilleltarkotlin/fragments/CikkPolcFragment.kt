package com.fusetech.mobilleltarkotlin.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.databinding.FragmentCikkPolcBinding
import com.fusetech.mobilleltarkotlin.showMe
import com.fusetech.mobilleltarkotlin.ui.interfaces.CikkPolcListener
import com.fusetech.mobilleltarkotlin.ui.viewModels.CikkPolcViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CikkPolcFragment : Fragment(),CikkPolcListener {

    private val viewModel: CikkPolcViewModel by viewModels()
    private lateinit var binding: FragmentCikkPolcBinding
    private lateinit var withMainActivity: WithMainActivity
    interface WithMainActivity{
        fun sendBundle(polcList: String)
        fun sendCikk(cikk: String)
        fun removeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cikk_polc, container, false)
        binding.viewModel = viewModel
        viewModel.cikkPolcListener = this
        binding.binOrItem.requestFocus()
        binding.progressBar.visibility = View.GONE
        return binding.root
    }

    fun setCode(code: String){
        viewModel.onBarcodeRecieved(code)
        viewModel.loadData(code)
    }

    override fun setText(code: String) {
        binding.binOrItem.setText(viewModel.binOrItemText)
        binding.binOrItem.selectAll()
    }

    override fun onProgressOn() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onProgressOff() {
        binding.progressBar.visibility = View.GONE
    }

    override fun sendBundle(code: String) {
        CoroutineScope(Main).launch {
            withMainActivity.sendBundle(code)
        }
    }

    override fun sendCikk(code: String) {
        CoroutineScope(Main).launch {
            withMainActivity.sendCikk(code)
        }
    }
    override fun errorCode(code: String) {
        CoroutineScope(Main).launch {
            showMe(code,requireContext())
            withMainActivity.removeFragment()
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        withMainActivity = if(context is WithMainActivity){
            context
        }else{
            throw RuntimeException(context.toString() + "must implement")
        }
    }
}
package com.fusetech.mobilleltarkotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.databinding.FragmentLeltarBinding
import com.fusetech.mobilleltarkotlin.ui.interfaces.LeltarListener
import com.fusetech.mobilleltarkotlin.ui.viewModels.LeltarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeltarFragment : Fragment(),LeltarListener {
    val viewModel : LeltarViewModel by viewModels()
    private lateinit var binding: FragmentLeltarBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_leltar, container, false)
        binding.viewModel = viewModel
        viewModel.leltarListener = this
        binding.progressBar2.visibility = View.GONE
        return binding.root
    }
    fun setCikk(code: String){
       viewModel.cikkTextSet(code)
    }
    override fun setCikkText(code: String) {
        binding.cikkszamText.setText(code)
    }
}
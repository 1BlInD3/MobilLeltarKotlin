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
import com.fusetech.mobilleltarkotlin.showMe
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
        binding.rakhelyText.isFocusable = true
        binding.rakhelyText.isFocusableInTouchMode = true
        binding.rakhelyText.requestFocus()
        return binding.root
    }
    fun setCikk(code: String){
       viewModel.cikkTextSet(code)
    }
    override fun setRaktarText(code: String) {
        if(binding.rakhelyText.text.isEmpty()){
            binding.rakhelyText.setText(code)
            binding.rakhelyText.isFocusable = false
            binding.rakhelyText.isFocusableInTouchMode = false
            binding.cikkszamText.isFocusable = true
            binding.cikkszamText.isFocusableInTouchMode = true
            binding.cikkszamText.requestFocus()
        }else{
            showMe("Cikket vigyél fel!",requireContext())
            binding.cikkszamText.requestFocus()
            binding.rakhelyText.isFocusable = false
            binding.rakhelyText.isFocusableInTouchMode = false
        }
    }

    override fun setCikkText(code: String) {
        if(binding.rakhelyText.text.isNotEmpty()){
            binding.cikkszamText.setText(code)
            binding.cikkszamText.isFocusable = false
            binding.cikkszamText.isFocusableInTouchMode = false
            binding.cikkszamHeader.isFocusable = true
            binding.cikkszamHeader.isFocusableInTouchMode = true
            binding.cikkszamHeader.requestFocus()
        }else{
            showMe("Raktárt vigyél fel előbb",requireContext())
        }
    }

    override fun setProgressOn() {
        binding.progressBar2.visibility = View.VISIBLE
    }

    override fun setProgressOff() {
        binding.progressBar2.visibility = View.GONE
    }

    override fun cikkAdatok(des1: String?, desc2: String?, unit: String) {
        if(binding.rakhelyText.text.isNotEmpty()){
            binding.desc1.text = des1
            binding.desc2.text = desc2
            binding.unitLeltar.text = unit
        }
    }

    override fun raktarAdat(terulet: String) {
        binding.internalNameText.text = terulet
    }

    override fun errorCode(code: String) {
        showMe(code, requireContext())
    }

    override fun clearAll() {
        binding.rakhelyText.setText("")
        binding.cikkszamHeader.setText("")
        binding.cikkszamText.setText("")
        binding.megjegyzesText.setText("")
        binding.unitLeltar.text = ""
        binding.desc1.text = ""
        binding.desc2.text = ""
        binding.internalNameText.text = ""
        binding.rakhelyText.isFocusable = true
        binding.rakhelyText.isFocusableInTouchMode = true
        binding.rakhelyText.requestFocus()
    }

}
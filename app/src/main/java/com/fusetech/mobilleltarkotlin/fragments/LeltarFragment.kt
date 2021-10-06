package com.fusetech.mobilleltarkotlin.fragments

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.rakhelyInfo
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.selectFocus
import com.fusetech.mobilleltarkotlin.closeBin
import com.fusetech.mobilleltarkotlin.databinding.FragmentLeltarBinding
import com.fusetech.mobilleltarkotlin.showMe
import com.fusetech.mobilleltarkotlin.ui.interfaces.LeltarListener
import com.fusetech.mobilleltarkotlin.ui.interfaces.UpdateInterface
import com.fusetech.mobilleltarkotlin.ui.viewModels.LeltarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class LeltarFragment : Fragment(), LeltarListener {
    val viewModel: LeltarViewModel by viewModels()
    private lateinit var binding: FragmentLeltarBinding
    private lateinit var mainMenuInteract: MainMenuInteract

    interface MainMenuInteract {
        fun tabSwitch()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leltar, container, false)
        binding.viewModel = viewModel
        viewModel.leltarListener = this
        binding.progressBar2.visibility = View.GONE
       /* binding.rakhelyText.isFocusable = true
        binding.rakhelyText.isFocusableInTouchMode = true
        binding.rakhelyText.requestFocus()*/
        binding.cikkszamHeader.filters = arrayOf<InputFilter>(
            DecimalDigitsInputFilter(9, 2)
        )
        focusDefault()
        return binding.root
    }

    fun setCikk(code: String) {
        viewModel.cikkTextSet(code)
    }

    override fun setRaktarText(code: String) {
        if (binding.rakhelyText.text.isEmpty()) {
            binding.rakhelyText.setText(code)
            binding.rakhelyText.isFocusable = false
            binding.rakhelyText.isFocusableInTouchMode = false
            binding.cikkszamText.isFocusable = true
            binding.cikkszamText.isFocusableInTouchMode = true
            binding.cikkszamText.requestFocus()
        } else {
            showMe("Cikket vigyél fel!", requireContext())
            binding.cikkszamText.requestFocus()
            binding.rakhelyText.isFocusable = false
            binding.rakhelyText.isFocusableInTouchMode = false
        }
    }

    override fun setCikkText(code: String) {
        if (binding.rakhelyText.text.isNotEmpty()) {
            binding.cikkszamText.setText(code)
            binding.cikkszamText.isFocusable = false
            binding.cikkszamText.isFocusableInTouchMode = false
            binding.cikkszamHeader.isFocusable = true
            binding.cikkszamHeader.isFocusableInTouchMode = true
            binding.cikkszamHeader.requestFocus()
        } else {
            showMe("Raktárt vigyél fel előbb", requireContext())
        }
    }

    override fun setProgressOn() {
        binding.progressBar2.visibility = View.VISIBLE
    }

    override fun setProgressOff() {
        binding.progressBar2.visibility = View.GONE
    }

    override fun cikkAdatok(des1: String?, desc2: String?, unit: String) {
        if (binding.rakhelyText.text.isNotEmpty()) {
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
        binding.megjegyzesText.isFocusable = false
        binding.megjegyzesText.isFocusable = false
        rakhelyInfo.clear()

    }

    override fun mennyisegListener(quantity: Double) {
        if (binding.cikkszamHeader.isFocusable && binding.cikkszamHeader.isFocusableInTouchMode) {
            binding.cikkszamHeader.setText(quantity.toString().trim())
            binding.cikkszamHeader.isFocusable = false
            binding.cikkszamHeader.isFocusableInTouchMode = false
            binding.megjegyzesText.isFocusable = true
            binding.megjegyzesText.isFocusableInTouchMode = true
            binding.megjegyzesText.requestFocus()
        }
    }

    override fun afterUpload() {
        binding.cikkszamHeader.setText("")
        binding.cikkszamText.setText("")
        binding.megjegyzesText.setText("")
        binding.unitLeltar.text = ""
        binding.desc1.text = ""
        binding.desc2.text = ""
        binding.internalNameText.text = ""
        binding.rakhelyText.requestFocus()
        binding.megjegyzesText.isFocusable = false
        binding.megjegyzesText.isFocusable = false
        binding.cikkszamText.isFocusable = true
        binding.cikkszamText.isFocusableInTouchMode = true
        binding.cikkszamText.requestFocus()
    }

    override fun showData(listener: UpdateInterface, code: String) {
        closeBin(listener, code, requireContext())
    }

    override fun tabSwitch() {
        mainMenuInteract.tabSwitch()
    }

    override fun deleteRakhely() {
        binding.rakhelyText.setText("")
    }

    override fun cikkSelect() {
       binding.cikkszamText.selectAll()
    }

    override fun setNewBinOn() {
        binding.rakhelyButton.visibility = View.VISIBLE
    }

    override fun setNewBinOff() {
        binding.rakhelyButton.visibility = View.GONE
    }

    class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) :
        InputFilter {
        private var mPattern: Pattern =
            Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            val matcher = mPattern.matcher(dest)
            return if (!matcher.matches()) "" else null
        }
    }

    private fun focusDefault() {
        binding.rakhelyText.isFocusable = true
        binding.rakhelyText.isFocusableInTouchMode = true
        binding.rakhelyText.requestFocus()
        binding.cikkszamText.isFocusable = false
        binding.cikkszamText.isFocusableInTouchMode = false
        binding.cikkszamHeader.isFocusable = false
        binding.cikkszamHeader.isFocusableInTouchMode = false
        binding.megjegyzesText.isFocusable = false
        binding.megjegyzesText.isFocusableInTouchMode = false
    }

    fun getRakhely(): String {
        return binding.rakhelyText.text.toString()
    }

    override fun onResume() {
        super.onResume()
        selectFocus = 1
        Log.d("RSM", "onResume: LELTAR")
        when {
            viewModel.mennyiseg?.isNotEmpty()!! -> {
                binding.megjegyzesText.requestFocus()
            }
            viewModel.cikkszam.isNotEmpty() -> {
                binding.cikkszamHeader.requestFocus()
            }
            viewModel.rakhely.isNotEmpty() -> {
                binding.cikkszamText.requestFocus()
            }
            else -> {
                binding.leltarConst.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
                binding.rakhelyText.requestFocus()
            }
        }
    }

    fun getData(
        cikkszam: String,
        meg1: String?,
        meg2: String?,
        qty: Double,
        megjegyzes: String?,
        bizszam: Int
    ) {
        binding.cikkszamText.setText(cikkszam)
        binding.cikkszamHeader.setText(qty.toString().trim())
        binding.megjegyzesText.setText(megjegyzes)
        binding.desc1.text = meg1
        binding.desc2.text = meg2
        viewModel.bizszam = bizszam
        viewModel.isUpdate = true
        binding.cikkszamText.isFocusable = false
        binding.cikkszamText.isFocusableInTouchMode = false
        binding.cikkszamHeader.isFocusable = true
        binding.cikkszamHeader.isFocusableInTouchMode = true
        binding.cikkszamHeader.requestFocus()
        binding.cikkszamHeader.selectAll()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainMenuInteract = if (context is MainMenuInteract) {
            context
        } else {
            throw Exception("Must implement")
        }
    }
}
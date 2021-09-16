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
import androidx.lifecycle.ViewModelProvider
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.activity.MainActivity
import com.fusetech.mobilleltarkotlin.databinding.FragmentLoginBinding
import com.fusetech.mobilleltarkotlin.showMe
import com.fusetech.mobilleltarkotlin.ui.interfaces.LoginListener
import com.fusetech.mobilleltarkotlin.ui.viewModels.LoginViewModel

class LoginFragment : Fragment(), LoginListener {

    private lateinit var withMainActivity: WithMainActivity

    interface WithMainActivity{
        fun onExit()
        fun loadMenuFragment(hasRight: Boolean)
    }
    var mainActivity : MainActivity? = null
    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[LoginViewModel::class.java]
        }?: throw Exception("Invalid activity")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container,false)
        binding.viewModel = viewModel
        mainActivity = activity as MainActivity
        viewModel.loginListener = this
        binding.loginProgress.visibility = View.GONE
        return binding.root
    }
    override fun onCancelClicked() {
        withMainActivity.onExit()
    }
    @SuppressLint("SetTextI18n")
    override fun onRequestFailed(code : String) {
        binding.idText.text = """"Helytelen bejelentkezés a $code-dal"""
        try{
            showMe(code,requireContext())
        }catch (e: Exception){
            Log.d("TAG", "onRequestFailed: $e")
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onRequestSuccess() {
        withMainActivity.loadMenuFragment(true)
    }

    override fun onProgressOn() {
        binding.loginProgress.visibility = View.VISIBLE
    }

    override fun onProgressOff() {
        binding.loginProgress.visibility = View.GONE
    }

    fun setCode(code: String){
        viewModel.onLoginCode(code)
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
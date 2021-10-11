package com.fusetech.mobilleltarkotlin.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.databinding.FragmentLoginBinding
import com.fusetech.mobilleltarkotlin.ui.interfaces.LoginListener
import com.fusetech.mobilleltarkotlin.ui.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment(), LoginListener {
    val viewModel: LoginViewModel by viewModels()
    private lateinit var withMainActivity: WithMainActivity
    private lateinit var myTimer: CountDownTimer

    interface WithMainActivity{
        fun onExit()
        fun loadMenuFragment(hasRight: Boolean)
    }
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container,false)
        binding.viewModel = viewModel
        viewModel.loginListener = this
        binding.loginProgress.visibility = View.GONE

        myTimer = object : CountDownTimer(60000,1000){
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick: ")
            }
            override fun onFinish() {
                withMainActivity.onExit()
            }
        }
        return binding.root
    }
    override fun onCancelClicked() {
        withMainActivity.onExit()
    }

    override fun onDestroy() {
        myTimer.cancel()
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    override fun onRequestFailed(code : String) {
        binding.idText.text = """"Helytelen bejelentkezés a $code-dal"""
    }

    override fun onResume() {
        super.onResume()
        myTimer.start()
    }

    override fun onPause() {
        myTimer.cancel()
        super.onPause()
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
package com.fusetech.mobilleltarkotlin.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.databinding.FragmentMenuBinding
import com.fusetech.mobilleltarkotlin.showMe
import com.fusetech.mobilleltarkotlin.ui.interfaces.MenuLstener
import com.fusetech.mobilleltarkotlin.ui.viewModels.MenuViewModel

private const val ARG_PARAM1 = "param1"

class MenuFragment : Fragment(), MenuLstener {
    private var param1: Boolean? = null
    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: FragmentMenuBinding
    private lateinit var withMainActivity: WithMainActivity
    interface WithMainActivity{
        fun loadLeltar()
        fun loadLekerdezes()
        fun loadKilepes()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this)[MenuViewModel::class.java]
        }?: throw Exception("Invalid activity")
        arguments?.let {
            param1 = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_menu,container,false)
        binding.viewModel = viewModel
        viewModel.menuListener = this
        return binding.root
    }

    companion object {
        fun newInstance(param1: Boolean) =
            MenuFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, param1)
                }
            }
    }

    override fun loadLeltar() {
        //showMe("Leltár",requireContext())
        withMainActivity.loadLeltar()
    }
    override fun loadLekerdezes() {
       // showMe("Lekerdezes",requireContext())
        withMainActivity.loadLekerdezes()
    }
    override fun loadKilep() {
        showMe("Kilep",requireContext())
        withMainActivity.loadKilepes()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        withMainActivity = if(context is WithMainActivity){
            context
        }else{
            throw RuntimeException(context.toString() + "must implement")
        }
       /* OnBackPressedCallback callback = new OnBackPressedCallback(
            true // default to enabled
        ) {
            Override
                    public void handleOnBackPressed() {
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
            this, // LifecycleOwner
            callback);*/
    }


}

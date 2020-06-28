package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentMainMenuBinding
import hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments.LoginPage
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import kotlin.system.exitProcess

class MainMenu : Fragment(), MVVM {

    private lateinit var binding: FragmentMainMenuBinding

    private lateinit var manager: Manager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
    }

    override fun setUpFragment() {
        setUpUI()
        setUpBinding()
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_main_menu)
        manager = activity as Manager
    }

    override fun setUpBinding() {
        binding.btnLogOut.setOnClickListener() {
            manager.openFragment(R.id.frameMainMenu, LoginPage())
        }

        binding.btnExit.setOnClickListener() {
            exitProcess(0)
        }

        binding.btnAddToStock.setOnClickListener() {
            manager.openFragment(R.id.frameMainMenu, AddToStock())
        }

        binding.btnViewStock.setOnClickListener() {
            manager.openFragment(R.id.frameMainMenu, ViewStock())
        }

        binding.btnCashRegister.setOnClickListener() {
            manager.openFragment(R.id.frameMainMenu, Invoice())
        }
    }
}
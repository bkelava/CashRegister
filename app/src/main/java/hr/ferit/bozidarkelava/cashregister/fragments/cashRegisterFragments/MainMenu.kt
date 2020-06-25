package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentMainMenuBinding
import hr.ferit.bozidarkelava.cashregister.fragments.userRegisterFragments.LoginPage
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import kotlin.system.exitProcess

class MainMenu : Fragment(), Manager, MVVM {

    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragment()
    }

    override fun openFragment(layoutID: Int, fragment: Fragment) {
        val context = activity as AppCompatActivity
        context.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation)
            .replace(layoutID, fragment)
            .commit()
    }

    override fun setUpFragment() {
        setUpUI()
        setUpBinding()
    }

    override fun setUpUI() {
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_main_menu)
    }

    override fun setUpBinding() {
        binding.btnLogOut.setOnClickListener() {
            openFragment(R.id.frameMainMenu, LoginPage())
        }

        binding.btnExit.setOnClickListener() {
            exitProcess(0)
        }

        binding.btnAddToStock.setOnClickListener() {
            openFragment(R.id.frameMainMenu, AddToStock())
        }

        binding.btnViewStock.setOnClickListener() {
            openFragment(R.id.frameMainMenu, ViewStock())
        }
    }
}
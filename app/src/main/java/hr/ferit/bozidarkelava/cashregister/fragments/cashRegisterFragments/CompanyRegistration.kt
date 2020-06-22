package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentCompanyRegistrationBinding
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentSignUpPageBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.miscellaneous.StringValues
import hr.ferit.bozidarkelava.cashregister.viewModels.CompanyRegistrationViewModel
import hr.ferit.bozidarkelava.cashregister.viewModels.SignUpViewModel
import kotlin.system.exitProcess


class CompanyRegistration : Fragment(), Manager, MVVM  {

    private var strValues: StringValues = StringValues()

    private lateinit var binding: FragmentCompanyRegistrationBinding
    private lateinit var viewModel: CompanyRegistrationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_company_registration, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //if (true) {
        //    openFragment(R.id.frameCompanyRegistration, MainMenu())
        //}
        //else {
            setUpFragment()
        //}
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
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_company_registration)
        viewModel = ViewModelProviders.of(this).get(CompanyRegistrationViewModel::class.java)
    }

    override fun setUpBinding() {
        binding.apply {
            binding.info = viewModel

            binding.btnExit.setOnClickListener() {
                exitProcess(0)
            }

            binding.btnSaveInformation.setOnClickListener() {
                //do something
            }
        }
    }
}
package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.CompanyInformation
import hr.ferit.bozidarkelava.cashregister.databinding.FragmentCompanyRegistrationBinding
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager
import hr.ferit.bozidarkelava.cashregister.containers.UserContainer
import hr.ferit.bozidarkelava.cashregister.viewModels.CompanyRegistrationViewModel
import kotlin.system.exitProcess


class CompanyRegistration : Fragment(), MVVM  {

    private var companyInformationDao = CashRegisterDatabase.getInstance().companyInformationDao()

    private lateinit var manager: Manager

    private lateinit var binding: FragmentCompanyRegistrationBinding
    private lateinit var viewModel: CompanyRegistrationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_company_registration, container, false)
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
        binding = DataBindingUtil.setContentView(this.requireActivity(), R.layout.fragment_company_registration)
        viewModel = ViewModelProviders.of(this).get(CompanyRegistrationViewModel::class.java)

        manager = activity as Manager
    }

    override fun setUpBinding() {
        binding.apply {
            binding.info = viewModel
            viewModel.setEmail(UserContainer.getEmail())
            binding.btnExit.setOnClickListener() {
                exitProcess(0)
            }

            binding.btnSaveInformation.setOnClickListener() {
                insertInfo()
            }
        }
        viewModel.notification.observe(this.requireActivity(), androidx.lifecycle.Observer { binding.invalidateAll() })
        viewModel.email.observe(this.requireActivity(), androidx.lifecycle.Observer { binding.invalidateAll() })
    }

    private fun insertInfo() {
        val companyName = binding.etCompanyName.text.toString()
        val companyAddress = binding.etCompanyAddress.text.toString()
        val companyCityAndPostal = binding.etCompanyCityAndPostal.text.toString()
        val companyContact = binding.etCompanyContact.text.toString()
        val companyCEO = binding.etCompanyCEO.text.toString()
        val companyEmail = binding.etCompanyEmail.text.toString()

        if (companyName == "" || companyAddress == "" || companyCityAndPostal==""||companyContact=="" || companyCEO=="" ||companyEmail=="") {
            viewModel.setCompanyRegistrationNotificationText("Some of the required fields are empty!")
        }
        else {
            val id: Int = companyInformationDao.seletMaxId() + 1
            val companyInfo = CompanyInformation(id, companyName, companyAddress, companyCityAndPostal, companyContact, companyCEO, companyEmail)
            companyInformationDao.insert(companyInfo)
            clearFields()
            viewModel.setCompanyRegistrationNotificationText("PROCESSING")
            manager.openFragment(R.id.frameCompanyRegistration, MainMenu())
        }
    }

    private fun clearFields()
    {
        binding.etCompanyName.setText("")
        binding.etCompanyAddress.setText("")
        binding.etCompanyCityAndPostal.setText("")
        binding.etCompanyContact.setText("")
        binding.etCompanyCEO.setText("")
        binding.etCompanyEmail.setText("")
    }
}
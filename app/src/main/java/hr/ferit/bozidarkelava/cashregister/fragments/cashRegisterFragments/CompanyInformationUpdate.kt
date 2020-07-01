package hr.ferit.bozidarkelava.cashregister.fragments.cashRegisterFragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.ferit.bozidarkelava.cashregister.R
import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase
import hr.ferit.bozidarkelava.cashregister.database.tables.CompanyInformation
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM

class CompanyInformationUpdate : CompanyRegistration() {

    private val databaseCompanyInformation = CashRegisterDatabase.getInstance().companyInformationDao()

    override fun setUpFragment() {
        super.setUpFragment()
        setFields()
    }

    private fun setFields() {
        val list: List<CompanyInformation> = databaseCompanyInformation.selectAll()
        binding.etCompanyName.setText(list[0].companyName)
        binding.etCompanyAddress.setText(list[0].companyAddress)
        binding.etCompanyCityAndPostal.setText(list[0].companyCityAndPostal)
        binding.etCompanyContact.setText(list[0].companyContact)
        binding.etCompanyCEO.setText(list[0].companyCEO)

        binding.etCompanyEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        binding.etCompanyEmail.isEnabled=true

        viewModel.setEmail(list[0].companyEmail)

        binding.etCompanyEmail.isEnabled = false
    }

    override fun insertInfo() {
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
            val id: Int = databaseCompanyInformation.seletMaxId()
            val companyInfo = CompanyInformation(id, companyName, companyAddress, companyCityAndPostal, companyContact, companyCEO, companyEmail)
            databaseCompanyInformation.update(companyInfo)
            viewModel.setCompanyRegistrationNotificationText("PROCESSING")
            manager.openFragment(R.id.frameCompanyRegistration, MainMenu())
        }
    }

}
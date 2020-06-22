package hr.ferit.bozidarkelava.cashregister.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompanyRegistrationViewModel: ViewModel() {

    var notification = MutableLiveData<String>()

    var email = MutableLiveData<String>()

    fun setCompanyRegistrationNotificationText(text: String) {
        notification.value = text
    }

    fun setEmail(email: String) {
        this.email.value = email
    }
}
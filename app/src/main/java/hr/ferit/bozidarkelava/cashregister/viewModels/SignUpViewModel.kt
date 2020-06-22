package hr.ferit.bozidarkelava.cashregister.viewModels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.ferit.bozidarkelava.cashregister.interfaces.MVVM
import hr.ferit.bozidarkelava.cashregister.interfaces.Manager

class SignUpViewModel: ViewModel() {

    var notification = MutableLiveData<String>() //live data

    fun setSignUpNotificationText(text: String) {
        this.notification.value=text
    }
}
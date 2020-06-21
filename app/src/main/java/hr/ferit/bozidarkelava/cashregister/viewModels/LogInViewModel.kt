package hr.ferit.bozidarkelava.cashregister.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInViewModel: ViewModel() {

    var notification = MutableLiveData<String>() //live data

    fun setLogInNotificationText(text: String) {
        this.notification.value=text
    }
}
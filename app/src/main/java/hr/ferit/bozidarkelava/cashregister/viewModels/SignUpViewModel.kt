package hr.ferit.bozidarkelava.cashregister.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {

    var notification = MutableLiveData<String>() //live data

    fun setText(text: String) {
        this.notification.value=text
    }
}
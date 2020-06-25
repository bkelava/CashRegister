package hr.ferit.bozidarkelava.cashregister.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StockViewModel: ViewModel() {

    var productType = MutableLiveData<String>()

    var notification = MutableLiveData<String>()

    fun setProductType(text: String) {
        productType.value = text
    }

    fun setMainMenuNotificationText(text: String) {
        notification.value = text
    }


}
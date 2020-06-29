package hr.ferit.bozidarkelava.cashregister.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InvoiceViewModel: ViewModel() {

    val totalPrice = MutableLiveData<Double>()

    fun setTotalPrice(price: Double) {
        this.totalPrice.value = price
    }
}
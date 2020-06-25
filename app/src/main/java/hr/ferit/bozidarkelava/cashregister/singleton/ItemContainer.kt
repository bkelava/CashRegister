package hr.ferit.bozidarkelava.cashregister.singleton

object ItemContainer {

    private var productOrService = ""

    fun setProductType(text: String) {
        this.productOrService = text
    }

    fun getProductType(): String {
        return this.productOrService
    }
}
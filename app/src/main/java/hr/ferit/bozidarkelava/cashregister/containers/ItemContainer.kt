package hr.ferit.bozidarkelava.cashregister.containers

object ItemContainer {

    private var productId: Int = 0

    private var productOrService = ""

    private var name = ""
    private var unit=""
    private var quantity=""
    private var price=""

    fun setProductId (id: Int) {
        this.productId=id
    }

    fun getProductId(): Int {
        return this.productId
    }

    fun setProductType(text: String) {
        this.productOrService = text
    }

    fun getProductType(): String {
        return this.productOrService
    }

    fun setUnit(text: String) {
        this.unit=text
    }

    fun getUnit(): String {
        return this.unit
    }
    fun setQuantity(text: String) {
        this.quantity=text
    }

    fun getQuantity(): String {
        return this.quantity
    }
    fun setPrice(text: String) {
        this.price=text
    }

    fun getPrice(): String {
        return this.price
    }
    fun setName(text: String) {
        this.name=text
    }

    fun getName(): String {
        return this.name
    }
}
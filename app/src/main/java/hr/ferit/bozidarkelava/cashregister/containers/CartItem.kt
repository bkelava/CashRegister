package hr.ferit.bozidarkelava.cashregister.containers

class CartItem(id: String, name: String, quantity: String, price: String, total: String) {

    private var id: String
    private var name: String
    private var quantity: String
    private var price: String
    private var total: String

    init {
        this.id=id
        this.name=name
        this.quantity=quantity
        this.price=price
        this.total=total
    }

    fun getId(): String {
        return this.id
    }
    fun getName(): String {
        return this.name
    }
    fun getQuantity(): String {
        return this.quantity
    }
    fun getPrice(): String {
        return this.price
    }
    fun getTotal(): String {
        return this.total
    }





}
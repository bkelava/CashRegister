package hr.ferit.bozidarkelava.cashregister.containers

object TotalPriceContainer {

    private var totalPrice: Double = 0.00

    fun getTotalPrice(): Double {
        return this.totalPrice
    }

    fun setTotalPrice(price: Double) {
        this.totalPrice=price
    }

}
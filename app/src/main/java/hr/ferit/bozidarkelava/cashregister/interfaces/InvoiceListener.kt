package hr.ferit.bozidarkelava.cashregister.interfaces

interface InvoiceListener {

    fun add(position: Int)

    fun eliminate(position: Int)

    fun remove(position: Int)

    fun setQuantityText(position: Int): String

    fun setItemTotalPrice(position: Int): String
}
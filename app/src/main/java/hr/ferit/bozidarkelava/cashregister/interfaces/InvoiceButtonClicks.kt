package hr.ferit.bozidarkelava.cashregister.interfaces

interface InvoiceButtonClicks {

    fun add(position: Int)

    fun eliminate(position: Int)

    fun remove(position: Int)

    fun setText(position: Int): String
}
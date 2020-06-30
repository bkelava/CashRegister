package hr.ferit.bozidarkelava.cashregister.interfaces

interface ProductButtonsClicks {
    fun delete(position: Int)

    fun update(position: Int)

    fun export(position: Int)
}
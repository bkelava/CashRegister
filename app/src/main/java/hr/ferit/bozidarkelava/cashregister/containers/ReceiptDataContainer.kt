package hr.ferit.bozidarkelava.cashregister.containers

import hr.ferit.bozidarkelava.cashregister.database.CashRegisterDatabase

object ReceiptDataContainer {

    private val databaseReceipts = CashRegisterDatabase.getInstance().receiptsDao()

    private var id: Int = 0

    private var path: String =""

    fun getId(): Int {
        return this.id
    }

    fun setId(id: Int) {
        this.id=id
    }

    fun getPath(): String {
        return this.path
    }

    fun setPath(path: String) {
        this.path=path
    }
}
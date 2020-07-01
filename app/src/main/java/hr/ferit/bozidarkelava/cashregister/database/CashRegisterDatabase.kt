package hr.ferit.bozidarkelava.cashregister.database

import androidx.room.Room
import androidx.room.RoomDatabase
import hr.ferit.bozidarkelava.cashregister.activity.CashRegisterApp
import hr.ferit.bozidarkelava.cashregister.database.tables.CompanyInformation
import hr.ferit.bozidarkelava.cashregister.database.interfaces.CompanyInformationDao
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.database.interfaces.ProductDao
import hr.ferit.bozidarkelava.cashregister.database.interfaces.ReceiptDao
import hr.ferit.bozidarkelava.cashregister.database.tables.Receipts

@androidx.room.Database(version = 1, entities = arrayOf(CompanyInformation::class, Product::class, Receipts::class))
abstract class CashRegisterDatabase: RoomDatabase() {

    abstract fun companyInformationDao(): CompanyInformationDao
    abstract fun productDao(): ProductDao
    abstract fun receiptsDao(): ReceiptDao

    companion object {
        private const val  NAME ="cash_register_database"
        private var INSTANCE: CashRegisterDatabase? = null
        fun getInstance(): CashRegisterDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(CashRegisterApp.ApplicationContext, CashRegisterDatabase::class.java, NAME).allowMainThreadQueries().build()
            }
            return INSTANCE as CashRegisterDatabase
        }
    }

}
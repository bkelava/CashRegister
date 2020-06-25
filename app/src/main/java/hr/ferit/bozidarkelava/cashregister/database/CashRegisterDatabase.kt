package hr.ferit.bozidarkelava.cashregister.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.ferit.bozidarkelava.cashregister.activity.CashRegister
import hr.ferit.bozidarkelava.cashregister.database.tables.CompanyInformation
import hr.ferit.bozidarkelava.cashregister.database.tables.CompanyInformationDao
import hr.ferit.bozidarkelava.cashregister.database.tables.Product
import hr.ferit.bozidarkelava.cashregister.database.tables.ProductDao

@androidx.room.Database(version = 1, entities = arrayOf(CompanyInformation::class, Product::class))
abstract class CashRegisterDatabase: RoomDatabase() {

    abstract fun companyInformationDao(): CompanyInformationDao
    abstract fun productDao(): ProductDao

    companion object {
        private const val  NAME ="InspiringPersonsDatabase"
        private var INSTANCE: CashRegisterDatabase? = null
        fun getInstance(): CashRegisterDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(CashRegister.ApplicationContext, CashRegisterDatabase::class.java, NAME).allowMainThreadQueries().build()
            }
            return INSTANCE as CashRegisterDatabase
        }
    }

}
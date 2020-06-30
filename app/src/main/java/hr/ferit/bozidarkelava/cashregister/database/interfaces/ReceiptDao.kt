package hr.ferit.bozidarkelava.cashregister.database.interfaces

import androidx.room.*
import hr.ferit.bozidarkelava.cashregister.database.tables.Receipt

@Dao
interface ReceiptDao {

    @Insert
    fun insert(receipt: Receipt)

    @Update
    fun update (receipt: Receipt)

    @Delete
    fun delete (receipt: Receipt)

    @Query ("SELECT * FROM receipts")
    fun selectAll(): List<Receipt>

}
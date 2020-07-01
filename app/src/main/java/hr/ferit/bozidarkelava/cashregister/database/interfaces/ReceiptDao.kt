package hr.ferit.bozidarkelava.cashregister.database.interfaces

import androidx.room.*
import hr.ferit.bozidarkelava.cashregister.database.tables.Receipts

@Dao
interface ReceiptDao {

    @Insert
    fun insert(receipts: Receipts)

    @Update
    fun update (receipts: Receipts)

    @Delete
    fun delete (receipts: Receipts)

    @Query ("SELECT * FROM receipts")
    fun selectAll(): List<Receipts>

    @Query("SELECT MAX(id) FROM receipts")
    fun selectMaxId(): Int

    @Query("SELECT path FROM receipts ORDER BY id DESC LIMIT 1")
    fun selectLastRecipePath(): String

    @Query ("SELECT id FROM receipts ORDER BY id DESC LIMIT 1")
    fun selectLastRecipeNumber(): Int

    @Query ("SELECT path FROM receipts WHERE id=:id")
    fun selectPathFromId(id: Int): String

}
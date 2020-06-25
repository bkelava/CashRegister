package hr.ferit.bozidarkelava.cashregister.database.tables

import androidx.room.*

@Dao
interface ProductDao {

    @Insert
    fun insert(product: Product)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("SELECT MAX(id) FROM product")
    fun seletMaxId(): Int

    @Query ("SELECT * FROM product WHERE id = :id")
    fun selectId(id: Int): Product

    @Query("SELECT * FROM product")
    fun selectAll(): List<Product>
}
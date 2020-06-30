package hr.ferit.bozidarkelava.cashregister.database.interfaces

import androidx.room.*
import hr.ferit.bozidarkelava.cashregister.database.tables.Product

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

    @Query("SELECT * FROM product WHERE name LIKE '%' || :string || '%'")
    fun filter(string: String): List<Product>

    @Query ("SELECT name FROM product")
    fun selectAllItemNames(): List<String>

    @Query("SELECT * FROM product WHERE name =  :name" )
    fun selectProductByName(name: String): Product

    @Query ("SELECT quantity FROM product WHERE id = :id")
    fun selectProductQuantity(id: Int) : Int


    @Query ("UPDATE product SET quantity= :quantity WHERE id =  :id")
    fun updateProductQuantity(id: Int, quantity: Int)

    @Query ("SELECT name FROM product WHERE id = :id")
    fun selectItemNameById(id: Int) : String

}
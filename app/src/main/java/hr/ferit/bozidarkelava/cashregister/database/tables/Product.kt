package hr.ferit.bozidarkelava.cashregister.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "type")val type: String,
    @ColumnInfo(name = "name") val productName: String,
    @ColumnInfo(name = "unitMeasure") val unitMeasure: String,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "price") val price: Double
) {

}
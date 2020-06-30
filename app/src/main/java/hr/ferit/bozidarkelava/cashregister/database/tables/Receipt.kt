package hr.ferit.bozidarkelava.cashregister.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "receipts")
data class Receipt (
    @PrimaryKey val  id: Int = 0,
    @ColumnInfo(name = "path") val receiptPath: String
)
package hr.ferit.bozidarkelava.cashregister.database.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companyInformation")
data class CompanyInformation(
    @PrimaryKey val companyId: Int = 0,
    @ColumnInfo(name = "companyName") val companyName: String,
    @ColumnInfo(name = "companyAddress") val companyAddress: String,
    @ColumnInfo(name = "companyCityAndPostal") val companyCityAndPostal: String,
    @ColumnInfo(name = "companyContact") val companyContact: String,
    @ColumnInfo(name = "companyCEO")val companyCEO: String,
    @ColumnInfo(name = "companyEmail") val companyEmail: String
)
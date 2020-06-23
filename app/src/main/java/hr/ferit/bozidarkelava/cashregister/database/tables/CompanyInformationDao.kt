package hr.ferit.bozidarkelava.cashregister.database.tables

import androidx.room.*

@Dao
interface CompanyInformationDao {

    @Insert
    fun insert(companyInformation: CompanyInformation)

    @Delete
    fun delete(companyInformation: CompanyInformation)

    @Update
    fun update(companyInformation: CompanyInformation)

    @Query("SELECT * FROM companyInformation")
    fun selectAll (): List<CompanyInformation>

    @Query("SELECT MAX(companyId) FROM companyInformation")
    fun seletMaxId(): Int

    @Query("SELECT * FROM companyInformation WHERE companyEmail=:email")
    fun checkUserByEmail(email: String) : List<CompanyInformation>
}
package com.example.project1.LocalDataSource
import androidx.room.*

@Dao
interface CurrencyDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertCurrencies(currencies: List<CurrencyEntity>)
    @Query("UPDATE currency SET favorite = :isFavorite AND rate = :rate WHERE name = :name")
    suspend fun updateCurrency(name: String, rate: Float, isFavorite: Boolean)
    @Insert
    suspend fun insertExchangeData(exchangeHistory: ExchangeHistory)
    @Query("SELECT * FROM exchange")
    suspend fun getExchangeData(): List<ExchangeHistory>
    @Query("SELECT * FROM currency")
    suspend fun getCurrenciesFromLocalDataSource(): List<CurrencyEntity>
}
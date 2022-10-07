package com.example.project1.data

import com.example.project1.LocalDataSource.CurrencyDao
import com.example.project1.LocalDataSource.CurrencyEntity
import com.example.project1.LocalDataSource.ExchangeHistory
import com.example.project1.RemoteDataSource.CurrencyApi
import com.example.project1.RemoteDataSource.CurrencyResponse


class CurrencyRepository(private val currencyApi: CurrencyApi, private val currencyDao: CurrencyDao) {

    suspend fun getCurrencies(): CurrencyResponse {
        return currencyApi.getCurrencies()
    }

    suspend fun insertExchangeData(exchangeHistory: ExchangeHistory){
        currencyDao.insertExchangeData(exchangeHistory)
    }

    suspend fun saveCurrencies(currencies: List<CurrencyEntity>){
        currencyDao.insertCurrencies(currencies)
    }

    suspend fun updateCurrency(currencyEntity: CurrencyEntity) {
        currencyDao.updateCurrency(currencyEntity.name, currencyEntity.rate, currencyEntity.favorite)
    }

    suspend fun getCurrenciesFromLocalDataSource(): List<CurrencyEntity> {
        return currencyDao.getCurrenciesFromLocalDataSource()
    }

    suspend fun getExchangeData(): List<ExchangeHistory>{
        return currencyDao.getExchangeData()
    }
}
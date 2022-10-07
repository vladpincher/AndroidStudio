package com.example.project1.RemoteDataSource

import retrofit2.http.GET

interface CurrencyApi {
    @GET("/api/latest?access_key=a70c497e9c98eedc695b9a4add7cdd05&format=1")
    suspend fun getCurrencies(): CurrencyResponse
}

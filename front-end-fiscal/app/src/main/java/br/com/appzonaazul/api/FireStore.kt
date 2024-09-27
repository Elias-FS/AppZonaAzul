package br.com.appzonaazul.api

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.gson.JsonObject
import retrofit2.http.POST


interface FireStore {
    @GET("/findByPlate")
    fun getTickets() : Call<JsonArray>

    @GET("/getZonaAzul")
    fun getZonaAzul() : Call<JsonArray>
}
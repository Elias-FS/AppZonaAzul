package br.com.appzonaazul.api

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import com.google.gson.JsonObject
import com.google.gson.JsonArray


interface FireStore {
    @GET("/getTickets")
    fun getTickets() : Call<JsonArray>
}
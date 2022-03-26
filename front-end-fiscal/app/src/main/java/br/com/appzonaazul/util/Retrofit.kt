
package br.com.appzonaazul.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        fun getRetrofitInstance() : Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://southamerica-east1-backendfiscal.cloudfunctions.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
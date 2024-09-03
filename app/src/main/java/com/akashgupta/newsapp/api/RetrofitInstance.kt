package com.akashgupta.newsapp.api

import com.akashgupta.newsapp.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    //Retrofit Singleton class
    companion object{
        //lazy - means that we only initialize this here(by lazy { }) once.
        private val retrofit by lazy {
            //log response of retrofit(useful for debugging), okhttp3:logging-interceptor
            val logging = HttpLoggingInterceptor()
            //Attach this to our retrofit object to be able to see which requests we are
            //actually making & what response are.
            logging.setLevel(HttpLoggingInterceptor.Level.BODY) //Actually see the body of our response.
            //Network client
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            //Now we use client and pass it to our retrofit instance
            //so we will return in this lazy block
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                /*addConverterFactory - used to determine how response should actually be interpreted
                 & converted to kotlin objects*/
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        //Finally get ur API instance from that retrofit builder
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}
package com.example.myquizapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface QuizApiService {
    @GET
    suspend fun getQuestions(@Url url: String): List<Question>
}

class QuizApiServiceImpl : QuizApiService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(QuizApiService::class.java)

    override suspend fun getQuestions(url: String): List<Question> {
        return apiService.getQuestions(url)
    }
}

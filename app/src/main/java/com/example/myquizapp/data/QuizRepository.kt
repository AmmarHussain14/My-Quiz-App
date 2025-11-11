package com.example.myquizapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(
    private val apiService: QuizApiService
) {
    private val questionsUrl = "https://gist.githubusercontent.com/dr-samrat/53846277a8fcb034e482906ccc0d12b2/raw"

    suspend fun getQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        try {
            val questions = apiService.getQuestions(questionsUrl)
            Result.success(questions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

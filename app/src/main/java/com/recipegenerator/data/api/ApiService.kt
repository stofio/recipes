package com.recipegenerator.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("completions")
    suspend fun getRecipes(@Body body: ApiRequestBody): ApiResponseBody

}
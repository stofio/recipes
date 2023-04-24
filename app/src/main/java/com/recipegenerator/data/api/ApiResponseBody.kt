package com.recipegenerator.data.api

data class ApiResponseBody(
    val choices: List<Choice>
)

data class Choice(
    val text: String
)
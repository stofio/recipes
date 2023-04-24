package com.recipegenerator.data.api

import com.google.gson.annotations.SerializedName
import com.recipegenerator.domain.util.OPENAI_MAX_TOKENS
import com.recipegenerator.domain.util.OPENAI_MODEL
import com.recipegenerator.domain.util.OPENAI_TEMPERATURE

data class ApiRequestBody(
    val model: String = OPENAI_MODEL,
    val prompt: String,
    @SerializedName("max_tokens") val maxTokens: Int = OPENAI_MAX_TOKENS,
    val temperature: Int = OPENAI_TEMPERATURE
)
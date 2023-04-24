package com.recipegenerator.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val name: String = "",
    val time: String = "",
    val difficulty: String = "",
    val ingredients: List<String> = emptyList(),
    val tools: List<String> = emptyList(),
    val instructions: List<String> = emptyList()
) : Parcelable
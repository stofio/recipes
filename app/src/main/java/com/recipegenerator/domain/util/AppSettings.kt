package com.recipegenerator.domain.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.data.storage.Settings

class AppSettings(context: Context) : Settings(context) {

    private val gson = Gson()

    fun getLastRecipes(): List<Recipe>? {
        return getString(KEY_LAST_RECIPES)?.let { json ->
            val recipeListType = object : TypeToken<List<Recipe>>() {}.type
            val list = gson.fromJson<List<Recipe>>(json, recipeListType)
            list
        }
    }

    fun setLastRecipes(list: List<Recipe>) {
        val json = gson.toJson(list)
        setString(KEY_LAST_RECIPES, json)
    }

}
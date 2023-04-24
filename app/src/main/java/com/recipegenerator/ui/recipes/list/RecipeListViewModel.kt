package com.recipegenerator.ui.recipes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.recipegenerator.data.api.ApiRequestBody
import com.recipegenerator.data.api.ApiService
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.domain.extensions.toStringList
import com.recipegenerator.domain.util.*
import com.recipegenerator.ui.common.Status
import com.recipegenerator.ui.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val apiService: ApiService,
    private val appSettings: AppSettings
) : ViewModel() {

    val recipesData = LiveEvent<ViewModelState<List<Recipe>>>()

    fun isRecipesLoaded() =
        recipesData.value?.status == Status.SUCCESS && recipesData.value?.data != null

    fun loadRecipes() = viewModelScope.launch(Dispatchers.IO) {
        val ingredients = GlobalDataHolder.ingredients
        if (ingredients.isEmpty()) {
            val lastRecipes = appSettings.getLastRecipes()
            if (lastRecipes != null) {
                recipesData.postValue(ViewModelState.success(lastRecipes))
            } else {
                recipesData.postValue(ViewModelState.error("Please select ingredients first"))
            }
        } else {
            recipesData.postValue(ViewModelState.loading())
            try {
                val body = ApiRequestBody(
                    prompt = OPENAI_PROMPT.format(ingredients)
                )
                val response = apiService.getRecipes(body)
                val json = response.choices[0].text
                val recipes = parseRecipes(json)
                appSettings.setLastRecipes(recipes)
                GlobalDataHolder.ingredients = ""
                recipesData.postValue(ViewModelState.success(recipes))
            } catch (e: Exception) {
                e.printStackTrace()
                recipesData.postValue(ViewModelState.error(e.message ?: "", e))
            }
        }
    }

    private fun parseRecipes(json: String): List<Recipe> = try {
        val recipes = mutableListOf<Recipe>()
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val recipe = parseRecipeObject(obj)
            recipes.add(recipe)
        }
        recipes
    } catch (e: JSONException) {
        listOf(parseRecipeObject(JSONObject(json)))
    }

    private fun parseRecipeObject(obj: JSONObject): Recipe {
        return Recipe(
            name = obj.getString(FIELD_NAME),
            time = obj.getString(FIELD_TIME),
            difficulty = obj.getString(FIELD_DIFFICULTY),
            ingredients = obj.getJSONArray(FIELD_INGREDIENTS).toStringList(),
            tools = obj.getJSONArray(FIELD_TOOLS).toStringList(),
            instructions = obj.getJSONArray(FIELD_INSTRUCTIONS).toStringList()
        )
    }

}
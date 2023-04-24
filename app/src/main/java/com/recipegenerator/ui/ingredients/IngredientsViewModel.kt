package com.recipegenerator.ui.ingredients

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.recipegenerator.domain.util.AUTOCOMPLETE_INGREDIENTS_FILENAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    auth: FirebaseAuth
) : ViewModel() {

    val ingredients = mutableListOf<String>()
    val ingredientsData = MutableLiveData<List<String>>()
    var autocompleteIngredients = mutableListOf<String>()

    init {
        ingredientsData.postValue(ingredients)
        auth.signInAnonymously()
    }

    fun addIngredient(name: String) {
        ingredients += name
        ingredientsData.postValue(ingredients)
    }

    fun removeIngredient(name: String) {
        ingredients -= name
        ingredientsData.postValue(ingredients)
    }

    fun fillAutocompleteIngredients(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        context.assets.open(AUTOCOMPLETE_INGREDIENTS_FILENAME).use { inputStream ->
            val reader = inputStream.bufferedReader()
            val list = reader.lineSequence()
                .flatMap { it.split(",") }
                .filter { it.isNotBlank() }
                .toList()
            autocompleteIngredients.clear()
            autocompleteIngredients.addAll(list)
        }
    }

}
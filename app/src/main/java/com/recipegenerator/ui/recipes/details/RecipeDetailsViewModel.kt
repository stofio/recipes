package com.recipegenerator.ui.recipes.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.domain.extensions.getCurrentUserId
import com.recipegenerator.domain.extensions.toMap
import com.recipegenerator.domain.util.COLLECTION_RECIPES
import com.recipegenerator.domain.util.FIELD_NAME
import com.recipegenerator.domain.util.FIELD_USER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    var recipe: Recipe? = null
        set(value) {
            field = value
            recipeData.postValue(value)
            checkRecipeIsSaved()
        }
    val recipeData = MutableLiveData<Recipe?>()
    val isSavedData = MutableLiveData<Boolean>()
    var recipeDocId: String? = null

    fun saveRecipe() = viewModelScope.launch(Dispatchers.IO) {
        recipe?.let {
            val recipeMap = it.toMap().apply {
                put(FIELD_USER, auth.getCurrentUserId())
            }
            firestore.collection(COLLECTION_RECIPES)
                .add(recipeMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        recipeDocId = task.result.id
                        isSavedData.postValue(true)
                    } else {
                        task.exception?.printStackTrace()
                    }
                }
        }
    }

    fun removeRecipe() = viewModelScope.launch(Dispatchers.IO) {
        recipe?.let {
            firestore.collection(COLLECTION_RECIPES)
                .document(recipeDocId ?: "")
                .delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        recipeDocId = null
                        isSavedData.postValue(false)
                    } else {
                        task.exception?.printStackTrace()
                    }
                }
        }
    }

    private fun checkRecipeIsSaved() = viewModelScope.launch(Dispatchers.IO) {
        recipe?.let {
            firestore.collection(COLLECTION_RECIPES)
                .whereEqualTo(FIELD_USER, auth.getCurrentUserId())
                .whereEqualTo(FIELD_NAME, recipe?.name)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result.isEmpty) {
                        recipeDocId = task.result.documents[0].id
                        isSavedData.postValue(!task.result.isEmpty)
                    } else {
                        recipeDocId = null
                        isSavedData.postValue(false)
                    }
                }
        }
    }

}
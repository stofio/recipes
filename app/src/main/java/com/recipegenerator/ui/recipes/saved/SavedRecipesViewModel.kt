package com.recipegenerator.ui.recipes.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hadilq.liveevent.LiveEvent
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.domain.extensions.getCurrentUserId
import com.recipegenerator.domain.util.COLLECTION_RECIPES
import com.recipegenerator.domain.util.FIELD_USER
import com.recipegenerator.ui.common.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRecipesViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    val recipesData = LiveEvent<ViewModelState<List<Recipe>>>()

    fun loadSavedRecipes() = viewModelScope.launch(Dispatchers.IO) {
        recipesData.postValue(ViewModelState.loading())
        try {
            firestore.collection(COLLECTION_RECIPES)
                .whereEqualTo(FIELD_USER, auth.getCurrentUserId())
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val recipes = task.result.documents.mapNotNull {
                            it.toObject(Recipe::class.java)
                        }
                        recipesData.postValue(ViewModelState.success(recipes))
                    } else {
                        task.exception?.printStackTrace()
                        recipesData.postValue(
                            ViewModelState.error(
                                task.exception?.message ?: "",
                                task.exception
                            )
                        )
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            recipesData.postValue(ViewModelState.error(e.message ?: "", e))
        }
    }

}
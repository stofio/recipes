package com.recipegenerator.domain.extensions

import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.auth.FirebaseAuth
import com.recipegenerator.R
import com.recipegenerator.domain.util.DEFAULT_USER_ID
import com.recipegenerator.domain.util.TAG
import kotlin.reflect.full.memberProperties

fun Any.log(message: Any?) =
    Log.d(TAG, message.toString())

inline fun <reified T : Any> T.toMap(): MutableMap<String, Any?> {
    val props = T::class.memberProperties.associateBy { it.name }
    return props.keys.associateWith { props[it]?.get(this) }.toMutableMap()
}

fun FirebaseAuth.getCurrentUserId() =
    this.currentUser?.uid ?: DEFAULT_USER_ID

fun FlexboxLayout.setIngredients(ingredients: List<String>) {
    this.removeAllViews()
    ingredients.forEach { ingredient ->
        val inflater = LayoutInflater.from(this.context)
        val view =
            inflater.inflate(R.layout.cell_ingredient_simple, this, false)
        val nameView = view.findViewById<TextView>(R.id.name_view)
        nameView.text = ingredient
        this.addView(view)
    }

}
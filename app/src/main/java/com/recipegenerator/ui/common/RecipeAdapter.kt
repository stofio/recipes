package com.recipegenerator.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.databinding.CellRecipeBinding
import com.recipegenerator.domain.extensions.setIngredients

class RecipeAdapter(
    private val onClickListener: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    var recipes: List<Recipe> = emptyList()
        set(value) {
            field = value
            @Suppress("NotifyDataSetChanged")
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeViewHolder {
        val binding =
            CellRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        recipes[position].let { holder.bind(it) }
    }

    override fun getItemCount() = recipes.size

    inner class RecipeViewHolder(private val binding: CellRecipeBinding) :
        ViewHolder(binding.root) {

        fun bind(recipe: Recipe) = with(binding) {
            nameView.text = recipe.name
            ingredientsContainer.setIngredients(recipe.ingredients)
            toolsView.text = recipe.tools.joinToString()
            difficultyView.text = recipe.difficulty
            timeView.text = recipe.time
            itemView.setOnClickListener {
                onClickListener.invoke(recipe)
            }
        }

    }

}
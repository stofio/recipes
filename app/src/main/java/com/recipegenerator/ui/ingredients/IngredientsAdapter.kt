package com.recipegenerator.ui.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.recipegenerator.databinding.CellIngredientRemoveBinding

class IngredientsAdapter(
    private val removeListener: ((String) -> Unit) = {},
) : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    var ingredients: List<String> = emptyList()
        set(value) {
            field = value
            @Suppress("NotifyDataSetChanged") notifyDataSetChanged()
        }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): IngredientsAdapter.IngredientsViewHolder {
        val binding =
            CellIngredientRemoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientsAdapter.IngredientsViewHolder, position: Int) {
        ingredients[position].let { holder.bind(it) }
    }

    override fun getItemCount() = ingredients.size

    inner class IngredientsViewHolder(
        private val binding: CellIngredientRemoveBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String) = with(binding) {
            nameView.text = name
            removeBtn.setOnClickListener {
                removeListener.invoke(name)
            }
        }

    }

}
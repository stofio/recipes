package com.recipegenerator.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.recipegenerator.R
import com.recipegenerator.databinding.FragmentIngredientsBinding
import com.recipegenerator.domain.extensions.hideKeyboard
import com.recipegenerator.ui.common.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<IngredientsViewModel>()
    private val adapter = IngredientsAdapter(::onRemoveIngredientClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initViewModel()
    }

    private fun initViews() = with(binding) {
        ingredientsList.layoutManager = FlexboxLayoutManager(requireContext()).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
            alignItems = AlignItems.FLEX_START
        }
        val spacing = resources.getDimensionPixelSize(R.dimen.ingredients_divider_size)
        ingredientsList.addItemDecoration(SpacingItemDecoration(spacing))
        ingredientsList.adapter = adapter

        val autocompleteAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            viewModel.autocompleteIngredients
        )
        inputView.setAdapter(autocompleteAdapter)

        addBtn.setOnClickListener {
            onAddIngredientClicked()
        }
        generateBtn.setOnClickListener {
            onGenerateRecipesClicked()
        }
    }

    private fun initViewModel() {
        viewModel.fillAutocompleteIngredients(requireContext())
        viewModel.ingredientsData.observe(viewLifecycleOwner) { ingredients ->
            with(binding) {
                val recycler = ingredientsList.Recycler().apply {
                    setViewCacheSize(0)
                }
                ingredientsList.layoutManager?.removeAndRecycleAllViews(recycler)
                adapter.ingredients = ingredients
                emptyView.isVisible = ingredients.isEmpty()
            }
        }
    }

    private fun onAddIngredientClicked() = with(binding) {
        val name = inputView.text.toString().trim().lowercase()
        if (name.isNotEmpty()) {
            inputView.text = null
            viewModel.addIngredient(name)
        }
    }

    private fun onRemoveIngredientClicked(name: String) {
        viewModel.removeIngredient(name)
    }

    private fun onGenerateRecipesClicked() {
        if (viewModel.ingredients.isNotEmpty()) {
            hideKeyboard()
            val action =
                IngredientsFragmentDirections.actionNavigationIngredientsToRecipeListFragment(
                    viewModel.ingredients.joinToString()
                )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
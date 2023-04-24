package com.recipegenerator.ui.recipes.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.recipegenerator.R
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.databinding.FragmentSavedRecipesBinding
import com.recipegenerator.ui.common.RecipeAdapter
import com.recipegenerator.ui.common.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedRecipesFragment : Fragment() {

    private var _binding: FragmentSavedRecipesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SavedRecipesViewModel>()
    private val adapter = RecipeAdapter(::onRecipeClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initViewModel()
        viewModel.loadSavedRecipes()
    }

    private fun initViews() = with(binding) {
        recipeList.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.recipesData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> showRecipes(it.data)
                Status.ERROR -> showError(it.message)
            }
        }
    }

    private fun onRecipeClicked(recipe: Recipe) {
        val action =
            SavedRecipesFragmentDirections.actionSavedRecipesFragmentToRecipeDetails(recipe)
        findNavController().navigate(action)
    }

    private fun showProgress() = with(binding) {
        progressView.visibility = View.VISIBLE
        recipeList.visibility = View.INVISIBLE
        resultView.visibility = View.INVISIBLE
    }

    private fun showRecipes(recipes: List<Recipe>?) = with(binding) {
        progressView.visibility = View.INVISIBLE
        adapter.recipes = recipes ?: emptyList()
        if (recipes?.isNotEmpty() == true) {
            recipeList.visibility = View.VISIBLE
            resultView.visibility = View.INVISIBLE
        } else {
            recipeList.visibility = View.INVISIBLE
            resultView.visibility = View.VISIBLE
            resultView.text = getString(R.string.saved_recipes_empty)
        }
    }

    private fun showError(error: String) = with(binding) {
        progressView.visibility = View.INVISIBLE
        recipeList.visibility = View.INVISIBLE
        resultView.visibility = View.VISIBLE
        resultView.text = error
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
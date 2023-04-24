package com.recipegenerator.ui.recipes.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.recipegenerator.R
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.databinding.FragmentRecipeListBinding
import com.recipegenerator.ui.common.RecipeAdapter
import com.recipegenerator.ui.common.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RecipeListViewModel>()
    private val adapter = RecipeAdapter(::onRecipeClicked)
    private val args by navArgs<RecipeListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initViewModel()
        if (!viewModel.isRecipesLoaded()) {
            viewModel.loadRecipes(args.ingredients)
        } else {
            showRecipes(viewModel.recipesData.value?.data)
        }
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
            RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailsFragment(recipe)
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
            resultView.text = getString(R.string.recipe_list_empty)
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
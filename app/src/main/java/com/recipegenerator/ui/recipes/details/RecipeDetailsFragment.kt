package com.recipegenerator.ui.recipes.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.recipegenerator.R
import com.recipegenerator.data.model.Recipe
import com.recipegenerator.databinding.FragmentRecipeDetailsBinding
import com.recipegenerator.domain.extensions.confirmDialog
import com.recipegenerator.domain.extensions.setIngredients
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RecipeDetailsViewModel>()
    private val args by navArgs<RecipeDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        initViews()
        initViewModel()
        viewModel.recipe = args.recipe
    }

    private fun initViews() = with(binding) {
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        saveBtn.setOnClickListener {
            onSaveRecipeClicked()
        }
    }

    private fun initViewModel() {
        viewModel.recipeData.observe(viewLifecycleOwner) { updateViews(it) }
        viewModel.isSavedData.observe(viewLifecycleOwner) {
            showProgress(false)
            updateSaveButton(it)
        }
    }

    private fun updateViews(recipe: Recipe?) = with(binding) {
        recipe?.let {
            nameView.text = recipe.name
            ingredientsContainer.setIngredients(recipe.ingredients)
            toolsView.text = recipe.tools.joinToString()
            difficultyView.text = recipe.difficulty
            timeView.text = recipe.time
            instructionsView.text = recipe.instructions
                .mapIndexed { index, s -> "${index + 1}. $s" }
                .joinToString(separator = "\n")
        }
    }

    private fun updateSaveButton(isSaved: Boolean) = with(binding) {
        if (isSaved) {
            saveBtn.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_remove_background)
            saveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            saveBtn.text = getString(R.string.recipe_details_remove)
        } else {
            saveBtn.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_save_background)
            saveBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            saveBtn.text = getString(R.string.recipe_details_save)
        }
    }

    private fun onSaveRecipeClicked() {
        if (viewModel.isSavedData.value != true) {
            viewModel.saveRecipe()
            showProgress(true)
        } else {
            confirmDialog(
                title = getString(R.string.recipe_details_remove_confirm_title),
                message = getString(R.string.recipe_details_remove_confirm_message)
            ) {
                viewModel.removeRecipe()
                showProgress(true)
            }
        }
    }

    private fun showProgress(show: Boolean) = with(binding) {
        saveBtn.visibility = if (show) View.INVISIBLE else View.VISIBLE
        saveProgress.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
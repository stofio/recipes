package com.recipegenerator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.recipegenerator.databinding.ActivityMainBinding
import com.recipegenerator.ui.ingredients.IngredientsFragment
import com.recipegenerator.ui.recipes.list.RecipeListFragment
import com.recipegenerator.ui.recipes.saved.SavedRecipesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupNavigation() = with(binding) {
        navView.setOnItemSelectedListener { item ->
            if (navView.selectedItemId != item.itemId) {
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                when (item.itemId) {
                    R.id.navigation_ingredients -> showFragment(IngredientsFragment())
                    R.id.navigation_recipe_list -> showFragment(RecipeListFragment())
                    R.id.navigation_saved -> showFragment(SavedRecipesFragment())
                }
            }
            true
        }
        showFragment(IngredientsFragment())
    }

    fun selectTab(id: Int) {
        binding.navView.selectedItemId = id
    }

    fun showFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false,
        enterAnim: Int = 0,
        exitAnim: Int = 0,
        popEnterAnim: Int = 0,
        popExitAnim: Int = 0,
    ) {
        supportFragmentManager.commit {
            setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
            replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                addToBackStack(fragment::class.qualifiedName)
            }
        }
    }

}
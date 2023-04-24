package com.recipegenerator.domain.util

// Common
const val TAG = "RecipeGenerator"
const val BASE_URL = "https://api.openai.com/v1/"
const val SPLASH_TIME = 2000L
const val AUTOCOMPLETE_INGREDIENTS_FILENAME = "ingredients.csv"

// Settings
const val KEY_LAST_RECIPES = "key_last_recipes"

// Firebase
const val COLLECTION_RECIPES = "recipes"
const val DEFAULT_USER_ID = "default"
const val FIELD_USER = "user"
const val FIELD_NAME = "name"
const val FIELD_TIME = "time"
const val FIELD_DIFFICULTY = "difficulty"
const val FIELD_INGREDIENTS = "ingredients"
const val FIELD_TOOLS = "tools"
const val FIELD_INSTRUCTIONS = "instructions"

// OpenAI
const val OPENAI_API_KEY = "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"
const val OPENAI_MODEL = "text-davinci-003"
const val OPENAI_PROMPT =
    "Give me list of recipes in JSON format with \"$FIELD_NAME\", \"$FIELD_TIME\", \"$FIELD_DIFFICULTY\", \"$FIELD_INGREDIENTS\", \"$FIELD_INSTRUCTIONS\" and necessary kitchen \"$FIELD_TOOLS\" using any of the following ingredients: %s. \"$FIELD_INGREDIENTS\", \"$FIELD_INSTRUCTIONS\" and \"$FIELD_TOOLS\" should be lists of strings. \"$FIELD_INGREDIENTS\" and \"$FIELD_TOOLS\" should be in short names."
const val OPENAI_MAX_TOKENS = 4000
const val OPENAI_TEMPERATURE = 0
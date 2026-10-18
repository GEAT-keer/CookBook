package com.example.cookbook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RecipeDetailActivity"
        const val EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID"
    }

    private lateinit var recipe: Recipe
    private var servings = 1

    private lateinit var tvServings: TextView
    private lateinit var ingredientsContainer: LinearLayout
    private lateinit var ivFavorite: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val id = intent.getIntExtra(EXTRA_RECIPE_ID, -1)
        Log.d(TAG, "onCreate: получен id рецепта = $id")

        val found = RecipeData.getById(id)
        if (found == null) {
            Log.e(TAG, "Рецепт с id=$id не найден, закрываем экран")
            finish()
            return
        }
        recipe = found
        servings = recipe.servings

        tvServings = findViewById(R.id.tvServings)
        ingredientsContainer = findViewById(R.id.ingredientsContainer)
        ivFavorite = findViewById(R.id.ivFavorite)

        findViewById<ImageView>(R.id.ivBanner).setImageResource(RecipeData.getPhoto(recipe.id))
        findViewById<TextView>(R.id.tvRecipeInfo).text =
            getString(R.string.item_info, recipe.minutes, recipe.category)
        findViewById<TextView>(R.id.tvDescription).text = recipe.description
        findViewById<TextView>(R.id.tvTip).text = recipe.tip

        showSteps()
        showIngredients()
        updateFavoriteIcon()

        findViewById<View>(R.id.ivBack).setOnClickListener {
            Log.i(TAG, "Нажата стрелка Назад")
            finish()
        }

        findViewById<View>(R.id.btnMinus).setOnClickListener {
            if (servings > 1) {
                servings--
                Log.i(TAG, "Порций стало: $servings")
                showIngredients()
            }
        }
        findViewById<View>(R.id.btnPlus).setOnClickListener {
            if (servings < 20) {
                servings++
                Log.i(TAG, "Порций стало: $servings")
                showIngredients()
            }
        }

        ivFavorite.setOnClickListener {
            val nowFavorite = Favorites.toggle(this, recipe.id)
            updateFavoriteIcon()
            if (nowFavorite) {
                Log.i(TAG, "Добавлено в избранное: ${recipe.title}")
                Toast.makeText(this, R.string.msg_fav_added, Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Убрано из избранного: ${recipe.title}")
                Toast.makeText(this, R.string.msg_fav_removed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showIngredients() {
        ingredientsContainer.removeAllViews()
        val factor = servings.toDouble() / recipe.servings

        for (ingredient in recipe.ingredients) {
            val row = LayoutInflater.from(this)
                .inflate(R.layout.item_ingredient, ingredientsContainer, false)
            val amount = ingredient.amount * factor

            row.findViewById<TextView>(R.id.tvIngName).text =
                ingredient.name.replaceFirstChar { it.uppercase() }
            row.findViewById<TextView>(R.id.tvIngAmount).text =
                formatAmount(amount) + " " + ingredient.unit

            ingredientsContainer.addView(row)
        }
        tvServings.text = servings.toString()
    }

    private fun showSteps() {
        val stepsContainer = findViewById<LinearLayout>(R.id.stepsContainer)
        var number = 1
        for (step in recipe.steps) {
            val row = LayoutInflater.from(this)
                .inflate(R.layout.item_step, stepsContainer, false)
            row.findViewById<TextView>(R.id.tvStepNumber).text = number.toString()
            row.findViewById<TextView>(R.id.tvStepText).text = step
            stepsContainer.addView(row)
            number++
        }
    }

    private fun formatAmount(amount: Double): String {
        val rounded = Math.round(amount * 10) / 10.0
        return if (rounded == rounded.toInt().toDouble()) {
            rounded.toInt().toString()
        } else {
            rounded.toString()
        }
    }

    private fun updateFavoriteIcon() {
        if (Favorites.isFavorite(this, recipe.id)) {
            ivFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            ivFavorite.setImageResource(R.drawable.ic_heart_outline)
        }
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}
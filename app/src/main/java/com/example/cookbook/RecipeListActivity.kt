package com.example.cookbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeListActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RecipeListActivity"
        const val EXTRA_MODE = "EXTRA_MODE"
        const val EXTRA_VALUE = "EXTRA_VALUE"
        const val MODE_ALL = "all"
        const val MODE_CATEGORY = "category"
        const val MODE_SEARCH = "search"
        const val MODE_FAVORITES = "favorites"
    }

    private lateinit var lvRecipes: ListView
    private lateinit var tvListTitle: TextView
    private lateinit var tvCount: TextView
    private lateinit var tvEmpty: TextView
    private lateinit var chipAny: TextView
    private lateinit var chip30: TextView
    private lateinit var chip60: TextView

    private var mode = MODE_ALL
    private var value = ""
    private var maxMinutes = 0
    private var recipes: List<Recipe> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        mode = intent.getStringExtra(EXTRA_MODE) ?: MODE_ALL
        value = intent.getStringExtra(EXTRA_VALUE) ?: ""
        Log.d(TAG, "onCreate: режим=$mode, значение=$value")

        lvRecipes = findViewById(R.id.lvRecipes)
        tvListTitle = findViewById(R.id.tvListTitle)
        tvCount = findViewById(R.id.tvCount)
        tvEmpty = findViewById(R.id.tvEmpty)
        chipAny = findViewById(R.id.chipAny)
        chip30 = findViewById(R.id.chip30)
        chip60 = findViewById(R.id.chip60)

        findViewById<View>(R.id.ivBack).setOnClickListener {
            Log.i(TAG, "Нажата стрелка Назад")
            finish()
        }

        chipAny.setOnClickListener { setTimeFilter(0) }
        chip30.setOnClickListener { setTimeFilter(30) }
        chip60.setOnClickListener { setTimeFilter(60) }
        updateChips()

        lvRecipes.setOnItemClickListener { _, _, position, _ ->
            val recipe = recipes[position]
            Log.i(TAG, "Выбран рецепт: ${recipe.title}")
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.id)
            startActivity(intent)
        }
    }

    private fun setTimeFilter(minutes: Int) {
        maxMinutes = minutes
        Log.i(TAG, "Фильтр по времени: $minutes (0 = любое)")
        updateChips()
        loadRecipes()
    }

    private fun updateChips() {
        styleChip(chipAny, maxMinutes == 0)
        styleChip(chip30, maxMinutes == 30)
        styleChip(chip60, maxMinutes == 60)
    }

    private fun styleChip(chip: TextView, selected: Boolean) {
        if (selected) {
            chip.setBackgroundResource(R.drawable.bg_segment_on)
            chip.setTextColor(getColor(R.color.brown))
        } else {
            chip.setBackgroundResource(R.drawable.bg_segment_off)
            chip.setTextColor(getColor(R.color.muted))
        }
    }
    private fun loadRecipes() {
        val source: List<Recipe>
        when (mode) {
            MODE_CATEGORY -> {
                source = RecipeData.getByCategory(value)
                tvListTitle.text = value
            }
            MODE_SEARCH -> {
                source = RecipeData.search(value)
                tvListTitle.text = getString(R.string.title_search, value)
            }
            MODE_FAVORITES -> {
                source = RecipeData.getFavorites(this)
                tvListTitle.setText(R.string.favorites)
            }
            else -> {
                source = RecipeData.all
                tvListTitle.setText(R.string.all_recipes)
            }
        }

        val filtered = ArrayList<Recipe>()
        for (recipe in source) {
            if (maxMinutes == 0 || recipe.minutes <= maxMinutes) {
                filtered.add(recipe)
            }
        }
        recipes = filtered

        lvRecipes.adapter = RecipeAdapter(this, recipes)
        tvCount.text = getString(R.string.list_count, recipes.size)

        if (recipes.isEmpty()) {
            Log.w(TAG, "Список пуст")
            tvEmpty.visibility = View.VISIBLE
        } else {
            tvEmpty.visibility = View.GONE
        }
        Log.d(TAG, "Показано рецептов: ${recipes.size}")
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        loadRecipes()
    }

    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}
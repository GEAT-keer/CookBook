package com.example.cookbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var etSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate: экран создан")

        etSearch = findViewById(R.id.etSearch)

        findViewById<View>(R.id.btnSearch).setOnClickListener {
            doSearch()
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
                true
            } else {
                false
            }
        }

        findViewById<View>(R.id.cardSoups).setOnClickListener {
            Log.i(TAG, "Нажата категория: супы")
            openList(RecipeListActivity.MODE_CATEGORY, RecipeData.CAT_SOUPS)
        }
        findViewById<View>(R.id.cardMain).setOnClickListener {
            Log.i(TAG, "Нажата категория: основные блюда")
            openList(RecipeListActivity.MODE_CATEGORY, RecipeData.CAT_MAIN)
        }
        findViewById<View>(R.id.cardDesserts).setOnClickListener {
            Log.i(TAG, "Нажата категория: десерты")
            openList(RecipeListActivity.MODE_CATEGORY, RecipeData.CAT_DESSERTS)
        }
        findViewById<View>(R.id.cardDrinks).setOnClickListener {
            Log.i(TAG, "Нажата категория: напитки")
            openList(RecipeListActivity.MODE_CATEGORY, RecipeData.CAT_DRINKS)
        }

        findViewById<View>(R.id.tvAll).setOnClickListener {
            Log.i(TAG, "Нажата ссылка: все рецепты")
            openList(RecipeListActivity.MODE_ALL, "")
        }

        findViewById<View>(R.id.cardFavorites).setOnClickListener {
            Log.i(TAG, "Нажата карточка: избранное")
            openList(RecipeListActivity.MODE_FAVORITES, "")
        }

        findViewById<View>(R.id.cardRandom).setOnClickListener {
            val index = Random.nextInt(RecipeData.all.size)
            val recipe = RecipeData.all[index]
            Log.i(TAG, "Случайный рецепт: ${recipe.title}")
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.id)
            startActivity(intent)
        }

        findViewById<View>(R.id.ivAbout).setOnClickListener {
            Log.i(TAG, "Открываем экран О приложении")
            val intent = Intent(this, AboutActivity::class.java)
            intent.putExtra(AboutActivity.EXTRA_COUNT, RecipeData.all.size)
            startActivity(intent)
        }
    }

    private fun doSearch() {
        val query = etSearch.text.toString().trim()
        if (query.isEmpty()) {
            Log.w(TAG, "Поиск: поле ввода пустое")
            Toast.makeText(this, R.string.msg_empty_search, Toast.LENGTH_SHORT).show()
        } else {
            Log.i(TAG, "Поиск: $query")
            openList(RecipeListActivity.MODE_SEARCH, query)
        }
    }

    private fun openList(mode: String, value: String) {
        val intent = Intent(this, RecipeListActivity::class.java)
        intent.putExtra(RecipeListActivity.EXTRA_MODE, mode)
        intent.putExtra(RecipeListActivity.EXTRA_VALUE, value)
        startActivity(intent)
    }

    override fun onStart() { super.onStart(); Log.d(TAG, "onStart") }
    override fun onResume() { super.onResume(); Log.d(TAG, "onResume") }
    override fun onPause() { super.onPause(); Log.d(TAG, "onPause") }
    override fun onStop() { super.onStop(); Log.d(TAG, "onStop") }
    override fun onDestroy() { super.onDestroy(); Log.d(TAG, "onDestroy") }
}
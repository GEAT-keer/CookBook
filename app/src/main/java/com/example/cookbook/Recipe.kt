package com.example.cookbook

data class Ingredient(val name: String, val amount: Double, val unit: String)

data class Recipe(
    val id: Int,
    val title: String,
    val category: String,
    val minutes: Int,
    val servings: Int,
    val description: String,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val tip: String
)
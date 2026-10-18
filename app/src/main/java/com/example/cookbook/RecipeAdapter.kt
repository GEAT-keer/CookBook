package com.example.cookbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class RecipeAdapter(context: Context, private val recipes: List<Recipe>) :
    ArrayAdapter<Recipe>(context, 0, recipes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false)
        val recipe = recipes[position]

        view.findViewById<ImageView>(R.id.ivDish).setImageResource(RecipeData.getPhoto(recipe.id))
        view.findViewById<TextView>(R.id.tvInfo).text =
            context.getString(R.string.item_info, recipe.minutes, recipe.category)

        val ivFav = view.findViewById<ImageView>(R.id.ivFav)
        if (Favorites.isFavorite(context, recipe.id)) {
            ivFav.visibility = View.VISIBLE
        } else {
            ivFav.visibility = View.GONE
        }

        return view
    }
}
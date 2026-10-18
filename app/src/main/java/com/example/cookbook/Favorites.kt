package com.example.cookbook

import android.content.Context

object Favorites {

    private const val PREFS_NAME = "cookbook_prefs"
    private const val KEY_IDS = "favorite_ids"

    fun getIds(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_IDS, emptySet()) ?: emptySet()
    }

    fun isFavorite(context: Context, id: Int): Boolean {
        return getIds(context).contains(id.toString())
    }

    fun toggle(context: Context, id: Int): Boolean {
        val ids = getIds(context).toMutableSet()
        val nowFavorite: Boolean
        if (ids.contains(id.toString())) {
            ids.remove(id.toString())
            nowFavorite = false
        } else {
            ids.add(id.toString())
            nowFavorite = true
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(KEY_IDS, ids).apply()
        return nowFavorite
    }
}
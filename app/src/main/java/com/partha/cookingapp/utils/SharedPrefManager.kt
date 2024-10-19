package com.partha.cookingapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.partha.cookingapp.pojos.Dish

class SharedPrefManager(context: Context) {

    companion object {
        private const val PREF_NAME = "cooking_app_prefs"
        private const val DISH_KEY = "dish_key"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveDish(dish: Dish) {
        val dishJson = gson.toJson(dish)
        sharedPreferences.edit().putString(DISH_KEY, dishJson).apply()
    }

    fun getDish(): Dish? {
        val dishJson = sharedPreferences.getString(DISH_KEY, null)
        return if (dishJson != null) {
            gson.fromJson(dishJson, Dish::class.java)
        } else {
            null
        }
    }

    fun removeDish() {
        sharedPreferences.edit().remove(DISH_KEY).apply()
    }
}

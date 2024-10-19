package com.partha.cookingapp.pojos

import com.google.gson.annotations.SerializedName

data class Dish(

	@field:SerializedName("isPublished")
	val isPublished: Boolean? = null,

	@field:SerializedName("dishId")
	val dishId: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("dishName")
	val dishName: String? = null,

	var scheduleTime: String? = null
)

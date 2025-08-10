// network/models/CategoryResponse.kt
package com.example.quizapp.network.models

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("_id")
    val _id: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("icon")
    val icon: String?,

    @SerializedName("questionCount")
    val questionCount: Int = 0,

    @SerializedName("isActive")
    val isActive: Boolean = true,

    @SerializedName("order")
    val order: Int = 0,

    @SerializedName("createdAt")
    val createdAt: String?,

    @SerializedName("updatedAt")
    val updatedAt: String?
)
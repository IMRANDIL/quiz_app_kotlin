package com.example.quizapp.network.models

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.parcelize.RawValue
import com.example.quizapp.R
import com.example.quizapp.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: String = "",
    val name: String,
    val iconRes: @RawValue Any? = null, // Can be Int (drawable resource) or String (URL)
    val description: String? = null,
    val questionCount: Int = 0
) : Parcelable {
    // Constructor for backward compatibility
    constructor(name: String, iconRes: Int) : this(
        id = name.lowercase().replace(" ", "_"),
        name = name,
        iconRes = iconRes,
        description = null,
        questionCount = 0
    )
}

class CategoryViewModel(
    private val repository: QuizRepository = QuizRepository()
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    // Mapping of category names to icons and descriptions
    private val categoryIconMap = mapOf(
        "science" to Pair(R.drawable.cat1, "Test your knowledge of physics, chemistry, and biology"),
        "history" to Pair(R.drawable.cat2, "Explore events and people from the past"),
        "sport" to Pair(R.drawable.cat3, "Questions about various sports and athletes"),
        "sports" to Pair(R.drawable.cat3, "Questions about various sports and athletes"), // Alternative spelling
        "art" to Pair(R.drawable.cat4, "Art history, famous artists, and techniques"),
        "geography" to Pair(R.drawable.ic_launcher_background, "Countries, capitals, and landmarks"),
        "music" to Pair(R.drawable.ic_launcher_background, "Music theory, artists, and genres"),
        "technology" to Pair(R.drawable.ic_launcher_background, "Computers, internet, and innovations"),
        "movies" to Pair(R.drawable.ic_launcher_background, "Films, actors, and cinema history"),
        "literature" to Pair(R.drawable.ic_launcher_background, "Books, authors, and literary classics"),
        "mathematics" to Pair(R.drawable.ic_launcher_background, "Numbers, equations, and problem solving"),
        "math" to Pair(R.drawable.ic_launcher_background, "Numbers, equations, and problem solving"),
        "biology" to Pair(R.drawable.ic_launcher_background, "Living organisms and life sciences"),
        "chemistry" to Pair(R.drawable.ic_launcher_background, "Elements, compounds, and reactions"),
        "physics" to Pair(R.drawable.ic_launcher_background, "Matter, energy, and natural phenomena"),
        "general knowledge" to Pair(R.drawable.ic_launcher_background, "Test your general knowledge"),
        "entertainment" to Pair(R.drawable.ic_launcher_background, "Movies, TV shows, and celebrities"),
        "food" to Pair(R.drawable.ic_launcher_background, "Cuisine, recipes, and culinary arts")
    )

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            repository.getAllCategories()
                .onSuccess { categoriesFromApi ->
                    // Map the string list from API to Category objects
                    _categories.value = categoriesFromApi.map { category ->
                        val lowerName = category.name.lowercase()
                        val (iconRes, description) = categoryIconMap[lowerName]
                            ?: Pair(R.drawable.ic_launcher_background, "Test your knowledge in ${category.name}")

                        Category(
                            id = category.id,
                            name = category.name,
                            iconRes = iconRes,
                            description = description,
                            questionCount = (20..50).random() // Random count for now, can be fetched from API
                        )
                    }
                }
                .onFailure {
                    // On failure, use default categories
                    _categories.value = getDefaultCategories()
                }
        }
    }

    fun getAllCategories(): List<Category> {
        // Return all categories (from API or default)
        return if (_categories.value.isNotEmpty()) {
            _categories.value
        } else {
            getDefaultCategories()
        }
    }

    private fun getDefaultCategories(): List<Category> {
        // Fallback categories if API fails
        return listOf(
            Category(
                id = "1",
                name = "Science",
                iconRes = R.drawable.cat1,
                description = "Test your knowledge of physics, chemistry, and biology",
                questionCount = 45
            ),
            Category(
                id = "2",
                name = "History",
                iconRes = R.drawable.cat2,
                description = "Explore events and people from the past",
                questionCount = 38
            ),
            Category(
                id = "3",
                name = "Sport",
                iconRes = R.drawable.cat3,
                description = "Questions about various sports and athletes",
                questionCount = 52
            ),
            Category(
                id = "4",
                name = "Art",
                iconRes = R.drawable.cat4,
                description = "Art history, famous artists, and techniques",
                questionCount = 30
            ),
            Category(
                id = "5",
                name = "Geography",
                iconRes = R.drawable.ic_launcher_background,
                description = "Countries, capitals, and landmarks",
                questionCount = 42
            ),
            Category(
                id = "6",
                name = "Music",
                iconRes = R.drawable.ic_launcher_background,
                description = "Music theory, artists, and genres",
                questionCount = 35
            ),
            Category(
                id = "7",
                name = "Technology",
                iconRes = R.drawable.ic_launcher_background,
                description = "Computers, internet, and innovations",
                questionCount = 48
            ),
            Category(
                id = "8",
                name = "Movies",
                iconRes = R.drawable.ic_launcher_background,
                description = "Films, actors, and cinema history",
                questionCount = 55
            ),
            Category(
                id = "9",
                name = "Literature",
                iconRes = R.drawable.ic_launcher_background,
                description = "Books, authors, and literary classics",
                questionCount = 33
            ),
            Category(
                id = "10",
                name = "Mathematics",
                iconRes = R.drawable.ic_launcher_background,
                description = "Numbers, equations, and problem solving",
                questionCount = 40
            ),
            Category(
                id = "11",
                name = "Biology",
                iconRes = R.drawable.ic_launcher_background,
                description = "Living organisms and life sciences",
                questionCount = 37
            ),
            Category(
                id = "12",
                name = "Chemistry",
                iconRes = R.drawable.ic_launcher_background,
                description = "Elements, compounds, and reactions",
                questionCount = 28
            )
        )
    }

    fun getCategoryById(id: String): Category? {
        return getAllCategories().find { it.id == id }
    }

    fun getCategoryByName(name: String): Category? {
        return getAllCategories().find { it.name.equals(name, ignoreCase = true) }
    }
}
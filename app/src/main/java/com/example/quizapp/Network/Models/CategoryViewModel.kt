package com.example.quizapp.Network.Models



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Category(
    val id: String,
    val name: String,
    val iconRes: Int // Or String if coming from server
)

class CategoryViewModel(
    private val repository: QuizRepository = QuizRepository()
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            repository.getAllCategories()
                .onSuccess { list -> _categories.value = list }
                .onFailure { /* handle error */ }
        }
    }
}

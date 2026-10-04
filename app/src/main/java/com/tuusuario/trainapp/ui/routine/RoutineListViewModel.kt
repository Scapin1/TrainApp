package com.tuusuario.trainapp.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.trainapp.di.AppContainer
import com.tuusuario.trainapp.domain.model.Routine
import com.tuusuario.trainapp.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoutineListViewModel(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _routines = MutableStateFlow<List<Routine>>(emptyList())
    val routines: StateFlow<List<Routine>> = _routines

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadRoutines()
    }

    fun loadRoutines() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            routineRepository.getAll().collect { list ->
                _routines.value = list
                _isLoading.value = false
            }
        }
    }

    fun deleteRoutine(routineId: Long) {
        viewModelScope.launch {
            routineRepository.delete(routineId)
        }
    }

    fun duplicateRoutine(routineId: Long) {
        viewModelScope.launch {
            routineRepository.duplicate(routineId)
        }
    }
}

// Factory for Hilt
class RoutineListViewModelFactory(
    private val container: AppContainer
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoutineListViewModel(container.routineRepository) as T
    }
}
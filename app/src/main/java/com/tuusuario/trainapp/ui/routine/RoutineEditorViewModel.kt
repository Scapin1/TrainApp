package com.tuusuario.trainapp.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.trainapp.domain.model.Routine
import com.tuusuario.trainapp.domain.model.RoutineDay
import com.tuusuario.trainapp.domain.model.RoutineWithDays
import com.tuusuario.trainapp.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoutineEditorViewModel(
    private val routineRepository: RoutineRepository,
    private val routineId: String
) : ViewModel() {

    private val _routine = MutableStateFlow<RoutineWithDays?>(null)
    val routine: StateFlow<RoutineWithDays?> = _routine

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val isNewRoutine: Boolean = routineId == "new"

    init {
        loadRoutine()
    }

    private fun loadRoutine() {
        if (isNewRoutine) {
            _routine.value = RoutineWithDays(
                routine = Routine(name = "", dayCount = 0),
                days = emptyList()
            )
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            routineRepository.getWithDays(routineId.toLong()).let { rwd ->
                _routine.value = rwd
                _isLoading.value = false
            }
        }
    }

    fun saveRoutine(name: String, description: String = "") {
        viewModelScope.launch {
            val current = _routine.value ?: return@launch
            val now = System.currentTimeMillis()
            val routine = current.routine.copy(
                id = if (isNewRoutine) 0 else current.routine.id,
                name = name,
                description = description,
                dayCount = current.days.size,
                updatedAt = now
            )
            val savedId = if (isNewRoutine) {
                routineRepository.insert(routine)
            } else {
                routineRepository.update(routine)
                routine.id
            }
            _routine.value = current.copy(routine = routine.copy(id = savedId))
        }
    }

fun addDay(name: String) {
        viewModelScope.launch {
            val current = _routine.value ?: return@launch
            val newDay = RoutineDay(
                routineId = current.routine.id,
                dayIndex = current.days.size,
                name = name,
                blockOrder = emptyList()
            )
            // TODO: Implement day insertion via repository
            // For now, just update local state
            _routine.value = current.copy(days = current.days + newDay)
        }
    }

    fun reorderDays(dayIds: List<Long>) {
        viewModelScope.launch {
            val routineId = _routine.value?.routine?.id ?: return@launch
            routineRepository.reorderDays(routineId, dayIds)
        }
    }
}

class RoutineEditorViewModelFactory(
    private val container: com.tuusuario.trainapp.di.AppContainer,
    private val routineId: String
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoutineEditorViewModel(container.routineRepository, routineId) as T
    }
}
package com.tuusuario.trainapp.data.mapper

import com.tuusuario.trainapp.data.db.entities.Workout
import com.tuusuario.trainapp.data.db.entities.WorkoutExercise
import com.tuusuario.trainapp.data.db.entities.SetLog
import com.tuusuario.trainapp.domain.model.Workout as DomainWorkout
import com.tuusuario.trainapp.domain.model.WorkoutExercise as DomainWorkoutExercise
import com.tuusuario.trainapp.domain.model.SetLog as DomainSetLog

object WorkoutMapper {

    fun toDomain(entity: Workout): DomainWorkout {
        return DomainWorkout(
            id = entity.id,
            routineId = entity.routineId,
            routineDayIndex = entity.routineDayIndex,
            startedAt = entity.startedAt,
            endedAt = entity.endedAt,
            durationSec = entity.durationSec,
            blockSnapshotsJson = entity.blockSnapshotsJson,
            notes = entity.notes,
            globalSensationStart = entity.globalSensationStart,
            globalSensationEnd = entity.globalSensationEnd
        )
    }

    fun toEntity(domain: DomainWorkout): Workout {
        return Workout(
            id = domain.id,
            routineId = domain.routineId,
            routineDayIndex = domain.routineDayIndex,
            startedAt = domain.startedAt,
            endedAt = domain.endedAt,
            durationSec = domain.durationSec,
            blockSnapshotsJson = domain.blockSnapshotsJson,
            notes = domain.notes,
            globalSensationStart = domain.globalSensationStart,
            globalSensationEnd = domain.globalSensationEnd
        )
    }

    fun toDomain(entity: WorkoutExercise): DomainWorkoutExercise {
        return DomainWorkoutExercise(
            id = entity.id,
            workoutId = entity.workoutId,
            blockId = entity.blockId,
            templateExerciseId = entity.templateExerciseId,
            exerciseName = entity.exerciseName,
            order = entity.order,
            setsJson = entity.setsJson,
            completedAt = entity.completedAt,
            skipped = entity.skipped,
            sensation = entity.sensationJson?.let { 
                com.google.gson.Gson().fromJson(it, com.tuusuario.trainapp.domain.model.ExerciseSensation::class.java) 
            }
        )
    }

    fun toEntity(domain: DomainWorkoutExercise): WorkoutExercise {
        return WorkoutExercise(
            id = domain.id,
            workoutId = domain.workoutId,
            blockId = domain.blockId,
            templateExerciseId = domain.templateExerciseId,
            exerciseName = domain.exerciseName,
            order = domain.order,
            setsJson = domain.setsJson,
            completedAt = domain.completedAt,
            skipped = domain.skipped,
            sensationJson = domain.sensation?.let { com.google.gson.Gson().toJson(it) }
        )
    }
}
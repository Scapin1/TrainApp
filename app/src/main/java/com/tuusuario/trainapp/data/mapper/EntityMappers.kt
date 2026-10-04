package com.tuusuario.trainapp.data.mapper

import com.tuusuario.trainapp.data.db.entities.Routine
import com.tuusuario.trainapp.data.db.entities.RoutineDay
import com.tuusuario.trainapp.data.db.entities.Block
import com.tuusuario.trainapp.data.db.entities.TemplateExercise
import com.tuusuario.trainapp.data.db.entities.ExerciseCatalog
import com.tuusuario.trainapp.data.db.entities.ExerciseCategory
import com.tuusuario.trainapp.data.db.entities.Workout
import com.tuusuario.trainapp.data.db.entities.WorkoutExercise
import com.tuusuario.trainapp.data.db.entities.SetLog
import com.tuusuario.trainapp.data.db.entities.BodyWeight
import com.tuusuario.trainapp.data.db.entities.GlobalSensation
import com.tuusuario.trainapp.data.db.entities.AppSettings
import com.tuusuario.trainapp.domain.model.Routine as DomainRoutine
import com.tuusuario.trainapp.domain.model.RoutineDay as DomainRoutineDay
import com.tuusuario.trainapp.domain.model.Block as DomainBlock
import com.tuusuario.trainapp.domain.model.TemplateExercise as DomainTemplateExercise
import com.tuusuario.trainapp.domain.model.ExerciseCatalog as DomainExerciseCatalog
import com.tuusuario.trainapp.domain.model.ExerciseCategory as DomainExerciseCategory
import com.tuusuario.trainapp.domain.model.Workout as DomainWorkout
import com.tuusuario.trainapp.domain.model.WorkoutExercise as DomainWorkoutExercise
import com.tuusuario.trainapp.domain.model.SetLog as DomainSetLog
import com.tuusuario.trainapp.domain.model.BodyWeight as DomainBodyWeight
import com.tuusuario.trainapp.domain.model.GlobalSensation as DomainGlobalSensation
import com.tuusuario.trainapp.domain.model.AppSettings as DomainAppSettings
import com.tuusuario.trainapp.data.db.entities.BlockType
import com.tuusuario.trainapp.data.db.entities.CycleType
import com.tuusuario.trainapp.data.db.entities.ExercisePattern
import com.tuusuario.trainapp.data.db.entities.MuscleGroup
import com.tuusuario.trainapp.data.db.entities.Equipment
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer

object RoutineMapper {

    private val json = Json { ignoreUnknownKeys = true }

    fun toDomain(entity: Routine): DomainRoutine {
        return DomainRoutine(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            dayCount = entity.dayCount,
            cycleType = entity.cycleType,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntity(domain: DomainRoutine): Routine {
        return Routine(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            dayCount = domain.dayCount,
            cycleType = domain.cycleType,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomainDay(entity: RoutineDay): DomainRoutineDay {
        return DomainRoutineDay(
            id = entity.id,
            routineId = entity.routineId,
            dayIndex = entity.dayIndex,
            name = entity.name,
            blockOrder = json.decodeFromString(serializer<List<Long>>(), entity.blockOrderJson)
        )
    }

    fun toEntityDay(domain: DomainRoutineDay): RoutineDay {
        return RoutineDay(
            id = domain.id,
            routineId = domain.routineId,
            dayIndex = domain.dayIndex,
            name = domain.name,
            blockOrderJson = json.encodeToString(domain.blockOrder)
        )
    }
}

object BlockMapper {

    fun toDomain(entity: Block): DomainBlock {
        return DomainBlock(
            id = entity.id,
            dayId = entity.dayId,
            blockIndex = entity.blockIndex,
            name = entity.name,
            type = entity.type,
            configJson = entity.configJson,
            order = entity.order
        )
    }

    fun toEntity(domain: DomainBlock): Block {
        return Block(
            id = domain.id,
            dayId = domain.dayId,
            blockIndex = domain.blockIndex,
            name = domain.name,
            type = domain.type,
            configJson = domain.configJson,
            order = domain.order
        )
    }
}

object TemplateExerciseMapper {

    fun toDomain(entity: TemplateExercise): DomainTemplateExercise {
        return DomainTemplateExercise(
            id = entity.id,
            blockId = entity.blockId,
            exerciseId = entity.exerciseId,
            order = entity.order,
            setsTarget = entity.setsTarget,
            repsTarget = entity.repsTarget,
            weightTarget = entity.weightTarget,
            rpeTarget = entity.rpeTarget,
            restMipSec = entity.restMipSec,
            restMapSec = entity.restMapSec,
            timeWorkSec = entity.timeWorkSec,
            timeRestSec = entity.timeRestSec,
            rounds = entity.rounds,
            configJson = entity.configJson
        )
    }

    fun toEntity(domain: DomainTemplateExercise): TemplateExercise {
        return TemplateExercise(
            id = domain.id,
            blockId = domain.blockId,
            exerciseId = domain.exerciseId,
            order = domain.order,
            setsTarget = domain.setsTarget,
            repsTarget = domain.repsTarget,
            weightTarget = domain.weightTarget,
            rpeTarget = domain.rpeTarget,
            restMipSec = domain.restMipSec,
            restMapSec = domain.restMapSec,
            timeWorkSec = domain.timeWorkSec,
            timeRestSec = domain.timeRestSec,
            rounds = domain.rounds,
            configJson = domain.configJson
        )
    }
}

object ExerciseMapper {

    private val json = Json { ignoreUnknownKeys = true }

    fun toDomain(entity: ExerciseCatalog): DomainExerciseCatalog {
        return DomainExerciseCatalog(
            id = entity.id,
            name = entity.name,
            pattern = entity.pattern,
            primaryMuscle = entity.primaryMuscle,
            equipment = entity.equipment,
            unilateral = entity.unilateral,
            bilateral = entity.bilateral,
            notes = entity.notes,
            aliases = json.decodeFromString(serializer<List<String>>(), entity.aliasesJson),
            categoryId = entity.categoryId,
            isCustom = entity.isCustom,
            createdAt = entity.createdAt
        )
    }

    fun toEntity(domain: DomainExerciseCatalog): ExerciseCatalog {
        return ExerciseCatalog(
            id = domain.id,
            name = domain.name,
            pattern = domain.pattern,
            primaryMuscle = domain.primaryMuscle,
            equipment = domain.equipment,
            unilateral = domain.unilateral,
            bilateral = domain.bilateral,
            notes = domain.notes,
            aliasesJson = json.encodeToString(domain.aliases),
            categoryId = domain.categoryId,
            isCustom = domain.isCustom,
            createdAt = domain.createdAt
        )
    }
}

object ExerciseCategoryMapper {

    fun toDomain(entity: ExerciseCategory): DomainExerciseCategory {
        return DomainExerciseCategory(
            id = entity.id,
            name = entity.name,
            isSystem = entity.isSystem,
            order = entity.order
        )
    }

    fun toEntity(domain: DomainExerciseCategory): ExerciseCategory {
        return ExerciseCategory(
            id = domain.id,
            name = domain.name,
            isSystem = domain.isSystem,
            order = domain.order
        )
    }
}
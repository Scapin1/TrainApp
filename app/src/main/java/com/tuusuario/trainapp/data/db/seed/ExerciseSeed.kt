package com.tuusuario.trainapp.data.db.seed

import android.content.Context
import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.entities.ExerciseCatalog
import com.tuusuario.trainapp.data.db.entities.ExerciseCategory
import com.tuusuario.trainapp.data.db.entities.ExercisePattern
import com.tuusuario.trainapp.data.db.entities.MuscleGroup
import com.tuusuario.trainapp.data.db.entities.Equipment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

object ExerciseSeed {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun seedDatabase(database: AppDatabase, context: Context) {
        withContext(Dispatchers.IO) {
            val categoryDao = database.exerciseCategoryDao()
            val exerciseDao = database.exerciseCatalogDao()

            // Check if already seeded
            val existingCategories = categoryDao.getAllSync()
            if (existingCategories.isNotEmpty()) return@withContext

            // Insert categories first
            val categories = listOf(
                ExerciseCategory(0, "Empuje", true, 1),
                ExerciseCategory(0, "Tirón", true, 2),
                ExerciseCategory(0, "Piernas", true, 3),
                ExerciseCategory(0, "Core", true, 4),
                ExerciseCategory(0, "Cardio", true, 5),
                ExerciseCategory(0, "Full Body", true, 6),
                ExerciseCategory(0, "Accesorios", true, 7),
            )
            val categoryIds = categories.map { categoryDao.insert(it) }
            val categoryMap = categories.associateBy({ it.name }, { it.id })

            // Insert exercises
            val exercises = buildExercises(categoryMap)
            exercises.forEach { exerciseDao.insert(it) }
        }
    }

    private fun buildExercises(categoryMap: Map<String, Long>): List<ExerciseCatalog> {
        val exercises = mutableListOf<ExerciseCatalog>()
        var order = 1

        // Helper to add exercise
        fun add(
            name: String,
            pattern: ExercisePattern,
            muscle: MuscleGroup,
            equipment: Equipment,
            unilateral: Boolean,
            bilateral: Boolean,
            aliases: List<String>,
            category: String,
            notes: String = ""
        ) {
            exercises.add(ExerciseCatalog(
                id = 0,
                name = name,
                pattern = pattern,
                primaryMuscle = muscle,
                equipment = equipment,
                unilateral = unilateral,
                bilateral = bilateral,
                notes = notes,
                aliasesJson = json.encodeToString(aliases),
                categoryId = categoryMap[category]!!,
                isCustom = false,
                createdAt = System.currentTimeMillis()
            ))
        }

        // ===== EMPUJE (Push) =====
        add("Press banca", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.BARBELL, false, true,
            listOf("Banca plana", "Bench press", "Press de banca"), "Empuje")
        add("Press banca con mancuernas", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.DUMBBELL, false, true,
            listOf("DB bench press", "Press mancuernas plano"), "Empuje")
        add("Press banca inclinado", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.BARBELL, false, true,
            listOf("Incline bench press", "Banca inclinada"), "Empuje")
        add("Press banca inclinado mancuernas", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.DUMBBELL, false, true,
            listOf("Incline DB press"), "Empuje")
        add("Press militar", ExercisePattern.PUSH_VERT, MuscleGroup.SHOULDERS, Equipment.BARBELL, false, true,
            listOf("Overhead press", "OHP", "Press militar standing"), "Empuje")
        add("Press militar con mancuernas", ExercisePattern.PUSH_VERT, MuscleGroup.SHOULDERS, Equipment.DUMBBELL, false, true,
            listOf("Seated DB overhead press"), "Empuje")
        add("Press Arnold", ExercisePattern.PUSH_VERT, MuscleGroup.SHOULDERS, Equipment.DUMBBELL, false, true,
            listOf("Arnold press"), "Empuje")
        add("Fondos en paralelas", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.BODYWEIGHT, false, true,
            listOf("Dips", "Parallel bar dips"), "Empuje")
        add("Flexiones", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.BODYWEIGHT, false, true,
            listOf("Push ups", "Pushups"), "Empuje")
        add("Flexiones con pies elevados", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.BODYWEIGHT, false, true,
            listOf("Decline push ups"), "Empuje")
        add("Press de máquina", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.MACHINE, false, true,
            listOf("Machine chest press", "Press máquina pecho"), "Empuje")
        add("Aperturas con mancuernas", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.DUMBBELL, false, true,
            listOf("Dumbbell flyes", "Flyes", "Aperturas"), "Empuje")
        add("Aperturas en polea", ExercisePattern.PUSH_HORIZ, MuscleGroup.CHEST, Equipment.CABLE, false, true,
            listOf("Cable flyes", "Crossovers"), "Empuje")
        add("Elevaciones laterales", ExercisePattern.PUSH_VERT, MuscleGroup.SHOULDERS, Equipment.DUMBBELL, false, true,
            listOf("Lateral raises", "Laterales"), "Empuje")
        add("Elevaciones frontales", ExercisePattern.PUSH_VERT, MuscleGroup.SHOULDERS, Equipment.DUMBBELL, false, true,
            listOf("Front raises", "Frontales"), "Empuje")
        add("Face pulls", ExercisePattern.PULL_HORIZ, MuscleGroup.SHOULDERS, Equipment.CABLE, false, true,
            listOf("Facepull", "Tirones faciales"), "Empuje")
        add("Extensiones de tríceps en polea", ExercisePattern.PUSH_VERT, MuscleGroup.TRICEPS, Equipment.CABLE, false, true,
            listOf("Tricep pushdowns", "Pushdowns", "Extensiones tríceps polea"), "Empuje")
        add("Extensiones de tríceps sobre cabeza", ExercisePattern.PUSH_VERT, MuscleGroup.TRICEPS, Equipment.DUMBBELL, false, true,
            listOf("Overhead tricep extension", "Extensiones tríceps overhead"), "Empuje")
        add("Press francés", ExercisePattern.PUSH_VERT, MuscleGroup.TRICEPS, Equipment.BARBELL, false, true,
            listOf("Skull crushers", "Lying tricep extension", "Press francés barra"), "Empuje")
        add("Fondos en banco", ExercisePattern.PUSH_HORIZ, MuscleGroup.TRICEPS, Equipment.BODYWEIGHT, false, true,
            listOf("Bench dips", "Tricep dips"), "Empuje")

        // ===== TIRÓN (Pull) =====
        add("Dominadas", ExercisePattern.PULL_VERT, MuscleGroup.BACK, Equipment.BODYWEIGHT, false, true,
            listOf("Pull ups", "Chin ups", "Pullups"), "Tirón")
        add("Dominadas con agarre supino", ExercisePattern.PULL_VERT, MuscleGroup.BACK, Equipment.BODYWEIGHT, false, true,
            listOf("Chin ups", "Dominadas supinas"), "Tirón")
        add("Jalón al pecho", ExercisePattern.PULL_VERT, MuscleGroup.BACK, Equipment.CABLE, false, true,
            listOf("Lat pulldown", "Lat pulldowns", "Jalón pecho"), "Tirón")
        add("Jalón detrás de nuca", ExercisePattern.PULL_VERT, MuscleGroup.BACK, Equipment.CABLE, false, true,
            listOf("Behind neck pulldown"), "Tirón")
        add("Remo con barra", ExercisePattern.PULL_HORIZ, MuscleGroup.BACK, Equipment.BARBELL, false, true,
            listOf("Barbell row", "Bent over row", "Remo barra"), "Tirón")
        add("Remo con mancuerna a una mano", ExercisePattern.PULL_HORIZ, MuscleGroup.BACK, Equipment.DUMBBELL, true, false,
            listOf("Single arm DB row", "Remo mancuerna una mano", "One arm row"), "Tirón")
        add("Remo en máquina", ExercisePattern.PULL_HORIZ, MuscleGroup.BACK, Equipment.MACHINE, false, true,
            listOf("Machine row", "Seated cable row", "Remo máquina"), "Tirón")
        add("Remo en polea baja", ExercisePattern.PULL_HORIZ, MuscleGroup.BACK, Equipment.CABLE, false, true,
            listOf("Seated cable row", "Low cable row"), "Tirón")
        add("Face pulls", ExercisePattern.PULL_HORIZ, MuscleGroup.SHOULDERS, Equipment.CABLE, false, true,
            listOf("Facepull", "Tirones faciales"), "Tirón")
        add("Pull over con mancuerna", ExercisePattern.PULL_HORIZ, MuscleGroup.BACK, Equipment.DUMBBELL, false, true,
            listOf("Dumbbell pullover", "Pullover"), "Tirón")
        add("Curl de bíceps con barra", ExercisePattern.PULL_HORIZ, MuscleGroup.BICEPS, Equipment.BARBELL, false, true,
            listOf("Barbell curl", "Bicep curl", "Curl barra"), "Tirón")
        add("Curl de bíceps con mancuernas", ExercisePattern.PULL_HORIZ, MuscleGroup.BICEPS, Equipment.DUMBBELL, true, true,
            listOf("DB curl", "Dumbbell bicep curl", "Curl mancuernas"), "Tirón")
        add("Curl martillo", ExercisePattern.PULL_HORIZ, MuscleGroup.BICEPS, Equipment.DUMBBELL, true, true,
            listOf("Hammer curl", "Curl neutro"), "Tirón")
        add("Curl en polea", ExercisePattern.PULL_HORIZ, MuscleGroup.BICEPS, Equipment.CABLE, true, true,
            listOf("Cable curl", "Curl polea"), "Tirón")
        add("Curl concentrado", ExercisePattern.PULL_HORIZ, MuscleGroup.BICEPS, Equipment.DUMBBELL, true, false,
            listOf("Concentration curl", "Curl concentrado"), "Tirón")

        // ===== PIERNAS (Legs) =====
        add("Sentadilla trasera", ExercisePattern.SQUAT, MuscleGroup.QUADS, Equipment.BARBELL, false, true,
            listOf("Back squat", "Sentadilla back squat", "Squat"), "Piernas")
        add("Sentadilla frontal", ExercisePattern.SQUAT, MuscleGroup.QUADS, Equipment.BARBELL, false, true,
            listOf("Front squat", "Sentadilla frontal"), "Piernas")
        add("Sentadilla con mancuernas", ExercisePattern.SQUAT, MuscleGroup.QUADS, Equipment.DUMBBELL, false, true,
            listOf("Goblet squat", "DB squat", "Sentadilla goblet"), "Piernas")
        add("Sentadilla búlgara", ExercisePattern.LUNGE, MuscleGroup.QUADS, Equipment.DUMBBELL, true, false,
            listOf("Bulgarian split squat", "Split squat", "Sentadilla búlgara"), "Piernas")
        add("Zancadas", ExercisePattern.LUNGE, MuscleGroup.QUADS, Equipment.DUMBBELL, true, false,
            listOf("Lunges", "Walking lunges", "Zancadas caminando"), "Piernas")
        add("Zancadas laterales", ExercisePattern.LUNGE, MuscleGroup.GLUTES, Equipment.DUMBBELL, true, false,
            listOf("Lateral lunges", "Side lunges", "Zancadas laterales"), "Piernas")
        add("Peso muerto", ExercisePattern.HINGE, MuscleGroup.HAMSTRINGS, Equipment.BARBELL, false, true,
            listOf("Deadlift", "Conventional deadlift", "Peso muerto convencional"), "Piernas")
        add("Peso muerto rumano", ExercisePattern.HINGE, MuscleGroup.HAMSTRINGS, Equipment.BARBELL, false, true,
            listOf("RDL", "Romanian deadlift", "Peso muerto rumano"), "Piernas")
        add("Peso muerto rumano con mancuernas", ExercisePattern.HINGE, MuscleGroup.HAMSTRINGS, Equipment.DUMBBELL, false, true,
            listOf("DB RDL", "Dumbbell RDL"), "Piernas")
        add("Peso muerto a una pierna", ExercisePattern.HINGE, MuscleGroup.HAMSTRINGS, Equipment.DUMBBELL, true, false,
            listOf("Single leg RDL", "Single leg deadlift"), "Piernas")
        add("Prensa de piernas", ExercisePattern.SQUAT, MuscleGroup.QUADS, Equipment.MACHINE, false, true,
            listOf("Leg press", "Prensa piernas"), "Piernas")
        add("Extensiones de cuádriceps", ExercisePattern.SQUAT, MuscleGroup.QUADS, Equipment.MACHINE, true, true,
            listOf("Leg extensions", "Extensiones quads"), "Piernas")
        add("Curl femoral", ExercisePattern.HINGE, MuscleGroup.HAMSTRINGS, Equipment.MACHINE, true, true,
            listOf("Leg curls", "Hamstring curls", "Curl femoral"), "Piernas")
        add("Elevación de talones", ExercisePattern.SQUAT, MuscleGroup.CALVES, Equipment.MACHINE, false, true,
            listOf("Calf raises", "Standing calf raises", "Gemelos"), "Piernas")
        add("Elevación de talones sentado", ExercisePattern.SQUAT, MuscleGroup.CALVES, Equipment.MACHINE, false, true,
            listOf("Seated calf raises", "Gemelos sentado"), "Piernas")
        add("Hip thrust", ExercisePattern.HINGE, MuscleGroup.GLUTES, Equipment.BARBELL, false, true,
            listOf("Barbell hip thrust", "Empuje de cadera"), "Piernas")
        add("Hip thrust con mancuerna", ExercisePattern.HINGE, MuscleGroup.GLUTES, Equipment.DUMBBELL, false, true,
            listOf("DB hip thrust"), "Piernas")
        add("Swing con kettlebell", ExercisePattern.HINGE, MuscleGroup.GLUTES, Equipment.KETTLEBELL, false, true,
            listOf("KB swing", "Kettlebell swing", "Swing KB"), "Piernas")
        add("Step ups", ExercisePattern.LUNGE, MuscleGroup.QUADS, Equipment.DUMBBELL, true, false,
            listOf("Subidas a cajón", "Box step ups"), "Piernas")

        // ===== CORE =====
        add("Plancha abdominal", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Plank", "Plancha frontal", "Front plank"), "Core")
        add("Plancha lateral", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, true, false,
            listOf("Side plank", "Plancha lateral"), "Core")
        add("Plancha con elevación de pierna", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, true, false,
            listOf("Plank leg lift"), "Core")
        add("Ab wheel rollout", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Ab wheel", "Rodillo abdominal", "Rueda abdominal"), "Core")
        add("Crunch", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Crunches", "Abdominales crunch"), "Core")
        add("Crunch bicicleta", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Bicycle crunch", "Crunch bicicleta"), "Core")
        add("Elevación de piernas colgado", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Hanging leg raises", "Toes to bar", "Elevación piernas colgado"), "Core")
        add("Elevación de rodillas colgado", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Hanging knee raises", "Knee raises", "Elevación rodillas colgado"), "Core")
        add("Dragon flag", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Dragon flags"), "Core")
        add("Bird dog", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, true, false,
            listOf("Bird-dog", "Perro pájaro"), "Core")
        add("Dead bug", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Deadbug", "Bicho muerto"), "Core")
        add("Pallof press", ExercisePattern.CORE, MuscleGroup.CORE, Equipment.CABLE, false, true,
            listOf("Pallof", "Anti-rotation press"), "Core")

        // ===== CARDIO =====
        add("Correr", ExercisePattern.CARDIO, MuscleGroup.FULL_BODY, Equipment.BODYWEIGHT, false, true,
            listOf("Running", "Jogging", "Trotar"), "Cardio")
        add("Bicicleta estática", ExercisePattern.CARDIO, MuscleGroup.QUADS, Equipment.MACHINE, false, true,
            listOf("Stationary bike", "Cycling", "Bici estática"), "Cardio")
        add("Remo ergómetro", ExercisePattern.CARDIO, MuscleGroup.FULL_BODY, Equipment.MACHINE, false, true,
            listOf("Rowing machine", "Ergometer", "Remo indoor"), "Cardio")
        add("Saltar la cuerda", ExercisePattern.CARDIO, MuscleGroup.FULL_BODY, Equipment.BODYWEIGHT, false, true,
            listOf("Jump rope", "Skipping", "Cuerda"), "Cardio")
        add("Burpees", ExercisePattern.CARDIO, MuscleGroup.FULL_BODY, Equipment.BODYWEIGHT, false, true,
            listOf("Burpee"), "Cardio")
        add("Mountain climbers", ExercisePattern.CARDIO, MuscleGroup.CORE, Equipment.BODYWEIGHT, false, true,
            listOf("Climbers", "Escaladores"), "Cardio")
        add("Battle ropes", ExercisePattern.CARDIO, MuscleGroup.FULL_BODY, Equipment.BODYWEIGHT, false, true,
            listOf("Cuerdas de batalla", "Rope waves"), "Cardio")
        add("Sprints", ExercisePattern.CARDIO, MuscleGroup.QUADS, Equipment.BODYWEIGHT, false, true,
            listOf("Sprinting", "Sprints"), "Cardio")

        // ===== FULL BODY =====
        add("Clean & jerk", ExercisePattern.HINGE, MuscleGroup.FULL_BODY, Equipment.BARBELL, false, true,
            listOf("Clean and jerk", "C&J", "Dos tiempos"), "Full Body")
        add("Snatch", ExercisePattern.HINGE, MuscleGroup.FULL_BODY, Equipment.BARBELL, false, true,
            listOf("Arrancada", "Snatch"), "Full Body")
        add("Thruster", ExercisePattern.SQUAT, MuscleGroup.FULL_BODY, Equipment.BARBELL, false, true,
            listOf("Thrusters"), "Full Body")
        add("Wall balls", ExercisePattern.SQUAT, MuscleGroup.FULL_BODY, Equipment.MEDICINE_BALL, false, true,
            listOf("Wall ball shots"), "Full Body")
        add("Man maker", ExercisePattern.SQUAT, MuscleGroup.FULL_BODY, Equipment.DUMBBELL, false, true,
            listOf("Man makers"), "Full Body")
        add("Turkish get up", ExercisePattern.CARRY, MuscleGroup.FULL_BODY, Equipment.KETTLEBELL, true, false,
            listOf("TGU", "Levantamiento turco"), "Full Body")

        // ===== ACCESORIOS =====
        add("Encogimientos de hombros", ExercisePattern.PUSH_VERT, MuscleGroup.TRAPS, Equipment.DUMBBELL, false, true,
            listOf("Shrugs", "Dumbbell shrugs", "Encogimientos"), "Accesorios")
        add("Encogimientos con barra", ExercisePattern.PUSH_VERT, MuscleGroup.TRAPS, Equipment.BARBELL, false, true,
            listOf("Barbell shrugs"), "Accesorios")
        add("Curl de muñeca", ExercisePattern.PULL_HORIZ, MuscleGroup.FOREARMS, Equipment.DUMBBELL, true, true,
            listOf("Wrist curls", "Curl muñeca"), "Accesorios")
        add("Extensiones de muñeca", ExercisePattern.PUSH_HORIZ, MuscleGroup.FOREARMS, Equipment.DUMBBELL, true, true,
            listOf("Reverse wrist curls", "Extensiones muñeca"), "Accesorios")
        add("Farmer's walk", ExercisePattern.CARRY, MuscleGroup.FOREARMS, Equipment.DUMBBELL, false, true,
            listOf("Farmer walk", "Caminata del granjero", "Farmer's carry"), "Accesorios")
        add("Waiter's walk", ExercisePattern.CARRY, MuscleGroup.SHOULDERS, Equipment.DUMBBELL, true, false,
            listOf("Waiter walk", "Caminata camarero"), "Accesorios")

        return exercises
    }
}
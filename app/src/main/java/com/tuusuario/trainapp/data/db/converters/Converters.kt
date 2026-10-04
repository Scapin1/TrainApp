package com.tuusuario.trainapp.data.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tuusuario.trainapp.data.db.entities.BlockType
import com.tuusuario.trainapp.data.db.entities.CycleType
import com.tuusuario.trainapp.data.db.entities.Equipment
import com.tuusuario.trainapp.data.db.entities.ExercisePattern
import com.tuusuario.trainapp.data.db.entities.MuscleGroup
import java.lang.reflect.Type

class Converters {

    private val gson = Gson()

    // --- Enums ---
    @TypeConverter
    fun blockTypeToString(type: BlockType): String = type.name

    @TypeConverter
    fun stringToBlockType(name: String): BlockType = BlockType.valueOf(name)

    @TypeConverter
    fun cycleTypeToString(type: CycleType): String = type.name

    @TypeConverter
    fun stringToCycleType(name: String): CycleType = CycleType.valueOf(name)

    @TypeConverter
    fun exercisePatternToString(pattern: ExercisePattern): String = pattern.name

    @TypeConverter
    fun stringToExercisePattern(name: String): ExercisePattern = ExercisePattern.valueOf(name)

    @TypeConverter
    fun muscleGroupToString(muscle: MuscleGroup): String = muscle.name

    @TypeConverter
    fun stringToMuscleGroup(name: String): MuscleGroup = MuscleGroup.valueOf(name)

    @TypeConverter
    fun equipmentToString(equip: Equipment): String = equip.name

    @TypeConverter
    fun stringToEquipment(name: String): Equipment = Equipment.valueOf(name)

    // --- Lists/Maps ---
    @TypeConverter
    fun listToJson(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun jsonToList(json: String): List<String> =
        gson.fromJson(json, getListType(String::class.java))

    @TypeConverter
    fun listLongToJson(list: List<Long>): String = gson.toJson(list)

    @TypeConverter
    fun jsonToListLong(json: String): List<Long> =
        gson.fromJson(json, getListType(Long::class.java))

    // --- Generic Map ---
    @TypeConverter
    fun mapToJson(map: Map<String, Any>): String = gson.toJson(map)

    @TypeConverter
    fun jsonToMap(json: String): Map<String, Any> =
        gson.fromJson(json, getMapType())

    private fun <T> getListType(clazz: Class<T>): Type =
        TypeToken.getParameterized(List::class.java, clazz).type

    private fun getMapType(): Type =
        TypeToken.getParameterized(Map::class.java, String::class.java, Any::class.java).type
}
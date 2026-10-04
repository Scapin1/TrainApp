# TrainApp ProGuard/R8 Rules

# Hilt
-keep class dagger.hilt.** { *; }
-keep class com.tuusuario.trainapp.**Hilt_Components { *; }
-dontwarn dagger.hilt.**

# Room
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class * implements androidx.room.TypeConverter { *; }
-keep class com.tuusuario.trainapp.data.db.** { *; }
-dontwarn androidx.room.**

# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.* *;
}
-dontwarn kotlinx.serialization.**

# Vico
-keep class com.patryk3211.vico.** { *; }
-dontwarn com.patryk3211.vico.**

# Coil
-keep class coil.** { *; }
-dontwarn coil.**

# Okio
-keep class okio.** { *; }
-dontwarn okio.**

# Coroutines
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Hilt ViewModel
-keep class androidx.hilt.** { *; }
-dontwarn androidx.hilt.**

# Navigation
-keep class androidx.navigation.** { *; }
-dontwarn androidx.navigation.**

# WorkManager
-keep class androidx.work.** { *; }
-dontwarn androidx.work.**

# Kotlin reflection
-keep class kotlin.reflect.** { *; }
-dontwarn kotlin.reflect.**

# Keep all model classes for serialization
-keep class com.tuusuario.trainapp.domain.model.** { *; }
-keep class com.tuusuario.trainapp.data.db.entities.** { *; }

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep companion objects for serialization
-keepclassmembers class * {
    companion object *;
}

# Keep data classes for Room entities
-keep class * {
    @androidx.room.Entity *;
}

# Keep DAO interfaces
-keep interface * {
    @androidx.room.Dao *;
}

# Keep Database class
-keep class * {
    @androidx.room.Database *;
}
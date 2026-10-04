package com.tuusuario.trainapp.data.db.callback

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tuusuario.trainapp.data.db.AppDatabase
import com.tuusuario.trainapp.data.db.seed.ExerciseSeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SeedCallback(private val context: Context) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            val database = AppDatabase.getInstance(context)
            ExerciseSeed.seedDatabase(database, context)
        }
    }
}
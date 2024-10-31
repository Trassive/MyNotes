package com.example.mynotes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class], version =1)
@TypeConverters(Convertor::class)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao


    companion object{
        private const val DATABASE_NAME = "notes_db"
        @Volatile var instance: NotesDatabase? = null
        fun getDatabase(context: Context): NotesDatabase {
            return instance ?: synchronized(this){
                Room.databaseBuilder(
                    context = context,
                    klass = NotesDatabase::class.java,
                    name = DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also{
                        instance = it
                    }
            }
        }
    }
}
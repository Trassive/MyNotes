package com.example.mynotes.di

import android.content.Context
import com.example.mynotes.data.local.NoteDao
import com.example.mynotes.data.local.NotesDatabase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun providesNotesDao(@ApplicationContext context: Context): NoteDao {
        return NotesDatabase.getDatabase(context).noteDao()
    }
    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}
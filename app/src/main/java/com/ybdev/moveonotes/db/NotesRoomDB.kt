package com.ybdev.moveonotes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ybdev.moveonotes.mvvm.model.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NotesRoomDB : RoomDatabase(){

    abstract fun getNotesDao() : NotesDao

    companion object{

        var instance : NotesRoomDB? = null

        operator fun invoke(context: Context) = instance ?: synchronized(Any()){
            instance ?: createDataBase(context).also { instance = it}
        }

        private fun createDataBase(context: Context) = Room
            .databaseBuilder(context,
                NotesRoomDB::class.java,
                "notes_db").build()
    }
}
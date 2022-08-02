package com.example.todoapp.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todoapp.data.dao.ToDoDao;
import com.example.todoapp.model.ToDoModel;
import com.example.todoapp.utils.AppConstants;

@Database(entities = ToDoModel.class, version = 1, exportSchema = false)
public abstract class ToDoDB extends RoomDatabase {
    private static ToDoDB instance;

    public abstract ToDoDao toDoDao();

    public static synchronized ToDoDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ToDoDB.class, AppConstants.DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

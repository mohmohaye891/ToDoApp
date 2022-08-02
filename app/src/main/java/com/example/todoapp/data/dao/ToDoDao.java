package com.example.todoapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todoapp.model.ToDoModel;

import java.util.List;

@Dao
public interface ToDoDao {

    @Insert
    void insertTodo(ToDoModel toDoModel);

    @Delete
    void deleteTodo(ToDoModel toDoModel);

    @Update
    void updateTodo(ToDoModel toDoModel);

    @Query("DELETE FROM tasks WHERE status_check = 0")
    void deleteAllTodo();

    @Query("SELECT COUNT(id) FROM tasks WHERE status_check = 0")
    LiveData<Integer> totalPendingTask();

    @Query("SELECT COUNT(id) FROM tasks WHERE status_check = 1")
    LiveData<Integer> totalCompleteTasks();

    @Query("SELECT *FROM tasks WHERE status_check = 1 ORDER BY todo_createAt DESC")
    LiveData<List<ToDoModel>> completedTasks();

    @Query("SELECT * FROM tasks WHERE status_check = 0 ORDER BY todo_createAt DESC")
    LiveData<List<ToDoModel>> getAllNotes();
}

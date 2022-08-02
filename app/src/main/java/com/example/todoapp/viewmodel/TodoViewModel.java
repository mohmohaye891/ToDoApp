package com.example.todoapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todoapp.data.repository.TodoRepository;
import com.example.todoapp.model.ToDoModel;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository repository;
    private LiveData<List<ToDoModel>> todoList, completeTasks;
    private LiveData<Integer> completed, pending;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        todoList = repository.getAllNotes();
        completeTasks = repository.getCompleteToDo();
        completed = repository.getCompleted();
        pending = repository.getPending();
    }

    public void insert(ToDoModel toDoModel) {
        repository.insert(toDoModel);
    }

    public void delete(ToDoModel toDoModel) {
        repository.delete(toDoModel);
    }

    public void update(ToDoModel toDoModel) {
        repository.update(toDoModel);
    }

    public void deleteAll() {
        repository.deleteAllNotes();
    }

    public LiveData<List<ToDoModel>> getTodoList() {
        return todoList;
    }

    public LiveData<List<ToDoModel>> getCompleteTasks() {
        return completeTasks;
    }

    public LiveData<Integer> getCompleted() {
        return completed;
    }

    public LiveData<Integer> getPending() {
        return pending;
    }
}

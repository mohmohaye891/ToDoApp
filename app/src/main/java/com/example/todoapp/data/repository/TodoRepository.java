package com.example.todoapp.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.todoapp.data.dao.ToDoDao;
import com.example.todoapp.data.database.ToDoDB;
import com.example.todoapp.model.ToDoModel;

import java.util.List;

public class TodoRepository {
    private final ToDoDao toDoDao;
    private final LiveData<List<ToDoModel>> todoList;
    private final LiveData<List<ToDoModel>> completedTodo;
    private final LiveData<Integer> pending;
    private final LiveData<Integer> completed;

    public TodoRepository(Application application) {
        ToDoDB db = ToDoDB.getInstance(application);
        toDoDao = db.toDoDao();
        todoList = toDoDao.getAllNotes();
        completedTodo = toDoDao.completedTasks();
        pending = toDoDao.totalPendingTask();
        completed = toDoDao.totalCompleteTasks();
    }

    public void insert(ToDoModel toDoModel) {
        new InsertTodoAsyncTask(toDoDao).execute(toDoModel);
    }

    public void update(ToDoModel toDoModel) {
        new UpdateTodoAsyncTask(toDoDao).execute(toDoModel);
    }

    public void delete(ToDoModel toDoModel) {
        new DeleteTodoAsyncTask(toDoDao).execute(toDoModel);
    }

    public void deleteAllNotes() {
        new DeleteAllTodoAsyncTask(toDoDao).execute();
    }

    public LiveData<Integer> getCompleted() {
        return completed;
    }

    public LiveData<Integer> getPending() {
        return pending;
    }

    public LiveData<List<ToDoModel>> getAllNotes() {
        return todoList;
    }

    public LiveData<List<ToDoModel>> getCompleteToDo() {
        return completedTodo;
    }

    private static class InsertTodoAsyncTask extends AsyncTask<ToDoModel, Void, Void> {
        private final ToDoDao toDoDao;

        private InsertTodoAsyncTask(ToDoDao toDoDao) {
            this.toDoDao = toDoDao;
        }

        @Override
        protected Void doInBackground(ToDoModel... toDoModels) {
            toDoDao.insertTodo(toDoModels[0]);
            return null;
        }
    }

    private static class DeleteTodoAsyncTask extends AsyncTask<ToDoModel, Void, Void> {
        private final ToDoDao toDoDao;

        private DeleteTodoAsyncTask(ToDoDao toDoDao) {
            this.toDoDao = toDoDao;
        }

        @Override
        protected Void doInBackground(ToDoModel... toDoModels) {
            toDoDao.deleteTodo(toDoModels[0]);
            return null;
        }
    }

    private static class UpdateTodoAsyncTask extends AsyncTask<ToDoModel, Void, Void> {
        private final ToDoDao toDoDao;

        private UpdateTodoAsyncTask(ToDoDao toDoDao) {
            this.toDoDao = toDoDao;
        }

        @Override
        protected Void doInBackground(ToDoModel... toDoModels) {
            toDoDao.updateTodo(toDoModels[0]);
            return null;
        }
    }

    private static class DeleteAllTodoAsyncTask extends AsyncTask<ToDoModel, Void, Void> {
        private final ToDoDao toDoDao;

        private DeleteAllTodoAsyncTask(ToDoDao toDoDao) {
            this.toDoDao = toDoDao;
        }

        @Override
        protected Void doInBackground(ToDoModel... toDoModels) {
            toDoDao.deleteAllTodo();
            return null;
        }
    }

}

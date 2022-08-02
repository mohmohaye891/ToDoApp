package com.example.todoapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class ToDoModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "todo_title")
    private String title;

    @ColumnInfo(name = "todo_createAt")
    private String created_at;

    @ColumnInfo(name = "status_check")
    private int status = 0;

    public ToDoModel(String title, String created_at) {
        this.title = title;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}

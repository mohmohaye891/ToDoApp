package com.example.todoapp.view.todocomplete;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.model.ToDoModel;
import com.example.todoapp.utils.AppConstants;
import com.example.todoapp.viewmodel.TodoViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedTodoActivity extends AppCompatActivity {

    @BindView(R.id.tv_item_completed)
    RecyclerView rvCompletedList;

    @BindView(R.id.no_data)
    TextView noData;

    TodoViewModel todoViewModel;
    CompletedListAdapter completedListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_todo);
        ButterKnife.bind(this);
        initialSetup();
    }

    private void initialSetup() {

        setupAdapter();

        //to show enable home back button
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            setTitle(AppConstants.TITLE_COMPLETED_TODO);
        }

        todoViewModel= new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.getCompleteTasks().observe((LifecycleOwner) this, toDoModels -> completedListAdapter.submitList(toDoModels));

        todoViewModel.getCompleted().observe(this, integer -> {
            if (integer == 0) {
                rvCompletedList.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
            else {
                rvCompletedList.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
            }
        });

        completedListAdapter.setCompletedListener(new CompletedListAdapter.ItemCompletedListener() {
            @Override
            public void onDeleteItem(ToDoModel todoModel) {
                new AlertDialog.Builder(CompletedTodoActivity.this)
                        .setTitle(AppConstants.DELETE)
                        .setMessage(AppConstants.DELETE_CONTENT)
                        .setPositiveButton(AppConstants.DELETE, (dialog, which) -> {
                            todoViewModel.delete(todoModel);
                            Log.d("response", "Deleted Item"+todoModel.toString()+"");
                            Toast.makeText(CompletedTodoActivity.this, AppConstants.TODO_DELETED, Toast.LENGTH_SHORT).show();
                        }).setNegativeButton(AppConstants.BTN_CANCEL, (dialog, which) -> dialog.dismiss()).setCancelable(true).show();
            }

            @Override
            public void onUndoItem(ToDoModel todoModel) {
                new AlertDialog.Builder(CompletedTodoActivity.this)
                        .setTitle(AppConstants.UNDO)
                        .setMessage(AppConstants.UNDO_CONTENT)
                        .setPositiveButton(AppConstants.UNDO, (dialog, which) -> {
                            todoModel.setStatus(0);
                            todoViewModel.update(todoModel);
                            Log.d("response", "Deleted Item"+ todoModel +"");
                            Toast.makeText(CompletedTodoActivity.this, AppConstants.TODO_DELETED, Toast.LENGTH_SHORT).show();
                        }).setNegativeButton(AppConstants.BTN_CANCEL, (dialog, which) -> dialog.dismiss()).setCancelable(true).show();
            }
        });
    }

    private void setupAdapter() {
        rvCompletedList.setLayoutManager(new LinearLayoutManager(this));
        rvCompletedList.setHasFixedSize(true);
        completedListAdapter = new CompletedListAdapter();
        rvCompletedList.setAdapter(completedListAdapter);
    }
}

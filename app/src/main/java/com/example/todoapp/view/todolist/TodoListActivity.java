package com.example.todoapp.view.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.model.ToDoModel;
import com.example.todoapp.utils.AppConstants;
import com.example.todoapp.view.todocomplete.CompletedTodoActivity;
import com.example.todoapp.view.todocreate.CreateTodoActivity;
import com.example.todoapp.viewmodel.TodoViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListActivity extends AppCompatActivity {

    @BindView(R.id.rv_todo_list)
    RecyclerView rvTodoList;

    @BindView(R.id.add_task)
    TextView addTask;

    @BindView(R.id.more)
    ImageView moreMenu;

    @BindView(R.id.no_data)
    TextView noData;

    TodoViewModel todoViewModel;
    TodoListAdapter todoListAdapter;
    WindowManager windowManager;

    ActivityResultLauncher<Intent> resultLauncherForAddTask;

    ActivityResultLauncher<Intent> resultLauncherForEditTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        ButterKnife.bind(this);
        windowManager=(WindowManager)getSystemService(WINDOW_SERVICE);

        initialState();
    }

    private void initialState() {
        setupAdapter();
        addTaskResult();
        editTaskResult();
        addNewTask();

        todoViewModel= new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.getTodoList().observe((LifecycleOwner) this, toDoModels -> todoListAdapter.submitList(toDoModels));

        todoListAdapter.setClickListener(new TodoListAdapter.ItemClickListener() {
            @Override
            public void onDeleteItem(ToDoModel todoModel) {
                new AlertDialog.Builder(TodoListActivity.this)
                        .setTitle(AppConstants.DELETE)
                        .setMessage(AppConstants.DELETE_CONTENT)
                        .setPositiveButton(AppConstants.DELETE, (dialog, which) -> {
                            // to delete list
                            todoViewModel.delete(todoModel);
                            Log.d("response", "Deleted Item"+todoModel.toString()+"");
                            Toast.makeText(getApplicationContext(), AppConstants.TODO_DELETED, Toast.LENGTH_SHORT).show();
                        }).setNegativeButton(AppConstants.BTN_CANCEL, (dialog, which) -> dialog.dismiss())
                        .setCancelable(true)
                        .show();
            }

            @Override
            public void onEditItem(ToDoModel todoModel) {
                Intent intent = new Intent(TodoListActivity.this, CreateTodoActivity.class);
                intent.putExtra(AppConstants.EXTRA_ID, todoModel.getId());
                intent.putExtra(AppConstants.EXTRA_TITLE, todoModel.getTitle());
                intent.putExtra(AppConstants.EXTRA_DATE, todoModel.getCreated_at() );
                resultLauncherForEditTask.launch(intent);
            }

            @Override
            public void onCheckItem(ToDoModel todoModel) {
                new AlertDialog.Builder(TodoListActivity.this)
                        .setTitle(AppConstants.TASK_COMPLETED)
                        .setMessage(AppConstants.TASK_COMPLETED_CONTENT)
                        .setPositiveButton(AppConstants.BTN_YES, (dialog, which) -> {
                            todoModel.setStatus(1);
                            todoViewModel.update(todoModel);
                            Log.d("response", "Item Completed"+ todoModel +"");
                            Toast.makeText(getApplicationContext(), AppConstants.TODO_COMPLETED, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(AppConstants.BTN_NO, (dialog, which) -> dialog.dismiss()).setCancelable(true).show();
            }
        });

        moreMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(TodoListActivity.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.deleteall) {
                    new AlertDialog.Builder(TodoListActivity.this)
                            .setTitle(AppConstants.DELETE)
                            .setMessage(AppConstants.DELETE_All_CONTENT)
                            .setPositiveButton(AppConstants.DELETE, (dialog, which) -> {
                                todoViewModel.deleteAll();
                                Toast.makeText(TodoListActivity.this, AppConstants.ALL_TODO_DELETED, Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton(AppConstants.BTN_CANCEL, (dialog, which) -> dialog.dismiss()).setCancelable(true).show();
                }

                if (menuItem.getItemId() == R.id.completed){
                    Intent intent=new Intent(getApplicationContext(), CompletedTodoActivity.class);
                    startActivity(intent);
                }
                return true;
            });
            popupMenu.show();
        });

        todoViewModel.getPending().observe(TodoListActivity.this, integer -> {
            if (integer == 0) {
                rvTodoList.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
            else {
                rvTodoList.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
            }
        });
    }

    private void setupAdapter() {
        rvTodoList.setLayoutManager(new LinearLayoutManager(this));
        rvTodoList.setHasFixedSize(true);
        todoListAdapter = new TodoListAdapter();
        rvTodoList.setAdapter(todoListAdapter);
    }

    private void addTaskResult() {
        resultLauncherForAddTask = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        String title = data.getStringExtra(AppConstants.EXTRA_TITLE);
                        String time = data.getStringExtra(AppConstants.EXTRA_DATE);

                        ToDoModel todoModel = new ToDoModel(title, time);
                        todoViewModel.insert(todoModel);
                        Toast.makeText(getApplicationContext(), AppConstants.TODO_SAVED, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editTaskResult() {
        resultLauncherForEditTask = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        int id = data.getIntExtra(AppConstants.EXTRA_ID, -1);
                        if (id == -1) {
                            Toast.makeText(getApplicationContext(), AppConstants.TODO_CANNOT_UPDATED, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String title = data.getStringExtra(AppConstants.EXTRA_TITLE);
                        String time = data.getStringExtra(AppConstants.EXTRA_DATE);
                        ToDoModel todoModel = new ToDoModel(title, time);
                        todoModel.setId(id);
                        todoViewModel.update(todoModel);
                        Toast.makeText(getApplicationContext(), AppConstants.TODO_UPDATED, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addNewTask() {
        addTask.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CreateTodoActivity.class);
            resultLauncherForAddTask.launch(intent);
        });
    }
}
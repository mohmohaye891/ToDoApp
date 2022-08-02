package com.example.todoapp.view.todocreate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todoapp.R;
import com.example.todoapp.utils.AppConstants;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTodoActivity extends AppCompatActivity {

    @BindView(R.id.et_todo_title)
    TextInputEditText editTextTitle;

    @BindView(R.id.et_todo_content)
    TextInputEditText editTextTime;

    private Date eventDate;
    private String startDateStr;
    private Calendar myCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_todo);
        ButterKnife.bind(this);

        initialState();
    }

    private void initialState() {
        String date_n = new SimpleDateFormat(AppConstants.SIMPLE_DATE_FORMAT, Locale.getDefault()).format(new Date());
        editTextTime.setText(date_n);
        //to disable editing in date section
        editTextTime.setKeyListener(null);
        editTextTime.setOnClickListener(v -> getDate());

        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        Intent intent = getIntent();
        if (intent.hasExtra(AppConstants.EXTRA_ID)) {
            setTitle(AppConstants.TODO_EDIT);
            editTextTitle.setText(intent.getStringExtra(AppConstants.EXTRA_TITLE));
            editTextTime.setText(intent.getStringExtra(AppConstants.EXTRA_DATE));
        } else {
            setTitle(AppConstants.TODO_ADD);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_todo) {
            saveTodo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTodo() {
        String title = editTextTitle.getText().toString();
        String time = editTextTime.getText().toString();
        //check if time or date id empty
        if (title.trim().isEmpty() || time.trim().isEmpty()){
            Toast.makeText(this, AppConstants.INSERT_TITLE_AND_DATE, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(AppConstants.EXTRA_TITLE, title);
        data.putExtra(AppConstants.EXTRA_DATE, time);
        int id = getIntent().getIntExtra(AppConstants.EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(AppConstants.EXTRA_ID, id);
        }
        setResult(RESULT_OK,data);
        finish();
    }

    private void getDate() {
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //create a date string
            String myFormat = AppConstants.SIMPLE_DATE_FORMAT;
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            try {
                eventDate = sdf.parse(sdf.format(myCalendar.getTime()));
                startDateStr = sdf.format(myCalendar.getTime());
                editTextTime.setText(startDateStr);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        };
        DatePickerDialog datePickerDialog = new
                DatePickerDialog(CreateTodoActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}

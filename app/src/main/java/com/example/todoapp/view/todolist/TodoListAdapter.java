package com.example.todoapp.view.todolist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;
import com.example.todoapp.model.ToDoModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListAdapter extends ListAdapter<ToDoModel, TodoListAdapter.TodoListViewHolder> {

    private ItemClickListener mListener;

    public TodoListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ToDoModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ToDoModel>() {
        //to check weather to items have same id or not
        @Override
        public boolean areItemsTheSame(ToDoModel oldItem, ToDoModel newItem) {
            return oldItem.getId() == newItem.getId();
        }
        //to check weather to items have same contacts or not
        @Override
        public boolean areContentsTheSame(ToDoModel oldItem, ToDoModel newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getCreated_at().equals(newItem.getCreated_at());
        }
    };

    @NonNull
    @Override
    public TodoListAdapter.TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_list, parent, false);
        return new TodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoListViewHolder holder, int position) {
        ToDoModel toDoModel = getItem(position);
        holder.title.setText(toDoModel.getTitle());
        holder.description.setText(toDoModel.getCreated_at());
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onDeleteItem(ToDoModel todoModel);
        void onEditItem(ToDoModel todoModel);
        void onCheckItem(ToDoModel todoModel);
    }

    public class TodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_item_title)
        TextView title;

        @BindView(R.id.tv_item_content)
        TextView description;

        @BindView(R.id.edit_todo)
        ImageButton editImage;

        @BindView(R.id.delete_todoImg)
        ImageButton deleteImage;

        @BindView(R.id.status_checked)
        ImageButton statusCheck;

        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            editImage.setOnClickListener(this);
            deleteImage.setOnClickListener(this);
            statusCheck.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition();
            switch (view.getId()) {
                case R.id.edit_todo:
                    if (mListener != null)
                        mListener.onEditItem(getItem(position));
                    break;
                case R.id.delete_todoImg:
                    if (mListener!=null)
                        mListener.onDeleteItem(getItem(position));
                    break;
                case R.id.status_checked:
                    if (mListener != null)
                        mListener.onCheckItem(getItem(position));
                    break;
                default:
                    break;

            }
        }
    }
}

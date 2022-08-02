package com.example.todoapp.view.todocomplete;

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

public class CompletedListAdapter extends ListAdapter<ToDoModel, CompletedListAdapter.CompletedTodoListViewHolder> {
    private ItemCompletedListener mListener;

    public CompletedListAdapter() {
        super(DIFF_CALLBACK);
    }

    //to check weather to items have same id or not
    private static final DiffUtil.ItemCallback<ToDoModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ToDoModel>() {
        @Override
        public boolean areItemsTheSame(ToDoModel oldItem, ToDoModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ToDoModel oldItem, @NonNull ToDoModel newItem) {
            return false;
        }

    };

    @NonNull
    @Override
    public CompletedTodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_list, parent, false);
        return new CompletedTodoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedTodoListViewHolder holder, int position) {
        ToDoModel currentTodo = getItem(position);
        holder.title.setText(currentTodo.getTitle());
        holder.description.setText(currentTodo.getCreated_at());
    }

    public void setCompletedListener(ItemCompletedListener itemCompletedListener) {
        this.mListener = itemCompletedListener;
    }

    public interface ItemCompletedListener {
        void onDeleteItem(ToDoModel todoModel);
        void onUndoItem(ToDoModel todoModel);
    }

    public class CompletedTodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_item_title)
        TextView title;

        @BindView(R.id.tv_item_content)
        TextView description;

        @BindView(R.id.undo_todo)
        ImageButton undoTodoImg;

        @BindView(R.id.delete_todo)
        ImageButton deleteTodoImg;

        public CompletedTodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            undoTodoImg.setOnClickListener(this);
            deleteTodoImg.setOnClickListener(this);
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition();
            switch (view.getId()) {
                case R.id.delete_todo:
                    if (mListener!=null)
                        mListener.onDeleteItem(getItem(position));
                    break;
                case R.id.undo_todo:
                    if (mListener != null)
                        mListener.onUndoItem(getItem(position));
                    break;
                default:
                    break;
            }
        }
    }
}

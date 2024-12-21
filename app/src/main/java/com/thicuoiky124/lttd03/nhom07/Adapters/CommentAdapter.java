package com.thicuoiky124.lttd03.nhom07.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albumia.CLasses.Comment;
import com.example.albumia.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList; // List of comments (custom object)

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView commentTextView;
        public TextView timeTextView;

        public CommentViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.commentBlock_user);
            commentTextView = itemView.findViewById(R.id.commentBlock_comment);
            timeTextView = itemView.findViewById(R.id.commentBlock_time);
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_block, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        Comment currentComment = commentList.get(position);
        holder.usernameTextView.setText(currentComment.getUsername());
        holder.commentTextView.setText(currentComment.getCommentText());
        holder.timeTextView.setText(currentComment.getTime().format(formatter));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}

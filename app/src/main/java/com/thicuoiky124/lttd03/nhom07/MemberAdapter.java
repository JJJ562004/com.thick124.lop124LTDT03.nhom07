package com.thicuoiky124.lttd03.nhom07;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.albumia.CLasses.User;
import com.example.albumia.R;

import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder> {

    private List<User> followerList;

    public FollowerAdapter(List<User> followerList) {
        this.followerList = followerList;
    }

    @NonNull
    @Override
    public FollowerAdapter.FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_block, parent, false);
        return new FollowerAdapter.FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerAdapter.FollowerViewHolder holder, int position) {
        User follower = followerList.get(position);
        holder.imageView.setImageResource(follower.getImageResId());
        holder.text1.setText(follower.getName());
        holder.text2.setText(follower.getDescription());
        holder.text3.setText(follower.getFollowerCount() + " followers");
        holder.button.setText("Unfollow");
    }

    @Override
    public int getItemCount() {
        return followerList.size();
    }

    public static class FollowerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView text1, text2, text3;
        Button button;

        public FollowerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.follower_block_imageView);
            button = itemView.findViewById(R.id.follower_block_button);
            text1 = itemView.findViewById(R.id.follower_block_text1);
            text2 = itemView.findViewById(R.id.follower_block_text2);
            text3 = itemView.findViewById(R.id.follower_block_text3);
        }
    }

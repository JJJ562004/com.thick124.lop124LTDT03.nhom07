package com.thicuoiky124.lttd03.nhom07.Adapters;

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

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> {

    private List<User> followingList;

    public FollowingAdapter(List<User> followingList) {
        this.followingList = followingList;
    }

    @NonNull
    @Override
    public FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_block, parent, false);
        return new FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingViewHolder holder, int position) {
        User Following = followingList.get(position);
        holder.imageView.setImageResource(Following.getImageResId());
        holder.text1.setText(Following.getName());
        holder.text2.setText(Following.getDescription());
        holder.text3.setText(Following.getFollowerCount() + " Followings");
        holder.button.setText("Follow");
    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    public static class FollowingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView text1, text2, text3;
        Button button;

        public FollowingViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.following_block_imageView);
            button = itemView.findViewById(R.id.following_block_button);
            text1 = itemView.findViewById(R.id.following_block_text1);
            text2 = itemView.findViewById(R.id.following_block_text2);
            text3 = itemView.findViewById(R.id.following_block_text3);
        }
    }
}

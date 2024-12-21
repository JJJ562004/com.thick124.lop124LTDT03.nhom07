package com.thicuoiky124.lttd03.nhom07.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.albumia.Adapters.CommentAdapter;
import com.example.albumia.CLasses.Comment;
import com.example.albumia.ImageZoomActivity;
import com.example.albumia.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class comment_section extends Fragment {
    private Button send_button;
    private ImageView star;
    public static List<Comment> commentList;
    private Button follow_button;
    private ImageView turn_back;
    private ImageView detail_image;
    private CommentAdapter adapter;
    private EditText comment;
    private RecyclerView recyclerView;
    private PhotoView commentImageDetail;
    private ImageView comment_user_image;
    private String imageUrl;
    public static List<Comment> getCommentList(){
        return commentList;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        recyclerView = view.findViewById(R.id.comment_recyclerview);
        commentList = new ArrayList<>();
        adapter = new CommentAdapter(commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        Log.d("RecyclerView", "Adapter attached with " + commentList.size() + " items");

        turn_back = view.findViewById(R.id.comment_return_from_comment);
        send_button = view.findViewById(R.id.comment_send_button);
        comment = view.findViewById(R.id.comment_comment);
        star = view.findViewById(R.id.comment_like);
        follow_button = view.findViewById(R.id.comment_follow_button);
        comment_user_image = view.findViewById(R.id.comment_user_image);

        Bundle args = getArguments();
        if (args != null) {
            imageUrl = args.getString("imageUrl");
            commentImageDetail = view.findViewById(R.id.comment_image_detail);
            // Set the image using the URL
            Glide.with(this)  // Using Glide to load the image
                    .load(imageUrl)
                    .into(commentImageDetail);
            commentImageDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ImageZoomActivity.class);
                    intent.putExtra("imageUrl", imageUrl); // Pass the image URL
                    requireContext().startActivity(intent);
                }
            });

        }

        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Unknown User");

        follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (follow_button.getText() == "Follow")
                {
                    follow_button.setText("Unfollow");
                }
                else{
                    follow_button.setText("Follow");
                }
            }
        });
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentText = comment.getText().toString();
                LocalTime localTime = LocalTime.now();
                if (!commentText.isEmpty()){
                    commentList.add(new Comment(username, commentText, localTime));
                    adapter.notifyDataSetChanged();
                    comment.setText("");
                }
            }
        });

        comment_user_image.setOnClickListener(v -> {
            Intent intent = new Intent(this.getContext(), ImageZoomActivity.class);
            startActivity(intent);
        });

        turn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) requireActivity()).customViewPager.setCurrentItem(2);
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
            }
        });

        comment_user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        return view;
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.user_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.usermenu_item_trangcanhan) {
                    return true;
                } else if (item.getItemId() == R.id.usermenu_item_nhantin) {
                    chat chatFragment = new chat();
                    Log.d("PopupMenu", "Switching to Chat Fragment");
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.homepage_layout, chatFragment)
                            .addToBackStack(null)
                            .commit();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
}
package com.thicuoiky124.lttd03.nhom07.CLasses;

import java.util.HashSet;
import java.util.Set;

public class User {
    private String name;
    private String description;
    private int followerCount;
    private int imageResId;
    private Set<User> followers;
    private Set<User> following;

    public User(String name, String description, int followerCount, int imageResId) {
        this.name = name;
        this.description = description;
        this.followerCount = followerCount;
        this.imageResId = imageResId;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getImageResId() {
        return imageResId;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void follow(User user){
        if(user != null && !this.equals(user) && !this.following.contains(user)){
            this.following.add(user);
            user.followers.add(this);
        }
    }

    public void unfollow(User user) {
        if (user != null && this.following.contains(user)) {
            this.following.remove(user);
            user.followers.remove(this);
        }
    }

    public boolean isFollowing(User user) {
        return this.following.contains(user);
    }

    public boolean isFollowedBy(User user) {
        return this.followers.contains(user);
    }
}


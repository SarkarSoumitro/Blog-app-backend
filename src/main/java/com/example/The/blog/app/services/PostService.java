package com.example.The.blog.app.services;

import com.example.The.blog.app.domain.CreatePostRequest;
import com.example.The.blog.app.domain.entities.Post;
import com.example.The.blog.app.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest createPostRequest);
}

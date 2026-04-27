package com.example.The.blog.app.repositories;

import com.example.The.blog.app.domain.PostStatus;
import com.example.The.blog.app.domain.entities.Category;
import com.example.The.blog.app.domain.entities.Post;
import com.example.The.blog.app.domain.entities.Tag;
import com.example.The.blog.app.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
     List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus status, Category category, Tag tag);
     List<Post> findAllByStatusAndCategory(PostStatus postStatus, Category category);
     List<Post> findAllByStatusAndTagsContaining(PostStatus status, Tag tag);
     List<Post> findAllByStatus(PostStatus status);
     List<Post> findAllByAuthorAndStatus (User  author, PostStatus postStatus);
}

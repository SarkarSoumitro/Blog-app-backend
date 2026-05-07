package com.example.The.blog.app.services.impl;

import com.example.The.blog.app.domain.CreatePostRequest;
import com.example.The.blog.app.domain.PostStatus;
import com.example.The.blog.app.domain.UpdatePostRequest;
import com.example.The.blog.app.domain.entities.Category;
import com.example.The.blog.app.domain.entities.Post;
import com.example.The.blog.app.domain.entities.Tag;
import com.example.The.blog.app.domain.entities.User;
import com.example.The.blog.app.repositories.PostRepository;
import com.example.The.blog.app.services.CategoryService;
import com.example.The.blog.app.services.PostService;
import com.example.The.blog.app.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private static final int WORDS_PER_MINUTE = 200;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null){
            Category category = categoryService.getcategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(PostStatus.PUBLISHED, category, tag);
        }

        if (categoryId != null){
            Category category = categoryService.getcategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(PostStatus.PUBLISHED, category);
        }

        if (tagId !=null){
            Tag tag = tagService.getTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public List<Post> getDraftPosts(User user) {
       return postRepository.findAllByAuthorAndStatus(user,PostStatus.DRAFT);

    }

    @Override
    @Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {
        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        Category category = categoryService.getcategoryById(createPostRequest.getCategoryId());
        newPost.setCategory(category);
        Set<UUID> tagIds = createPostRequest.getTagIds();
        List<Tag> tags = tagService.getTagByIds(tagIds);
        newPost.setTags(new HashSet<>(tags));
        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(UUID id, UpdatePostRequest updatePostRequest) {
        Post ExistingPost = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post does not found with this id " + id));
        ExistingPost.setTitle(updatePostRequest.getTitle());
        ExistingPost.setContent(updatePostRequest.getContent());
        ExistingPost.setStatus(updatePostRequest.getStatus());
        ExistingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();

        if (!ExistingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            Category newCategory = categoryService.getcategoryById(updatePostRequestCategoryId);
            ExistingPost.setCategory(newCategory);
        }
        Set<UUID> existingTagIds = ExistingPost.getTags().stream().map(tag -> tag.getId()).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagids();
        if (!existingTagIds.equals(updatePostRequestTagIds)){
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            ExistingPost.setTags(new HashSet<>(newTags));
        }
        return postRepository.save(ExistingPost);
    }

    @Override
    public Post getPostById(UUID id) {
       return postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Post not found according to this id " + id));
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    private Integer calculateReadingTime(String content) {
        if(content == null || content.isEmpty()) {
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }
}

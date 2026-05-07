package com.example.The.blog.app.controller;

import com.example.The.blog.app.domain.CreatePostRequest;
import com.example.The.blog.app.domain.PostStatus;
import com.example.The.blog.app.domain.UpdatePostRequest;
import com.example.The.blog.app.domain.dtos.CreatePostDtos;
import com.example.The.blog.app.domain.dtos.PostDto;
import com.example.The.blog.app.domain.dtos.UpdatePostRequestDto;
import com.example.The.blog.app.domain.entities.Post;
import com.example.The.blog.app.domain.entities.User;
import com.example.The.blog.app.mappers.PostMapper;
import com.example.The.blog.app.services.PostService;
import com.example.The.blog.app.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private  final PostService postService;
     private final PostMapper postMapper;
     private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false)UUID categoryId, @RequestParam(required = false) UUID tagId){
    List<Post> posts=  postService.getAllPosts(categoryId,tagId);
    List<PostDto> postDtos= posts.stream().map(postMapper::toDto).toList();
    return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDrafts(@RequestAttribute UUID id){
    User loggedInUser = userService.getUserById(id);
    List<Post> draftPosts = postService.getDraftPosts(loggedInUser);
    List<PostDto> postDtos=  draftPosts.stream().map(post -> postMapper.toDto(post)).toList();
    return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostDtos createPostDtos,
            @RequestAttribute  UUID userid
    ){
        User loggedInUser = userService.getUserById(userid);
      CreatePostRequest createPostRequest= postMapper.tocreatePostRequest(createPostDtos);
      Post createdPost = postService.createPost(loggedInUser,createPostRequest);
      PostDto createdPostDtos = postMapper.toDto(createdPost);
      return  new ResponseEntity<>(createdPostDtos, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable UUID id,
           @Valid@RequestBody UpdatePostRequestDto updatePostRequestDto
            ){
        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);
        Post upadatedPost = postService.updatePost(id, updatePostRequest);
        PostDto updatedDto = postMapper.toDto(upadatedPost);
        return ResponseEntity.ok(updatedDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID id){

        Post post = postService.getPostById(id);
        PostDto postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }

    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
         postService.deletePost(id);
         return ResponseEntity.noContent().build();
    }

}

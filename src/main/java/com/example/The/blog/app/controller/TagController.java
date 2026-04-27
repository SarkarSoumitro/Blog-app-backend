package com.example.The.blog.app.controller;


import com.example.The.blog.app.domain.dtos.CreateTagRequest;
import com.example.The.blog.app.domain.dtos.TagResponse;
import com.example.The.blog.app.domain.entities.Tag;
import com.example.The.blog.app.mappers.TagMapper;
import com.example.The.blog.app.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path ="/api/v1/tags" )
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags(){
      List<Tag> tags = tagService.getTags();
    List<TagResponse> tagResponse=  tags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();
    return ResponseEntity.ok(tagResponse);

    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(@Valid @RequestBody CreateTagRequest createTagRequest){
    List<Tag> savedTags =     tagService.createTags(createTagRequest.getNames());
    List<TagResponse> createdTagResponse = savedTags.stream().map(tag -> tagMapper.toTagResponse(tag)).toList();
    return new  ResponseEntity<>(
            createdTagResponse,
        HttpStatus.CREATED
            );
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID id){
          tagService.deleteTag(id);
          return ResponseEntity.noContent().build();
    }


}

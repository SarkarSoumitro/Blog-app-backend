package com.example.The.blog.app.services.impl;

import com.example.The.blog.app.domain.dtos.TagResponse;
import com.example.The.blog.app.domain.entities.Tag;
import com.example.The.blog.app.repositories.TagRepository;
import com.example.The.blog.app.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
  private final   TagRepository tagRepository;
    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
       List<Tag> existingTags= tagRepository.findByNameIn(tagNames);
     Set<String> existingTagNames= existingTags.stream().map(tag -> tag.getName()).collect(Collectors.toSet());

  List<Tag> newTags = tagNames.stream()
             .filter(name -> !existingTagNames.contains(name))
             .map(name-> Tag.builder()
                     .name(name)
                     .posts(new HashSet<>())
                     .build())
             .collect(Collectors.toList());
   List<Tag> savedTags = new ArrayList<>();
  if (!newTags.isEmpty()){
      savedTags= tagRepository.saveAll(newTags);
   }
  savedTags.addAll(existingTags);
     return savedTags;
    }

    @Override
    public void deleteTag(UUID id) {
        tagRepository.findById(id).ifPresent(tag->{
            if (!tag.getPosts().isEmpty()){
                throw new IllegalStateException("can not delete tag with posts");
            }
            tagRepository.deleteById(id);
        });
    }

    @Override
    public Tag getTagById(UUID id) {
      return   tagRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Tag not found with ID "+ id));
    }

    @Override
    public List<Tag> getTagByIds(Set<UUID> ids) {
        List<Tag> foundTags = tagRepository.findAllById(ids);
        if (foundTags.size() != ids.size()){
            throw new EntityNotFoundException("not all specified tag ids exist");
        }
        return foundTags;
    }
}

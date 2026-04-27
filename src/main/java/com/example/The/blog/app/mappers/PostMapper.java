package com.example.The.blog.app.mappers;

import com.example.The.blog.app.domain.CreatePostRequest;
import com.example.The.blog.app.domain.dtos.CreatePostDtos;
import com.example.The.blog.app.domain.dtos.PostDto;
import com.example.The.blog.app.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "author", source = "author")
    @Mapping(target = "category",source = "category")
    @Mapping(target = "tags" , source = "tags")
    @Mapping(target = "postStatus", source = "status")
    PostDto toDto(Post post);

    CreatePostRequest tocreatePostRequest(CreatePostDtos dtos);
}

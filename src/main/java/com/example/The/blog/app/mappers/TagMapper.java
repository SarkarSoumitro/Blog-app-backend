package com.example.The.blog.app.mappers;

import com.example.The.blog.app.domain.PostStatus;
import com.example.The.blog.app.domain.dtos.TagResponse;
import com.example.The.blog.app.domain.entities.Post;
import com.example.The.blog.app.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    public TagResponse toTagResponse(Tag tag);

    @Named("calculatePostCount")

    public default Integer calculatePostCount(Set<Post> posts){
        if(posts == null){
            return 0;
        }
        return (int) posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}

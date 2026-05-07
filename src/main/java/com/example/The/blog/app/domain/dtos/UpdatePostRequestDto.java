package com.example.The.blog.app.domain.dtos;

import com.example.The.blog.app.domain.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.aspectj.bridge.Message;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequestDto {
    @NotNull(message = "Post id is required")
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(min = 10,max = 200, message = "Title must be between the {min} to {max} words")
    private String  title;

    @NotBlank(message = "Content is required")
    @Size(min = 10,max = 200, message = "Content must be between {min} to {max} words")
    private String content;

    @NotNull(message = "Category id is required")
    private UUID categoryId;


    @Builder.Default
    @Size(max = 10, message = "Minimum {max} tags are allowed")
    private Set<UUID> tagIds = new HashSet<>();

    @NotNull(message = "Status is required")
    private PostStatus status;
}

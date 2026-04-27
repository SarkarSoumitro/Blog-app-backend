package com.example.The.blog.app.services;

import com.example.The.blog.app.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}

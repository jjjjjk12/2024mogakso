package com.ajoudev.practice.repository;

import com.ajoudev.practice.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);
    void delete(Post post);
    //Long update(Post post);
    Optional<Post> findByNum(Long num);
    Optional<Post> findByTitle(String title);
    List<Post> findAll();
    List<Post> findByBoard(String name);
}

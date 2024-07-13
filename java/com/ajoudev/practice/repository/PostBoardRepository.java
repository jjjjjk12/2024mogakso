package com.ajoudev.practice.repository;

import com.ajoudev.practice.Post;
import com.ajoudev.practice.PostBoard;

import java.util.List;
import java.util.Optional;

public interface PostBoardRepository {
    PostBoard save(PostBoard postBoard);
    void delete(PostBoard postBoard);
    Optional<PostBoard> findByName(String name);
    List<PostBoard> findAll();
}

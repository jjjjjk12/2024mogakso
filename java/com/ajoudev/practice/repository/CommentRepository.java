package com.ajoudev.practice.repository;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Post;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save (Comment comment);
    void delete (Comment comment);
    //void update (Comment comment);
    Optional<Comment> findByNum(Long num);
    List<Comment> findByPost(Post post);
    List<Comment> findAll();

}

package com.ajoudev.practice.repository;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepositoryJPA extends JpaRepository<Comment, Long>, CommentRepository {
    @Override
    @Query(value = "SELECT * FROM comment c WHERE commentnum = :commentnum", nativeQuery = true)
    Optional<Comment> findByNum(@Param("commentnum") Long num);
    @Override
    //@Query(value = "SELECT c FROM Comment c WHERE c.post = :post", nativeQuery = false)
    List<Comment> findByPost(@Param("post") Post post);
}

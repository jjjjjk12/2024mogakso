package com.ajoudev.practice.repository;

import com.ajoudev.practice.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryJPA extends JpaRepository<Post, Long>, PostRepository {
    @Override
    @Query(value = "SELECT * FROM post p WHERE postnum = :postnum", nativeQuery = true)
    Optional<Post> findByNum(@Param("postnum") Long num);

    @Override
    @Query(value = "SELECT * FROM post p WHERE title = :title", nativeQuery = true)
    Optional<Post> findByTitle(@Param("title") String title);

    @Override
    @Query(value = "SELECT * FROM post p WHERE name = :postboard", nativeQuery = true)
    List<Post> findByBoard(@Param("postboard") String name);
}

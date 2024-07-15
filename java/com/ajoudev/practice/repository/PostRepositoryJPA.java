package com.ajoudev.practice.repository;

import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.PostBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    Page<Post> findByPostBoard(@Param("postboard")PostBoard postBoard, Pageable pageable);
    Page<Post> findByMember(Member member, Pageable pageable);
}

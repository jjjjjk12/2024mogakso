package com.ajoudev.practice.repository;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save (Comment comment);
    void delete (Comment comment);
    //void update (Comment comment);
    Optional<Comment> findByNum(Long num);
    List<Comment> findByPost(Post post);
    Page<Comment> findByMember(Member member, Pageable pageable);
    List<Comment> findAll();

}

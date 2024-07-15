package com.ajoudev.practice.service;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> findComments() {
        return commentRepository.findAll();
    }
    public List<Comment> findComments(Post post) {
        return commentRepository.findByPost(post);
    }

    public List<Comment> findComments(Member member) {
        return commentRepository.findAll().stream().
                filter(s -> s.getMember().equals(member)).toList();
    }

    public Page<Comment> findComments(Member member, Pageable pageable) {
        return commentRepository.findByMember(member, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "commentNum"));
    }

    public Optional<Comment> findOne(Long num) {
        return commentRepository.findByNum(num);
    }

    public void deleteComment(Comment comment) {
        comment.setPost(null);
        commentRepository.delete(comment);
    }

}

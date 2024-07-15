package com.ajoudev.practice.service;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.repository.PostBoardRepository;
import com.ajoudev.practice.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostBoardRepository postBoardRepository;
    private final CommentService commentService;
    public PostService(PostRepository postRepository, PostBoardRepository postBoardRepository, CommentService commentService) {
        this.postRepository = postRepository;
        this.postBoardRepository = postBoardRepository;
        this.commentService = commentService;
    }

    public Long posting(Post post) {
        if (!validateDuplicatePost(post)) {
            postRepository.save(post);
            System.out.println(post.getTitle());
            return post.getPostNum();
        }
        return -1L;
    }

    private boolean validateDuplicatePost(Post post) {
        return postRepository.findByNum(post.getPostNum()).isPresent();
    }

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public List<Post> findPosts(String board) {

        return postRepository.findByBoard(board);
    }

    public Page<Post> findPosts(String board, Pageable pageable) {
        return postRepository.findByPostBoard(postBoardRepository.findByName(board).get(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "postNum"));
    }

    public List<Post> findPosts(Member member) {

        return postRepository.findAll().stream().
                filter(s -> s.getMember().equals(member)).toList();

    }

    public Page<Post> findPosts(Member member, Pageable pageable) {

        return postRepository.findByMember(member, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "postNum"));

    }

    public Optional<Post> findOne(Long num) {
        return postRepository.findByNum(num);
    }

    public Optional<Post> findOne(String title) {
        return postRepository.findByTitle(title);
    }

    public Long deletePost(Post post) {
        if(validateDuplicatePost(post)) {
            for (Comment comment : commentService.findComments(post)) {
                commentService.deleteComment(comment);
            }
            post.setPostBoard(null);
            postRepository.delete(post);
            return 1L;
        }
        return -1L;
    }

    public Long updatePost(Post post) {
        if(validateDuplicatePost(post)) {
            return 1L;
        }
        return -1L;
    }
}

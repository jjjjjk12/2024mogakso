package com.ajoudev.practice.repository;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Post;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class CommentRepositoryTmp implements CommentRepository{

    private static Map<Long, Comment> store = new HashMap<>();
    private static Long sequence = 0L;
    @Override
    public Comment save(Comment comment) {
        comment.setCommentNum(++sequence);
        store.put(sequence, comment);
        return comment;
    }

    @Override
    public void delete(Comment comment) {
        store.remove(comment.getCommentNum());
    }

    @Override
    public Optional<Comment> findByNum(Long num) {
        return Optional.ofNullable(store.get(num));
    }

    @Override
    public List<Comment> findByPost(Post post) {
        return store.values().stream()
                .filter(s -> s.getPost().getPostNum().equals(post.getPostNum()))
                .toList();
    }

    @Override
    public List<Comment> findAll() {
        return new ArrayList<>(store.values());
    }
}

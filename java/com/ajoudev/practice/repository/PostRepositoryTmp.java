/*
package com.ajoudev.practice.repository;

import com.ajoudev.practice.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;

//@Repository
public class PostRepositoryTmp implements PostRepository{
    private static Map<Long, Post> store = new HashMap<>();
    private static long sequence = 0L;
    @Override
    public Post save(Post post) {
        post.setPostNum(++sequence);
        store.put(post.getPostNum(), post);
        return post;
    }

    @Override
    public void delete(Post post) {
        long tmp = post.getPostNum();
        store.remove(post.getPostNum());
    }

    public Long update(Post post) {
        long tmp = post.getPostNum();
        store.replace(tmp, post);
        return tmp;
    }

    @Override
    public Optional<Post> findByNum(Long num) {

        return Optional.ofNullable(store.get(num));
    }

    @Override
    public Optional<Post> findByTitle(String title) {

        return store.values().stream()
                .filter(s -> s.getTitle().equals(title))
                .findAny();
    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Post> findByBoard(String name) {
        return store.values().stream()
                .filter(s -> s.getPostBoard().getName().equals(name))
                .toList();
    }

    @Override
    public Page<Post> findByName(String name, Pageable pageable) {
        return null;
    }
}
*/
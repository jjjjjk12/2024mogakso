package com.ajoudev.practice.repository;

import com.ajoudev.practice.PostBoard;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PostBoardRepositoryTmp implements PostBoardRepository{

    private static List<PostBoard> store = new ArrayList<>();

    public PostBoardRepositoryTmp() {
        PostBoard tmp = new PostBoard();
        tmp.setName("기본");
        save(tmp);
    }

    @Override
    public PostBoard save(PostBoard postBoard) {
        store.add(postBoard);
        return postBoard;
    }

    @Override
    public void delete(PostBoard postBoard) {
        long tmp = store.indexOf(postBoard);
        store.remove(postBoard);
    }

    @Override
    public Optional<PostBoard> findByName(String name) {
        return store.stream().
                filter(s -> s.getName().equals(name)).
                findAny();
    }

    @Override
    public List<PostBoard> findAll() {
        return store;
    }
}

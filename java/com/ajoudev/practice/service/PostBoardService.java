package com.ajoudev.practice.service;

import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.PostBoard;
import com.ajoudev.practice.repository.PostBoardRepository;
import com.ajoudev.practice.repository.PostBoardRepositoryTmp;
import com.ajoudev.practice.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostBoardService {
    private final PostBoardRepository postBoardRepository;
    private final PostRepository postRepository;

    public PostBoardService(PostBoardRepository postBoardRepository, PostRepository postRepository) {
        this.postBoardRepository = postBoardRepository;
        this.postRepository = postRepository;
    }

    public void addBoard(PostBoard postBoard) {
        if(!validateDuplicateBoard(postBoard)) {
            postBoardRepository.save(postBoard);
        }
    }
    public void addBoard(String name) {
        PostBoard postBoard = new PostBoard();
        postBoard.setName(name);
        if(!validateDuplicateBoard(postBoard)) {
            postBoardRepository.save(postBoard);
        }
    }

    private boolean validateDuplicateBoard(PostBoard postBoard) {
        return postBoardRepository.findByName(postBoard.getName()).isPresent();
    }

    public List<PostBoard> findBoards() {return postBoardRepository.findAll();};

    public Optional<PostBoard> findOne(String name) { return postBoardRepository.findByName(name);}

    public void deleteBoard(PostBoard board) {
        if (validateDuplicateBoard(board) && !board.getName().equals("기본")) {
            List<Post> tmp = postRepository.findByBoard(board.getName());
            for (Post post : tmp) {
                post.setPostBoard(postBoardRepository.findByName("기본").get());
            }
            postBoardRepository.delete(board);
        }


    }
    public void deleteBoard(String[] boards) {
        if(boards != null) {
        for (String board : boards) {
            deleteBoard(findOne(board).get());
        }
        }
    }
}

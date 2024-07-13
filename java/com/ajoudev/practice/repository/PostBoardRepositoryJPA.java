package com.ajoudev.practice.repository;

import com.ajoudev.practice.PostBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostBoardRepositoryJPA extends JpaRepository<PostBoard, String>, PostBoardRepository {
}

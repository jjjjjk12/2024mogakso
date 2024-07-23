package com.ajoudev.practice.DTO;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.PostBoard;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PageDTO {
    private List<Post> posts;
    private List<Member> members;
    private List<Comment> comments;
    private Post post;
    private PostBoard board;
    private int page;
    private boolean hasPrevious;
    private boolean hasNext;
}

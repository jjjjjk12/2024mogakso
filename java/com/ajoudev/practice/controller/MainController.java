package com.ajoudev.practice.controller;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.DTO.CommentDTO;
import com.ajoudev.practice.DTO.PageDTO;
import com.ajoudev.practice.DTO.PostDTO;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.service.CommentService;
import com.ajoudev.practice.service.MemberService;
import com.ajoudev.practice.service.PostBoardService;
import com.ajoudev.practice.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private final PostService postService;
    private final PostBoardService postBoardService;
    private final CommentService commentService;
    private final MemberService memberService;

    public MainController(PostService postService, PostBoardService postBoardService, CommentService commentService, MemberService memberService) {
        this.postService = postService;
        this.postBoardService = postBoardService;
        this.commentService = commentService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    String home(Model model) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberService.findOne(id).get();
        model.addAttribute("member", member);
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    String posts(@RequestParam(required = false) String board, Model model, @PageableDefault(page=0, size = 10)Pageable pageable) {
        board = board == null ? "기본" : board;
        Page<Post> posts = postService.findPosts(board, pageable);
        PageDTO pageDTO = PageDTO.builder()
                .posts(posts.getContent())
                .page(posts.getNumber())
                .hasNext(posts.hasNext())
                .hasPrevious(posts.hasPrevious())
                .board(postBoardService.findOne(board).get())
                .build();
        model.addAttribute("page", pageDTO);

        return "boards";
    }

    @GetMapping("/posts/new")
    String postsNew(Model model, @RequestParam(required = false) String board) {
        model.addAttribute("pb", board);
        return "createNewForm";
    }
    @PostMapping("/posts/new")
    String create(@ModelAttribute PostDTO postDTO,
                  HttpServletRequest request,
                  Model model) {
        Post post = new Post();
        post.setMember((Member) request.getAttribute("member"));
        post.setTitle(postDTO.getTitle());
        post.setTextBody(postDTO.getTextBody());
        post.setPostBoard(postBoardService.findOne(postDTO.getBoard()).get());
        postService.posting(post);
        return "redirect:/posts/" + post.getPostNum();
    }

    @GetMapping("/posts/{postNum}")
    String viewPost(@PathVariable Long postNum, Model model) {
        Post post = postService.findOne(postNum).isPresent() ? postService.findOne(postNum).get() : null;
        if (post == null) return "redirect:/list";
        model.addAttribute("post", post);
        if (!commentService.findComments(post).isEmpty()) model.addAttribute("comments", commentService.findComments(post));
        return "viewPost";
    }

    @PostMapping("/posts/{postNum}")
    String deletePost(@PathVariable Long postNum,
                      @ModelAttribute PostDTO postDTO,
                      @ModelAttribute CommentDTO commentDTO,
                      HttpServletRequest request,
                      Model model) {
        Member member = (Member) request.getAttribute("member");
        Post post = postService.findOne(postNum).isPresent() ? postService.findOne(postNum).get() : null;
        if (post != null) {
            List<Comment> comments = commentService.findComments(post);
            model.addAttribute("comments", comments);
        }

        if (postDTO.getAttr() != null) {
            if(!member.equals(post.getMember())) return "redirect:/posts/" + post.getPostNum();

            if (postDTO.getAttr().equals("삭제"))
                postService.deletePost(post);
            else if(postDTO.getAttr().equals("수정")) {
                model.addAttribute("post", post);
                model.addAttribute("pb", post.getPostBoard().getName());
                return "editForm";
            }
            else if(postDTO.getAttr().equals("저장")) {
                post.setTitle(postDTO.getTitle());
                post.setPostBoard(postBoardService.findOne(postDTO.getBoard()).get());
                post.setTextBody(postDTO.getTextBody());
                postService.updatePost(post);
                model.addAttribute("post", post);
                return "viewPost";
            }
        }

        if (commentDTO.getNewComment() != null) {
            Comment tmp = new Comment();
            tmp.setPost(post);
            tmp.setCommentBody(commentDTO.getNewComment());
            tmp.setMember(member);
            commentService.addComment(tmp);
            model.addAttribute("comments", commentService.findComments(post));
            model.addAttribute("post", post);
            return "viewPost";
        }

        if (commentDTO.getAttrComment() != null) {
            Comment tmp = commentService.findOne(commentDTO.getCommentNum()).get();

            if(!member.equals(tmp.getMember())) return "redirect:/posts/" + postNum;

            if (commentDTO.getAttrComment().equals("삭제")) {
                commentService.deleteComment(tmp);
            }

            else if (commentDTO.getAttrComment().equals("수정")) {
                tmp.setEdit(true);
            }

            else if (commentDTO.getAttrComment().equals("저장")) {
                tmp.setEdit(false);
                tmp.setCommentBody(commentDTO.getComment());
            }
            model.addAttribute("comments", commentService.findComments(post));
            model.addAttribute("post", post);
            return "viewPost";
        }
        return "redirect:/list";
    }

    @GetMapping("/list")
    String viewBoard(Model model) {
        return "editBoard";
    }

    @PostMapping("/list")
    String editBoard(@RequestParam(required = false) String attr,
                     @RequestParam(required = false) String newBoard,
                     @RequestParam(required = false) String[] board, Model model) {
        if(attr != null && attr.equals("추가")) {
            postBoardService.addBoard(newBoard);
        }
        else if(attr != null && attr.equals("삭제")) {
            postBoardService.deleteBoard(board);
        }
        return "editBoard";
    }
}

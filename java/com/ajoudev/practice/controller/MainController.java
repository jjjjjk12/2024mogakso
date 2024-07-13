package com.ajoudev.practice.controller;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.service.CommentService;
import com.ajoudev.practice.service.MemberService;
import com.ajoudev.practice.service.PostBoardService;
import com.ajoudev.practice.service.PostService;
import jakarta.servlet.http.HttpSession;
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
    String home(Model model, HttpSession session) {
        String id = (String) session.getAttribute("id");
        Member member = memberService.findOne(id).get();
        model.addAttribute("member", member);
        return "home";
    }

    @GetMapping("/posts")
    String posts(@RequestParam(required = false) String board, Model model) {
        board = board == null ? "기본" : board;
        model.addAttribute("posts", postService.findPosts(board));
        return "posts";
    }

    @GetMapping("/posts/new")
    String postsNew(Model model, HttpSession session) {
        String id = (String) session.getAttribute("id");
        model.addAttribute("member", memberService.findOne(id).get());
        model.addAttribute("boards", postBoardService.findBoards());
        return "createNewForm";
    }
    @PostMapping("/posts/new")
    String create(@RequestParam String title,
                  @RequestParam String textbody,
                  @RequestParam String board,
                  HttpSession session, Model model) {
        Member member = memberService.findOne((String) session.getAttribute("id")).get();
        Post post = new Post();
        post.setMember(member);
        post.setTitle(title);
        post.setTextBody(textbody);
        post.setPostBoard(postBoardService.findOne(board).get());
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
                      @RequestParam(required = false) String attr,
                      @RequestParam(required = false) String title,
                      @RequestParam(required = false) String textBody,
                      @RequestParam(required = false) String comment,
                      @RequestParam(required = false) String board,
                      @RequestParam(required = false) Long commentNum,
                      @RequestParam(required = false) String attrComment,
                      HttpSession session,
                      Model model) {
        Post post = postService.findOne(postNum).isPresent() ? postService.findOne(postNum).get() : null;

        if (post != null) {
            List<Comment> comments = commentService.findComments(post);
            model.addAttribute("comments", comments);
        }

        if (attr != null) {
            Member member = memberService.findOne((String) session.getAttribute("id")).get();
            if(!member.equals(post.getMember())) return "redirect:/posts/" + post.getPostNum();

            if (attr.equals("삭제"))
                postService.deletePost(post);
            else if(attr.equals("수정")) {
                model.addAttribute("post", post);
                model.addAttribute("boards", postBoardService.findBoards());
                return "editForm";
            }
            else if(attr.equals("저장")) {
                post.setTitle(title);
                post.setPostBoard(postBoardService.findOne(board).get());
                post.setTextBody(textBody);
                postService.updatePost(post);
                model.addAttribute("post", post);
                return "viewPost";
            }
        }

        if (comment != null) {
            Member member = memberService.findOne((String) session.getAttribute("id")).get();
            Comment tmp = new Comment();
            tmp.setPost(post);
            tmp.setCommentBody(comment);
            tmp.setMember(member);
            commentService.addComment(tmp);
            model.addAttribute("comments", commentService.findComments(post));
            model.addAttribute("post", post);
            return "viewPost";
        }

        if (attrComment != null) {
            Member member = memberService.findOne((String) session.getAttribute("id")).get();
            Comment tmp = commentService.findOne(commentNum).get();

            if(!member.equals(tmp.getMember())) return "redirect:/posts/" + postNum;

            if (attrComment.equals("삭제")) {
                commentService.deleteComment(tmp);
            }

            else if (attrComment.equals("수정")) {
                tmp.setEdit(true);
            }

            else if (attrComment.equals("저장")) {
                tmp.setEdit(false);
                tmp.setCommentBody(textBody);
            }
            model.addAttribute("comments", commentService.findComments(post));
            model.addAttribute("post", post);
            return "viewPost";
        }
        return "redirect:/list";
    }

    @GetMapping("/list")
    String viewBoard(Model model) {
        model.addAttribute("boards", postBoardService.findBoards());
        return "boards";
    }

    @PostMapping("/list")
    String editBoard(@RequestParam(required = false) String attr,
                     @RequestParam(required = false) String newBoard,@RequestParam(required = false) String[] board,Model model) {
        if(attr != null && attr.equals("추가")) {
            postBoardService.addBoard(newBoard);
        }
        else if(attr != null && attr.equals("삭제")) {
            postBoardService.deleteBoard(board);
        }
        model.addAttribute("boards", postBoardService.findBoards());
        return "editBoard";
    }
}

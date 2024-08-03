package com.ajoudev.practice.controller;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.DTO.MemberDTO;
import com.ajoudev.practice.DTO.PageDTO;
import com.ajoudev.practice.Image;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;
    private final PostBoardService postBoardService;

    public MemberController(MemberService memberService, PostService postService, CommentService commentService, PostBoardService postBoardService) {
        this.memberService = memberService;
        this.postService = postService;
        this.commentService = commentService;
        this.postBoardService = postBoardService;
    }

    @GetMapping("/login")
    public String viewLogin(Model model, @RequestParam(required = false) String error) {
        Boolean wrong = error != null && error.equals("true");
        model.addAttribute("wrong", wrong);
        return "login";
    }

    public String doLogin(@RequestParam String id, @RequestParam String pw, HttpSession httpSession, Model model) {
        boolean wrong = true;
        Member member = new Member();
        member.setId(id);
        member.setPassword(pw);
        if(memberService.login(member)) {
            httpSession.setAttribute("id", id);
            httpSession.setMaxInactiveInterval(600);
            return "redirect:/";
        }
        model.addAttribute("wrong", wrong);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        if(httpSession != null) httpSession.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String viewRegi(Model model) {
        boolean duplicated = false;
        model.addAttribute("duplicated", duplicated);
        return "registerMember";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name ,@RequestParam String id, @RequestParam String pw, Model model) {
        Member member = new Member();
        member.setId(id);
        member.setPassword(pw);
        member.setName(name);
        if(memberService.addMember(member)) {
            return "/login";
        }
        boolean duplicated = true;
        model.addAttribute("duplicated" , duplicated);
        return "registerMember";
    }
    @GetMapping("/remove-user")
    public String remove(HttpServletRequest request) {
        Member member = (Member) request.getAttribute("member");
        memberService.deleteMember(member);
        return "redirect:/logout";
    }

    public String edit(@RequestParam String name, @RequestParam String pw, Model model, HttpServletRequest request) {
        Member replace = new Member();
        replace.setName(name);
        replace.setPassword(pw);
        Member origin = (Member) request.getAttribute("member");
        memberService.editMember(origin, replace);
        return "redirect:/logout";
    }

    @GetMapping("/user/list")
    public String viewMembers(Model model, @PageableDefault(size = 15) Pageable pageable) {
        Page<Member> members = memberService.findAll(pageable);
        PageDTO pageDTO = PageDTO.builder()
                .members(members.getContent())
                .page(members.getNumber())
                .hasPrevious(members.hasPrevious())
                .hasNext(members.hasNext())
                .build();
        model.addAttribute("page", pageDTO);
        return "userList";
    }

    @GetMapping("/user")
    public String viewEditMember(Model model, @RequestParam(required = false) String user, @PageableDefault Pageable pageable, HttpServletRequest request) {
        user = user == null ? ((Member) request.getAttribute("member")).getId() : user;
        Member u = memberService.findOne(user).get();
        Page<Post> posts = postService.findPosts(u, pageable);
        PageDTO pageDTO = PageDTO.builder()
                .posts(posts.getContent())
                .page(posts.getNumber())
                .hasPrevious(posts.hasPrevious())
                .hasNext(posts.hasNext()).build();
        boolean isEdit = false;
        boolean isPost = true;
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isPost", isPost);
        model.addAttribute("user", u);
        model.addAttribute("page", pageDTO);
        return "userInfo";
    }

    @PostMapping("/user")
    public String editMember(Model model, HttpServletRequest request,
                             @ModelAttribute MemberDTO memberDTO,
                             @RequestParam(required = false) String attr,
                             @RequestParam(required = false) String user,
                             @RequestParam(required = false) String list,
                             @PageableDefault Pageable pageable) {
        System.out.println(user);
        user = user == null ? ((Member) request.getAttribute("member")).getId() : user;
        System.out.println("---------------");
        System.out.println(user);
        System.out.println("----------------");
        System.out.println(memberDTO.getName());
        Member u = memberService.findOne(user).get();
        boolean isEdit = false;
        boolean isPost = true;
        PageDTO pageDTO = null;


        if(list == null || list.equals("게시글")) {
            Page<Post> posts = postService.findPosts(u,pageable);
            pageDTO = PageDTO.builder()
                    .posts(posts.getContent())
                    .page(posts.getNumber())
                    .hasPrevious(posts.hasPrevious())
                    .hasNext(posts.hasNext()).build();
            model.addAttribute("page", pageDTO);
        }
        else if (list.equals("댓글")){
            isPost = false;
            Page<Comment> comments = commentService.findComments(u, pageable);
            pageDTO = PageDTO.builder()
                    .comments(comments.getContent())
                    .page(comments.getNumber())
                    .hasPrevious(comments.hasPrevious())
                    .hasNext(comments.hasNext()).build();
            model.addAttribute("page", comments.getNumber());
        }

        if (attr != null && attr.equals("수정")) {
            isEdit = true;
        }
        else if (attr != null && attr.equals("저장")) {
            Member replace = new Member();
            Image image = null;
            try {
                if (memberDTO.getImage() != null) {
                    image = new Image(memberDTO.getImage().getOriginalFilename(), memberDTO.getImage().getBytes(), memberDTO.getImage().getContentType());
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
            replace.setImage(image);
            replace.setName(memberDTO.getName());
            replace.setPassword(memberDTO.getPw());
            System.out.println(replace.getName());
            Member origin = (Member) request.getAttribute("member");
            memberService.editMember(origin, replace);
            u = memberService.findOne(user).get();
            System.out.println(u.getName().equals(memberDTO.getName()));
        }
        model.addAttribute("user", u);
        model.addAttribute("page", pageDTO);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isPost", isPost);
        return "userInfo";
    }
}

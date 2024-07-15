package com.ajoudev.practice.controller;

import com.ajoudev.practice.Comment;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.Post;
import com.ajoudev.practice.service.CommentService;
import com.ajoudev.practice.service.MemberService;
import com.ajoudev.practice.service.PostBoardService;
import com.ajoudev.practice.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String viewLogin(Model model) {
        boolean wrong = false;
        model.addAttribute("wrong", wrong);
        return "login";
    }

    @PostMapping("/login")
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
    public String remove(HttpSession session) {
        Member member = memberService.findOne((String) session.getAttribute("id")).get();
        memberService.deleteMember(member);
        return "redirect:/logout";
    }

    public String editView(HttpSession session, Model model) {
        Member member = memberService.findOne((String) session.getAttribute("id")).get();
        model.addAttribute("boards", postBoardService.findBoards());
        model.addAttribute("member", member);
        return "editMember";
    }


    public String edit(@RequestParam String name, @RequestParam String pw, HttpSession session, Model model) {
        Member replace = new Member();
        replace.setName(name);
        replace.setPassword(pw);
        Member origin = memberService.findOne((String) session.getAttribute("id")).get();
        memberService.editMember(origin, replace);
        return "redirect:/logout";
    }

    @GetMapping("/user/list")
    public String viewMembers(Model model, HttpSession session,@PageableDefault(size = 15) Pageable pageable) {
        Page<Member> members = memberService.findAll(pageable);
        model.addAttribute("member", memberService.findOne((String) session.getAttribute("id")).get());
        model.addAttribute("members", members.get());
        model.addAttribute("boards", postBoardService.findBoards());
        model.addAttribute("hasPrevious",members.hasPrevious());
        model.addAttribute("hasNext",members.hasNext());
        model.addAttribute("page",members.getNumber());
        return "userList";
    }
/*
    @GetMapping("/user")
    public String viewMember(@RequestParam(required = false) String user, Model model, HttpSession session) {
        user = user == null ? (String) session.getAttribute("id") : user;
        Member member = memberService.findOne(user).get();
        model.addAttribute("member", memberService.findOne((String) session.getAttribute("id")).get());
        model.addAttribute("user", member);
        model.addAttribute("boards", postBoardService.findBoards());
        model.addAttribute("posts", postService.findPosts(member));
        model.addAttribute("comments", commentService.findComments(member));
        return "userInfo";
    }
*/
    @GetMapping("/user")
    public String viewEditMember(Model model, @RequestParam(required = false) String user, HttpSession session,@PageableDefault Pageable pageable) {
        Member member = memberService.findOne((String) session.getAttribute("id")).get();
        user = user == null ? (String) session.getAttribute("id") : user;
        Member u = memberService.findOne(user).get();
        Page<Post> posts = postService.findPosts(u, pageable);
        boolean isEdit = false;
        boolean isPost = true;
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isPost", isPost);
        model.addAttribute("member", member);
        model.addAttribute("user", u);
        model.addAttribute("posts", posts.get());
        model.addAttribute("page", posts.getNumber());
        model.addAttribute("boards", postBoardService.findBoards());
        model.addAttribute("hasPrevious",posts.hasPrevious());
        model.addAttribute("hasNext",posts.hasNext());
        return "userInfo";
    }

    @PostMapping("/user")
    public String editMember(Model model, HttpSession session,
                             @RequestParam(required = false) String attr,
                             @RequestParam(required = false) String pw,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String user,
                             @RequestParam(required = false) String list,
                             @PageableDefault Pageable pageable) {
        Member member = memberService.findOne((String) session.getAttribute("id")).get();
        user = user == null ? (String) session.getAttribute("id") : user;
        Member u = memberService.findOne(user).get();
        boolean isEdit = false;
        boolean isPost = true;
        model.addAttribute("member", member);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isPost", isPost);
        model.addAttribute("user", u);
        model.addAttribute("boards", postBoardService.findBoards());


        if(list == null || list.equals("게시글")) {
            Page<Post> posts = postService.findPosts(u,pageable);
            model.addAttribute("posts", posts.get());
            model.addAttribute("hasPrevious",posts.hasPrevious());
            model.addAttribute("hasNext",posts.hasNext());
            model.addAttribute("page", posts.getNumber());
        }
        else if (list.equals("댓글")){
            isPost = false;
            Page<Comment> comments = commentService.findComments(u, pageable);
            model.addAttribute("comments", comments.get());
            model.addAttribute("hasPrevious",comments.hasPrevious());
            model.addAttribute("hasNext",comments.hasNext());
            model.addAttribute("page", comments.getNumber());
        }

        if (attr != null && attr.equals("수정")) {
            isEdit = true;
        }
        else if (attr != null && attr.equals("저장")) {
            Member replace = new Member();
            replace.setName(name);
            replace.setPassword(pw);
            Member origin = memberService.findOne((String) session.getAttribute("id")).get();
            memberService.editMember(origin, replace);
        }

        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isPost", isPost);
        return "userInfo";
    }
}

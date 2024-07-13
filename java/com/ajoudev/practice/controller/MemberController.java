package com.ajoudev.practice.controller;

import com.ajoudev.practice.Member;
import com.ajoudev.practice.service.CommentService;
import com.ajoudev.practice.service.MemberService;
import com.ajoudev.practice.service.PostService;
import jakarta.servlet.http.HttpSession;
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

    public MemberController(MemberService memberService, PostService postService, CommentService commentService) {
        this.memberService = memberService;
        this.postService = postService;
        this.commentService = commentService;
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
    public String viewMembers(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "userList";
    }

    @GetMapping("/user")
    public String viewMember(@RequestParam(required = false) String user, Model model) {
        if(user == null) return "redirect:/user/list";
        Member member = memberService.findOne(user).get();
        model.addAttribute("posts", postService.findPosts(member));
        model.addAttribute("comments", commentService.findComments(member));
        return "userHistory";
    }

    @GetMapping("/edit-user")
    public String viewEditMember(Model model, HttpSession session) {
        Member member = memberService.findOne((String) session.getAttribute("id")).get();
        boolean isEdit = false;
        boolean isPost = true;
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isPost", isPost);
        model.addAttribute("member", member);
        model.addAttribute("posts", postService.findPosts(member));
        return "userInfo";
    }

    @PostMapping("/edit-user")
    public String editMember(Model model, HttpSession session,
                             @RequestParam(required = false) String attr,
                             @RequestParam(required = false) String pw,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String list) {
        Member member = memberService.findOne((String) session.getAttribute("id")).get();
        boolean isEdit = false;
        boolean isPost = true;
        model.addAttribute("member", member);
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("isPost", isPost);


        if(list == null || list.equals("게시글")) {
            model.addAttribute("posts", postService.findPosts(member));
        }
        else if (list.equals("댓글")){
            isPost = false;
            model.addAttribute("comments", commentService.findComments(member));
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

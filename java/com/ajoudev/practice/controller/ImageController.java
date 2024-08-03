package com.ajoudev.practice.controller;

import com.ajoudev.practice.Image;
import com.ajoudev.practice.Member;
import com.ajoudev.practice.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {
    private final MemberService memberService;

    public ImageController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> viewLogin(Model model, @PathVariable String id, HttpServletRequest request) {
        id = id == null ? ((Member) request.getAttribute("member")).getId() : id;
        System.out.println(((Member) request.getAttribute("member")).getId());
        System.out.println(memberService.findOne(id));
        Image image = memberService.findOne(id).get().getImage();
        if (image == null) return null;
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getImageName() + "\"")
                .body(image.getImageFile());
    }
}

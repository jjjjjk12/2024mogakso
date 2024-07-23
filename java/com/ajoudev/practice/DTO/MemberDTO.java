package com.ajoudev.practice.DTO;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class MemberDTO {
    private String id;
    private String pw;
    private String name;
    private MultipartFile image;
}

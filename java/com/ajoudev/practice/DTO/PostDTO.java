package com.ajoudev.practice.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PostDTO {
    private String title;
    private String textBody;
    private String board;
    private String attr;
}

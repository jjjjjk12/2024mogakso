package com.ajoudev.practice.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDTO {
    private String comment;
    private String newComment;
    private Long commentNum;
    private String attrComment;
}

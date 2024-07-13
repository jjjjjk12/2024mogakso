package com.ajoudev.practice;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "comment")
@NoArgsConstructor
public class Comment {

    @Column(name = "commentbody")
    private String commentBody;

    @ManyToOne
    @JoinColumn(name = "postnum")
    private Post post;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentnum")
    private Long commentNum;

    @Column(name = "isedit")
    private boolean isEdit = false;

    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;
}

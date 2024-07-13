package com.ajoudev.practice;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "post")
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postnum")
    private Long postNum;
    @Column
    private String title;
    @Column(name = "textbody")
    private String textBody;
    @ManyToOne
    @JoinColumn(name = "name")
    private PostBoard postBoard;
    @ManyToOne
    @JoinColumn(name = "id")
    private Member member;
}

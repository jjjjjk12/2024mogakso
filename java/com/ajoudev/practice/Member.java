package com.ajoudev.practice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "member")
@NoArgsConstructor
public class Member {


    String name;
    @Id
    @Column(unique = true)
    private String id;
    String password;
    String salt;
    boolean admin;
}

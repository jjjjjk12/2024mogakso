package com.ajoudev.practice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "postboard")
@NoArgsConstructor
public class PostBoard {
    @Id
    String name;

    public String toString() {
        return name;
    }
}

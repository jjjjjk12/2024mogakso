package com.ajoudev.practice;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Embeddable
public class Image {
    public Image(String name, byte[] file, String type) {
        this.imageName = name;
        this.imageFile = file;
        this.imageType = type;
    }

    @Column(name = "imagename")
    private String imageName;
    @Lob
    @Column(name = "imagefile", columnDefinition = "LONGBLOB")
    private byte[] imageFile;
    @Column(name = "imagetype")
    private String imageType;


}

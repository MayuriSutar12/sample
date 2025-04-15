package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "chapter")
//@Subselect("SELECT * FROM your_table") // Prevents Hibernate from creating/modifying the table
@Immutable
public class Chapter {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="chapter__id")
    private Integer chapterId;

    @Column(name ="chapter_name11")
    private String chapterName;

    private Date createdOn;
    private Date createdOn1;

    private String chapterNameNew;

}

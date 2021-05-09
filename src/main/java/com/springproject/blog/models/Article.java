package com.springproject.blog.models;

import com.springproject.blog.models.security.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    private User author;

    private Timestamp createdAt;

    private Timestamp lastModified;
}

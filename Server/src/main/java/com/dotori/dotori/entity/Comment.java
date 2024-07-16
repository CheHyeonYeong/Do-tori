package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private int pid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aid")
    private Auth auth;

    @NotNull
    private String content;

    public void setPost(int pid) {
        this.post = Post.builder().pid(pid).build();
    }

}

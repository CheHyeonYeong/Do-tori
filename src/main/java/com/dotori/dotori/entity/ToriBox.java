package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToriBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @JoinColumn(name = "pid")
    private int pid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Auth auth;

    @JoinColumn(name = "aid")
    private int aid;

    public void setPost(int pid) {
        this.post = Post.builder().pid(pid).build();
    }
    public void setAuth(int aid) {
        this.auth = Auth.builder().aid(aid).build();
    }


}

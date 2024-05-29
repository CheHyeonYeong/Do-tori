package com.dotori.dotori.entity;

import jakarta.persistence.*;
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

    private int pid;

    private int aid;

    public void setPost(int pid) {
        this.post = Post.builder().pid(pid).build();
    }

}

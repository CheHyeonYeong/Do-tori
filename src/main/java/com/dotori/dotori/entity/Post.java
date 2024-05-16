package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private int pid;

    @Column(name = "nickname")
    private String nickname;

    @Column(nullable = false,length = 500)
    private String content;

    @ColumnDefault("0")
    private int likeCount;

    @ColumnDefault("0")
    private int commentCount;

    public void changePost(String content, int likeCount) {
        this.content = content;
        this.likeCount = likeCount;
    }

}

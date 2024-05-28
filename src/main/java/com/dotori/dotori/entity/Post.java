package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(value = {AuditingEntityListener.class})
public class Post{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private int pid;

    @Column(name = "nickName")
    private String nickName;

    @Column(nullable = false,length = 500)
    private String content;

    @ColumnDefault("0")
    private int likeCount;

    @CreatedDate
    @Builder.Default
    @Column(name = "regDate", updatable = false)
    private LocalDateTime regDate = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "modDate")
    private LocalDateTime modDate;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostThumbnail> thumbnails = new ArrayList<>();


    public void changePost(String content, int likeCount, List<PostThumbnail> thumbnails) {
        this.content = content;
        this.likeCount = likeCount;
        this.thumbnails = thumbnails;
    }

    public void changePost(String content, int likeCount) {
        this.content = content;
        this.likeCount = likeCount;
    }

    public void addThumbnail(PostThumbnail postThumbnail) {
        this.thumbnails.add(postThumbnail);
        postThumbnail.setPost(this);
    }


}

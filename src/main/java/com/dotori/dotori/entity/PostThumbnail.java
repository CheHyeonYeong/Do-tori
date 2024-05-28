package com.dotori.dotori.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "post_thumbnail")
@Getter
@Setter
@NoArgsConstructor
public class PostThumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String thumbnail;

    public PostThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
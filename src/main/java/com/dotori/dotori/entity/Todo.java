package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int aid;

    @Builder.Default
    @Column(length = 50)
    @ColumnDefault("'No category'")
    private String category = "No category";

    @Column(nullable = false,length = 500)
    private String content;

    @ColumnDefault("false")
    private boolean done;

    @CreatedDate
    @Builder.Default
    @Column(name = "todoDate")
    private LocalDateTime todoDate = LocalDateTime.now();

    public void changeTodo(String category, String content, boolean done) {
        this.category = category;
        this.content = content;
        this.done = done;
    }

}

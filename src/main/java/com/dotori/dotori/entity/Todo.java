package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Todo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int aid;

    @Column(length = 50, nullable = false)
    @ColumnDefault("'None'")
    private String category;

    @Column(nullable = false,length = 500)
    private String content;

    @ColumnDefault("false")
    private boolean done;

    public void changeContent(String content){
        this.content = content;
    }

}

package com.dotori.dotori.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoDTO {

    private int id;
    private int aid;
    private String category;
    private String content;
    private boolean done;
    private LocalDateTime todoDate;


}

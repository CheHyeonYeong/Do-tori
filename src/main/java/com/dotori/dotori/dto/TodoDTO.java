package com.dotori.dotori.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TodoDTO {

    private int id;

    private int aid;

    @NotEmpty
    @Builder.Default
    private String category="No category";

    @NotEmpty
    @NotNull
    private String content;

    private boolean done;

    @Builder.Default
    private LocalDate todoDate = LocalDate.now();


}

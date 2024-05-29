package com.dotori.dotori.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToriBoxDTO {

    private int id;

    @NotNull
    private int aid;

    @NotNull
    private int pid;

}

package com.dotori.dotori.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Data
@Builder
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {

    private int id;

    private int aid;

    @Builder.Default
    private String category = "'None'";

    @NotBlank(message = "todo 내용은 빈칸으로 둘 수 없어요")
    private String content;

    @Builder.Default
    private boolean done = false;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

}

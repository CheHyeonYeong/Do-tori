package com.dotori.dotori.dto;

import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ToriBoxDTO {
    private int aid;
    private int pid;


    public ToriBoxDTO(int aid, int pid) {
        this.aid = aid;
        this.pid = pid;
    }
}

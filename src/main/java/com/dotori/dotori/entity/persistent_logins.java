package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@DynamicUpdate // Entity update시, 원하는 데이터만 update하기 위함
@AllArgsConstructor
@NoArgsConstructor
public class persistent_logins {

    @Column(unique = true, name = "username")
    @NotNull
    private String id;

    @Id
    @NotNull
    private String series;

    @NotNull
    private String token;

    @NotNull
    private Timestamp last_used;

}

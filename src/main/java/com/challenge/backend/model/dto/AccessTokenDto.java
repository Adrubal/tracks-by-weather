package com.challenge.backend.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class AccessTokenDto {

    private String appName;
    private String token;
    private LocalDateTime creationDate;
}

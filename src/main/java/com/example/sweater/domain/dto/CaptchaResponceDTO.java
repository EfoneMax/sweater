package com.example.sweater.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaptchaResponceDTO {
    private boolean success;
    private Set<String> errorCode;
}

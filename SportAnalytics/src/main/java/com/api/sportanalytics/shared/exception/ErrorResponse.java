package com.api.sportanalytics.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Setter
@Getter
@Data
public class ErrorResponse {
    private int status;
    private String message;
}

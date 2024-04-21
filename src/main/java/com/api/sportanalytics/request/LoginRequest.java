package com.api.sportanalytics.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LoginRequest {

    private String correo;

    private String contraseña;
}
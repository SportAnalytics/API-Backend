package com.api.sportanalytics.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LoginRequest {
    @NotBlank
    @Email
    private String correo;
    
    @NotBlank
    private String contraseña;
}
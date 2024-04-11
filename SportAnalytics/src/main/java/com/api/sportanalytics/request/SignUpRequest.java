package com.api.sportanalytics.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignUpRequest {
    @NotBlank
    private String nombre;
    
    @NotBlank
    private String apellido;
    
    @NotBlank
    private String direccion;
    
    @NotBlank
    private String telefono;
    
    @NotBlank
    @Email
    private String correo;
    
    @NotBlank
    private String contraseña;

}
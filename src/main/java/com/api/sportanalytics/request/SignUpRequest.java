package com.api.sportanalytics.request;



import lombok.Data;

@Data
public class SignUpRequest {
    private String nombre;
    
    private String apellido;
    
    private String direccion;
    
    private String telefono;
    
    private String correo;
    
    private String contraseña;
}
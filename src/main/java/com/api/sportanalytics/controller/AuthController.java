package com.api.sportanalytics.controller;


import com.api.sportanalytics.response.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.api.sportanalytics.model.Atleta;
import com.api.sportanalytics.model.Perfil;
import com.api.sportanalytics.request.LoginRequest;
import com.api.sportanalytics.request.SignUpRequest;
import com.api.sportanalytics.service.AtletaService;
import com.api.sportanalytics.service.PerfilService;
import com.api.sportanalytics.shared.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AtletaService atletaService;
    private final PerfilService perfilService;

    public AuthController(AtletaService atletaService, PerfilService perfilService) {
        this.atletaService = atletaService;
        this.perfilService = perfilService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
        // Crear un nuevo atleta con los datos proporcionados en la solicitud
        Atleta nuevoAtleta = new Atleta();
        nuevoAtleta.setNombre(signUpRequest.getNombre());
        nuevoAtleta.setApellido(signUpRequest.getApellido());
        nuevoAtleta.setDireccion(signUpRequest.getDireccion());
        nuevoAtleta.setTelefono(signUpRequest.getTelefono());
        nuevoAtleta.setCompetencia(signUpRequest.getObjetivo());

        // Guardar el nuevo atleta en la base de datos
        Atleta atletaGuardado = atletaService.crearAtleta(nuevoAtleta);

        // Crear un nuevo perfil asociado al atleta recién creado
        Perfil nuevoPerfil = new Perfil();
        nuevoPerfil.setAtleta(atletaGuardado);
        nuevoPerfil.setCorreo(signUpRequest.getCorreo());
        nuevoPerfil.setContraseña(signUpRequest.getContraseña());
        nuevoPerfil.setPin(1234);

        // Guardar el nuevo perfil en la base de datos
        perfilService.crearPerfil(nuevoPerfil);

        // Retornar una respuesta de éxito
        return ResponseEntity.ok("Registro exitoso");
    }

    
    // Endpoint para el inicio de sesión (login)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Buscar el perfil asociado al correo electrónico proporcionado en la solicitud
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(loginRequest.getCorreo())
                .orElseThrow(() -> new ResourceNotFoundException( String.format( "Correo: %s not found", loginRequest.getCorreo() )));

        LoginResponse response = new LoginResponse();

        // Verificar si la contraseña proporcionada coincide con la contraseña almacenada en el perfil
        if (perfil.getContraseña().equals(loginRequest.getContraseña())) {
            // La contraseña es correcta, se puede iniciar sesión exitosamente
            response.setId(perfil.getId());
            response.setNombre(perfil.getAtleta().getNombre());
            response.setCompetencia(perfil.getAtleta().getCompetencia());
            response.setResponse("Inicio de sesión exitoso");
            return ResponseEntity.ok(response);
        } else {
            // La contraseña es incorrecta, se devuelve un mensaje de error
            response.setResponse("Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/login/pin")
    public ResponseEntity<LoginResponse> loginPin(@RequestBody LoginRequest loginRequest) {
        // Buscar el perfil asociado al correo electrónico proporcionado en la solicitud
        Perfil perfil = perfilService.obtenerPerfilPorId(loginRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException( String.format( "Correo: %s not found", loginRequest.getCorreo() )));

        LoginResponse response = new LoginResponse();

        // Verificar si la contraseña proporcionada coincide con la contraseña almacenada en el perfil
        if (perfil.getPin().equals(loginRequest.getPin())) {
            // La contraseña es correcta, se puede iniciar sesión exitosamente
            response.setId(perfil.getId());
            response.setNombre(perfil.getAtleta().getNombre());
            response.setResponse("Inicio de sesión exitoso");
            return ResponseEntity.ok(response);
        } else {
            // La contraseña es incorrecta, se devuelve un mensaje de error
            response.setResponse("Credenciales incorrectas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
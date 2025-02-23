package com.salesianos.dam.sportify.user.controller;

import com.salesianos.dam.sportify.security.jwt.access.JwtService;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshToken;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshTokenRequest;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshTokenService;
import com.salesianos.dam.sportify.user.dto.*;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @Operation(summary = "Registra un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha registrado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido registrar el usuario",
                    content = @Content),
    })
    @PostMapping("/user/auth/register")
    public ResponseEntity<UserResponse> registerUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del usuario", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateUserRequest.class),
                    examples = @ExampleObject(value = """
                                {
                                   "username": "manuelgmez",
                                   "password": "Password123.",
                                   "verifyPassword": "Password123.",
                                   "email": "gomez.maman24@triana.salesianos.edu",
                                   "verifyEmail": "gomez.maman24@triana.salesianos.edu",
                                   "fechaNacimiento": "2000-01-01",
                                   "nombre": "Manuel"
                                 }
                            """))) @RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @Operation(summary = "Registra un nuevo usuario con rol escritor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha registrado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido registrar el usuario",
                    content = @Content),
    })
    @PostMapping("/writer/auth/register")
    public ResponseEntity<UserResponse> registerWriter(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del usuario", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateUserRequest.class),
                    examples = @ExampleObject(value = """
                                {
                                   "username": "manuelgmez",
                                   "password": "Password123.",
                                   "verifyPassword": "Password123.",
                                   "email": "gomez.maman24@triana.salesianos.edu",
                                   "verifyEmail": "gomez.maman24@triana.salesianos.edu",
                                   "fechaNacimiento": "2000-01-01",
                                   "nombre": "Manuel"
                                 }
                            """))) @RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createWriter(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }


    @Operation(summary = "Registra un nuevo usuario con rol admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha registrado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido registrar el usuario",
                    content = @Content),
    })
    @PostMapping("/admin/auth/register")
    public ResponseEntity<UserResponse> registerAdmin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del usuario", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateUserRequest.class),
                    examples = @ExampleObject(value = """
                                {
                                   "username": "manuelgmez",
                                   "password": "Password123.",
                                   "verifyPassword": "Password123.",
                                   "email": "gomez.maman24@triana.salesianos.edu",
                                   "verifyEmail": "gomez.maman24@triana.salesianos.edu",
                                   "fechaNacimiento": "2000-01-01",
                                   "nombre": "Manuel"
                                 }
                            """))) @RequestBody @Valid CreateUserRequest createUserRequest) {
        User user = userService.createAdmin(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @Operation(summary = "Inicia sesión con un usuario ya registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha iniciado sesión",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido iniciar sesión",
                    content = @Content),
    })

    @PostMapping("/auth/login")
    public ResponseEntity<UserResponse> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del usuario", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = @ExampleObject(value = """
                                {
                                          "username":"admin_user",
                                          "password":"admin"
                                      }
                            """))) @RequestBody LoginRequest loginRequest) {


        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.username(),
                                loginRequest.password()
                        )
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));

    }


    @Operation(summary = "Genera el token de refresco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha generado el token de refresco para el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido generar el token de refresco para el usuario",
                    content = @Content),
    })
    @PostMapping("/auth/refresh/token")
    public ResponseEntity<UserResponse> refreshToken(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la petición", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RefreshTokenRequest.class),
                    examples = @ExampleObject(value = """
                                {
                                           "refreshToken": "b362dff3-84c0-499d-ab30-6449a40f2264"
                                       }
                            """))) @RequestBody RefreshTokenRequest req) {
        String token = req.refreshToken();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(refreshTokenService.refreshToken(token));
    }


    @Operation(summary = "Activa la cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha activado la cuenta correctamente",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido activar la cuenta",
                    content = @Content),
    })
    @PostMapping("/activate/account/")
    public ResponseEntity<UserResponse> activateAccount(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la petición", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ActivateAccountRequest.class),
                    examples = @ExampleObject(value = """
                                {
                                            "token": "5ec695a8-b267-43f4-9e8f-9216c8588cf7"
                                        }
                            """))) @RequestBody ActivateAccountRequest req) {
        String token = req.token();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(userService.activateAccount(token)));
    }


    @Operation(summary = "Edita al usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario autenticado",
                    content = @Content),
    })
    @PutMapping("/edit/me")
    public GetUsuarioDto updateMe(@AuthenticationPrincipal User user, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la petición", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EditUserDto.class),
                    examples = @ExampleObject(value = """
                                {
                                  "password": "Password1234.",
                                  "verifyPassword": "Password1234.",
                                  "email": "gomez.maman24@triana.salesianos.eduaz",
                                  "verifyEmail": "gomez.maman24@triana.salesianos.eduaz",
                                  "fechaNacimiento": "2000-06-01",
                                  "nombre": "Manuelillo adsjkfhaksljdhfklasjhdklfjhasdlkjhflaksjdhflkasj"
                                }
                            """))) @RequestBody @Valid EditUserDto createUserRequest) {

        User updatedUser = userService.editMe(user, createUserRequest);
        return GetUsuarioDto.of(updatedUser);
    }


    @Operation(summary = "Edita a un usuario buscado por username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario",
                    content = @Content),
    })
    @PutMapping("/edit/{username}")
    public GetUsuarioDto updateUser(@PathVariable String username, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la petición", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EditUserDto.class),
                    examples = @ExampleObject(value = """
                                {
                                  "password": "Password12345.",
                                  "verifyPassword": "Password12345.",
                                  "email": "gomez.maman24@triana.salesianos.eduaz",
                                  "verifyEmail": "gomez.maman24@triana.salesianos.eduaz",
                                  "fechaNacimiento": "2000-06-01",
                                  "nombre": "Manuelillo adsjkfhaksljdhfklasjhdklfjhasdlkjhflaksjdhflkasj"
                                }
                            """))) @RequestBody @Valid EditUserDto createUserRequest) {

        User updatedUser = userService.editUser(username, createUserRequest);
        return GetUsuarioDto.of(updatedUser);
    }

    @Operation(summary = "Obtiene todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado usuarios",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": "a6c97d8f-99d0-43fe-8daf-32b9247c4fc6",
                                                "username": "admin_user"
                                                }                                                                                        }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario autenticado",
                    content = @Content),
    })
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal User user) {
        return UserResponse.of(user);
    }


    @Operation(summary = "Obtiene todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado usuarios",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            {
                                                "id": "a6c97d8f-99d0-43fe-8daf-32b9247c4fc6",
                                                "username": "admin_user"
                                                }                                                                                        }
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario autenticado",
                    content = @Content),
    })
    @GetMapping("/me/admin")
    public UserResponse adminMe(@AuthenticationPrincipal User user) {
        return UserResponse.of(user);
    }


    @Operation(summary = "Borra un usuario buscado por su username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )}),
    })
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Borra al usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )}),
    })
    @DeleteMapping("/delete/me")
    public ResponseEntity<?> deleteMe(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}

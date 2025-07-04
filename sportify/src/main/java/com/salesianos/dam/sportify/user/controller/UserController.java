package com.salesianos.dam.sportify.user.controller;

import com.salesianos.dam.sportify.deporte.dto.FollowDeporteRequest;
import com.salesianos.dam.sportify.equipo.dto.FollowEquipoRequest;
import com.salesianos.dam.sportify.liga.dto.FollowLigaRequest;
import com.salesianos.dam.sportify.like.model.Like;
import com.salesianos.dam.sportify.like.service.LikeService;
import com.salesianos.dam.sportify.security.jwt.access.JwtService;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshToken;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshTokenRequest;
import com.salesianos.dam.sportify.security.jwt.refresh.RefreshTokenService;
import com.salesianos.dam.sportify.user.dto.*;
import com.salesianos.dam.sportify.user.model.User;
import com.salesianos.dam.sportify.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    private final LikeService likeService;

    @Operation(summary = "Registra un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se ha registrado el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "No se ha podido registrar el usuario", content = @Content),
    })
    @PostMapping("/user/auth/register")
    public ResponseEntity<UserResponse> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo del usuario", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequest.class), examples = @ExampleObject(value = """
                        {
                           "username": "manuelgmez",
                           "password": "Password123.",
                           "verifyPassword": "Password123.",
                           "email": "gomez.maman24@triana.salesianos.edu",
                           "verifyEmail": "gomez.maman24@triana.salesianos.edu",
                           "fechaNacimiento": "2000-01-01",
                           "nombre": "Manuel"
                         }
                    """))) @RequestPart("createUserRequest") @Valid CreateUserRequest createUserRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImageFile) {
        User user = userService.createUser(createUserRequest, profileImageFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @Operation(summary = "Registra un nuevo usuario con rol escritor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se ha registrado el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "No se ha podido registrar el usuario", content = @Content),
    })
    @PostMapping("/writer/auth/register")
    public ResponseEntity<UserResponse> registerWriter(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo del usuario", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequest.class), examples = @ExampleObject(value = """
                        {
                           "username": "manuelgmez",
                           "password": "Password123.",
                           "verifyPassword": "Password123.",
                           "email": "gomez.maman24@triana.salesianos.edu",
                           "verifyEmail": "gomez.maman24@triana.salesianos.edu",
                           "fechaNacimiento": "2000-01-01",
                           "nombre": "Manuel"
                         }
                    """))) @RequestPart("createUserRequest") @Valid CreateUserRequest createUserRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImageFile) {
        User user = userService.createWriter(createUserRequest, profileImageFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user));
    }

    @Operation(summary = "Registra un nuevo usuario con rol admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se ha registrado el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "No se ha podido registrar el usuario", content = @Content),
    })
    @PostMapping("/admin/auth/register")
    public ResponseEntity<AdminResponse> registerAdmin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo del usuario", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequest.class), examples = @ExampleObject(value = """
                        {
                           "username": "manuelgmez",
                           "password": "Password123.",
                           "verifyPassword": "Password123.",
                           "email": "gomez.maman24@triana.salesianos.edu",
                           "verifyEmail": "gomez.maman24@triana.salesianos.edu",
                           "fechaNacimiento": "2000-01-01",
                           "nombre": "Manuel"
                         }
                    """))) @RequestPart("createUserRequest") @Valid CreateUserRequest createUserRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImageFile) {
        User user = userService.createAdmin(createUserRequest, profileImageFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AdminResponse.of(user));
    }

    @Operation(summary = "Inicia sesión con un usuario ya registrado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se ha iniciado sesión", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "No se ha podido iniciar sesión", content = @Content),
    })

    @PostMapping("/auth/login")
    public ResponseEntity<UserResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo del usuario", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class), examples = @ExampleObject(value = """
                        {
                              "username":"admin_user",
                              "password":"admin"
                          }
                    """))) @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        if (Boolean.TRUE.equals(user.getDeleted())) {
            throw new org.springframework.security.authentication.BadCredentialsException(
                    "La cuenta ha sido eliminada");
        }

        String accessToken = jwtService.generateAccessToken(user);

        RefreshToken refreshToken = refreshTokenService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserResponse.of(user, accessToken, refreshToken.getToken()));
    }

    @Operation(summary = "Genera el token de refresco")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se ha generado el token de refresco para el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "No se ha podido generar el token de refresco para el usuario", content = @Content),
    })
    @PostMapping("/auth/refresh/token")
    public ResponseEntity<UserResponse> refreshToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenRequest.class), examples = @ExampleObject(value = """
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
            @ApiResponse(responseCode = "201", description = "Se ha activado la cuenta correctamente", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))) }),
            @ApiResponse(responseCode = "400", description = "No se ha podido activar la cuenta", content = @Content),
    })
    @PostMapping("/activate/account/")
    public ResponseEntity<UserResponse> activateAccount(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivateAccountRequest.class), examples = @ExampleObject(value = """
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
            @ApiResponse(responseCode = "200", description = "Se ha editado el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el usuario autenticado", content = @Content),
    })
    @PutMapping("/edit/me")
    public GetUsuarioDto updateMe(@AuthenticationPrincipal User user,
            @RequestPart("editUserDto") @Valid EditUserDto createUserRequest,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImageFile) {

        User updatedUser = userService.editMe(user, createUserRequest, profileImageFile);
        return GetUsuarioDto.of(updatedUser);
    }

    @PutMapping("/edit/password")
    public ResponseEntity<?> upodatePassword(@AuthenticationPrincipal User user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditPasswordDto.class), examples = @ExampleObject(value = """
                        {
                         "oldPassword": "Password12345.",
                          "password": "Password12345,",
                          "verifyPassword": "Password12345,"
                         
                        }
                    """))) @RequestBody @Valid EditPasswordDto editPasswordDto) {

        User updatedUser = userService.editPassword(user, editPasswordDto);
    return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

    @Operation(summary = "Edita a un usuario buscado por username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha editado el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el usuario", content = @Content),
    })
    @PutMapping("/edit/{username}")
    public GetUsuarioDto updateUser(@PathVariable String username,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditUserDto.class), examples = @ExampleObject(value = """
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

    @Operation(summary = "Obtiene todos los datos de mi usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se han encontrado usuarios", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)), examples = {
                            @ExampleObject(value = """
                                    {
                                        "id": "a6c97d8f-99d0-43fe-8daf-32b9247c4fc6",
                                        "username": "admin_user"
                                        }                                                                                        }
                                    """) }) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el usuario autenticado", content = @Content),
    })
    @GetMapping("/me")
    public GetUsuarioDto me(@AuthenticationPrincipal User user) {
        user = userService.getAuthenticatedUser(user.getId());
        return GetUsuarioDto.of(user);
    }

    @Operation(summary = "Borra un usuario buscado por su username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se ha eliminado el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }),
    })
    @DeleteMapping("/admin/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Borra al usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se ha eliminado el usuario", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }),
    })
    @DeleteMapping("/delete/me")
    public ResponseEntity<?> deleteMe(@AuthenticationPrincipal User username) {
        userService.deleteMe(GetUserNoAsociacionesDto.of(username));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Sigue a un equipo ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha seguido al equipo", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado al equipo", content = @Content),
    })
    @PutMapping("/seguirEquipo")
    public GetUsuarioDto seguirEquipo(@AuthenticationPrincipal User user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowEquipoRequest.class), examples = @ExampleObject(value = """
                        {
                             "nombreEquipo":"real-madrid"
                         }
                    """))) @RequestBody @Valid FollowEquipoRequest nombreEquipo) {
        User u = userService.seguirEquipo(user.getUsername(), nombreEquipo);
        return GetUsuarioDto.of(u);
    }

    @Operation(summary = "Deja de seguir a un equipo ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha dejado de seguir al equipo", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado al equipo", content = @Content),
    })
    @PutMapping("/unfollowEquipo")
    public GetUsuarioDto unfollowEquipo(@AuthenticationPrincipal User user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowEquipoRequest.class), examples = @ExampleObject(value = """
                        {
                             "nombreEquipo":"real-madrid"
                         }
                    """))) @RequestBody @Valid FollowEquipoRequest nombreEquipo) {
        User u = userService.dejarDeSeguirEquipo(user.getUsername(), nombreEquipo);
        return GetUsuarioDto.of(u);
    }

    @Operation(summary = "Sigue a un deporte ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha seguido al deporte", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado al deporte", content = @Content),
    })
    @PutMapping("/seguirDeporte")
    public GetUsuarioDto seguirDeporte(@AuthenticationPrincipal User user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowDeporteRequest.class), examples = @ExampleObject(value = """
                        {
                             "nombreDeporte":"real-madrid"
                         }
                    """))) @RequestBody FollowDeporteRequest nombreDeporte) {
        User u = userService.seguirDeporte(user.getUsername(), nombreDeporte);
        return GetUsuarioDto.of(u);
    }

    @Operation(summary = "Deja des seguir  un deporte ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha dejado de seguir al deporte", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado al deporte", content = @Content),
    })
    @PutMapping("/unfollowDeporte")
    public GetUsuarioDto unfollowDeporte(@AuthenticationPrincipal User user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowDeporteRequest.class), examples = @ExampleObject(value = """
                        {
                             "nombreDeporte":"futbol"
                         }
                    """))) @RequestBody FollowDeporteRequest nombreDeporte) {
        User u = userService.dejarDeSeguirDeporte(user.getUsername(), nombreDeporte);
        return GetUsuarioDto.of(u);
    }

    @Operation(summary = "Sigue una Liga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha dejado de seguir la liga", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado la liga", content = @Content),
    })
    @PutMapping("/seguirLiga")
    public GetUsuarioDto seguirLiga(@AuthenticationPrincipal User user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowLigaRequest.class), examples = @ExampleObject(value = """
                        {
                             "nombreLiga":"laliga-easports"
                         }
                    """))) @RequestBody FollowLigaRequest nombreLiga) {
        User u = userService.seguirLiga(user.getUsername(), nombreLiga);
        return GetUsuarioDto.of(u);
    }

    @Operation(summary = "Deja des seguir  una liga ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se ha dejado de seguir a la liga", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetUsuarioDto.class))) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado la liga", content = @Content),
    })
    @PutMapping("/unfollowLiga")
    public GetUsuarioDto unFollowLiga(@AuthenticationPrincipal User user,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la petición", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = FollowLigaRequest.class), examples = @ExampleObject(value = """
                        {
                             "nombreLiga":"laliga-easports"
                         }
                    """))) @RequestBody FollowLigaRequest nombreLiga) {
        User u = userService.dejarDeSeguirLiga(user.getUsername(), nombreLiga);
        return GetUsuarioDto.of(u);
    }

    @Operation(summary = "Obtiene todas las ligas favoritas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se han encontrado ligas", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetLigasFavoritasDto.class)), examples = {
                            @ExampleObject(value = """
                                    "username": "regular_user",
                                        "ligasFavoritas": {
                                            "content": [
                                                {
                                                    "nombreLiga": "LaLiga EaSports"
                                                }
                                            ],
                                            "pageable": {
                                                "pageNumber": 0,
                                                "pageSize": 10,
                                                "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                },
                                                "offset": 0,
                                                "paged": true,
                                                "unpaged": false
                                            },
                                            "last": true,
                                            "totalElements": 1,
                                            "totalPages": 1,
                                            "first": true,
                                            "size": 10,
                                            "number": 0,
                                            "sort": {
                                                "empty": true,
                                                "sorted": false,
                                                "unsorted": true
                                            },
                                            "numberOfElements": 1,
                                            "empty": false
                                        }
                                    }                                                                                       }
                                    """) }) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado la liga", content = @Content),
    })
    @GetMapping("/ligasFavoritas")
    public GetLigasFavoritasDto getLigasFavoritas(@AuthenticationPrincipal User user,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        GetLigasFavoritasDto lF = GetLigasFavoritasDto.of(user,
                userService.findLigasFavoritasByUsername(user.getUsername(), pageable));
        return lF;
    }

    @Operation(summary = "Obtiene todas los deportes favoritos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se han encontrado los deportes", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetDeportesFavoritosDto.class)), examples = {
                            @ExampleObject(value = """
                                    {
                                                       "username": "regular_user",
                                                       "deportesFavoritos": {
                                                           "content": [
                                                               {
                                                                   "nombreDeporte": "Futbol"
                                                               }
                                                           ],
                                                           "pageable": {
                                                               "pageNumber": 0,
                                                               "pageSize": 10,
                                                               "sort": {
                                                                   "empty": true,
                                                                   "sorted": false,
                                                                   "unsorted": true
                                                               },
                                                               "offset": 0,
                                                               "paged": true,
                                                               "unpaged": false
                                                           },
                                                           "last": true,
                                                           "totalPages": 1,
                                                           "totalElements": 1,
                                                           "size": 10,
                                                           "number": 0,
                                                           "sort": {
                                                               "empty": true,
                                                               "sorted": false,
                                                               "unsorted": true
                                                           },
                                                           "first": true,
                                                           "numberOfElements": 1,
                                                           "empty": false
                                                       }
                                                   }                                                                         }
                                    """) }) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado los deportes", content = @Content),
    })
    @GetMapping("/deportesFavoritos")
    public GetDeportesFavoritosDto getDeportesFavoritos(@AuthenticationPrincipal User user,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        GetDeportesFavoritosDto dF = GetDeportesFavoritosDto.of(user,
                userService.findDeportesFavoritosByUsername(user.getUsername(), pageable));
        return dF;
    }

    @Operation(summary = "Obtiene todas los equipops favoritos ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se han encontrado los equipos", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetEquiposFavoritos.class)), examples = {
                            @ExampleObject(value = """
                                    {
                                                       "username": "regular_user",
                                                       "equiposFavoritos": {
                                                           "content": [
                                                               {
                                                                   "nombreEquipo": "Fc Barcelona"
                                                               }
                                                           ],
                                                           "pageable": {
                                                               "pageNumber": 0,
                                                               "pageSize": 10,
                                                               "sort": {
                                                                   "empty": true,
                                                                   "sorted": false,
                                                                   "unsorted": true
                                                               },
                                                               "offset": 0,
                                                               "paged": true,
                                                               "unpaged": false
                                                           },
                                                           "last": true,
                                                           "totalPages": 1,
                                                           "totalElements": 1,
                                                           "size": 10,
                                                           "number": 0,
                                                           "sort": {
                                                               "empty": true,
                                                               "sorted": false,
                                                               "unsorted": true
                                                           },
                                                           "first": true,
                                                           "numberOfElements": 1,
                                                           "empty": false
                                                       }
                                                   }                                                                         }
                                    """) }) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado los equipos", content = @Content),
    })
    @GetMapping("/equiposFavoritos")
    public GetEquiposFavoritos getEquiposFavoritos(@AuthenticationPrincipal User user,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        GetEquiposFavoritos eF = GetEquiposFavoritos.of(user,
                userService.findEquiposFavoritosByUsername(user.getUsername(), pageable));
        return eF;
    }

    @Operation(summary = "Obtiene todas las noticias que le han gustado al usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se han encontrado las noticias", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetNoticiasLikedDto.class)), examples = {
                            @ExampleObject(value = """
                                    {
                                        "username": "regular_user",
                                        "noticiasLiked": {
                                            "content": [],
                                            "pageable": {
                                                "pageNumber": 0,
                                                "pageSize": 10,
                                                "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                },
                                                "offset": 0,
                                                "paged": true,
                                                "unpaged": false
                                            },
                                            "last": true,
                                            "totalPages": 0,
                                            "totalElements": 0,
                                            "first": true,
                                            "size": 10,
                                            "number": 0,
                                            "sort": {
                                                "empty": true,
                                                "sorted": false,
                                                "unsorted": true
                                            },
                                            "numberOfElements": 0,
                                            "empty": true
                                        }
                                    }                                                                         }
                                    """) }) }),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado los equipos", content = @Content),
    })
    @GetMapping("/noticiasLiked")
    public GetNoticiasLikedDto getNoticiasLiked(@AuthenticationPrincipal User user,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        GetNoticiasLikedDto nL = GetNoticiasLikedDto.of(user,
                likeService.findNoticiasLikedByUsuario(user.getUsername(), pageable));
        return nL;
    }

    @Operation(summary = "Cierra la sesión del Usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sesión cerrada exitosamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se pudo cerrar la sesión", content = @Content),
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal User user) {
        refreshTokenService.logout(user);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/all")
    public List<GetUsuarioDto> getAllUsers() {
        return userService.findAllWithAllSeguidos()
                .stream()
                .map(GetUsuarioDto::of)
                .toList();
    }
}

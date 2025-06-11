package com.salesianos.dam.sportify.noticia.controller;

import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.comentario.service.ComentarioService;
import com.salesianos.dam.sportify.deporte.dto.FollowDeporteRequest;
import com.salesianos.dam.sportify.equipo.dto.FollowEquipoRequest;
import com.salesianos.dam.sportify.liga.dto.FollowLigaRequest;
import com.salesianos.dam.sportify.noticia.dto.*;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.service.NoticiaService;
import com.salesianos.dam.sportify.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/noticias")
@Tag(name = "Noticias", description = "Gestión de las operaciones relacionadas con las noticias")
public class NoticiaController {
    private final NoticiaService noticiaService;
    private final ComentarioService comentarioService;


    @Operation(summary = "Crea una nueva noticia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado la noticia",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado una noticia",
                    content = @Content),
    })
    @PostMapping
    public ResponseEntity<GetNoticiaDto> createNoticia(@AuthenticationPrincipal User username, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la noticia", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateNoticiaRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                                "titular": "Como mola crear noticias nuevas",
                                                "cuerpo": "En fin vaya pasada de noticia no te parece?",
                                                "multimedia": [
                                                ],
                                                "fechaPublicacion": "2025-02-23"
                                            }
                            """))) @RequestPart("createNoticiaRequest") @Valid CreateNoticiaRequest createNoticiaRequest,
                             @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(GetNoticiaDto.of(noticiaService.saveNoticia(createNoticiaRequest, username, files)));
    }


    @Operation(summary = "Obtiene todas las noticias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las noticias",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaNoAuthDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                             "titular": "Como mola coleguita kjajsjja",
                                                             "cuerpo": "En fin vaya pasada de noticia no te parece?",
                                                             "multimedia": [],
                                                             "fechaCreacion": "2025-02-23",
                                                             "slug": "como-mola-coleguita-kjajsjja"
                                                         },
                                                         {
                                                             "titular": "Titular de la noticia 1",
                                                             "cuerpo": "Cuerpo de la noticia 1. Este es el contenido de la noticia.",
                                                             "multimedia": null,
                                                             "fechaCreacion": "2025-02-22",
                                                             "slug": "titular-de-la-noticia-1"
                                                         }
                                            ]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia",
                    content = @Content),
    })
    @GetMapping
    public Page<GetNoticiaNoAuthDto> findAllNoticias(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<GetNoticiaNoAuthDto> noticias = noticiaService.findAllNoticias(pageable)
                .map(GetNoticiaNoAuthDto::of);


        return noticias;
    }


    @Operation(summary = "Edita una noticia buscada por su slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado la noticia",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia con ese Slug",
                    content = @Content),
    })
    @PutMapping("/edit/{slug}")
    public GetNoticiaDto editNoticia(
            @PathVariable String slug,
            @AuthenticationPrincipal User me,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Cuerpo de la noticia", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditNoticiaDto.class),
                            examples = @ExampleObject(value = """
                                       {
                                            "titular": "Este es el nuevo titular",
                                            "cuerpo": "Este es el nuevo cuerpo de la noticia."
                                        }
                                    """)))
            @RequestPart("editNoticiaDto") @Valid EditNoticiaDto editNoticiaDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        return GetNoticiaDto.of(noticiaService.editNoticia(slug, editNoticiaDto, me, files));
    }


    @Operation(summary = "Borra una noticia buscada por su slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado la noticia",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Noticia.class))
                    )}),
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITER')")
    @DeleteMapping("/delete/{slug}")
    public ResponseEntity<?> deleteNoticia(@PathVariable String slug, @AuthenticationPrincipal User me) {
        noticiaService.deleteNoticia(slug, me);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Añade el deporte sobre el que trata la noticia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha añadido el deporte",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia con ese Slug",
                    content = @Content),
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITER')")
    @PutMapping("/addDeporte/{slug}")
    public GetNoticiaDto addDeporte(@AuthenticationPrincipal User me, @PathVariable String slug, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la petición", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FollowDeporteRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                                "nombreDeporte": "futbol"
                                            }
                            """))) @RequestBody FollowDeporteRequest followDeporteRequest) {
        Noticia d = noticiaService.addDeporteEnNoticia(me, slug, followDeporteRequest);
        return GetNoticiaDto.of(d);
    }


    @Operation(summary = "Añade el equipo sobre el que trata la noticia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha añadido el equipo",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia con ese Slug",
                    content = @Content),
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'WRITER')")
    @PutMapping("/addEquipo/{slug}")
    public GetNoticiaDto addEquipo(@AuthenticationPrincipal User me, @PathVariable String slug, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la petición", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FollowEquipoRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                                "nombreEquipo": "fc-barcelona"
                                            }
                            """))) @RequestBody FollowEquipoRequest followEquipoRequest) {
        Noticia d = noticiaService.addEquipoEnNoticia(me, slug, followEquipoRequest);
        return GetNoticiaDto.of(d);
    }


    @Operation(summary = "Añade la liga sobre la que trata la noticia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha añadido la liga",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia con ese Slug",
                    content = @Content),
    })
    @PutMapping("/addLiga/{slug}")
    public GetNoticiaDto addLiga(@AuthenticationPrincipal User me, @PathVariable String slug, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la petición", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FollowLigaRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                                "nombreLiga": "la-liga-easports"
                                            }
                            """))) @RequestBody FollowLigaRequest followLigaRequest) {
        Noticia d = noticiaService.addLigaNoticia(me, slug, followLigaRequest);
        return GetNoticiaDto.of(d);
    }
    @Operation(summary = "Obtiene todos los comentarios de una noticia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las noticias",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaNoAuthDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [  {
                                                        "titular": "Titular de la noticia 9",
                                                        "cuerpo": "Cuerpo de la noticia 9. Este es el contenido de la noticia.",
                                                        "multimedia": null,
                                                        "fechaCreacion": "2025-02-17",
                                                        "slug": "titular-de-la-noticia-9",
                                                        "usuario": {
                                                            "username": "writer_user"
                                                        },
                                                        "equipo": null,
                                                        "liga": null,
                                                        "deporte": null
                                                    },
                                                    {
                                                        "titular": "Titular de la noticia 10",
                                                        "cuerpo": "Cuerpo de la noticia 10. Este es el contenido de la noticia.",
                                                        "multimedia": null,
                                                        "fechaCreacion": "2025-02-16",
                                                        "slug": "titular-de-la-noticia-10",
                                                        "usuario": {
                                                            "username": "writer_user"
                                                        },
                                                        "equipo": null,
                                                        "liga": null,
                                                        "deporte": null
                                                    }
                                                   ]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia",
                    content = @Content),
    })
    @GetMapping("/{slug}/comentarios")
    public GetNoticiaComentarioDto findComentariosByNoticiaSlug(@PathVariable String slug, @PageableDefault(size = 10, page = 0) Pageable pageable) {


        NoticiaService.NoticiaWithComentarios result = noticiaService.findNoticiaWithComentariosBySlug(slug, pageable);

        GetNoticiaComentarioDto response = GetNoticiaComentarioDto.of(result.getNoticia(), result.getComentarios());

        return response;
    }

    @Operation(summary = "Obtiene todas las noticias filtradas por criterios y paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado las noticias",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaNoAuthDto.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [  {
                                                        "titular": "Titular de la noticia 9",
                                                        "cuerpo": "Cuerpo de la noticia 9. Este es el contenido de la noticia.",
                                                        "multimedia": null,
                                                        "fechaCreacion": "2025-02-17",
                                                        "slug": "titular-de-la-noticia-9",
                                                        "usuario": {
                                                            "username": "writer_user"
                                                        },
                                                        "equipo": null,
                                                        "liga": null,
                                                        "deporte": null
                                                    },
                                                    {
                                                        "titular": "Titular de la noticia 10",
                                                        "cuerpo": "Cuerpo de la noticia 10. Este es el contenido de la noticia.",
                                                        "multimedia": null,
                                                        "fechaCreacion": "2025-02-16",
                                                        "slug": "titular-de-la-noticia-10",
                                                        "usuario": {
                                                            "username": "writer_user"
                                                        },
                                                        "equipo": null,
                                                        "liga": null,
                                                        "deporte": null
                                                    }
                                                   ]
                                            """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia",
                    content = @Content),
    })
    @GetMapping("/filtradas")
    public Page<GetNoticiaNoAuthDto> findNoticiasByCriteria(@RequestParam(required = false) String username,
                                                            @RequestParam(required = false) String slug,
                                                            @RequestParam(required = false) String deporte,
                                                            @RequestParam(required = false) String liga,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaInicio,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaFin,
                                                            @PageableDefault(size = 10, sort = "fechaPublicacion", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Noticia> n = noticiaService.getNoticiasFiltradas(username, slug, deporte, liga, fechaInicio, fechaFin, pageable);
        return n.map(GetNoticiaNoAuthDto::of);

    }

}

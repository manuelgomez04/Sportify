package com.salesianos.dam.sportify.noticia.controller;

import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.dto.EditNoticiaDto;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaNoAuthDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/noticias")
@Tag(name = "Noticias", description = "Gestión de las operaciones relacionadas con las noticias")
public class NoticiaController {
    private final NoticiaService noticiaService;


    @Operation(summary = "Crea un nuevo historico de curso para un alumno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado el historico de curso",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado un historico de curso",
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
                            """))) @RequestBody @Valid CreateNoticiaRequest createNoticiaRequest) {
        return ResponseEntity.ok(GetNoticiaDto.of(noticiaService.saveNoticia(createNoticiaRequest, username)));
    }


    @Operation(summary = "Obtiene todos los alumnos")
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
            @PageableDefault(size = 10, page = 0) Pageable pageable) { // Configuración por defecto
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
    public GetNoticiaDto editNoticia(@PathVariable String slug, @AuthenticationPrincipal User me, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la noticia", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EditNoticiaDto.class),
                    examples = @ExampleObject(value = """
                               {
                                                "titular": "Este es el nuevo titular",
                                                "cuerpo": "Este es el nuevo cuerpo de la noticia.",
                                                "multimedia": [
                                                    "imagen1.jpg",
                                                    "video1.mp4"
                                                ]
                                            }
                            """))) @RequestBody @Valid EditNoticiaDto createNoticiaRequest) {
        return GetNoticiaDto.of(noticiaService.editNoticia(slug, createNoticiaRequest, me));
    }


    @Operation(summary = "Borra una noticia buscada por su slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el usuario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Noticia.class))
                    )}),
    })
    @PostAuthorize("hasAnyRole('ADMIN', 'WRITER')")
    @DeleteMapping("/delete/{slug}")
    public ResponseEntity<?> deleteNoticia(@PathVariable String slug, @AuthenticationPrincipal User me) {
        noticiaService.deleteNoticia(slug, me);
        return ResponseEntity.noContent().build();
    }

}

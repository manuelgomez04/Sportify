package com.salesianos.dam.sportify.comentario.controller;

import com.salesianos.dam.sportify.comentario.dto.CreateComentarioRequest;
import com.salesianos.dam.sportify.comentario.dto.EditComentarioRequest;
import com.salesianos.dam.sportify.comentario.dto.GetComentarioDto;
import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.comentario.model.ComentariosPk;
import com.salesianos.dam.sportify.comentario.service.ComentarioService;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.dto.EditNoticiaDto;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import com.salesianos.dam.sportify.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comentario")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @Operation(summary = "Crea un nuevo comentario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado el comentario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetComentarioDto.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado el comentario",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<GetComentarioDto> createComentario(@AuthenticationPrincipal User user, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del comentario", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateComentarioRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                   "titular": "titular-de-la-noticia-",
                                   "titulo":"Noticia jodidamente asquerosa",
                                   "comentario": "vaya mierda de noticia"
                               }
                            """))) @RequestBody @Valid CreateComentarioRequest request) {

        Comentario c = comentarioService.createComentario(user, request.titular(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(GetComentarioDto.of(c));
    }


    @Operation(summary = "Edita un comentario de una noticia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha editado la el comentario",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetComentarioDto.class))
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna noticia con ese Slug",
                    content = @Content),
    })
    @PutMapping("/{slug}")
    public ResponseEntity<GetComentarioDto> editComentario(@AuthenticationPrincipal User user, @PathVariable String slug, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la noticia", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EditComentarioRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                   "titulo": "con el tiempo es mejor",
                                   "comentario": "yo que se no lo se"
                               }
                            """))) @RequestBody @Valid EditComentarioRequest request) {
        Comentario c = comentarioService.editComentario(user, request, slug);
        return ResponseEntity.ok(GetComentarioDto.of(c));
    }

    @DeleteMapping("/{slug}/{username}")
    public ResponseEntity<?> deleteComentarioAdmin(@PathVariable String slug, @PathVariable String username) {
        comentarioService.deleteComentario(slug, username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<?> deleteComentario(@AuthenticationPrincipal User user, @PathVariable String slug) {
        comentarioService.deleteComentarioMe(user.getUsername(), slug);
        return ResponseEntity.noContent().build();
    }

}



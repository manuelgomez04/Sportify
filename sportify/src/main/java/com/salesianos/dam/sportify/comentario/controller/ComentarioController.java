package com.salesianos.dam.sportify.comentario.controller;

import com.salesianos.dam.sportify.comentario.dto.CreateComentarioRequest;
import com.salesianos.dam.sportify.comentario.dto.GetComentarioDto;
import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.comentario.service.ComentarioService;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
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
                    content = @Content),
    })
    @PostMapping
    public ResponseEntity<GetComentarioDto> createComentario(@AuthenticationPrincipal User user,@io.swagger.v3.oas.annotations.parameters.RequestBody(
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
}

package com.salesianos.dam.sportify.like.controller;

import com.salesianos.dam.sportify.comentario.dto.CreateComentarioRequest;
import com.salesianos.dam.sportify.comentario.dto.GetComentarioDto;
import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.like.dto.CreateLikeRequest;
import com.salesianos.dam.sportify.like.dto.GetLikeDto;
import com.salesianos.dam.sportify.like.model.Like;
import com.salesianos.dam.sportify.like.service.LikeService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "Da me gusta a la noticia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha dado el me gusta",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetLikeDto.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado el comentario",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<GetLikeDto> createComentario(@AuthenticationPrincipal User user, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del comentario", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateLikeRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                   "titular": "titular-de-la-noticia-",
                            
                               }
                            """))) @RequestBody @Valid CreateLikeRequest request) {

        Like l = likeService.createLike(user, request.titular(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(GetLikeDto.of(l));
    }

}

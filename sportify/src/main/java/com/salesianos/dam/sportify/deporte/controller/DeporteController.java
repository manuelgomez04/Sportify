package com.salesianos.dam.sportify.deporte.controller;

import com.salesianos.dam.sportify.deporte.dto.CreateDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.service.DeporteService;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import com.salesianos.dam.sportify.noticia.model.Noticia;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deporte")
@RequiredArgsConstructor
@Tag(name = "Deporte", description = "Gestión de deportes")
public class DeporteController {

    private final DeporteService deporteService;


    @Operation(summary = "Crea un nuevo deporte")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado el deporte",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado el deporte",
                    content = @Content),
    })
    @PostAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Deporte> createDeporte(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo del deporte", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateDeporteRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                    {
                                        "nombre": "Futbol",
                                        "descripcion": "Deporte de 11 contra 11"
                                        }
                                    }
                            """))) @RequestBody @Valid CreateDeporteRequest deporte) {
        return ResponseEntity.ok(deporteService.createDeporte(deporte));
    }


    @Operation(summary = "Borra un deporte buscado por su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el deporte",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Deporte.class))
                    )}),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteDeporte(@PathVariable String name) {
        deporteService.deleteDeporte(name);
        return ResponseEntity.noContent().build();
    }
}

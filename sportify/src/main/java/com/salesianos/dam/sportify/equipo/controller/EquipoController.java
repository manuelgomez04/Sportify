package com.salesianos.dam.sportify.equipo.controller;

import com.salesianos.dam.sportify.equipo.dto.CreateEquipoRequest;
import com.salesianos.dam.sportify.equipo.dto.GetEquipoDto;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.equipo.service.EquipoService;
import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/equipo")
@RequiredArgsConstructor
@Tag(name = "Equipo", description = "Gestión de las operaciones relacionadas con los equipos")
public class EquipoController {

    private final EquipoService equipoService;


    @Operation(summary = "Crea un nuevo equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado el equipo",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GetEquipoDto.class))
                    )}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha creado una noticia",
                    content = @Content),
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GetEquipoDto> createEquipo(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la noticia", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateNoticiaRequest.class),
                    examples = @ExampleObject(value = """
                               {
                                   "nombre": "Recreativo de Huelva",
                                   "nombreLiga":"laliga-easports",
                                   "ciudad": "Huelva",
                                   "pais": "España",
                                   "fechaCreacion":"1889-01-01"
                               }
                            """))) @RequestPart("equipo") @Valid CreateEquipoRequest createEquipoRequest, @RequestPart("escudo") MultipartFile escudo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GetEquipoDto.of(equipoService.createEquipo(createEquipoRequest, escudo)));
    }

   

    @GetMapping("/{nombreLiga}")
    public Page<GetEquipoDto> getEquiposPorLiga(@PathVariable String nombreLiga, Pageable pageable) {
        return equipoService.getEquiposPorLiga(nombreLiga, pageable).map(GetEquipoDto::of);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{nombre}")
    public ResponseEntity<?> deleteEquipo(@PathVariable String nombre) {
        equipoService.deleteEquipo(nombre);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/paginados-por-liga")
    public Page<GetEquipoDto> getEquiposPaginadosPorLiga(Pageable pageable) {
        return equipoService.findAllOrderByLigaNombre(pageable).map(GetEquipoDto::of);
    }
}

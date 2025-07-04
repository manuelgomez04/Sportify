package com.salesianos.dam.sportify.liga.controller;

import com.salesianos.dam.sportify.deporte.dto.CreateDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.liga.dto.CreateLigaRequest;
import com.salesianos.dam.sportify.liga.dto.GetLigaDto;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.liga.service.LigaService;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/liga")
@RequiredArgsConstructor
public class LigaController {

        private final LigaService ligaService;

        @Operation(summary = "Crea una nueva liga")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Se ha creado la liga", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetNoticiaDto.class))) }),
                        @ApiResponse(responseCode = "400", description = "No se ha creado la noticia", content = @Content),
        })
        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping
        public ResponseEntity<GetLigaDto> createLiga(
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la liga", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateLigaRequest.class), examples = @ExampleObject(value = """
                                           {
                                                {
                                                    "nombre": "LaLiga EaSports",
                                                    "descripcion":"Es la primera división española de futbol",
                                                    "nombreDeporte": "Futbol",
                                                    "imagen": "https://example.com/imagen.jpg"
                                                }
                                        """))) @RequestPart("createLigaRequest") @Valid CreateLigaRequest createLigaRequest,
                        @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(GetLigaDto.of(ligaService.createLiga(createLigaRequest, imagen)));
        }

        @Operation(summary = "Borra una liga buscada por su nombre")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Se ha eliminado la liga", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Liga.class))) }),
        })
        @PreAuthorize("hasRole('ADMIN')")
        @DeleteMapping("/delete/{nombre}")
        public ResponseEntity<?> deleteLiga(@PathVariable String nombre) {
                ligaService.deleteLiga(nombre);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{deporteNombreNoEspacio}")
        public Page<GetLigaDto> getLigasPorDeporte(
                        @PathVariable String deporteNombreNoEspacio,
                        Pageable pageable) {
                return ligaService.findByDeporteNombreNoEspacio(deporteNombreNoEspacio, pageable).map(GetLigaDto::of);
        }

        @GetMapping
        public List<GetLigaDto> getAllLigas() {
                return ligaService.getAllLigas().stream()
                                .map(GetLigaDto::of)
                                .toList();
        }

}

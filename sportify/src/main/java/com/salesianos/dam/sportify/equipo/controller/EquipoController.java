package com.salesianos.dam.sportify.equipo.controller;

import com.salesianos.dam.sportify.equipo.dto.CreateEquipoRequest;
import com.salesianos.dam.sportify.equipo.dto.GetEquipoDto;
import com.salesianos.dam.sportify.equipo.service.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/equipo")
@RequiredArgsConstructor
public class EquipoController {

    private final EquipoService equipoService;

    @PostMapping
    public ResponseEntity<GetEquipoDto> createEquipo(@RequestPart("equipo") @Valid CreateEquipoRequest createEquipoRequest, @RequestPart("escudo") MultipartFile escudo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(GetEquipoDto.of(equipoService.createEquipo(createEquipoRequest, escudo)));
    }
}

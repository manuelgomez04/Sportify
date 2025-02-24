package com.salesianos.dam.sportify.deporte.controller;

import com.salesianos.dam.sportify.deporte.dto.CreateDeporteRequest;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.deporte.service.DeporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deporte")
@RequiredArgsConstructor
public class DeporteController {

    private final DeporteService deporteService;

    @PostAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Deporte> createDeporte(@RequestBody @Valid CreateDeporteRequest deporte) {
        return ResponseEntity.ok(deporteService.createDeporte(deporte));
    }

}

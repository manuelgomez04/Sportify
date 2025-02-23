package com.salesianos.dam.sportify.noticia.controller;

import com.salesianos.dam.sportify.noticia.dto.CreateNoticiaRequest;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaDto;
import com.salesianos.dam.sportify.noticia.dto.GetNoticiaNoAuthDto;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.noticia.service.NoticiaService;
import com.salesianos.dam.sportify.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/noticias")
public class NoticiaController {
    private final NoticiaService noticiaService;

    @PostMapping
    public ResponseEntity<GetNoticiaDto> createNoticia(@AuthenticationPrincipal User username, @RequestBody @Valid CreateNoticiaRequest createNoticiaRequest) {
        return ResponseEntity.ok(GetNoticiaDto.of(noticiaService.saveNoticia(createNoticiaRequest, username)));
    }

    @GetMapping
    public Page<GetNoticiaNoAuthDto> findAllNoticias(
            @PageableDefault(size = 10, page = 0) Pageable pageable) { // Configuración por defecto
        Page<GetNoticiaNoAuthDto> noticias = noticiaService.findAllNoticias(pageable)
                .map(GetNoticiaNoAuthDto::of);


        return noticias;
    }


}

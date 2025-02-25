package com.salesianos.dam.sportify.query;

import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import com.salesianos.dam.sportify.util.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

@Log
@AllArgsConstructor
public abstract class GenericSpecificationBuilder<U> {

    private List<SearchCriteria> params;


    public Specification<U> build() {
        if (params.isEmpty()) {
            return null;
        }

        log.info("Adding first specification: " + params.get(0));
        Specification<U> result = build(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            log.info("Adding new specification: " + params.get(i));
            result = result.and(build(params.get(i)));
        }

        log.info("Final Specification: " + result.toString());
        return result;
    }

    private Specification<U> build(SearchCriteria criteria) {
        return (Root<U> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            String key = criteria.key();
            String operation = criteria.operation();
            Object value = criteria.value();

            // Validar que el valor no sea nulo
            if (value == null) {
                return null; // O lanzar una excepción si prefieres
            }

            // Ignorar mayúsculas y minúsculas en las búsquedas de texto
            if (operation.equalsIgnoreCase(":")) {
                if (key.equals("autor.username")) {
                    return builder.like(
                            builder.lower(root.join("autor").get("username")), // Convertir a minúsculas
                            "%" + value.toString().toLowerCase() + "%" // Coincidencia parcial
                    );
                } else if (key.equals("slug")) {
                    // Usar el método para generar el slug
                    String slug = generarSlug(value.toString());
                    return builder.like(
                            builder.lower(root.get("slug")), // Convertir a minúsculas
                            "%" + slug.toLowerCase() + "%" // Coincidencia parcial
                    );
                } else if (key.equals("deporteNoticia.nombre")) {
                    return builder.like(
                            builder.lower(root.join("deporteNoticia").get("nombre")), // Convertir a minúsculas
                            "%" + value.toString().toLowerCase() + "%" // Coincidencia parcial
                    );
                } else if (key.equals("ligaNoticia.nombreNoEspacio")) {
                    // Usar el método para generar el nombre sin espacios
                    String nombreNoEspacio = generarNombreNoEspacio(value.toString());
                    return builder.like(
                            builder.lower(root.join("ligaNoticia").get("nombreNoEspacio")), // Convertir a minúsculas
                            "%" + nombreNoEspacio.toLowerCase() + "%" // Coincidencia parcial
                    );
                }
            }

            // Manejar otros operadores (>, <, etc.)
            switch (operation) {
                case ">":
                    if (value instanceof LocalDate) {
                        return builder.greaterThanOrEqualTo(root.get(key), (LocalDate) value); // Filtro por fecha posterior
                    } else {
                        throw new IllegalArgumentException("El valor para el operador '>' debe ser de tipo LocalDate");
                    }
                case "<":
                    if (value instanceof LocalDate) {
                        return builder.lessThanOrEqualTo(root.get(key), (LocalDate) value); // Filtro por fecha anterior
                    } else {
                        throw new IllegalArgumentException("El valor para el operador '<' debe ser de tipo LocalDate");
                    }
                case ":":
                    return builder.equal(root.get(key), value); // Comparación exacta
                default:
                    return null;
            }
        };
    }


    private String generarSlug(String titular) {
        return titular.toLowerCase()
                .replace(" ", "-")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replaceAll("[^a-z0-9-]", "");
    }

    private String generarNombreNoEspacio(String nombre) {
        return nombre.toLowerCase()
                .replace(" ", "-")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replaceAll("[^a-z0-9-]", "");
    }

}

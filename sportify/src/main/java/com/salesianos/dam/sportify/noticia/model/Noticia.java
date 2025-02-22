package com.salesianos.dam.sportify.noticia.model;

import com.salesianos.dam.sportify.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Noticia {

    @Id
    @GeneratedValue
    private UUID ID;

    private String titular;
    private String cuerpo;
    private List<String> multimedia;
    private LocalDate fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_noticia_autor"))
    private User autor;
}

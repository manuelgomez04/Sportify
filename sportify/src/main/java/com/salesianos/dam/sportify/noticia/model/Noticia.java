package com.salesianos.dam.sportify.noticia.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.salesianos.dam.sportify.comentario.model.Comentario;
import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.liga.model.Liga;
import com.salesianos.dam.sportify.like.model.Like;
import com.salesianos.dam.sportify.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.*;

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

    private String slug;

    private String cuerpo;

    private List<String> multimedia;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaPublicacion;

    @ManyToOne
    @JoinColumn(name = "autor_id", foreignKey = @ForeignKey(name = "fk_noticia_autor"))
    private User autor;

    @ManyToOne
    @JoinColumn(name = "deporte_id", foreignKey = @ForeignKey(name = "fk_noticia_deporte"))
    private Deporte deporteNoticia;

    @ManyToOne
    @JoinColumn(name = "equipo_id", foreignKey = @ForeignKey(name = "fk_noticia_equipo"))
    private Equipo equipoNoticia;

    @ManyToOne
    @JoinColumn(name = "liga_id", foreignKey = @ForeignKey(name = "fk_noticia_liga"))
    private Liga ligaNoticia;

    public void generarSlug() {
        this.slug = this.titular.toLowerCase()
                .replace(" ", "-")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replaceAll("[^a-z0-9-]", "");
    }

    @OneToMany(mappedBy = "noticia_comentario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonManagedReference
    private Set<Comentario> comentarios = new HashSet<>();

    @OneToMany(mappedBy = "noticia_like", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @JsonManagedReference
    private Set<Like> likes = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Noticia noticia = (Noticia) o;
        return getID() != null && Objects.equals(getID(), noticia.getID());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

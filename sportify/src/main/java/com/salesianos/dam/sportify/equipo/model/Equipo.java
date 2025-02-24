package com.salesianos.dam.sportify.equipo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianos.dam.sportify.liga.model.Liga;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Equipo {

    @Id
    @GeneratedValue
    private UUID id;


    private String nombre;

    private String ciudad;

    private String pais;

    private String escudo;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCreacion;

    private String nombreNoEspacio;

    public void generarNombreNoEspacio() {
        this.nombreNoEspacio = this.nombre.toLowerCase()
                .replace(" ", "-")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replaceAll("[^a-z0-9-]", "");
    }


    @ManyToOne
    @JoinColumn(name = "liga_id", foreignKey = @ForeignKey(name = "fk_equipo_liga"))
    private Liga liga;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Equipo equipo = (Equipo) o;
        return getId() != null && Objects.equals(getId(), equipo.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

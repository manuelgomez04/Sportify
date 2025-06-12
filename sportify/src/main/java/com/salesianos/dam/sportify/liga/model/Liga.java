package com.salesianos.dam.sportify.liga.model;

import com.salesianos.dam.sportify.deporte.model.Deporte;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Liga {

    @Id
    @GeneratedValue
    private UUID id;

    private String nombre;
    private String descripcion;

    private String imagen;

    @ManyToOne
    @JoinColumn(name = "deporte_id", foreignKey = @ForeignKey(name = "fk_liga_deporte"))
    private Deporte deporte;

    private String nombreNoEspacio;

    @OneToMany(mappedBy = "liga", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<Equipo> equipos = new ArrayList<>();


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

    public void addEquipo(Equipo e) {
        this.equipos.add(e);
        e.setLiga(this);
    }

    public void deleteEquipo(Equipo e) {
        this.equipos.remove(e);
        e.setLiga(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Liga liga = (Liga) o;
        return getId() != null && Objects.equals(getId(), liga.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

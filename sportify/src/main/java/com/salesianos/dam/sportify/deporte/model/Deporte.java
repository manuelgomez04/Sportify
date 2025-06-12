package com.salesianos.dam.sportify.deporte.model;

import com.salesianos.dam.sportify.liga.model.Liga;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Deporte {

    @Id
    @GeneratedValue
    private UUID id;

    private String nombre;
    private String descripcion;

    private String imagen;

    private String nombreNoEspacio;

    @OneToMany(mappedBy = "deporte", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private java.util.List<Liga> ligas = new ArrayList<>();

    public void addLiga(Liga l) {
        this.ligas.add(l);
        l.setDeporte(this);
    }

    public void deleteLiga(Liga l) {
        this.ligas.remove(l);
        l.setDeporte(null);
    }

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

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        Deporte deporte = (Deporte) o;
        return getId() != null && Objects.equals(getId(), deporte.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

}

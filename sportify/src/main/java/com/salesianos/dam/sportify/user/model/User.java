package com.salesianos.dam.sportify.user.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianos.dam.sportify.equipo.model.Equipo;
import com.salesianos.dam.sportify.noticia.model.Noticia;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE usuario SET deleted = true WHERE id = ?")
@FilterDef(name = "deletedUsuarioFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String username;

    private String password;


    private String nombre;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;

    private String email;


    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Builder.Default
    private boolean enabled = false;
    private Boolean deleted = Boolean.FALSE;


    private String activationToken;

    @Builder.Default
    private Instant createdAt = Instant.now();


    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<Noticia> noticias = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    @Builder.Default
    @JoinTable(name = "usuario_equipo",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "equipo_id"))
    private Set<Equipo> equiposSeguidos = new HashSet<>();

    public void addEquipo(Equipo e) {
        equiposSeguidos.add(e);
    }

    public void removeEquipo(Equipo e) {
        equiposSeguidos.remove(e);
    }



    public void addNoticia(Noticia n) {
        noticias.add(n);
        n.setAutor(this);
    }

    public void removeNoticia(Noticia n) {
        noticias.remove(n);
        n.setAutor(null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

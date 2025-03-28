package com.hive5.hive5.model;

import com.hive5.hive5.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = true, unique = true, length = 100)
    private  String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
//    private Post profileImage;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
//    private Post coverImage;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
//    private List<Post> images;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private  LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.enabled = true;
        this.role = Role.USER;
    }

    @PreUpdate
    public void setUpdateAt() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // Ako su reference iste, objekti su jednaki
        if (obj == null || getClass() != obj.getClass()) return false; // Ako su klase različite, nisu jednaki
        User user = (User) obj;
        return Objects.equals(id, user.id); // Poređenje samo po ID-u
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Hashcode generisan na osnovu ID-a
    }

}

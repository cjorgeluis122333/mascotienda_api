package com.jorgeluis.mascotionda.model.entity.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table
@Entity
@Getter
@Setter
@EqualsAndHashCode
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    String password;
    @Email
    String email;
    String picture;

    @Transient
    boolean isAdmin;
    boolean enabled;

    @ManyToMany
    @JsonIgnoreProperties({"users", "handler", "hibernateLazyInitializer"})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "user_id"})
    )
    List<RoleEntity> roleEntities;

    public UserEntity() {
        roleEntities = new ArrayList<>();

    }

    @PrePersist
    public void prePersist() {
        enabled = true;
    }


    public UserEntity(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", enabled=" + enabled +
                ", roleEntities=" + roleEntities +
                '}';
    }

}

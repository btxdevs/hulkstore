package com.btxdev.hulkstore.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@ToString
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 30)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private Role rol;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "password_hash")
    private String passwordHash;

    public enum Role{
        USER, ADMIN
    }

}
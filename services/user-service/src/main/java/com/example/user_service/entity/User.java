package com.example.user_service.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tbluser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    long id;

    @Column(name = "username", unique = true, nullable = false)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "fullname", nullable = false)
    String fullname;

    @Column(name = "phoneNumber", unique = true, nullable = false)
    String phoneNumber;

    @Column(name = "email", unique = true, nullable = false)
    String email;   

    @Column(name = "dob", nullable = true)
    LocalDate dob;

    @ManyToMany
    @JoinTable(
        name = "tbl_users_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_name")
    )
    Set<Role> roles;

    public User(long id, String fullname) {
        this.id = id;
        this.fullname = fullname;
    }
}
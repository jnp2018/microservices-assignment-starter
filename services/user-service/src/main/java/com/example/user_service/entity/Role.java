package com.example.user_service.entity;
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
@Table(name = "tblrole")
public class Role {
    @Id
    @Column(name = "name", unique = true, nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @ManyToMany
    @JoinTable(
        name = "tbl_role_permission",
        joinColumns = @JoinColumn(name = "role_name"),
        inverseJoinColumns = @JoinColumn(name = "permission_name")
    )
    Set<Permission> permissions;
}

package com.example.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tblpermission")
public class Permission {
    @Id
    @Column(name = "name", unique = true, nullable = false)
    String name;
    
    @Column(name = "description")
    String description;
}

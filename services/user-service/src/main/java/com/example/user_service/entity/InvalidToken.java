package com.example.user_service.entity;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "tblinvalidtoken")
public class InvalidToken {
    @Id
    String id;

    Date expiryTime;
}

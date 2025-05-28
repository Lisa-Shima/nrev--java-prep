// src/main/java/com/example/nrevbook/model/AuditLog.java
package com.example.nrevbook.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entityType;   // e.g. "Book"
    private Long   entityId;     // the PK of the entity changed
    private String action;       // CREATE, UPDATE, DELETE
    private String username;     // who did it
    private Instant timestamp;   // when
}

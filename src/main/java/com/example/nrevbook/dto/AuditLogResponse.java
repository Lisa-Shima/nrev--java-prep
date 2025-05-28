// src/main/java/com/example/nrevbook/dto/AuditLogResponse.java
package com.example.nrevbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class AuditLogResponse {
    private Long    id;
    private String  entityType;
    private Long    entityId;
    private String  action;
    private String  username;
    private Instant timestamp;
}

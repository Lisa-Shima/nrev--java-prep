// src/main/java/com/example/nrevbook/service/AuditLogService.java
package com.example.nrevbook.service;

import com.example.nrevbook.model.AuditLog;
import com.example.nrevbook.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository repo;

    public void log(String entityType, Long entityId, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String user = (auth != null &&
                auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken))
                ? auth.getName()
                : "SYSTEM";

        AuditLog entry = new AuditLog();
        entry.setEntityType(entityType);
        entry.setEntityId(entityId);
        entry.setAction(action);
        entry.setUsername(user);
        entry.setTimestamp(Instant.now());
        repo.save(entry);
    }
}

// src/main/java/com/example/nrevbook/repository/AuditLogRepository.java
package com.example.nrevbook.repository;

import com.example.nrevbook.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> { }

package com.example.nrevbook.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null ||
                !auth.isAuthenticated() ||
                auth instanceof AnonymousAuthenticationToken) {
            // no user in context → attribute actions to SYSTEM
            return Optional.of("SYSTEM");
        }

        return Optional.of(auth.getName());
    }
}


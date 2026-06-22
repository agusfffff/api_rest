package com.example.library.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    RESEARCHER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}

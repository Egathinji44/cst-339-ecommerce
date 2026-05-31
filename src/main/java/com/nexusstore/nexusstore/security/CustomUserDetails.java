package com.nexusstore.nexusstore.security;

import com.nexusstore.nexusstore.models.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security UserDetails wrapper around UserModel.
 * Exposes firstName and role for use in Thymeleaf via #authentication.principal.
 */
public class CustomUserDetails implements UserDetails {

    private final UserModel userModel;

    public CustomUserDetails(UserModel userModel) {
        this.userModel = userModel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userModel.getRole()));
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
    }

    public String getFirstName() {
        return userModel.getFirstName();
    }

    public String getRole() {
        return userModel.getRole();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

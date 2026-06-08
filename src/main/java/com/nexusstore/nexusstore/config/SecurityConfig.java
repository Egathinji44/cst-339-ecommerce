package com.nexusstore.nexusstore.config;

import com.nexusstore.nexusstore.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security configuration for Milestone 7.
 *
 * <p>Two independent filter chains are registered:
 * <ol>
 *   <li><strong>API chain</strong> (order 1) – matches {@code /api/**} requests,
 *       enforces stateless HTTP Basic Authentication backed by the MongoDB
 *       {@code users} collection. No session is created for REST clients.</li>
 *   <li><strong>Web chain</strong> (order 2) – all remaining requests use the
 *       existing form-based login flow from Milestone 6.</li>
 * </ol>
 *
 * <p>Both chains share the same {@link DaoAuthenticationProvider}, so the same
 * MongoDB user credentials work for both the browser UI and the REST API.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * BCrypt password encoder – shared by both filter chains.
     *
     * @return a BCryptPasswordEncoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DAO authentication provider wired to the MongoDB-backed
     * {@link CustomUserDetailsService}.
     *
     * @return configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ── REST API Security Chain (order = 1, evaluated first) ──────────────────

    /**
     * Secures all {@code /api/**} endpoints with stateless HTTP Basic Authentication.
     *
     * <p>Milestone 7 requirement: REST APIs must use Basic HTTP Authentication
     * backed by the MongoDB user database.
     *
     * @param http the HttpSecurity to configure
     * @return the built SecurityFilterChain for the API
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")          // applies only to REST endpoints
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()    // all /api/** require a valid user
            )
            .httpBasic(Customizer.withDefaults()) // HTTP Basic (username:password)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no cookies for REST
            )
            .csrf(csrf -> csrf.disable());        // CSRF not needed for stateless APIs

        return http.build();
    }

    // ── Web / Browser Security Chain (order = 2, evaluated second) ────────────

    /**
     * Secures all browser-facing routes with form-based login (Milestone 6 behaviour).
     *
     * @param http the HttpSecurity to configure
     * @return the built SecurityFilterChain for the web UI
     * @throws Exception if configuration fails
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
}

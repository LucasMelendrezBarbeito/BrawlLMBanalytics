package com.brawllmbanalytics.config;

import com.brawllmbanalytics.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

              
                .requestMatchers(
                        "/",
                        "/favicon.ico",
                        "/favicon_kenji.ico",
                        "/index.html",
                        "/login.html",
                        "/register.html",
                        "/dashboard.html",
                        "/mapas.html",
                        "/tierlist.html",
                        "/tierlist_crear.html",
                        "/tierlist_ver.html",
                        "/estadisticas.html",
                        "/cuenta.html",
                        "/cuentas.html",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll()

             
                .requestMatchers("/admin/mapas/importar").hasRole("ADMIN")

           
                .requestMatchers("/auth/login", "/auth/register").permitAll()

          
                .requestMatchers(HttpMethod.GET, "/brawl/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/mapas/**").permitAll()

               
                .requestMatchers(HttpMethod.GET, "/brawl/brawlers").permitAll()
                .requestMatchers(HttpMethod.GET, "/brawl/brawlers/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/brawlers").permitAll()
                .requestMatchers(HttpMethod.GET, "/brawlers/**").permitAll()

              
                .requestMatchers(HttpMethod.GET, "/tierlists").permitAll()
                .requestMatchers(HttpMethod.GET, "/tierlists/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/tierlists/*/review").authenticated()

                .requestMatchers(HttpMethod.POST, "/tierlists/crear").authenticated()
                .requestMatchers(HttpMethod.POST, "/tierlists/*/items").authenticated()

         
                .requestMatchers(HttpMethod.POST, "/cuentas/vincular").authenticated()
                .requestMatchers(HttpMethod.GET, "/cuentas/**").authenticated()

              
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

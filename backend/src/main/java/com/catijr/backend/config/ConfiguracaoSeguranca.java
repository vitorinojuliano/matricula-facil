package com.catijr.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {
    @Bean
    public BCryptPasswordEncoder codificadorSenhaComBcrypt() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private FiltroAutenticacaoJwt filtroAutenticacaoJwt;

    @Bean
    public CorsConfigurationSource origensPermitidas() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOrigins(List.of("http://localhost:5173", "https://matricula-facil-app.onrender.com"));
        configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource origensPorRota = new UrlBasedCorsConfigurationSource();
        origensPorRota.registerCorsConfiguration("/**", configuracao);
        return origensPorRota;
    }

    @Bean
    public SecurityFilterChain cadeiaFiltrosSeguranca(HttpSecurity configuracaoHttp) throws Exception {
        configuracaoHttp
                .cors(cors -> cors.configurationSource(origensPermitidas()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessao -> sessao
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(autorizacao -> autorizacao
                        .requestMatchers("/MatriculaFacil/cadastro",
                                "/MatriculaFacil/login",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(filtroAutenticacaoJwt, UsernamePasswordAuthenticationFilter.class);

        return configuracaoHttp.build();
    }
}

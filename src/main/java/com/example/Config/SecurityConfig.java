package com.example.Config;

import com.example.Service.CandidateDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CandidateDetailsService candidateDetailsService ;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("Security filter chain loaded");
        return
                httpSecurity.csrf(customizer -> customizer.disable())
                        .authorizeHttpRequests(customizer -> customizer
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/**").hasAnyRole("USER","ADMIN","HR")
                                //allow unauthenticated access for a new user
                                .requestMatchers("/user/**").permitAll()
                                .anyRequest().authenticated())
                        //all the requests above

                                .httpBasic(Customizer.withDefaults())
                        .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider() ;
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(candidateDetailsService);

        return daoAuthenticationProvider ;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder() ;
    }

}

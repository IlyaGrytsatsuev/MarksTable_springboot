package gratchuvalsky.spring_boot.config;

import gratchuvalsky.spring_boot.security.AuthProvider;
import gratchuvalsky.spring_boot.service.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static javax.management.Query.and;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

//    @Bean
//    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .userDetailsService(personDetailsService)
//                .formLogin();
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests) ->
                requests
                        .requestMatchers("/SubMarks/**").hasRole("USER")
                        .requestMatchers("/auth",
                                "/error", "/loginStyle.css",
                                "/signUp", "/login_success",
                                "/SubjectsAndMarksStyle.css",
                                "MarkInfoStyle.css").permitAll()
                        .anyRequest().hasRole("ADMIN"));
        http.userDetailsService(personDetailsService);
        http.formLogin((formLogin) -> formLogin.loginPage("/auth")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/login_success", true)
                .failureUrl("/auth?error"));
        http.logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/auth"));
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}

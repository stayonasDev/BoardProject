package lab.board.web.security;

import jakarta.servlet.DispatcherType;
import lab.board.domain.Role;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequest) ->
                                authorizeRequest
                                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                                        .requestMatchers("/", "/user/**", "/user/login/**", "/user/join", "/post").permitAll()
                                        .requestMatchers("/post/**").hasRole("USER")
//                                .requestMatchers("유저 인가").hasRole(Role.USER.name())
//                                .requestMatchers("관리자 인가").hasRole(Role.ADMIN.name())
                                        .anyRequest().authenticated() //나머지 페이지 인증된 사용자만 접근
                )
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/user/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/user/logout?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    public void configure(WebSecurity web) {
        web.ignoring()
                .requestMatchers("/favicon.ico", "/**/favicon.ico");

        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}

package datto.iam.config;

import datto.iam.dto.auth.UserPrincipal;
import datto.iam.service.OAuth2UserService;
import datto.iam.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserService userService;
    private final OAuth2UserService oauth2UserService;
    private final PasswordEncoder passwordEncoder;
    private static final String[] WHITELIST = {
            "/login.html",
            "/createUser",
            "/html/**",
            "/css/**",
            "/js/**",
            "/swagger-ui/index.html",
    };

    private static final String HOME_ENDPOINT = "/user";
    private static final String LOGOUT_URL = "/auth/logout";
    private static final String LOGIN_PAGE = "/login.html";

    @Bean
    public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {
        log.warn("Configuring http filterChain");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .formLogin(formConigurer ->{
                    Customizer.withDefaults().customize(formConigurer);
                    formConigurer.defaultSuccessUrl(HOME_ENDPOINT);
                })
                .oauth2Login(oauth2 -> {
                    Customizer.withDefaults().customize(oauth2);
                    oauth2.userInfoEndpoint(infoEndpoint ->
                            infoEndpoint.userService(oauth2UserService)
                    );
                    oauth2.defaultSuccessUrl(HOME_ENDPOINT, false);
                })
                .authenticationProvider(daoAuthenticationProvider())
                .logout(logout -> logout.logoutUrl(LOGOUT_URL)
                        .logoutSuccessUrl(LOGIN_PAGE)
                        .addLogoutHandler(logoutHandler())
                        .deleteCookies()
                        .logoutSuccessHandler(
                                (request, response, authentication)
                                        -> SecurityContextHolder.clearContext()));

        return http.build();
    }

    private UserDetailsService defaultUserDetailsService(){
        return username -> {
            UserPrincipal user = UserPrincipal.create(userService.loadUserByUsername(username));
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            return user;
        };
    }

    private LogoutHandler logoutHandler(){
        return (request, response, authentication) ->
        {
            if (authentication != null) {
                authentication.setAuthenticated(false);
            }
        };
    }
    private AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(defaultUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}

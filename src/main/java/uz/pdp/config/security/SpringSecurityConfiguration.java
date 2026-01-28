package uz.pdp.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true, // @PreAutorize uchun true bolsin
        securedEnabled = true,
        jsr250Enabled = true
)
// api larda security ni method levelda check
public class SpringSecurityConfiguration {
    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticatedFailerHandler customAuthenticatedFailerHandler;


    public SpringSecurityConfiguration(CustomUserDetailService customUserDetailService, CustomAuthenticatedFailerHandler customAuthenticatedFailerHandler) {
        this.customUserDetailService = customUserDetailService;
        this.customAuthenticatedFailerHandler = customAuthenticatedFailerHandler;
    }

    private final String[] PUBLIC_URLS = {
            "/auth/register",
            "/auth/login",
            "/auth/logout",
            "/auth/homeModel",
            "/home",
            "/userinfo",
            "/test",
            "/css/**",
            "/js/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .userDetailsService(customUserDetailService)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_URLS).permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("user/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );

        http.formLogin()
                .loginPage("/auth/login")
                .usernameParameter("uname")
                .passwordParameter("pswd")
                .defaultSuccessUrl("/homeModel", false)
                .failureHandler(customAuthenticatedFailerHandler);

        http.logout()
                .logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(false)
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"));

        http.rememberMe()
                .rememberMeParameter("rememberMe")
                .rememberMeCookieName("remember-me-cookie")
                .tokenValiditySeconds(24 * 60 * 60)
                .key("secret_key")
                .userDetailsService(customUserDetailService);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // faqat test uchun
        return new BCryptPasswordEncoder();
    }
}

package at.wst.online_webshop.config;


import at.wst.online_webshop.security.JwtAuthenticationFilter;
import at.wst.online_webshop.security.JwtTokenUtil;
import at.wst.online_webshop.services.CustomerDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomerDetailsService customerDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final WebConfig webConfig;

    @Autowired
    public SecurityConfig(CustomerDetailsService customerDetailsService, JwtTokenUtil jwtTokenUtil, JwtAuthenticationFilter jwtAuthenticationFilter, WebConfig webConfig) {
        this.customerDetailsService = customerDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.webConfig = webConfig;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and()
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(req -> req
                        .antMatchers("/api/sql/shopping-cart/**").permitAll()
                        .antMatchers("/api/sql/reviews/**").permitAll()
                        .antMatchers("/api/sql/customers/**").permitAll()
                        .antMatchers("/api/sql/orders/**").permitAll()
                        .antMatchers("/api/sql/reports/**").permitAll()
                        .antMatchers("/api/sql/customers/register").permitAll()
                        .antMatchers("/api/sql/customers/login").permitAll()
                        .antMatchers(HttpMethod.GET, "/**").permitAll()
                        .antMatchers("/api/sql/products/get-products").permitAll()
                        .antMatchers("/api/fill").permitAll()
                        .antMatchers("/api/nosql/**").permitAll()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

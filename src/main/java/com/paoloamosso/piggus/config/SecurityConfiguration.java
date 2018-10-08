/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/*
 * In this class we manage the security configurations of the application using Spring Security with bCryptPasswordEncoder
 */

package com.paoloamosso.piggus.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // == fields ==
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    // == public methods ==
    // Method to define db queries and encryption encoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    // Method to define the main security elements
    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        http.
                // Here we manage the security and the login
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/400","/403","/404","/500","/503").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/recover-credentials").permitAll()
                .antMatchers("/set-credentials").permitAll()
                .antMatchers("/privacy-cookie-policy").permitAll()
                .antMatchers("/**").hasAuthority("USER").anyRequest()
                .authenticated().and().csrf().disable().formLogin()
                .loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/home")
                .usernameParameter("username")
                .passwordParameter("password")
                .and() // LOGOUT
                .logout().deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling()
                .accessDeniedPage("/access-denied")
                .and()
                .rememberMe().key("uniqueAndSecret")
                .and()
                // Here we manage the Concurrent Session Control
                .sessionManagement()
                .sessionFixation().migrateSession()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                // Forcing the cleaning of an invalid sessionID through the logout has to be verified, it's a work-around
                // TODO improvement Priority 2: Understand if this work-around has meaning to exist
                .invalidSessionUrl("/logout")
                .maximumSessions(1)
                .expiredUrl("/session-expired");
    }

    // Method necessary to show the stylesheet of the website, it makes it ignore security settings
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/static/**","/js/**","/css/**", "/img/**", "/json/**", "/assets/**", "/data/**", "/frontend/**", "/plugins/**");
    }

    // This bean make sure to track the event of destruction on session
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}

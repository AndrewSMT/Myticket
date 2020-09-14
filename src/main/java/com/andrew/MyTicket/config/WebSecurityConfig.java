package com.andrew.MyTicket.config;

import com.andrew.MyTicket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration spring security
 *
 * @author Andreii Matveiev
 * @author andrei.matviev@gmail.com
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Dependency injection UserService into WebSecurityConfig
     */
    @Autowired
    private  UserService userService;

    /**
     * Dependency injection PasswordEncoder into WebSecurityConfig
     */
    @Autowired
    private  PasswordEncoder passwordEncoder;

    /**
     * Set password encoder type
     *
     * @return Instance of BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    /**
     * Set security configuration
     *
     * @param http HttpSecurity
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/event/**", "/login", "/registration/**", "/static/**", "/picture/**"
                        , "/main/**", "/event", "/mobile/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .permitAll()
                .and()
                .rememberMe().key("uniqueAndSecret");
    }

    /**
     * Set authentication config
     *
     * @param auth AuthenticationManagerBuilder
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

}

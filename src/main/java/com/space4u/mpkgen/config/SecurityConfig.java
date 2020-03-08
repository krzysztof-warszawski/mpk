package com.space4u.mpkgen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("securityDataSource")
    private DataSource securityDataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // use jdbc authentication ...
        auth.jdbcAuthentication().dataSource(securityDataSource);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("HTTP SEC>>>>>>>>>>");
        http.authorizeRequests()
//                .antMatchers("/employees/showForm*").hasAnyRole("MANAGER", "ADMIN")
//                .antMatchers("/employees/save*").hasAnyRole("MANAGER", "ADMIN")
//                .antMatchers("/employees/delete").hasRole("ADMIN")
//                .antMatchers("/employees/**").hasRole("EMPLOYEE")
                .antMatchers("/mpkList").hasRole("MANAGER")
                .antMatchers("/resources/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll();
//                .and()
//                .logout().permitAll()
//                .and()
//                .exceptionHandling().accessDeniedPage("/access-denied");

    }
}

//package com.space4u.mpkgen.config;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableJpaRepositories(basePackages={"${spring.data.jpa.repository.packages}"})
//public class DataSourceConfig {
//
//    @Primary
//    @Bean
//    @ConfigurationProperties(prefix = "app.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "entityManagerFactory")
//    @ConfigurationProperties(prefix = "spring.data.jpa.entity")
//    public LocalContainerEntityManagerFactoryBean
//    entityManagerFactoryBean(EntityManagerFactoryBuilder builder, DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .build();
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix = "security.datasource")
//    public DataSource securityDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//}

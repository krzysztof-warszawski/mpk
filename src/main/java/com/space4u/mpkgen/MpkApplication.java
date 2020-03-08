package com.space4u.mpkgen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.space4u.mpkgen.config",
		"com.space4u.mpkgen.controller",
		"com.space4u.mpkgen.entity",
		"com.space4u.mpkgen.repository",
		"com.space4u.mpkgen.service",
		"com.space4u.mpkgen.service.implementation"
})
public class MpkApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpkApplication.class, args);
	}

}
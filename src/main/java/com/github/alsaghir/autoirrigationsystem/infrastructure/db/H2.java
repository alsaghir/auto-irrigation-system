package com.github.alsaghir.autoirrigationsystem.infrastructure.db;

import com.github.alsaghir.autoirrigationsystem.infrastructure.db.h2.V20230514__Base_tables;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("default")
@Configuration
@ComponentScan(basePackageClasses = V20230514__Base_tables.class)
public class H2 {
}

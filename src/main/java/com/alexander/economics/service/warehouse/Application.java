package com.alexander.economics.service.warehouse;

import com.alexander.economics.service.warehouse.converter.ConverterConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@Import(ConverterConfiguration.class)
public class Application {
    public static void main(String[] args) {
        run(Application.class, args);
    }
}

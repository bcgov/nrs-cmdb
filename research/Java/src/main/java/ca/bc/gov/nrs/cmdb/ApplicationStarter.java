package ca.bc.gov.nrs.cmdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// reference https://blog.openshift.com/using-spring-boot-on-openshift/

//@Configuration
@ComponentScan ("ca.bc.gov.nrs.cmdb")
@EnableAutoConfiguration
@SpringBootApplication

public class ApplicationStarter extends SpringBootServletInitializer {

    @Autowired
    private com.tinkerpop.blueprints.impls.orient.OrientGraphFactory factory;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationStarter.class);
    }

}
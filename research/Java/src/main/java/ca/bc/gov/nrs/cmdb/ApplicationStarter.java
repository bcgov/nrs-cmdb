package ca.bc.gov.nrs.cmdb;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.FuseGenerator", date = "2017-07-17T11:39:48.709-07:00")



@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan("ca.bc.gov.nrs.cmdb")

public class ApplicationStarter implements CommandLineRunner {



    @Autowired
    private com.tinkerpop.blueprints.impls.orient.OrientGraphFactory factory;

    
    public static void main(final String[] args) throws Exception {
        SpringApplication.run(ApplicationStarter.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

    }
    


}

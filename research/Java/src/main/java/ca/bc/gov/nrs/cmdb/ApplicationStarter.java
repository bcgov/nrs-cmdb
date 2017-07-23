package ca.bc.gov.nrs.cmdb;

import ca.bc.gov.nrs.cmdb.repository.ApplicationRepository;


import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ca.bc.gov.nrs.cmdb.model.Application;
import org.springframework.data.orient.commons.repository.config.EnableOrientRepositories;
import org.springframework.data.orient.object.OrientObjectDatabaseFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@javax.annotation.Generated(value = "class io.swagger.codegen.languages.FuseGenerator", date = "2017-07-17T11:39:48.709-07:00")



@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan("ca.bc.gov.nrs.cmdb")
@EnableOrientRepositories("ca.bc.gov.nrs.cmdb.repository")
public class ApplicationStarter implements CommandLineRunner {

//    @Autowired
//    private ApplicationRepository applicationRepository;

    @Autowired
    private OrientObjectDatabaseFactory factory;

    
    public static void main(final String[] args) throws Exception {
        //startServer().join();
        SpringApplication.run(ApplicationStarter.class, args);
    }

    /*
    public static Server startServer() throws Exception {

        // use system property first
        String port = System.getProperty("HTTP_PORT");
        if (port == null) {
            // and fallback to use environment variable
            port = System.getenv("HTTP_PORT");
        }
        if (port == null) {
            // and use port 8080 by default
            port = "8080";
        }
        Integer num = Integer.parseInt(port);
        String service = Systems.getEnvVarOrSystemProperty("WEB_CONTEXT_PATH", "WEB_CONTEXT_PATH", "");
        String servicesPath = "/servicesList";

        String servletContextPath = "/" + service;

        System.out.println("Starting REST server at:         http://localhost:" + port + servletContextPath);
        System.out.println("View the services at:            http://localhost:" + port + servletContextPath + servicesPath);
        System.out.println("View an example REST service at: http://localhost:" + port + servletContextPath + "cxfcdi/customerservice/customers/123");
        System.out.println();

		
        InetSocketAddress inetaddr = new InetSocketAddress("0.0.0.0", num);
        final Server server = new Server(inetaddr);

        // Register and map the dispatcher servlet
        final ServletHolder servletHolder = new ServletHolder(new CXFCdiServlet());

        // change default service list URI
        servletHolder.setInitParameter("service-list-path", servicesPath);
               

        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addEventListener(new Listener());
        context.addEventListener(new BeanManagerResourceBindingListener());

        String servletPath = "/*";
        if (Strings.isNotBlank(service)) {
            servletPath = servletContextPath + "/*";
        }
        context.addServlet(servletHolder, servletPath);

        server.setHandler(context);
        
        
        server.start();
        return server;
    }

*/
  
    @Override
    public void run(String... args) throws Exception {
        OObjectDatabaseTx db = null;
   
        try {
            db = factory.openDatabase();
            db.getEntityManager().registerEntityClass(ca.bc.gov.nrs.cmdb.model.Application.class);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    
    }
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.gov.nrs.cmdb.rest;

import ca.bc.gov.nrs.cmdb.model.Application;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import com.orientechnologies.orient.object.db.OObjectDatabaseTx;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.orient.object.OrientObjectDatabaseFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Properties;

import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 *
 * @author George
 */

@RestController
@RequestMapping("/applications")

public class ApplicationsController {
    
    private static Gson gson;

    @Autowired
    private OrientGraphFactory factory;

    //@Autowired
    //private ApplicationRepository applicationRepository;

    /*
    @RequestMapping(method = RequestMethod.GET)
    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }
*/
    
    @RequestMapping("/load")    
    public String applicationsLoad()
    {
        OrientGraphNoTx graph =  factory.getNoTx(); //factory.getTx();

        if (graph.getVertexType("Application") == null)
        {
            graph.createVertexType("Application");
        }

        if (graph.getVertexType("Module") == null)
        {
            graph.createVertexType("Module");
        }

        if (graph.getVertexType("Environment") == null)
        {
            graph.createVertexType("Environment");
        }

        if (graph.getVertexType("Property") == null)
        {
            graph.createVertexType("Property");
        }

        String configDir = "C:\\repo\\dcts\\sdk-config";
        // open the config dir and parse out the subdirectories.
        File directory = new File(configDir);
        String[] apps = directory.list();
        for (String filename : apps) {
            // if it is not a directory, ignore it.
            String filePath = configDir + "\\" + filename;
            File subdir = new File(filePath);
            if (subdir.isDirectory()) {
                System.out.println("Processing app " + filename);
                // create the app


                Vertex vApplication = null;

                // lookup the application.
                Iterable<Vertex> verticies = graph.getVertices("Application.abbreviation", filename);
                if (verticies != null && verticies.iterator().hasNext())
                {
                    vApplication = verticies.iterator().next();
                }
                else
                {
                    vApplication = graph.addVertex("class:Application");
                    vApplication.setProperty("abbreviation", filename);
                }

                
                // now look for modules.
                String[] items = subdir.list();

                for (String item : items) {
                    // parse the item.
                    String[] parts = item.split("[.]");
                    String module = parts[0];
                    String environment = parts[1];

                    String moduleKey = filename.toUpperCase() + "_" + module.toUpperCase();

                    Vertex vModule = null;
                    // lookup the module.
                    Iterable<Vertex> modules = graph.getVertices("Module.key", moduleKey);
                    if (modules != null && modules.iterator().hasNext())
                    {
                        vModule = modules.iterator().next();
                    }
                    else
                    {
                        vModule = graph.addVertex("class:Module");
                        vModule.setProperty("name", module);
                        vModule.setProperty("key", moduleKey);
                        // ensure there is an edge.
                        graph.addEdge(null, vApplication, vModule, "has");
                    }

                    String environmentKey = moduleKey + "_" + environment.toUpperCase();

                    Vertex vEnvironment = null;
                    // lookup the environment.
                    Iterable<Vertex> environments = graph.getVertices("Environment.key", environmentKey);

                    if (environments != null && environments.iterator().hasNext())
                    {
                        vEnvironment = environments.iterator().next();
                    }
                    else
                    {
                        vEnvironment = graph.addVertex("class:Environment");
                        vEnvironment.setProperty("name", environment);
                        vEnvironment.setProperty("key", environmentKey);
                        // ensure there is an edge.
                        graph.addEdge(null, vModule, vEnvironment, "has");
                    }

                    System.out.println("Module: " + module);

                    // now load the properties.
                    Properties prop = new Properties();
                    InputStream input = null;
                    try {
                        input = new FileInputStream(filePath + File.separatorChar + item);
                        prop.load(input);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        if (input != null) {
                            try {
                                input.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    // and add the name / value pairs.
                    Enumeration<?> e = prop.propertyNames();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = prop.getProperty(key);
                        System.out.println("Key : " + key + ", Value : " + value);

                        String propertyKey = environmentKey + "_" + key.toUpperCase();

                        Vertex vProperty = null;
                        // lookup the environment.
                        Iterable<Vertex> properties = graph.getVertices("Property.key", propertyKey);

                        if (properties != null && properties.iterator().hasNext())
                        {
                            vProperty = properties.iterator().next();
                            vProperty.setProperty("value", value);
                        }
                        else
                        {
                            vProperty = graph.addVertex("class:Property");
                            vProperty.setProperty("name", key);
                            vProperty.setProperty("value", value);
                            vProperty.setProperty("key", propertyKey);
                            // ensure there is an edge.
                            graph.addEdge(null, vEnvironment, vProperty, "has");
                        }

                    }

                }

            }

        }
        graph.shutdown();
        return "OK";
    }    
    
    
    
}

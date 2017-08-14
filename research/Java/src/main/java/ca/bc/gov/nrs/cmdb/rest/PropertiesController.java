/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.gov.nrs.cmdb.rest;

import com.google.gson.Gson;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author George
 */

@RestController
@RequestMapping("/properties")

public class PropertiesController {
    
    private static Gson gson;

    @Autowired
    private OrientGraphFactory factory;

    @RequestMapping("/getvalue")
    /**
     * Returns the value for a given property.
     */

    public String GetValue(
        @RequestParam(required = true) String project,
        @RequestParam(required = true) String component,
        @RequestParam(required = true) String environment,
        @RequestParam(required = true) String property
        )
    {
        String result = "";
        // get a graph database connection
        OrientGraphNoTx graph =  factory.getNoTx();

        // construct the property key
        String ComponentKey = project.toUpperCase() + "_" + component.toUpperCase();
        String executionEnvironmentKey = ComponentKey + "_" + environment.toUpperCase();
        String propertyKey = executionEnvironmentKey + "_" + property.toUpperCase();

        // get a list of matching values
        Iterable<Vertex> properties = graph.getVertices("PropertyValue.key", propertyKey);
        OrientVertex vProperty = null;
        if (properties != null && properties.iterator().hasNext())
        {
            vProperty = (OrientVertex) properties.iterator().next();
            result = vProperty.getProperty ("value");
        }

        // shutdown the graph database connection
        graph.shutdown();
        return result;
    }    
    
    
    
}

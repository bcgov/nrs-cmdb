/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.gov.nrs.cmdb.rest;

import ca.bc.gov.nrs.cmdb.model.Artifact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 *
 * @author George
 */

@RestController
@RequestMapping("/artifacts")

public class ArtifactsController {
    
    private static Gson gson;

    @Autowired
    private OrientGraphFactory factory;


    /***
     * Get an artifact template.
     * @return
     */

    @RequestMapping("/getTemplate")
    public String GetTemplate()
    {
        gson = new Gson();


        OrientGraphNoTx graph =  factory.getNoTx();
        OrientVertex vArtifact = null;
        Artifact artifact = new Artifact();

        String defaultName = "DemoArtifact";

        if (graph.getVertexType("Artifact") == null)
        {
            graph.createVertexType("Artifact");
        }

        Iterable<Vertex> Artifacts = graph.getVertices("Artifact.name", defaultName);
        if (Artifacts != null && Artifacts.iterator().hasNext())
        {
            vArtifact = (OrientVertex) Artifacts.iterator().next();
            artifact.setKey((String)vArtifact.getProperty("key"));
            artifact.setName((String)vArtifact.getProperty("name"));
        }
        else // create the demo item.
        {
            // create the view model
            artifact.setKey(UUID.randomUUID().toString());
            artifact.setName(defaultName);
            // create the item in the graph database.
            vArtifact = graph.addVertex("class:Artifact");
            vArtifact.setProperty("key", artifact.getKey());
            vArtifact.setProperty("name", artifact.getName());
        }

        graph.shutdown();

        // return the result
        //return result.toJson();//gson.toJson(result);
        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(artifact);
        }
        catch (Exception e)
        {
            result = "ERROR" + e.toString();
        }
        return result;
    }

}

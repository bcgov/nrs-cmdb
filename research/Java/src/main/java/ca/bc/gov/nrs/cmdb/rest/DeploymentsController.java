/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.gov.nrs.cmdb.rest;

import ca.bc.gov.nrs.cmdb.model.Artifact;
import ca.bc.gov.nrs.cmdb.model.DeploymentSpecificationPlan;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 *
 * @author George
 */

@RestController
@RequestMapping("/deployments")

public class DeploymentsController {
    
    private static Gson gson;

    @Autowired
    private OrientGraphFactory factory;


    void CreateVertexTypeIfNotExists(OrientGraphNoTx graph, String name)
    {
        if (graph.getVertexType(name) == null)
        {
            graph.createVertexType(name);
        }
    }

    OrientVertex CreateVertexIfNotExists(OrientGraphNoTx graph, String vertexType, String key)
    {
        OrientVertex result = null;
        // lookup the Component.
        Iterable<Vertex> Components = graph.getVertices(vertexType + ".key", key);
        if (Components != null && Components.iterator().hasNext())
        {
            result = (OrientVertex) Components.iterator().next();
        }
        else
        {
            result = graph.addVertex("class:" + vertexType);
            result.setProperty("key", key);
        }
        return result;
    }

    OrientVertex GetVertex(OrientGraphNoTx graph, String vertexType, String key)
    {
        OrientVertex result = null;
        // lookup the Component.
        Iterable<Vertex> Components = graph.getVertices(vertexType + ".key", key);
        if (Components != null && Components.iterator().hasNext())
        {
            result = (OrientVertex) Components.iterator().next();
        }
        return result;
    }



    void CreateEdgeIfNotExists (OrientGraphNoTx graph, OrientVertex vSource, OrientVertex vDestination, String edgeLabel)
    {
        // ensure there is an edge between the ExecutionEnvironment and the property.
        Iterable<com.tinkerpop.blueprints.Edge> edges = vSource.getEdges( vDestination, Direction.BOTH, edgeLabel);
        if (edges == null || ! edges.iterator().hasNext()) {
            graph.addEdge(null, vSource, vDestination, edgeLabel);
        }
    }


    /***
     * Get a new deployment specification plan.
     * @return
     */
    @PostMapping("/start")

    public ResponseEntity<String> StartDeployment(@RequestBody Artifact input)
    {
        // setup the new deployment specification plan.
        DeploymentSpecificationPlan deploymentSpecificationPlan = new DeploymentSpecificationPlan();

        deploymentSpecificationPlan.setKey(UUID.randomUUID().toString());

        DateFormat dateFormat = new SimpleDateFormat("yy/mm/dd-hh:mm");
        Calendar cal = Calendar.getInstance();

        deploymentSpecificationPlan.setName("New Deployment Specification Plan " + dateFormat.format(cal.getTime()));
        deploymentSpecificationPlan.setArtifact(input);

        OrientGraphNoTx graph =  factory.getNoTx();


        if (graph.getVertexType("DeploymentSpecificationPlan") == null)
        {
            graph.createVertexType("DeploymentSpecificationPlan");
        }

        OrientVertex vDeploymentSpecificationPlan = graph.addVertex("class:DeploymentSpecificationPlan" );
        vDeploymentSpecificationPlan.setProperty("key", deploymentSpecificationPlan.getKey());
        vDeploymentSpecificationPlan.setProperty("name", deploymentSpecificationPlan.getName());

        // get the Artifact.

        OrientVertex vArtifact = null;
        Iterable<Vertex> Artifacts = graph.getVertices("Artifact.key", input.getKey());
        if (Artifacts != null && Artifacts.iterator().hasNext())
        {
            vArtifact = (OrientVertex) Artifacts.iterator().next();
            // Add an edge to the Artifact.

            Iterable<com.tinkerpop.blueprints.Edge> edges = vDeploymentSpecificationPlan.getEdges( vArtifact, Direction.BOTH, "Deploys");
            if (edges == null || ! edges.iterator().hasNext()) {
                graph.addEdge(null, vDeploymentSpecificationPlan, vArtifact, "Deploys");
            }
        }


        // done with the graph
        graph.shutdown();

        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(deploymentSpecificationPlan);
        }
        catch (Exception e)
        {
            result = "ERROR" + e.toString();
        }

        // return the result
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(result, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/{deploymentId}/finish")
    public ResponseEntity<String> FinishDeployment(@PathVariable("deploymentId") String deploymentId, @RequestParam(required = true) Boolean success, @RequestBody DeploymentSpecificationPlan input )
    {
        OrientGraphNoTx graph =  factory.getNoTx();

        DeploymentSpecificationPlan deploymentSpecificationPlan = new DeploymentSpecificationPlan();

        if (graph.getVertexType("DeploymentSpecificationPlan") == null)
        {
            graph.createVertexType("DeploymentSpecificationPlan");
        }

        // find the associated OrientDb object
        OrientVertex vDeploymentSpecificationPlan = GetVertex (graph, "DeploymentSpecificationPlan", deploymentId);

        if (vDeploymentSpecificationPlan != null) {
            // update the status
            vDeploymentSpecificationPlan.setProperty("deployment-successful", success.toString());
            deploymentSpecificationPlan.setDeployed(success);

            deploymentSpecificationPlan.setKey((String)vDeploymentSpecificationPlan.getProperty("key"));
            deploymentSpecificationPlan.setName((String)vDeploymentSpecificationPlan.getProperty("name"));
            // see if there are any edges.
            Iterable<com.tinkerpop.blueprints.Edge> edges = vDeploymentSpecificationPlan.getEdges( Direction.BOTH, "Deploys");
            if (edges != null && edges.iterator().hasNext()) {
                OrientEdge vEdge = (OrientEdge) edges.iterator().next();
                OrientVertex vArtifact = graph.getVertex( vEdge.getInVertex());
                Artifact artifact = new Artifact();
                artifact.setKey((String) vArtifact.getProperty("key"));
                artifact.setName((String) vArtifact.getProperty("name"));
                deploymentSpecificationPlan.setArtifact(artifact);
            }


        }
        else
        {
            throw new ResourceNotFoundException(deploymentId);
        }

        // done with the graph
        graph.shutdown();


        ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(deploymentSpecificationPlan);
        }
        catch (Exception e)
        {
            result = "ERROR" + e.toString();
        }

        // return the result
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(result, httpHeaders, HttpStatus.OK);

    }
}

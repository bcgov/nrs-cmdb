/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.gov.nrs.cmdb.rest;

import ca.bc.gov.nrs.cmdb.model.SchemaSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/schema")

public class SchemaController {

    private static Gson gson;


    /***
     * Get an artifact template.
     * @return
     */

    @GetMapping
    public String GetSchema()
    {
        gson = new Gson();

        ObjectMapper mapper = new ObjectMapper();

        SchemaSpec schema = new SchemaSpec();

        String result = "";

        try {

            JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);

            JsonSchema jsonSchema = schemaGen.generateSchema(SchemaSpec.class);

            result = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
        }
        catch (Exception e)
        {

        }



        return result;

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.gov.nrs.cmdb.rest;

import ca.bc.gov.nrs.cmdb.model.Application;
import ca.bc.gov.nrs.cmdb.repository.ApplicationRepository;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Properties;


/**
 *
 * @author George
 */

@RestController
@RequestMapping("/applications")

public class ApplicationsController {
    
    private static Gson gson;


    @Autowired
    private ApplicationRepository applicationRepository;
    
    @RequestMapping(method = RequestMethod.GET)
    public List<Application> findAllApplications() {
        return applicationRepository.findAll();
    }

    
    @RequestMapping("/load")    
    public String applicationsLoad()
    {
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

                Application application = new Application();
                application.name(filename);                
                applicationRepository.save(application);
                
                
                // now look for modules.
                String[] items = subdir.list();

                for (String item : items) {
                    // parse the item.
                    String[] parts = item.split("[.]");
                    String module = parts[0];
                    String environment = parts[1];

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
                    }

                }

            }

        }
        return "OK";
    }    
    
    
    
}

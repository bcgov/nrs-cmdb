/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.bc.gov.nrs.cmdb.repository;

import org.springframework.data.orient.commons.repository.annotation.Query;
import org.springframework.data.orient.object.repository.OrientObjectRepository;
import ca.bc.gov.nrs.cmdb.model.Application;

import java.util.List;
/**
 *
 * @author George
 */   

public interface ApplicationRepository extends OrientObjectRepository<Application> {
     List<Application> findByName(String Name);
}
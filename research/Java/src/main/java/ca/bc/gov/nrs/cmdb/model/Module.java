package ca.bc.gov.nrs.cmdb.model;

import ca.bc.gov.nrs.cmdb.model.Application;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;


/**
 * Application Module
 **/

import io.swagger.annotations.*;
import java.util.Objects;
//import org.springframework.data.gremlin.annotation.*;
@ApiModel(description = "Application Module")

//@Vertex
public class Module   {
//  @Id
  private Integer id = null;
//  @Property("module_name")
  private String name = null;
//  @Link("part_of_application")
  private Application module = null;

  /**
   * A system-generated unique identifier for a module
   **/
  public Module id(Integer id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(example = "null", value = "A system-generated unique identifier for a module")
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * The name of the application.
   **/
  public Module name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(example = "null", value = "The name of the application.")
  @JsonProperty("name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * A foreign key reference to the Application.
   **/
  public Module module(Application module) {
    this.module = module;
    return this;
  }

  
  @ApiModelProperty(example = "null", required = true, value = "A foreign key reference to the Application.")
  @JsonProperty("module")
  public Application getModule() {
    return module;
  }
  public void setModule(Application module) {
    this.module = module;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Module module = (Module) o;
    return Objects.equals(id, module.id) &&
        Objects.equals(name, module.name) &&
        Objects.equals(module, module.module);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, module);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Module {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    module: ").append(toIndentedString(module)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


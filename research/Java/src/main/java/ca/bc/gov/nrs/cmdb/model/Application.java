package ca.bc.gov.nrs.cmdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;

/**
 * Application
 **/

import java.util.Objects;
//import org.springframework.data.gremlin.annotation.*;
@ApiModel(description = "Application")

//@Vertex
public class Application   {
  
    
  @Id  
  private Integer id = null;
  
//  @Property("application_name")
  private String name = null;

  /**
   * A system-generated unique identifier for an application.
   **/
  public Application id(Integer id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(example = "null", required = true, value = "A system-generated unique identifier for an application.")
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
  public Application name(String name) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Application application = (Application) o;
    return Objects.equals(id, application.id) &&
        Objects.equals(name, application.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Application {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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


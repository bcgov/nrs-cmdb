package ca.bc.gov.nrs.cmdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Project Component
 **/
//import org.springframework.data.gremlin.annotation.*;

//@Vertex
public class DeploymentSpecificationPlan {

  private String key = null;


  /**
   * A system-generated unique identifier for a module
   **/
  public DeploymentSpecificationPlan key(String key) {
    this.key = key;
    return this;
  }

  /* Deployment status
   * null - no attempt to deploy yet
   * true - deployed successfully
   * false - not deployed
   */
  private Boolean deployed = null;

  // The Artifact specified as a parameter when the DeploymentSpecificationPlan was created.
  private Artifact artifact;

  /**
   * The name of the application.
   **/
  private String name = null;

  public DeploymentSpecificationPlan name(String name) {
    this.name = name;
    return this;
  }


  @JsonProperty("key")
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }

  @JsonProperty("deployed")
  public Boolean getDeployed() {
    return deployed;
  }
  public void setDeployed(Boolean deployed) {
    this.deployed = deployed;
  }

  @JsonProperty("artifact")
  public Artifact getArtifact() {return artifact;}
  public void setArtifact(Artifact artifact) {this.artifact = artifact;}

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
    DeploymentSpecificationPlan deployment = (DeploymentSpecificationPlan) o;
    return Objects.equals(key, deployment.key) &&
        Objects.equals(name, deployment.name) ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Component {\n");
    
    sb.append("    id: ").append(toIndentedString(key)).append("\n");
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


-- Infrastructure
CREATE VERTEX DeviceNode SET key = "BLEWIT"

-- Middle Tier
CREATE VERTEX  LogicalExecutionEnvironment SET key = "WLH01"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "DELIVERY_WLH01"
CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLH01") TO (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_WLH01")
CREATE EDGE Hosted_By FROM (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_WLH01") TO (SELECT FROM DeviceNode WHERE key = "BLEWIT")

-- Application: IRS
CREATE VERTEX LogicalComponent SET key = "IRS"
CREATE VERTEX PhysicalComponent SET key = "DELIVERY_IRS"
CREATE VERTEX  Artifact SET key = "IRS_EAR"
CREATE VERTEX  LogicalDeploymentSpec SET key = "IRS_DEPLOYMENTSPEC"
CREATE VERTEX  PhysicalDeploymentSpec SET key = "DELIVERY_IRS_DEPLOYMENTSPEC"

CREATE EDGE Delivery_Instance FROM (SELECT FROM COMPONENT WHERE key = "IRS") TO (SELECT FROM COMPONENT WHERE key = "DELIVERY_IRS")
CREATE EDGE Manifested_By FROM (SELECT FROM COMPONENT WHERE key = "IRS") TO (SELECT FROM Artifact WHERE key = "IRS_EAR")
CREATE EDGE Deployed_To FROM (SELECT FROM Artifact WHERE key = "IRS_EAR") TO (SELECT FROM ExecutionEnvironment WHERE key = "WLH01")
CREATE EDGE Deployed_To FROM (SELECT FROM COMPONENT WHERE key = "DELIVERY_IRS") TO (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_WLH01")
CREATE EDGE Has FROM (SELECT FROM COMPONENT WHERE key = "DELIVERY_IRS") TO (Select FROM DeploymentSpec WHERE key = "DELIVERY_IRS_DEPLOYMENTSPEC")
CREATE EDGE Instance_Of FROM (Select FROM DeploymentSpec WHERE key = "IRS_DEPLOYMENTSPEC") TO (Select FROM DeploymentSpec WHERE key = "DELIVERY_IRS_DEPLOYMENTSPEC")
CREATE EDGE Deployed_To FROM (Select FROM Artifact WHERE key = "IRS_EAR") TO (Select FROM DeploymentSpec WHERE key = "IRS_DEPLOYMENTSPEC")

-- Application: VMAD

CREATE VERTEX LogicalComponent SET key = "VMAD"
CREATE VERTEX PhysicalComponent SET key = "DELIVERY_VMAD"
CREATE VERTEX  Artifact SET key = "VMAD_EAR"
CREATE VERTEX  DeploymentSpec SET key = "DELIVERY_VMAD_DEPLOYMENTSPEC"
CREATE VERTEX  DeploymentSpec SET key = "VMAD_DEPLOYMENTSPEC"

CREATE EDGE Delivery_Instance FROM (SELECT FROM COMPONENT WHERE key = "VMAD") TO (SELECT FROM COMPONENT WHERE key = "DELIVERY_VMAD")
CREATE EDGE Manifested_By FROM (SELECT FROM COMPONENT WHERE key = "VMAD") TO (SELECT FROM Artifact WHERE key = "VMAD_EAR")
CREATE EDGE Deployed_To FROM (SELECT FROM Artifact WHERE key = "VMAD_EAR") TO (SELECT FROM ExecutionEnvironment WHERE key = "WLH01")
CREATE EDGE Deployed_To FROM (SELECT FROM COMPONENT WHERE key = "DELIVERY_VMAD") TO (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_WLH01")
CREATE EDGE Has FROM (SELECT FROM COMPONENT WHERE key = "DELIVERY_VMAD") TO (Select FROM DeploymentSpec WHERE key = "DELIVERY_VMAD_DEPLOYMENTSPEC")
CREATE EDGE Instance_Of FROM (Select FROM DeploymentSpec WHERE key = "VMAD_DEPLOYMENTSPEC") TO (Select FROM DeploymentSpec WHERE key = "DELIVERY_VMAD_DEPLOYMENTSPEC")
CREATE EDGE Deployed_To FROM (Select FROM Artifact WHERE key = "VMAD_EAR") TO (Select FROM DeploymentSpec WHERE key = "VMAD_DEPLOYMENTSPEC")

-- Application: TITAN

CREATE VERTEX DeviceNode SET key = "SERVER1"
CREATE VERTEX DeviceNode SET key = "SERVER2"

CREATE VERTEX  LogicalExecutionEnvironment SET key = "TC01"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "DELIVERY_TC01"
CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "TC01") TO (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_TC01")
CREATE EDGE Hosted_By FROM (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_TC01") TO (SELECT FROM DeviceNode WHERE key = "SERVER1")
CREATE EDGE Hosted_By FROM (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_TC01") TO (SELECT FROM DeviceNode WHERE key = "SERVER2")

CREATE VERTEX LogicalComponent SET key = "TITAN"
CREATE VERTEX PhysicalComponent SET key = "DELIVERY_TITAN"
CREATE VERTEX  Artifact SET key = "TITAN_EAR"
CREATE VERTEX  DeploymentSpec SET key = "DELIVERY_TITAN_DEPLOYMENTSPEC"
CREATE VERTEX  DeploymentSpec SET key = "TITAN_DEPLOYMENTSPEC"

CREATE EDGE Delivery_Instance FROM (SELECT FROM COMPONENT WHERE key = "TITAN_EAR") TO (SELECT FROM COMPONENT WHERE key = "DELIVERY_TITAN")
CREATE EDGE Manifested_By FROM (SELECT FROM COMPONENT WHERE key = "TITAN") TO (SELECT FROM Artifact WHERE key = "TITAN_EAR")
CREATE EDGE Deployed_To FROM (SELECT FROM Artifact WHERE key = "TITAN_EAR") TO (SELECT FROM ExecutionEnvironment WHERE key = "TC01")
CREATE EDGE Deployed_To FROM (SELECT FROM COMPONENT WHERE key = "DELIVERY_TITAN") TO (SELECT FROM ExecutionEnvironment WHERE key = "DELIVERY_TC01")
CREATE EDGE Has FROM (SELECT FROM COMPONENT WHERE key = "DELIVERY_TITAN") TO (Select FROM DeploymentSpec WHERE key = "DELIVERY_TITAN_DEPLOYMENTSPEC")
CREATE EDGE Instance_Of FROM (Select FROM DeploymentSpec WHERE key = "TITAN_DEPLOYMENTSPEC") TO (Select FROM DeploymentSpec WHERE key = "DELIVERY_TITAN_DEPLOYMENTSPEC")
CREATE EDGE Deployed_To FROM (Select FROM Artifact WHERE key = "TITAN_EAR") TO (Select FROM DeploymentSpec WHERE key = "TITAN_DEPLOYMENTSPEC")

-- Database connection

CREATE VERTEX  DatabaseConnection SET key = "Conn1", user = "titan_proxy", schema = "titan", database_instance = "OSDB", database_server = "Cluster1"

CREATE VERTEX LogicalComponent SET key = "TITAN_DB"

CREATE EDGE Manifested_By FROM (SELECT FROM LogicalComponent WHERE key = "TITAN") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_DB")

CREATE EDGE Connects_To FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_DB") TO (SELECT FROM DatabaseConnection WHERE key = "Conn1")

--	What if Titan is down?
--  SELECT FROM (TRAVERSE outE(), outV() from (SELECT FROM `LogicalComponent` where key = "TITAN"))

--	What is the impact of the Delivery Instance being down?
--  SELECT FROM (TRAVERSE outE(), outV() from (SELECT FROM `LogicalComponent` where key = "DELIVERY_TITAN")) 

--	What is the impact of the delivery servers down?
--  SELECT FROM (TRAVERSE outE(), outV() from (SELECT FROM `ExecutionEnvironment` WHERE key = "DELIVERY_TC01"))


--	What is the impact of the cluster being down?
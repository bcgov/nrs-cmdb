--connect remote:localhost/cmdb user pass
-- LOAD SCRIPT C:/Data/projects/dcts/research/OrientDB/model.sql

-- QUERIES:

-- List the all nodes (paths) affected by 'SERVERA_I1'
-- select from (TRAVERSE in() FROM (select * from `PhysicalServer` where key = "SERVERA_I1")) 
-- List all LogicalComponent affected by 'SERVERA_I1'
-- select from (TRAVERSE in() FROM (select * from `PhysicalServer` where key = "SERVERA_I1")) where @class='LogicalComponent'

-- select from (TRAVERSE out('Manifests') FROM (select * from `Artifact` where key = "TITAN_JDBC")) 

-- Impacted components when an artifact is Updated/Deployed
--select from ( TRAVERSE in() FROM (select from (TRAVERSE out('Manifests') FROM (select * from `Artifact` where key = "TITAN_JDBC")) where @class='LogicalComponent'))


--Delete ALL data (vertex/edge)
DELETE VERTEX V;
DELETE EDGE E;

--Drop all (user) classes
js; function dropClass(oclass, bdel){  var oclasses=oclass.getSubclasses().iterator();  while (oclasses.hasNext()) {    dropClass(oclasses.next(), true);  };  if (bdel){print("Dropping '"+oclass.getName()+"'");  db.executeCommand("DROP CLASS "+oclass.getName());};}; dropClass(db.metadata.schema.getClass('E'), false); dropClass(db.metadata.schema.getClass('V'), false);end;


CREATE CLASS Component IF NOT EXISTS EXTENDS V
CREATE CLASS Environment IF NOT EXISTS EXTENDS V
CREATE CLASS ExecutionEnvironment IF NOT EXISTS EXTENDS V 
CREATE CLASS Artifact IF NOT EXISTS EXTENDS V 
CREATE CLASS DeploymentSpec IF NOT EXISTS EXTENDS V 
CREATE CLASS DeviceNode IF NOT EXISTS EXTENDS V
CREATE CLASS VirtualServer IF NOT EXISTS EXTENDS DeviceNode
CREATE CLASS PhysicalServer IF NOT EXISTS EXTENDS DeviceNode
CREATE CLASS DeploymentSpec IF NOT EXISTS EXTENDS V 
CREATE CLASS PropertyValue IF NOT EXISTS EXTENDS V 
CREATE CLASS PropertyName IF NOT EXISTS EXTENDS V 
CREATE CLASS LogicalComponent IF NOT EXISTS EXTENDS Component 
CREATE CLASS PhysicalComponent IF NOT EXISTS EXTENDS Component
CREATE CLASS BundleLogicalComponent IF NOT EXISTS EXTENDS LogicalComponent  
CREATE CLASS LogicalExecutionEnvironment IF NOT EXISTS EXTENDS ExecutionEnvironment 
CREATE CLASS PhysicalExecutionEnvironment IF NOT EXISTS EXTENDS ExecutionEnvironment 
CREATE CLASS LogicalDeploymentSpec IF NOT EXISTS EXTENDS DeploymentSpec 
CREATE CLASS PhysicalDeploymentSpec IF NOT EXISTS EXTENDS DeploymentSpec

CREATE CLASS Delivery_Instance IF NOT EXISTS EXTENDS E 
CREATE CLASS Manifested_By IF NOT EXISTS EXTENDS E 
CREATE CLASS Manifests IF NOT EXISTS EXTENDS E 
CREATE CLASS Deployed_To IF NOT EXISTS EXTENDS E 
CREATE CLASS Instance_Of IF NOT EXISTS EXTENDS E 
CREATE CLASS Has_Instance IF NOT EXISTS EXTENDS E 
CREATE CLASS Has IF NOT EXISTS EXTENDS E 
CREATE CLASS Hosted_By IF NOT EXISTS EXTENDS E 
CREATE CLASS Uses IF NOT EXISTS EXTENDS E 
CREATE CLASS Is_Used_By IF NOT EXISTS EXTENDS E 

CREATE VERTEX Environment SET key = "INTEGRATION"
CREATE VERTEX Environment SET key = "TEST"
CREATE VERTEX Environment SET key = "PRODUCTION"

-- Infrastructure
CREATE VERTEX PhysicalServer SET key = "ORADBSA1"
CREATE VERTEX PhysicalServer SET key = "ORADBSA2"


CREATE VERTEX PhysicalServer SET key = "SERVERA_I1"
CREATE VERTEX PhysicalServer SET key = "SERVERA_T1"
CREATE VERTEX PhysicalServer SET key = "SERVERA_P1"

CREATE VERTEX VirtualServer  SET key = "SERVERB_1"
CREATE VERTEX VirtualServer  SET key = "SERVERB_2"

CREATE VERTEX  LogicalExecutionEnvironment SET key = "ORADB1", name = "Oracle Database"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "ORADB1I"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "ORADB1T"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "ORADB1P"

CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "ORADB1I") TO (SELECT FROM ExecutionEnvironment WHERE key = "ORADB1")
CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "ORADB1T") TO (SELECT FROM ExecutionEnvironment WHERE key = "ORADB1")
CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "ORADB1P") TO (SELECT FROM ExecutionEnvironment WHERE key = "ORADB1")


-- Middle Tier
CREATE VERTEX  LogicalExecutionEnvironment SET key = "WLS", name = "Oracle WebLogic"
CREATE VERTEX  LogicalExecutionEnvironment SET key = "WLS1"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "WLS1I"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "WLS1T"
CREATE VERTEX  PhysicalExecutionEnvironment SET key = "WLS1P"

CREATE VERTEX  LogicalExecutionEnvironment SET key = "TC", name = "Tomcat"
CREATE VERTEX  LogicalExecutionEnvironment SET key = "IIS", name = "IIS"


CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1") TO (SELECT FROM ExecutionEnvironment WHERE key = "WLS")

--CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1I") TO (SELECT FROM Environment WHERE key = "INTEGRATION")
CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1I") TO (SELECT FROM ExecutionEnvironment WHERE key = "WLS1")

--CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1T") TO (SELECT FROM Environment WHERE key = "TEST")
CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1T") TO (SELECT FROM ExecutionEnvironment WHERE key = "WLS1")

--CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1P") TO (SELECT FROM Environment WHERE key = "PRODUCTION")
CREATE EDGE Instance_Of FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1P") TO (SELECT FROM ExecutionEnvironment WHERE key = "WLS1")

CREATE EDGE Deployed_To FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1I") TO (SELECT FROM DeviceNode WHERE key = "SERVERA_I1")
CREATE EDGE Deployed_To FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1T") TO (SELECT FROM DeviceNode WHERE key = "SERVERA_T1")
CREATE EDGE Deployed_To FROM (SELECT FROM ExecutionEnvironment WHERE key = "WLS1P") TO (SELECT FROM DeviceNode WHERE key = "SERVERA_P1")



--Artifacts
CREATE VERTEX  Artifact SET key = "TITAN_EAR"
CREATE VERTEX  Artifact SET key = "TITAN_JDBC"
CREATE VERTEX  Artifact SET key = "TITAN_DB"

--Logical Components
CREATE VERTEX LogicalComponent SET key = "TITAN_EAR"
CREATE VERTEX LogicalComponent SET key = "TITAN_JDBC"
CREATE VERTEX LogicalComponent SET key = "TITAN_DB"

-- Define which Artifact manisfest which LogicalComponent
CREATE EDGE Manifests FROM (SELECT FROM Artifact WHERE key = "TITAN_EAR") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_EAR")
CREATE EDGE Manifests FROM (SELECT FROM Artifact WHERE key = "TITAN_JDBC") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_JDBC")
CREATE EDGE Manifests FROM (SELECT FROM Artifact WHERE key = "TITAN_DB") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_DB")

--CREATE EDGE Is_Used_By FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_JDBC") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_EAR")
CREATE EDGE Uses FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_EAR") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_JDBC")


-- Create Parent (top-level) component/bundle
CREATE VERTEX LogicalComponent SET key = "TITAN"
CREATE EDGE Has FROM (SELECT FROM LogicalComponent WHERE key = "TITAN") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_EAR")
CREATE EDGE Has FROM (SELECT FROM LogicalComponent WHERE key = "TITAN") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_JDBC")
CREATE EDGE Has FROM (SELECT FROM LogicalComponent WHERE key = "TITAN") TO (SELECT FROM LogicalComponent WHERE key = "TITAN_DB")

-- Physical Components
CREATE VERTEX PhysicalComponent SET key = "TITAN_EAR_I"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_EAR") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_EAR_I")
CREATE VERTEX PhysicalComponent SET key = "TITAN_EAR_T"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_EAR") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_EAR_T")
CREATE VERTEX PhysicalComponent SET key = "TITAN_EAR_P"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_EAR") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_EAR_P")
CREATE EDGE Deployed_To FROM (SELECT FROM PhysicalComponent WHERE key = "TITAN_EAR_I") TO (SELECT FROM PhysicalExecutionEnvironment WHERE key = "WLS1I")
CREATE EDGE Deployed_To FROM (SELECT FROM PhysicalComponent WHERE key = "TITAN_EAR_T") TO (SELECT FROM PhysicalExecutionEnvironment WHERE key = "WLS1T")
CREATE EDGE Deployed_To FROM (SELECT FROM PhysicalComponent WHERE key = "TITAN_EAR_P") TO (SELECT FROM PhysicalExecutionEnvironment WHERE key = "WLS1P")


CREATE VERTEX PhysicalComponent SET key = "TITAN_JDBC_I"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_JDBC") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_JDBC_I")
CREATE VERTEX PhysicalComponent SET key = "TITAN_JDBC_T"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_JDBC") To (SELECT FROM PhysicalComponent WHERE key = "TITAN_JDBC_T")
CREATE VERTEX PhysicalComponent SET key = "TITAN_JDBC_P"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_JDBC") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_JDBC_P")
CREATE EDGE Deployed_To FROM (SELECT FROM PhysicalComponent WHERE key = "TITAN_JDBC_I") TO (SELECT FROM PhysicalExecutionEnvironment WHERE key = "WLS1I")
CREATE EDGE Deployed_To FROM (SELECT FROM PhysicalComponent WHERE key = "TITAN_JDBC_T") TO (SELECT FROM PhysicalExecutionEnvironment WHERE key = "WLS1T")
CREATE EDGE Deployed_To FROM (SELECT FROM PhysicalComponent WHERE key = "TITAN_JDBC_P") TO (SELECT FROM PhysicalExecutionEnvironment WHERE key = "WLS1P")


CREATE VERTEX PhysicalComponent SET key = "TITAN_DB_I"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_DB") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_DB_I")
CREATE VERTEX PhysicalComponent SET key = "TITAN_DB_T"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_DB") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_DB_T")
CREATE VERTEX PhysicalComponent SET key = "TITAN_DB_P"
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_DB") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_DB_P")


--CREATE EDGE Deployed_To FROM (SELECT FROM Artifact WHERE key = "TITAN_EAR") TO (SELECT FROM V WHERE key = "WLS1")
--CREATE EDGE Deployed_To FROM (SELECT FROM Artifact WHERE key = "TITAN_JDBC") TO (SELECT FROM V WHERE key = "WLS1")
--CREATE EDGE Deployed_To FROM (SELECT FROM Artifact WHERE key = "TITAN_DB") TO (SELECT FROM V WHERE key = "WLS1")

--CREATE EDGE Deployed_To FROM (SELECT FROM V WHERE key = "TITAN_EAR_I") TO (SELECT FROM V WHERE key = "WLS1I")


CREATE VERTEX  LogicalComponent SET key = "TITAN_SCHEMA"
CREATE VERTEX PhysicalComponent SET key = "TITAN_SCHEMA_I"
CREATE VERTEX PhysicalComponent SET key = "TITAN_SCHEMA_T"
CREATE VERTEX PhysicalComponent SET key = "TITAN_SCHEMA_P"

CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_SCHEMA") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_SCHEMA_I")
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_SCHEMA") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_SCHEMA_T")
CREATE EDGE Has_Instance FROM (SELECT FROM LogicalComponent WHERE key = "TITAN_SCHEMA") TO (SELECT FROM PhysicalComponent WHERE key = "TITAN_SCHEMA_P")

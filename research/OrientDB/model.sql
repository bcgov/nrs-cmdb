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

# NRS CMDB

NRS CMDB is an exploratory project to implement a modern Configuration Management Database (CMDB) for the Natural Resource Sector (NRS) of the BC Government. 

## Current Focus

While there is a lot of existing Configuration Management (CM) data available to the NRS Ops team (for example in the current IRS application and data store) and a lot of CM data that we would like to have in the envisioned CMDB goal, this section documents the current focus of this project - what is currently in place and what we are trying to do.

### Getting App Properties for Deployment

An app (which made up of a number of separately deployed components) under management at NRS, has a series of Properties files stored in a private Stash (git) repository that are managed by both the development team and the NRS Ops team. The files form a hierarchy:

* App default
* App Environment default
* Component
* Component Environment default

During the deployment of a component to an environment, the hierarchy of files are loaded in order to create the set of properties needed for the deployment.

In general, there are three classes of properties (name/value pairs):

* Elements used internally by the component
* Elements that imply relationships between systems - e.g. database connect strings, API endpoints to other systems, etc.
* Secrets - e.g. credentials needed by the component to access resources (e.g. database user name and password, API credentials)

### New Approach

Rather than storing the component properties in a git repository in files, we'd like to put the properties into a database of some type and put in front of the database:

* An api to retrieve the properties at deploy time - e.g. given a component and environment.
* A UI to manage the properties in the database

With that in place, we'd like to increase the value of the properties by:

* Exploring the properties to identify relationships and, most importantly, dependencies that are implied by the properties.
* Implement mechanisms to instantiate those dependencies.
* Add mechanisms to query/visualize those dependencies, so that application relationships can be easily found.

## Tech Stack

We are just getting started on this project. At this point, the only decision made so far is to assume (or now, at least) that we'll be using the graph database [OrientDB](http://orientdb.com/) as the persistence layer. During the initial sprint of this project, we're doing some technical spikes aimed at determining a good language/framework to use for the API layer (and possibly other layers). Good candidates include Django (python), Java (the language of choice at NRS) and the [Sailjs](http://sailsjs.com/) Framework (JavaScript).

## Project Tracking

We are using github issues and a [project Kanban board](https://github.com/bcgov/dcts/projects/1) to track progress on this project. Please checkout our progress there.


----------


## Demo Requirements

The demo system requires the following:
1. An OpenShift 3.x cluster
2. Access to Red Hat Enterprise Linux (RHEL) images
	1. RHEL7 is a dependency of the OrientDB container
3. The ability to create working Persistent Volumes 
	1. For testing purposes you can use ephemeral storage, however it is not recommended

## CMDB Demonstration

To setup the CMDB demonstration:

1. Create projects for OpenShift tools and dev environments.
	1. oc create project cmdb-tools
	2. oc create project cmdb-dev
2. Grant image pull access from dev to tools
	1.  oc policy add-role-to-user system:image-puller system:serviceaccount:cmdb-dev:default -n cmdb-tools
3. Go to the Tools project
	1. oc project cmdb-tools
4. Import the Tools environment
	1. oc process -f cmdb-build-template.json | oc process -f -
5. Verify that the orientdb and cmdb build configurations run successfully.  You may have to manually start the builds if they do not run immediately after import.
6. Go to the Dev project
	1. oc project cmdb-dev
7. Import the Dev environment
	1. oc process -f cmdb-deployment-template.json | oc process -f -
8. Verify that Orient DB and the CMDB rest server are running
	1. Go to the Routes page in OpenShift
		1. Go to the Orientdb route (Do not use Internet Explorer; OrientDB requires a recent browser such as Chrome)
		2. Verify you can login. 
	2. Go to the CMDB route
		1. Verify that you get a 404 "Whitelable Error Page"
	3. If either of the above do not work, investigate the OpenShift pods and determine if there are any issues.
9.  Install OrientDB client tools
	1.  Go to http://orientdb.com/download/
	2.  Download the version 2 .zip file
	3.  Unzip the zip file
	4.  You will use bin/console.bat to interact with the database.
9.  Load sample data
	1.  Connect a command prompt to the OpenShift cluster using `oc login`
	2.  Switch to the cmdb-dev project
		1.  `oc project cmdb-dev`
	3.  Find the name of the orientdb pod
		1.  `oc get pods`
	4.  Forward the OrientDB data port
		1.  `oc port-forward <podname> 2424:2424`
	5.  Run the OrientDB console.bat command to start the OrientDB interactive console
	6.  Execute the OrientDB console commands located in the following files:
		1. https://github.com/bcgov/dcts/blob/master/research/OrientDB/model.sql
		2. https://github.com/bcgov/dcts/blob/master/research/OrientDB/sample-db.sql
10.  Demonstration of Graph Queries
	1.  First, login to the graph database viewer.  If you do not know the URL, look it up in the OpenShift routes section.
	2.  Navigate to the Schema view
	3.  Do a basic query and send it to the graph.  For example, click "Query All" next to a Vertex and then click the "Send to Graph" button (icon is a small circle).
	4.  On the graph, click on a node and then configure the graph viewer settings so that the visual graph will have meaningful information (click on a node, and use the settings view on the left side of the screen to configure the Display settings.)
	5.  At the end of sample-db.sql there are several commented out queries.  You can run these in the OrientDB web viewer, and use the "Send to Graph" feature to show a visual representation of the graph for various scenarios. 
11.  Demonstration of Get / Set properties
	1.  This project includes a proof of concept system that allows elements of the graph database to be retrieved / updated from a CI script.
	2.  In the repository there is a batch file that can be run to demonstrate this activity.  
		1.    The batch file is located at https://github.com/bcgov/dcts/blob/master/research/demo_get_set.bat 
		2.    Pass the base URL to the rest service as a parameter to the batch file   


## License

Code released under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).




    

# dcts
Deployment Configuration Tracking Services

DCTS is an exploratory project to implement a modern Configuration Management Database (CMDB) for the Natural Resource Sector (NRS) of the BC Government. Depending on how far we get with this initial spike and how useful it is, this project may be renamed to CMDB.

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

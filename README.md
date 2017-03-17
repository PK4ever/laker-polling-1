Waffle: [![Stories in Ready](https://badge.waffle.io/CSC480/laker-polling.png?label=ready&title=Ready)](https://waffle.io/CSC480/laker-polling)
| Master Branch:   [![Build Status](https://travis-ci.org/CSC480/laker-polling.svg?branch=master)](https://travis-ci.org/CSC480/laker-polling)
| Develop Branch:   [![Build Status](https://travis-ci.org/CSC480/laker-polling.svg?branch=develop)](https://travis-ci.org/CSC480/laker-polling)

# Laker Polling
Open source implementation of clicker software

## How to deploy this application
The following guide explains how to run this application on your system.
##### Install MySQL(If you already have MySQL, skip this step)  
Look up a guide for installing MySQL on your Operating system. Also find out how to
add MySQL to your path and start your MySQL server for your relavant operating system.
##### Create a database and a user(If you already have on ready, skip this step.)  
Using your cmd/terminal log into MySQL server as root user.
```
mysql -u root -p
```
Issue the following commands in the given order to create a database, 
create a user, and grant the user access to the database.
```MySQL
CREATE DATABASE your_database_name;
CREATE USER 'your_user_name'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL ON your_database_name.* TO 'your_user_name'@'localhost';
```

#####Configure the project.
Rename or clone the file [application.example.groovy](./grails-app/conf/application.example.groovy) inside the
[grails-app/conf](./grails-app/conf) folder to 'application.groovy'.

Inside this file you will find several lines of the form  
something = \<Your info here>  
Replace the text inside the \< > with the relevant information.  
ex  
```groovy
username = '<Your db username here>'
```
becomes
```groovy
username = 'databaseadmin'
```

#####Run the application
```
./grailsw run-app
```

## Teams
### Requirements
#### Members
<ul>
 <li>Malik Riddle</li>
 <li>Kenny Roffo</li>
 <li>Ryan Staring</li>
 <li>Logan Wells</li>
</ul>
#### Purpose
To determine and document the agreed upon requirements for the project, making adjustments when appropriate.

### QA
#### Members
<ul>
 <li>Amber Bang</li>
 <li>Mateusz Wiater</li>
 <li>Sam Schneller</li>
 <li>Calvin Lawrence</li>
 <li>Andres Ramos</li>
</ul>
#### Purpose
Manage software for project, and maintain code quality through use of tools, like test suites and linters, as well as other software management tools, such as Gantt charts and Waffle.io.

### Database
#### Members
<ul>
 <li>Josh Post</li>
 <li>Ricky Rojas</li>
 <li>Matt Wu</li>
 <li>Akeem Davis</li>
 <li>Keith Martin</li>
</ul>
#### Purpose
Store all necessary data in a structure that is easy and efficient to access.

### GUI
#### Members
<ul>
 <li>Paul Kwoyelo</li>
 <li>Mike Mekker</li>
 <li>Lincoln Daniel</li>
 <li>Mike Cavataio</li>
 <li>Jamie Garcia</li>
 <li>Francisco Ovalle</li>
 <li>Tyler Moson</li>
</ul>
#### Purpose
Developing an interface for students and an interface for professors.

### Engine
#### Members
<ul>
 <li>Lauren Mulvehill</li>
 <li>Brandon Lanthrip</li>
 <li>Max Sokolovsky</li>
 <li>Steve DiCerce</li>
 <li>Zach Sabin</li>
 <li>Jeff Registre</li>
</ul>
#### Purpose
Developing a RESTful (Representational State Transfer) web service that the front end can interface with. This is being done in Groovy on Grails, which is an open source web framework that runs on the JVM stack.

### Usability
#### Members
<ul>
 <li>Ashley Lefebvre</li>
 <li>Christian Damico</li>
 <li>Connor Gannon</li>
 <li>Christopher Jankovski</li>
</ul>
#### Purpose
Focused on evaluating the quality of a user's experience when interacting with a product or system. Tests, analyzes and reports results, and makes recommendations to improve effectiveness, efficiency, and overall satisfaction of the user.

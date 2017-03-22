Waffle: [![Stories in Ready](https://badge.waffle.io/CSC480/laker-polling.png?label=ready&title=Ready)](https://waffle.io/CSC480/laker-polling)
| Master Branch:   [![Build Status](https://travis-ci.org/CSC480/laker-polling.svg?branch=master)](https://travis-ci.org/CSC480/laker-polling)
| Develop Branch:   [![Build Status](https://travis-ci.org/CSC480/laker-polling.svg?branch=develop)](https://travis-ci.org/CSC480/laker-polling)

# Laker Polling
Open source implementation of clicker software

## How to deploy this application
Clone this repository into your system. It works right out of the box without any additonal 
configuration required. However, note that by default it is using an in memory data store system. This
is perfect for a development environment but not ideal for production.

To run this project, navigate to the root of the project in your terminal/cmd and run the
following command.

```
./grailsw run-app
```

## How to configure a data source.
The following details how to configure this project to use a database (We'd recommend using 
[PostgreSQL](https://www.postgresql.org/)) rather than the 
in-memory data store used by default. This assumes you already have a valid installation of
Postgres or MySQL on your system. If not, look up a guide to install one on your OS.

We'll also assume you have a database and a user for that database created. Once again,
follow a guide for the data store you are using.

Copy the file [dbconfig.example.yml](./grails-app/conf/dbconfig.example.yml)
under the <i>[grails-app/conf](./grails-app/conf)</i> directory as <i>dbconfig.yml.</i>

Edit this file and replace the placeholders such as 
<b>`username: <YOUR DATABASE USER NAME HERE>`</b> 
with your actual information.

Note the lines 
`driverClassName: org.postgresql.Driver` (use `com.mysql.jdbc.Driver` for MySQL) and
`url: jdbc:postgresql://<HOST>:<PORT>/<DATABASE NAME>` (use `jdbc:mysql://<HOST>:<PORT>/<DATABASE NAME>` for MySQL)
change depending on if you're using PostgreSQL or MySQL.

## Teams
### Requirements
* Malik Riddle
* Kenny Roffo
* Ryan Staring
* Logan Wells

### QA
* Amber Bang
* Mateusz Wiater
* Sam Schneller
* Calvin Lawrence
* Andres Ramos

### Database
* Josh Post
* Ricky Rojas
* Matt Wu
* Akeem Davis
* Keith Martin

### GUI
* Paul Kwoyelo
* Mike Mekker
* Lincoln Daniel
* Mike Cavataio
* Jamie Garcia
* Francisco Ovalle
* Tyler Moson

### Engine
* Lauren Mulvehill
* Brandon Lanthrip
* Max Sokolovsky
* Steve DiCerce
* Zach Sabin
* Jeff Registre
 
### Usability
* Ashley Lefebvre
* Christian Damico
* Connor Gannon
* Christopher Jankovski

# SageLoanAPound

LoanAPound is a new online loan broker looking to launch in the next few weeks. They have asked you to work on the design of their new system and there are 2 tasks you have been asked to do:
- Design a data model to support the LoanAPound solution
- Implement a Credit Check Service (user stories 3 and 4) and a basic UI to test it

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### MySQL Server

* [Download](https://dev.mysql.com/downloads/installer/) - MySQL installer

Installer which deals with installation and configuration


### Apache Maven

* [Download](https://maven.apache.org/download.cgi) - Maven zip download
* [Installation](https://maven.apache.org/install.html) - Maven instalation instructions
* [Configuration](https://maven.apache.org/configure.html) - Maven configuration instructions


### Apache Tomcat

 Used for the mock credit score API

* [Download](https://tomcat.apache.org/download-70.cgi) - Tomcat download
 
Installer which deals with installation and configuration


## Installing

### Database Setup
```
 All the scripts to build the database are in ./database/tables and ./database/views, however I have included an export of the full schema to make it easier to run.
 The commands are set to use the root user but this can be changed for any user who can create users and schemas;

 cd ./database/setup
 mysql -uroot -p < create_application_user.sql
 mysql -uroot -p < create_loanapound_schema.sql
 mysql -uroot -p loanapound < export_loanapound.sql
```

### Java compilation
```
 Java complilation is done using Maven. There is a parent pom file in the root of the repository which compiles everything.
 
 mvn clean package
```

### CreditScoreAPI deployment
```
 The API will have been packed into a war file. This can be found in ./bin/CreditScoreAPI-1.0.war
 
 This war file needs to be deployed in Tomcat. To be safe I would recommend stopping the server before deployment and this can be done by going to %TOMCAT_HOME%/bin/shutdown.bat.

 Copy the war file to %TOMCAT_HOME%/webapps.

 Once the war file is deployed start the Tomcat server again with %TOMCAT_HOME%/bin/startup.bat.
```

### Loan Engine
```
 The loan engine does not output to a console, so we can just start it using a javaw command;

 cd ./bin
 javaw -jar SageLoanAPoundLoanEngine-1.0.jar
```

### Front End
```
 There is a (very) basic front end which users can use to interact with the database. This is produced by a java console.

 cd ./bin
 java -jar SageLoanAPound-1.0.jar
```

## Using the front end

A brief explination of how to use the front end

The available logins are;
  * administrator
  * applicant
  * applicant_cs
  * underwriter

All passwords are set to “secret”

Once logged in the user will see the screen based on what type of user they are (administrator, applicant or underwriter). 

Initially the underwriter will see no data, an applicant will have to make an application which gets approved before they will be able to do anything. The loan engine must be running before any applications will be approved and sent to the underwriter.

Administrators have the option to;
  * View the existing products
  * Add new products
  * Delete old products

Applicants have the option to;
  * View existing products
  * Create an application for a product
  * View the status of previous applications

Underwriters have the option to;
  * View and approve pending requests


## Running the tests

To run the unit tests run the command "mvn clean test" at the root of the repsoitory.

All unit tests are run at build time as well and the build will not go ahead on if any tests are failed.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## External libaries used

### Testing

* junit 			version 4.12
* hamcrest-libary 		version 1.3
* dumbster 			version 1.7.1

### Database

* mysql-connector-java		version 5.1.44

### Email

* mail				version 1.4

### Rest-API

* jersy-client			version 2.26

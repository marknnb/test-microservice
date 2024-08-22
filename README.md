# README

# How to?
* Follow the steps to run the project
  * Prerequisite : docker is running in local
  * Run docker compose file to start DB. ```docker compose -f infra.yml up -d```
  * Run ./mvnw mvn clean install
  * Run java -jar jar_name
  * or you can run the project from IDE like intellij
  * run ```./mvnw -X spring-boot:build-image -DskipTests``` to build docker image out of the recipe-service
  * Swagger : http://localhost:8081/swagger-ui.html#/

* Run Test Cases:
  * Prerequisite : docker is running in local as TestContainers are being used
  * run command ```docker compose -f infra.yml up -d``` or intellij has internal support for docker that can be also used.

    
# Resources
* Kanban for project : https://github.com/users/marknnb/projects/1
* build pipeline : https://github.com/marknnb/recipe-service/actions/workflows/recipe-service.yml

# Component of the project
* Docker is used to spin up the Database
* TestContainer are used to simulate real time test cases
* Flyway is used for Database migration
* Application is dockerized and image can be built from running spring boot image plugin
* Spotless Maven plugin is being used to make sure code is readable and maintainable
* Git maven plugin is used to capture the commit details on application
* 

# decisions taken while implementing the recipe-service
* package by layer
  * file structure is based on domain layers in the application.

* text vs separate table for Ingredients and Instructions
  * Deliberate decision has been taken while deciding table structure for Recipe payload to convert Ingredients and Instructions in to separate project 
  * If we have different table for these two attribute , recipes can be managed on fined grained level
  * For filter native is used to improve performance of the operation. Search criteria can be created using JPA on application side.
  * But I have taken decision to do in it query so that performance is enhanced.
# Energy Management System

## Overview
This project leverages **Docker** to create a consistent and portable development environment, featuring eleven services:

* Angular Frontend application
* Two Spring Boot User microservices
* Spring Boot Device microservice
* Spring Boot Monitoring microservice
* Spring Boot Chat microservice
* PostgreSQL User/Device/Monitor databases
* RabbitMq Server
* Reverse Proxy Server

Services communicate through a custom Docker network.

## Prerequisites

Before bulding and running the project, the following should be installed:

- [Docker](https://docs.docker.com/get-docker/)

## Building the Project

To build the Docker images for services, run:

`docker-compose build --no-cache`

The `--no-cache` option ensures that Docker builds the images without using any cached layers, ensuring that the latest code and dependencies are included in the build.

## Running the Project

To start the application and run all services defined in the docker-compose.yml file, run:

`docker-compose up`

Once the services are up and running, a link to the application will be displayed in the terminal output. The application can be accessed by navigating to that link in the web browser. 

## Closing the Project

To close the application and shut down all services, run:

`docker-compose down`
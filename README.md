# Energy Management System

## Overview
This project is a distributed energy management system designed to monitor and manage energy consumption across various devices, featuring a microservices architecture deployed using Docker. The system includes the following key features:

### Microservices Architecture:

* User Management: Handles user authentication and administration.
* Device Management: Manages device registration and monitoring.
* Energy Monitoring: Tracks energy consumption in real-time, with notifications for anomalies.
* Chat Service: Enables communication between clients and administrators.

### Technology Stack:
* Backend (Spring): Built with a microservices architecture, ensuring scalability and modularity.
* Frontend (Angular): User interface for client and admin roles, including authentication, device management and chat functionalities.
* Load Balancing: Implemented via Traefik for enhanced performance and reliability.
* Messaging Queue: Leverages RabbitMQ for efficient communication between microservices.
* Real-Time Updates: WebSockets for live notifications and chat functionality.

### System Features:
* Sends alerts when device energy consumption exceeds predefined thresholds.
* Supports role-based operations for administrators and clients.
* Ensures secure data transmission with JWT-based authentication.
* Logs energy usage and synchronizes data across microservices through dedicated APIs.

## Docker  
This project leverages **Docker** to create a consistent and portable development environment, including database persistence with Docker volumes, featuring eleven services:

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

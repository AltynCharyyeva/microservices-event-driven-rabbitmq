## Energy Management System (Part 2):
This project extends the previous REST-based microservices system into an event-driven architecture using RabbitMQ as a message broker and Traefik as load balancer and reverse proxy.
Here is an illustration of how a basic queue between Producer and Consumer works:
<img width="720" height="318" alt="producer-consumer" src="https://github.com/user-attachments/assets/7320ebb5-d5b1-44a2-9966-2f5b08fddfa1" />

- Monitoring microservice:
  One more microservice(Monitoring) was added which processes data from smart metering devices, compute the hourly energy
consumption, and store the results in its dedicated database.

- Producer:
  A simple desktop application that simulates smart meter readings by generating energy consumption values at 10-minute intervals.

  Overall conceptual architecture of the application:
  <img width="760" height="797" alt="conceptual_diag" src="https://github.com/user-attachments/assets/5ae42aaf-9616-4f16-9f52-ea199667ce64" />

  Deployed using Docker
  <img width="762" height="797" alt="docker_sd_a2" src="https://github.com/user-attachments/assets/7d02a475-4667-4cba-9d03-6caa378a154d" />


  


#!/bin/bash
chmod +x ./mvnw
./mvnw clean verify
docker rmi -f clinic:0.0.1
docker build -t clinic:0.0.1 .
docker compose up -d
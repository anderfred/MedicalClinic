# MedicalClinic Project

This project is built using Java 17, Spring Boot 3.3, and MySQL 8.0.33.

## Application Run Instructions

1. **With Docker**  
   If Docker is installed on your system, simply execute the `run_docker_compose.sh` script. This will:
    - Run the Maven wrapper
    - Execute tests
    - Build the jar
    - Create the Docker image
    - Run the application alongside a MySQL database in a Docker container

2. **Without Docker Compose**  
   If Docker Compose is unavailable, you can run the `run.sh` script. This script executes the pre-built jar (`clinic-0.0.1-SNAPSHOT.jar`). Before running, review the script and modify the MySQL properties according to your environment.

### Note

Ensure that Java 17 is used in the script execution context.

### Optional: Start a Local MySQL Server

If needed, start a local MySQL server with Docker using the following command:

```bash
docker run -d \
--name mysql-db \
-e MYSQL_PASSWORD=test \
-e MYSQL_DATABASE=testdb \
-e MYSQL_USER=test \
-e MYSQL_ROOT_PASSWORD=test \
-p 3306:3306 \
mysql:8.0.33
```

### For api testing prepared postman project in file - PostmanAPI.json
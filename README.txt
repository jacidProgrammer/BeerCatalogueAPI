🍺 BEERCATALOGUE API

BeerCatalogueAPI is a RESTful API built with Java and Spring Boot. It allows users to manage a catalogue of beers and their manufacturers. The API supports basic CRUD operations and is configured to run in both local development and Kubernetes production environments.

✨ FEATURES

CRUD operations for beers and manufacturers
PostgreSQL integration for persistence
Docker and Kubernetes ready (Minikube compatible)
Separate Spring Boot profiles: dev and prod
Swagger UI for API documentation and testing
Kubernetes secrets for sensitive configuration

🚀 GETTING STARTED

🧰 PREREQUISITES

Java 21
Maven 3.9+
Docker
Minikube (with kubectl)
PostgreSQL (local or managed, e.g. AWS RDS)

⚙️ CONFIGURATION
Application properties can be configured using environment variables or the files application.properties / application-prod.properties.

Example properties:
server.port=8081
spring.datasource.url=jdbc:postgresql://:5432/beerdb
spring.datasource.username=youruser
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=validate

▶️ RUNNING THE APPLICATION

Local Development:
mvn spring-boot:run -Dspring-boot.run.profiles=dev

Build and run JAR with prod profile:
mvn clean package -DskipTests -Pprod
java -jar target/beer-catalogue-api.jar --spring.profiles.active=prod

🔗 API ENDPOINTS

🍻 BEER

GET /beers: List all beers
GET /beers/{id}: Get beer by ID
POST /beers: Create a new beer
PUT /beers/{id}: Update beer by ID
DELETE /beers/{id}: Delete beer by ID

🏭 MANUFACTURER

GET /manufacturers: List all manufacturers
GET /manufacturers/{id}: Get manufacturer by ID
POST /manufacturers: Create a new manufacturer
PUT /manufacturers/{id}: Update manufacturer by ID
DELETE /manufacturers/{id}: Delete manufacturer by ID

🧪 SWAGGER UI
You can access the Swagger UI for interactive documentation:
http://localhost:8081/swagger-ui/index.html

📦 DEPLOYMENT

Using Minikube:

Start Minikube:
minikube start

Deploy with PowerShell (Windows):
.\deploy.ps1

Deploy with Shell script (Linux/macOS):
chmod +x deploy.sh
./deploy.sh

Access the service:
minikube service beer-catalogue-service

🐳 Docker Build:
docker build -t beer-catalogue:latest .
docker run -p 8081:8081 beer-catalogue:latest

🔐 SECRETS AND CONFIG
Database credentials are stored in Kubernetes secrets and injected via envFrom or env blocks in the Kubernetes manifests.

This project includes a Helm chart that automatically creates a Kubernetes Secret named "secrets-db" for PostgreSQL credentials. It uses the template templates/secrets.yaml with values from values.yaml.

To generate the secret using Helm:

Install the chart:
helm install beer-catalogue ./helm

🗂️ PROJECT STRUCTURE

controller: REST API endpoints
model: Entity classes (Beer, Manufacturer)
repository: JPA interfaces
service: Business logic
config: Spring Boot configuration
k8s: Kubernetes YAML manifests
scripts: Shell and PowerShell deployment scripts


📝 Implementation Strategy and Prioritization

For the main part of the project, I developed REST controllers, service classes, and repositories to handle CRUD operations for the entities.
I also created a custom exception handling class to manage errors consistently across the API.
DTOs (Data Transfer Objects) were used to isolate and control the data exposed in API responses.

Additionally, I set up a Docker Compose configuration to run the application alongside a PostgreSQL database container.
The PostgreSQL database is initialized automatically at startup using an SQL script (init.sql), which creates the necessary tables.

Given the scope of the project and the description, I understood that cloud deployment and related infrastructure topics were a key priority for the client. With that in mind, I structured my approach to focus first on the most impactful areas from a DevOps and deployment perspective.

🛠️ Initial Setup: Docker as a Starting Point
I started by containerizing the application using Docker. Even though the final goal was to run it on Kubernetes, Docker allowed me to quickly iterate during development and served as a solid foundation for creating the image that would later be deployed to a cluster.

☁️ Cloud Deployment: Kubernetes
Since I had no prior experience with Kubernetes, I spent time researching and testing different approaches. Initially, I considered deploying to AWS EKS, especially since the next optional task involved connecting to a PostgreSQL database on AWS RDS.

However, I realized that even at minimum usage, EKS would incur costs for the worker nodes, and since I wasn’t yet familiar with the AWS cloud ecosystem, I opted to use Minikube for this project. This allowed me to focus on understanding Kubernetes concepts (manifests, deployments, services, Helm charts, secrets, etc.) without dealing with cloud-specific complexities.

Eventually, I was able to successfully deploy my Docker image into a local Kubernetes cluster using Minikube.

🗄️ Cloud Database (AWS RDS - PostgreSQL)
After achieving a working deployment, I prioritized the task of integrating with AWS RDS (PostgreSQL Free Tier), as it was closely related to the Kubernetes deployment.

Since I’m already comfortable working with databases, this step was straightforward. I provisioned the instance, configured the schema (which I had already used locally), and switched the connection from local to cloud.
To keep things secure and clean, I stored database credentials using Kubernetes Secrets and exposed them via values.yaml in my Helm chart, as required.

From the beginning, I had opted to use PostgreSQL instead of H2 in local development, anticipating this future step and avoiding redundant changes to the data layer. The repository, JPA configuration, and schema were already in place—only the credentials needed to change.

🧪 Final Steps Before Deadline
As I was approaching the deadline, I decided to stop adding new features and instead focus on:

Ensuring everything already implemented was robust and well-tested

Preparing a clear and detailed report

Documenting deployment steps and design decisions

📋 Features Not Implemented (and Why)
If I had more time, the next feature I would have implemented is Role-Based Access Control (RBAC).
Security is essential in any application and should be handled carefully to ensure all permissions and actions align with the specifications. This would require thoughtful planning and thorough testing.

After that, I would have added image upload functionality. While valuable, this is a relatively smaller effort compared to security. My plan was to upload images to AWS S3 and store only the public URLs in the database to keep it lightweight and efficient.

Lastly, I would have tackled search filters and pagination. Although essential for scalability, I deemed it lower priority for a first version with limited data.
However, I recognize its future importance. For reference, Untappd, one of the largest beer databases in the world, lists over 350,000 unique beers, so having efficient filtering and pagination will become crucial as the dataset grows.

👨‍💻 AUTHOR
Developed by: José Antonio Cid Montero
Contact: joseantoniocid.programmer@gmail.com
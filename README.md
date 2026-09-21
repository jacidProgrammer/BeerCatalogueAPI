# BeerCatalogueAPI

[![CI](https://github.com/jacidProgrammer/BeerCatalogueAPI/actions/workflows/ci.yml/badge.svg)](https://github.com/jacidProgrammer/BeerCatalogueAPI/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

BeerCatalogueAPI is a REST API built with Java 21 and Spring Boot 3.5 to manage a catalogue of beers and their
manufacturers. It persists data in PostgreSQL and ships with Docker, Docker Compose, Kubernetes (Minikube) manifests
and a small Helm chart for the database credentials.

## Features

- CRUD operations for beers and manufacturers (resources are identified by their unique **name**)
- PostgreSQL persistence with Spring Data JPA
- Consistent JSON error responses via a global exception handler
- OpenAPI documentation and Swagger UI (springdoc-openapi)
- Unit tests (Mockito) and integration tests against a real PostgreSQL started by Testcontainers
- Docker image, Docker Compose setup and Kubernetes manifests (Minikube compatible)
- Helm chart that creates the Kubernetes Secret with the database credentials
- GitHub Actions CI running `./mvnw -B verify` on every push and pull request to `master`

## Tech stack

| Area        | Technology                                   |
|-------------|----------------------------------------------|
| Language    | Java 21                                      |
| Framework   | Spring Boot 3.5 (Web, Data JPA)              |
| Database    | PostgreSQL 17                                |
| API docs    | springdoc-openapi 2.8 (Swagger UI)           |
| Testing     | JUnit 5, Mockito, MockMvc, Testcontainers    |
| Build       | Maven (wrapper included)                     |
| Deployment  | Docker, Docker Compose, Kubernetes, Helm     |

## Getting started

### Prerequisites

- Java 21
- Docker (required for Docker Compose and for the Testcontainers integration tests)
- Maven is optional: use the bundled wrapper `./mvnw`
- For Kubernetes: Minikube, `kubectl` and Helm 3
- A PostgreSQL database (local via Docker Compose, or managed, e.g. AWS RDS)

### Run locally

The easiest way is to let Spring Boot start the database for you. `spring-boot-docker-compose` is on the classpath, so
`spring-boot:run` runs `docker compose up` on [`compose.yaml`](compose.yaml), which starts PostgreSQL and initialises
the schema from [`db/init.sql`](db/init.sql):

```bash
./mvnw spring-boot:run
```

The API is then available at <http://localhost:8081>.

Alternatively, run the whole stack (database + application) in containers:

```bash
./mvnw -B package -DskipTests       # the Docker image copies target/*.jar
docker compose --profile app up --build
```

### Run the tests

```bash
./mvnw -B verify
```

The integration tests start a throwaway PostgreSQL container with Testcontainers, so Docker must be running.

## Configuration

Configuration lives in `src/main/resources` and every property can be overridden with environment variables
(e.g. `SPRING_DATASOURCE_URL`).

| File                           | Used for                                                                                          |
|--------------------------------|---------------------------------------------------------------------------------------------------|
| `application.properties`       | Default (local development): PostgreSQL on `localhost:5432`, `ddl-auto=update`                   |
| `application-prod.properties`  | Production: `ddl-auto=validate`, no datasource URL or credentials (supplied through environment) |
| `application-test.properties`  | Test settings; the integration tests get their datasource from Testcontainers                    |

Default local configuration:

```properties
server.port=8081
spring.datasource.url=jdbc:postgresql://localhost:5432/BeerCatalogueAPI
spring.datasource.username=admin
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
```

### Production build

The Maven `prod` profile packages `application-prod.properties` as the jar's `application.properties`, so the
datasource must be provided through environment variables:

```bash
./mvnw -B clean package -DskipTests -Pprod

SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/BeerCatalogueAPI \
SPRING_DATASOURCE_USERNAME=<user> \
SPRING_DATASOURCE_PASSWORD=<password> \
java -jar target/BeerCatalogueAPI-0.0.1-SNAPSHOT.jar
```

With `ddl-auto=validate` the schema must already exist; create it with [`db/init.sql`](db/init.sql).

## API reference

Base URL: `http://localhost:8081`. All request and response bodies are JSON (`Content-Type: application/json`).

Interactive documentation:

- Swagger UI: <http://localhost:8081/swagger-ui/index.html>
- OpenAPI spec: <http://localhost:8081/v3/api-docs>

### Beers

| Method   | Path                | Body       | Success                  | Description                                     |
|----------|---------------------|------------|--------------------------|-------------------------------------------------|
| `GET`    | `/api/beer`         | none       | `200` array of beers     | List all beers, sorted by name (case-insensitive) |
| `GET`    | `/api/beer/{name}`  | none       | `200` beer               | Get a beer by name                              |
| `POST`   | `/api/beer`         | beer       | `200` created beer       | Create a beer; the manufacturer must exist      |
| `PUT`    | `/api/beer`         | beer       | `200` updated beer       | Update the beer whose `name` matches the body   |
| `DELETE` | `/api/beer/{name}`  | none       | `200` empty body         | Delete a beer by name                           |

Beer body:

```json
{
  "name": "Cinco Estrellas",
  "type": "Lager",
  "abv": 5.5,
  "description": "Classic Madrid lager",
  "manufacturerName": "Mahou"
}
```

### Manufacturers

| Method   | Path                        | Body          | Success                      | Description                                             |
|----------|-----------------------------|---------------|------------------------------|---------------------------------------------------------|
| `GET`    | `/api/manufacturer`         | none          | `200` array of manufacturers | List all manufacturers, sorted by name (case-insensitive) |
| `GET`    | `/api/manufacturer/{name}`  | none          | `200` manufacturer           | Get a manufacturer by name                              |
| `POST`   | `/api/manufacturer`         | manufacturer  | `200` created manufacturer   | Create a manufacturer                                   |
| `PUT`    | `/api/manufacturer`         | manufacturer  | `200` updated manufacturer   | Update the manufacturer whose `name` matches the body   |
| `DELETE` | `/api/manufacturer/{name}`  | none          | `200` empty body             | Delete a manufacturer by name                           |

Manufacturer body:

```json
{
  "name": "Mahou",
  "country": "Spain"
}
```

### Other

| Method | Path        | Success       | Description                  |
|--------|-------------|---------------|------------------------------|
| `GET`  | `/api/test` | `200` `true`  | Simple liveness check        |

### Notes on identifiers

- Names are unique and act as the identifier. Names containing spaces must be URL-encoded, e.g.
  `GET /api/beer/Cinco%20Estrellas`.
- `PUT` looks the resource up by the `name` in the body, so a resource cannot be renamed through the API.
- A manufacturer that still has beers cannot be deleted; delete its beers first.

### Errors

Errors are returned as:

```json
{
  "code": "BAD_REQUEST",
  "message": "Beer with this name doesn't exist"
}
```

| Status | `code`                  | When                                                                                          |
|--------|-------------------------|-----------------------------------------------------------------------------------------------|
| `400`  | `BAD_REQUEST`           | Business rule violated: name not found, name already exists, unknown `manufacturerName`       |
| `404`  | `NOT_FOUND`             | The path does not match any endpoint                                                          |
| `500`  | `INTERNAL_SERVER_ERROR` | Any other error, e.g. malformed JSON, missing required fields, deleting a manufacturer in use |

Note that a lookup by a name that does not exist returns `400`, not `404`.

### Example

```bash
curl -X POST http://localhost:8081/api/manufacturer \
  -H 'Content-Type: application/json' \
  -d '{"name":"Mahou","country":"Spain"}'

curl -X POST http://localhost:8081/api/beer \
  -H 'Content-Type: application/json' \
  -d '{"name":"Cinco Estrellas","type":"Lager","abv":5.5,"description":"Classic","manufacturerName":"Mahou"}'

curl http://localhost:8081/api/beer/Cinco%20Estrellas
```

## Deployment

### Docker

The image copies the jar built by Maven, so package first:

```bash
./mvnw -B package -DskipTests -Pprod
docker build -t beer-catalogue:latest .
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/BeerCatalogueAPI \
  -e SPRING_DATASOURCE_USERNAME=<user> \
  -e SPRING_DATASOURCE_PASSWORD=<password> \
  beer-catalogue:latest
```

### Kubernetes (Minikube)

[`k8s/deployment.yaml`](k8s/deployment.yaml) reads the datasource URL, username and password from a Secret named
`db-secret`. Create it with the Helm chart in [`helm/beer-catalogue`](helm/beer-catalogue); the password has no
default and must be passed at install time:

```bash
minikube start

helm upgrade --install beer-catalogue ./helm/beer-catalogue \
  --set database.credentials.url=jdbc:postgresql://<host>:5432/BeerCatalogueAPI \
  --set database.credentials.username=<user> \
  --set database.credentials.password='<password>'
```

Then build the jar and run the deploy script, which builds the image inside Minikube's Docker daemon, applies the
manifests in `k8s/` and waits for the rollout:

```bash
./mvnw -B package -DskipTests -Pprod

./deploy.sh        # Linux / macOS
.\deploy.ps1       # Windows (PowerShell)
```

Open the service:

```bash
minikube service beer-catalogue-service
```

### Secrets

| Chart value                        | Secret key | Injected as                  |
|------------------------------------|------------|------------------------------|
| `database.secretName`              | —          | Secret name (`db-secret`)    |
| `database.credentials.url`         | `url`      | `SPRING_DATASOURCE_URL`      |
| `database.credentials.username`    | `username` | `SPRING_DATASOURCE_USERNAME` |
| `database.credentials.password`    | `password` | `SPRING_DATASOURCE_PASSWORD` |

Never commit real credentials to `values.yaml`; pass them with `--set` or a values file kept out of version control.

## Project structure

```text
.
├── src/main/java/com/example/beercatalogueapi
│   ├── Controllers/     REST endpoints
│   ├── DTO/             Request/response objects
│   ├── Entity/          JPA entities (Beer, Manufacturer)
│   ├── ErrorHandle/     Global exception handler and error body
│   ├── Repository/      Spring Data JPA repositories
│   └── Service/         Business logic
├── src/main/resources   application*.properties
├── src/test/java        Unit and integration tests
├── db/init.sql          PostgreSQL schema used by Docker Compose
├── helm/beer-catalogue  Helm chart for the database Secret
├── k8s/                 Kubernetes manifests
├── compose.yaml         PostgreSQL (+ app with --profile app)
├── Dockerfile
└── deploy.sh / deploy.ps1  Minikube deployment scripts
```

## Implementation strategy and prioritisation

For the core of the project I built REST controllers, services and repositories to handle CRUD operations for both
entities, a global exception handler to return errors consistently, and DTOs to control exactly what the API exposes.

I also set up Docker Compose to run the application next to a PostgreSQL container, initialised at startup with an SQL
script (`db/init.sql`) that creates the tables.

Given the scope and the description of the assignment, I understood that cloud deployment and infrastructure were a
key priority for the client, so I focused first on the most impactful areas from a DevOps and deployment perspective.

### Docker as a starting point

I started by containerising the application. Even though the final goal was Kubernetes, Docker let me iterate quickly
during development and produced the image later deployed to the cluster.

### Kubernetes

I had no prior experience with Kubernetes, so I spent time researching and testing approaches. I first considered AWS
EKS, since the next optional task involved connecting to PostgreSQL on AWS RDS. However, even at minimum usage EKS
incurs costs for the worker nodes, and as I was not yet familiar with the AWS ecosystem I chose Minikube. That let me
focus on Kubernetes concepts (manifests, deployments, services, Helm charts, secrets) without cloud-specific
complexity. I successfully deployed the Docker image to a local Minikube cluster.

### Cloud database (AWS RDS, PostgreSQL)

With a working deployment, I prioritised integrating AWS RDS (PostgreSQL Free Tier), as it was closely related to the
Kubernetes deployment. I provisioned the instance, applied the schema I was already using locally and switched the
connection from local to cloud. Database credentials are stored in a Kubernetes Secret generated by the Helm chart.

From the beginning I used PostgreSQL instead of H2 locally, anticipating this step and avoiding changes to the data
layer: only the credentials needed to change.

### Final steps before the deadline

Approaching the deadline, I stopped adding features and focused on making what was implemented robust and tested,
preparing a clear report, and documenting deployment steps and design decisions.

### Not implemented (and why)

- **Role-based access control (RBAC).** Security is essential and must be designed and tested carefully so that every
  permission matches the specification; it would have been my next feature.
- **Image upload.** Valuable but a smaller effort: images would go to AWS S3, storing only their URLs in the database.
- **Search filters and pagination.** Lower priority for a first version with little data, but crucial at scale:
  Untappd, one of the largest beer databases, lists over 350,000 unique beers.

## License

Released under the [MIT License](LICENSE).

## Author

José Antonio Cid Montero — <joseantoniocid.programmer@gmail.com>

# The Blog App

`The Blog App` is a Spring Boot REST API for a simple blogging platform with JWT authentication, post management, categories, and tags.

This README is written for the intended completed scope of the project, including `create`, `read`, `update`, and `delete` operations for posts.

## Features

- JWT-based authentication
- Protected and public API routes
- Create and list blog posts
- Filter published posts by category and tag
- View authenticated user's draft posts
- Update and delete posts
- Create and delete categories
- Create and delete tags
- Validation for request payloads
- Global API error handling
- PostgreSQL for main persistence
- H2 configuration for tests

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- H2 Database
- JWT (`jjwt`)
- MapStruct
- Lombok
- Maven
- Docker Compose

## Project Structure

```text
src/main/java/com/example/The/blog/app
|- config
|- controller
|- domain
|  |- dtos
|  `- entities
|- mappers
|- repositories
|- security
`- services
   `- impl
```

## API Modules

### Authentication

- Login with email and password
- Receive a JWT token for protected routes

### Posts

- List all published posts
- Filter posts by `categoryId` and `tagId`
- View current user's drafts
- Create a post as draft or published
- Update an existing post
- Delete a post

### Categories

- List all categories
- Category response includes published `postCount`
- Create a category
- Delete a category if no posts are attached

### Tags

- List all tags
- Tag response includes published `postCount`
- Create tags in bulk
- Reuse existing tags if names already exist
- Delete a tag if no posts are attached

## Security

The API uses JWT authentication.

Public routes:

- `POST /api/v1/auth/login`
- `GET /api/v1/posts`
- `GET /api/v1/posts/{id}`
- `GET /api/v1/categories`
- `GET /api/v1/tags`

Protected routes require:

```http
Authorization: Bearer <your-jwt-token>
```

## Default Local User

On startup, the application seeds a local test user if it does not already exist:

- Email: `user@test.com`
- Password: `password`

Use this account to log in locally and test protected endpoints.

## Database Configuration

The default application configuration uses PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=admin123
spring.jpa.hibernate.ddl-auto=update
```

Test configuration uses in-memory H2.

## Running the Project

### 1. Start PostgreSQL with Docker Compose

```bash
docker compose up -d
```

This starts:

- PostgreSQL on `localhost:5432`
- Adminer on `http://localhost:8888`

### 2. Run the Spring Boot app

On Windows:

```bash
mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The API will run on:

```text
http://localhost:8080
```

## Running Tests

```bash
mvnw.cmd test
```

## Authentication Flow

### Login

`POST /api/v1/auth/login`

Example request:

```json
{
  "email": "user@test.com",
  "password": "password"
}
```

Example response:

```json
{
  "token": "your-jwt-token",
  "expiresIn": 86400
}
```

## Main Endpoints

### Auth

- `POST /api/v1/auth/login`

### Posts

- `GET /api/v1/posts`
- `GET /api/v1/posts?categoryId={uuid}`
- `GET /api/v1/posts?tagId={uuid}`
- `GET /api/v1/posts?categoryId={uuid}&tagId={uuid}`
- `GET /api/v1/posts/{id}`
- `GET /api/v1/posts/drafts`
- `POST /api/v1/posts`
- `PUT /api/v1/posts/{id}`
- `DELETE /api/v1/posts/{id}`

### Categories

- `GET /api/v1/categories`
- `POST /api/v1/categories`
- `DELETE /api/v1/categories/{id}`

### Tags

- `GET /api/v1/tags`
- `POST /api/v1/tags`
- `DELETE /api/v1/tags/{id}`

## Example Payloads

### Create Category

```json
{
  "name": "Java"
}
```

### Create Tags

```json
{
  "names": ["spring", "backend", "jwt"]
}
```

### Create or Update Post

```json
{
  "title": "Getting Started With Spring Boot",
  "content": "This is the full post content...",
  "categoryId": "category-uuid",
  "tagId": ["tag-uuid-1", "tag-uuid-2"],
  "status": "PUBLISHED"
}
```

Supported post statuses:

- `DRAFT`
- `PUBLISHED`

## Validation and Error Handling

The project includes:

- Bean Validation for request DTOs
- Centralized exception handling with consistent JSON responses
- `400` for validation and bad requests
- `401` for invalid login
- `404` for missing resources
- `409` for invalid delete operations or conflicting state
- `500` for unexpected server errors

## Notes

- Reading time is calculated from post content.
- Category and tag responses expose published post counts.
- Tags are many-to-many with posts.
- A category cannot be deleted while posts are still attached.
- A tag cannot be deleted while posts are still attached.

## Current Status

The codebase already includes most of the application layers and wiring for a blog API. This README documents the expected finished version of the project, including post update and delete support.

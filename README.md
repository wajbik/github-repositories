# GitHub Repositories API

REST API built with Java 25 and Spring Boot 4 that exposes GitHub repositories for a given user.

## Requirements

* Java 25
* Gradle

## Running the application

Clone the repository and run:

```bash
./gradlew bootRun
```

The application will start on:

```text
http://localhost:8080
```

## Endpoint

### Get user repositories

```http
GET /repositories/{username}
```

Example:

```http
GET /repositories/octocat
```

### Successful response

```json
[
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "master",
        "lastCommitSha": "7fd1a60b01f91b314f59955a4e4d4e80d8edf11d"
      }
    ]
  }
]
```

The response contains:

* repository name
* repository owner login
* branch name
* last commit SHA

Forked repositories are excluded from the response.

### User not found

If the specified GitHub user does not exist, the API returns:

HTTP Status:

```text
404 Not Found
```

Response body:

```json
{
  "status": 404,
  "message": "User not found"
}
```

## Testing

The project contains integration tests implemented with:

* Spring Boot Test
* WireMock

Run tests with:

```bash
./gradlew test
```

## Technologies

* Java 25
* Spring Boot 4
* Spring MVC
* Gradle Kotlin DSL
* WireMock
* JUnit 5

```
```

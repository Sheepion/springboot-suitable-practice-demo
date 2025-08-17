# Springboot suitable practice demo

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

This repo is a **suitable** practice demo for Springboot,
trying to demonstrate a good(maybe) practice of using Springboot with these libraries/artifacts:

1. [Spring Boot](https://spring.io/projects/spring-boot)
2. [MyBatis](https://github.com/mybatis/spring-boot-starter) for database access.
3. [Flyway](https://github.com/flyway/flyway) for database schema migration/versioning.
4. [PostgreSQL](https://www.postgresql.org/) as the database.
5. [JUnit 5](https://junit.org/junit5/) for unit testing.
6. [Testcontainers](https://www.testcontainers.org/) for better mapper test.
7. [MapStruct-plus](https://github.com/linpeilie/mapstruct-plus) for easier mapping between DTO and entity.
8. [Knife4j](https://doc.xiaominfo.com) for better OpenAPI swagger documents.

## Dependency version used

| Dependency     | Version | Remark                           |
| -------------- | ------- | -------------------------------- |
| JDK            | 17      |                                  |
| PostgreSQL     | 15      |                                  |
| Springboot     | 3.5.4   |                                  |
| Flyway         | 11.7.2  | inherited from Springboot        |
| JUnit          | 5.12.2  | inherited from Springboot        |
| MyBatis        | 3.5.19  | from Mybatis-spring-boot-starter |
| Testcontainer  | 1.21.3  | from spring-boot-testcontainers  |
| MapStruct-plus | 1.4.8   |                                  |
| Knife4j        | 4.4.0   |                                  |

## What you may learn from this repo

### Testing

1. [Mapper test with Testcontainer](docs/test/MapperTest.md)

### Design

1. [Service layer design practices](docs/design/Service.md)

## Todo list

Docs about:

- Common class like Result.
- File structure about springboot web application.
- Mock test usage.

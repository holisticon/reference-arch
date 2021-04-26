[![Build Status](https://travis-ci.org/holisticon/reference-arch.svg?branch=master)](https://travis-ci.org/holisticon/reference-arch)
[![CI](https://github.com/holisticon/reference-arch/actions/workflows/build.yml/badge.svg)](https://github.com/holisticon/reference-arch/actions/workflows/build.yml)
[![Known Vulnerabilities](https://snyk.io/test/github/holisticon/reference-arch/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/holisticon/reference-arch?targetFile=pom.xml)
[![codecov](https://codecov.io/gh/holisticon/reference-arch/branch/master/graph/badge.svg)](https://codecov.io/gh/holisticon/reference-arch)
[![sonarcube](https://sonarcloud.io/api/project_badges/measure?project=de.holisticon.reference-arch%3Areference-arch-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=de.holisticon.reference-arch%3Areference-arch-parent)

- [Reference Architecture](#reference-architecture)
  - [Setup](#setup)
  - [Demo Data](#demo-data)
    - [Keycloak Users](#keycloak-users)
  - [Development](#development)
# Reference Architecture

> Reference stack demonstrating the build and application composition with Keycloak, SpringBoot, Kotlin, Swagger, Angular and TypeScript.

## Setup

Start the docker stack:

```
docker-compose up -d
```

## Demo Data

### Keycloak Users

* Master Realm
  * admin / admin
* App Realm
  * user1 / user1
  * user2 / user2

## Development

```
./mvn clean install
```

**Backend**
```
(cd assembly/ && ../mvnw spring-boot:run)
```
**Frontend**
```
(cd frontend/ && npm start)
```
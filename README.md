# 📅 Calendar API

[![Build](https://github.com/thaisvsalmeida/calendar-api/actions/workflows/maven.yml/badge.svg)](https://github.com/thaisvsalmeida/calendar-api/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/thaisvsalmeida/calendar-api/branch/main/graph/badge.svg)](https://codecov.io/gh/thaisvsalmeida/calendar-api)

A RESTful API built with Java and Spring Boot for managing both single and recurring calendar events. It allows you to create appointments, set recurrence rules, and associate labels and owners with each event.

---

## 🚀 Technologies

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Hibernate
- PostgreSQL
- H2 (in-memory)
- Maven

---

## 📦 Running the Project

### Using H2 (In-Memory Database)
Before running the application, set the following VM option:
```bash
-Dspring.profiles.active=h2
```
This activates the h2 profile, using an in-memory database ideal for development and testing.

---

## 📊 Code Coverage

This project uses [JaCoCo](https://www.jacoco.org/) and [Codecov](https://codecov.io/) to track code coverage and quality.

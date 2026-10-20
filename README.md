# Java CRUD App

A beginner-friendly contact manager built with Java 17, Spring Boot, Vaadin, and Maven.

## Features

- Create a contact with a name, email address, and phone number
- Read and search the contact list
- Update an existing contact
- Delete a contact

> **Note:** This starter project stores contacts in memory. The sample data resets when the application restarts. A database can be added as a future improvement.

## Requirements

- JDK 17 or later
- Maven 3.6+ (or use Maven support in your IDE)
- Internet access on the first build to download dependencies

Check your installations:

```bash
java -version
mvn -version
```

## Run locally

From the project root, run:

```bash
mvn spring-boot:run
```

When startup completes, open <http://localhost:8080> in your browser.

To build the project:

```bash
mvn clean package
```

## Project structure

```text
java-crud-app/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/example/crud/
    │   │   ├── CrudApplication.java
    │   │   ├── Contact.java
    │   │   ├── ContactService.java
    │   │   └── MainView.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/example/crud/
            └── ContactServiceTest.java
```

## Basic architecture

- `Contact.java` represents one contact.
- `ContactService.java` contains the in-memory CRUD operations.
- `MainView.java` builds the browser interface and connects its buttons to the service.
- `CrudApplication.java` starts Spring Boot.
- `ContactServiceTest.java` tests the service's basic operations.

## Suggested Git workflow

1. Create a **public** repository on GitHub named `java-crud-app`.
2. Add this project to the repository.
3. Commit your initial version:

   ```bash
   git add .
   git commit -m "Create Java CRUD starter application"
   git push -u origin main
   ```

4. Invite your classmate as a collaborator from the repository's **Settings → Collaborators** page.
5. Work on separate branches, open pull requests, review each other's changes, and merge them.

If you create an empty GitHub repository, follow GitHub's instructions for connecting an existing local project. Do not initialize the online repository with a README if you already plan to push this complete project as its first commit.

## Ideas for collaboration

- Student A: improve the form validation.
- Student B: add sorting or a clearer empty-state message.
- Together: review each other's pull requests and test create, edit, and delete.

## Learning resource

[Spring: Building a CRUD UI with Vaadin](https://spring.io/guides/gs/crud-with-vaadin/)

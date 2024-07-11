# 🎟️ VotacaoCRUD BackEnd

As you can see in the title above, this project is the API that revolves about a voting system.

The voting system has agendas and users that can vote in these agendas.

## Table of Contents
- [✨ Running the Project](#running-the-project)
- [🏗️ Developing the Project](#developing-the-project)
- [📄 License](#license)

## Running the Project

This part is of the project is still in development, but rather will be building Docker Containers or will be deployed on Render.

## Developing the Project

This topic refers to all the technologies used in the project and the steps to build it using them.

### Technologies

- ☕ Java
- 🍃 Spring Boot
    - 📦 Spring Boot JPA
- 🌶️ Lombok
- 🧪 JUnit 5
- 🐘 PostgreSQL

### US's - MPV

- ✅ [US000] Configure Repository
  - ✅ [US000-1] Implements a README
  - ✅ [US000-2] Configure GitHub Actions
- ✅ [US001] Configure Project
  - ✅ [US001-1] Create a Postgres Container
  - ✅ [US001-2] Configure JPA
  - ✅ [US001-3] Swagger Implementation
- 🚧 [US002-1] Create Database
  - 🚧 [US002-1] Create User Entity
  - 🚧 [US002-2] Create Agenda Entity
  - 🚧 [US002-3] Create Log Entity
- 🚧 [US003] User Flow
  - 🚧 [US003-1] Create Request and Response User DTO  
  - 🚧 [US003-2] Create User
  - 🚧 [US003-3] Implement Random CPF Treatment (Valid/Invalid)
  - 🚧 [US003-4] Implement Random if User is allowed to vote
  - 🚧 [US003-5] Implement Login Feature
- 🚧 [US004] Agenda Flow
  - 🚧 [US004-1] Create Request and Response Agenda DTO 
  - 🚧 [US004-2] Create Agenda
  - 🚧 [US004-3] Implements Agenda Start, Duration and End
  - 🚧 [US004-4] Implement User Vote in Agenda
- 🚧 [US005] Implement Log Population
  - 🚧 [US005-1] Create Request and Response Log DTO 
  - 🚧 [US005-2] Implement Methods Related to User
  - 🚧 [US005-3] Implement Methods Related to Agenda
- 🚧 [US006] Error Treatment
  - 🚧 [US006-1] Implement ErrorHandlers that are Related to User
  - 🚧 [US006-2] Implement ErrorHandlers that are Related to Agenda
  - 🚧 [US006-3] Implement ErrorHandlers that are Related to Log
- 🚧 [US007] Test Implementation
  - 🚧 [US007-1] Unitary Tests - Happy
  - 🚧 [US007-2] Unitary Tests - Sad

### US's - Extra

- 🚧 [US00X] Related to Extra Features
  - 🚧 [US00X-1] Integration Tests - Happy
  - 🚧 [US00X-2] Integration Tests - Sad
  - 🚧 [US00X-3] Performance Tests 


## License
This project is licensed under the Apache 2.0 License. See the [LICENSE](LICENSE) file for details.

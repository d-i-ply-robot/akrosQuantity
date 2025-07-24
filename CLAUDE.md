# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Shell CLI application for construction project quantity management. The application processes PDF drawings and CAD model files (DWG/DXF) to calculate material quantities for construction projects. It uses PostgreSQL for data persistence and will eventually evolve into a full web application with REST APIs and a frontend.

## Development Commands

### Building and Running
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test
```

### Database Setup
The application requires PostgreSQL 12+ to be running:
```sql
CREATE DATABASE construction_quantities;
```

Database connection is configured in `src/main/resources/application.properties`. Default credentials are postgres/postgres for local development.

## Architecture Overview

### Core Components
- **Application.java**: Spring Boot entry point with JPA and entity scanning configuration
- **shell/**: Spring Shell command classes that provide the CLI interface
  - `ProjectShell.java`: Project management commands (create, list, delete)
- **service/**: Business logic layer
  - `ProjectService.java`: Core project operations with transactional support
- **repository/**: JPA data access layer using Spring Data JPA
- **model/**: JPA entities
  - `Project.java`: Main project entity with Lombok annotations

### Spring Shell CLI Commands
The application provides these interactive commands:
- `create-project --name <name>`: Create a new project
- `list-projects`: List all projects
- `delete-project --id <id>`: Delete a project

Future commands (per PDR document):
- `upload-file --projectId <id> --file <path> --author <author> --discipline <discipline>`
- `define-model-area --fileId <id> --xMin <val> --yMin <val> --xMax <val> --yMax <val>`
- `stitch-project --projectId <id>`: For PDF stitching using BoofCV
- `analyze-project --projectId <id>`: Decompose model into element groups
- `list-quantities --projectId <id>`: Display calculated quantities

### Technology Stack
- **Framework**: Spring Boot 2.7.18 with Spring Shell 2.1.6
- **Database**: PostgreSQL with Spring Data JPA/Hibernate
- **Build Tool**: Gradle with Java 17
- **Planned Libraries**: Apache PDFBox (PDF processing), Kabeja (DXF), Teigha (DWG), BoofCV (image stitching)
- **Annotations**: Lombok for reducing boilerplate code

### Database Configuration
- Uses Hibernate with `ddl-auto=update` for schema management
- SQL logging enabled for debugging (`show-sql=true`)
- PostgreSQL dialect configured
- File upload size limits set to 10MB

### Development Phases
According to the PDR document, development follows these phases:
1. **Phase 1** (Current): Spring Shell CLI with core business logic
2. **Phase 2**: RESTful API controllers mirroring CLI commands  
3. **Phase 3**: Frontend integration with interactive UI
4. **Phase 4**: Security and authentication

### File Processing Strategy
The application will handle different file types:
- **PDF drawings**: Vector PDFs requiring stitching for overlapping drawings
- **DWG/DXF models**: CAD files processed directly without stitching
- Files organized by author and discipline metadata
- Model area definition primarily for PDF cropping

The CLI serves as the foundation for testing business logic before adding web APIs and frontend components.
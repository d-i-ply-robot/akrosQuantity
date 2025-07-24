# Construction Quantities CLI

A Spring Shell CLI application for managing construction project quantities, supporting PDF drawings and CAD model files.

## Prerequisites

- Java 17 or higher
- PostgreSQL 12 or higher
- Gradle 8.x

## Setup

1. Create PostgreSQL database:
```sql
CREATE DATABASE construction_quantities;
```

2. Configure database connection in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/construction_quantities
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build the project:
```bash
./gradlew build
```

4. Run the application:
```bash
./gradlew bootRun
```

## Available Commands

### Project Management
- `create-project --name <name>`: Create a new project
- `list-projects`: List all projects
- `delete-project --id <id>`: Delete a project

### File Management
- `upload-file --projectId <id> --file <path> --author <author> --discipline <discipline>`: Upload a file to a project
- `define-model-area --fileId <id> --xMin <val> --yMin <val> --xMax <val> --yMax <val>`: Define model area for a file

## Example Usage

1. Create a project:
```shell
create-project --name "Office Building A"
```

2. Upload a drawing:
```shell
upload-file --projectId 1 --file /path/to/drawing.pdf --author "John Doe" --discipline "Architecture"
```

3. Define model area:
```shell
define-model-area --fileId 1 --xMin 0 --yMin 0 --xMax 100 --yMax 100
``` 
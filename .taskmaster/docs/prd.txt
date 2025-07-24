Product Design Requirement (PDR): Construction Quantities Web App (Development Starting with Spring Shell CLI)
1. Overview
The Construction Quantities Web App enables users to upload vector PDF drawings, as well as DWG and DXF model files, define a project model, decompose into element groups, calculate material quantities, and display results in a table. Users can create and share projects, with quantities linked to visual elements in the model. Development will start with a Spring Shell CLI app to implement and test the core business logic interactively. This CLI will serve as the foundation for the backend, with RESTful APIs, database integration, front-end, and security added in subsequent phases. The backend uses Spring Boot with PostgreSQL, exposing JSON for testing where needed.
Clarification on File Types and Processing:

PDF Drawings: These are vector PDFs that may come in multiple files covering parts of the project. Stitching is required for PDFs to knit overlapping drawings together, recreating a unified "model" of the project in a format similar to a CAD model.
DWG and DXF Model Files: These are CAD model files that represent the project model directly and do not require stitching. They can be processed as-is for decomposition and quantity calculation.

2. Development Approach

Phase 1: Spring Shell CLI App
Build an interactive CLI using Spring Shell to develop and test business logic.
Implement core functionalities as shell commands (e.g., project creation, drawing/model file upload, model area definition, stitching for PDFs, analysis).
Integrate with services, repositories, and database from the start.
Use the CLI to validate logic without a front-end or full web server.


Phase 2: Backend APIs
Extend the CLI app to include RESTful controllers mirroring CLI commands.
Add file handling for uploads via APIs.


Phase 3: Front-End Integration
Develop UI for interactive features (e.g., model area selection).


Phase 4: Security and Enhancements
Add authentication and other features.



3. Functional Requirements
3.1 Spring Shell CLI Commands

create-project --name <name>: Create a new project and return details.
list-projects: List all projects with IDs and names.
upload-file --projectId <id> --file <local-path> --author <author> --discipline <discipline>: Upload a file (PDF drawing or DWG/DXF model), detect format, store in database. (Updated command name for generality.)
define-model-area --fileId <id> --xMin <val> --yMin <val> --xMax <val> --yMax <val>: Set model area coordinates for a file (applicable to PDFs primarily).
stitch-project --projectId <id>: Stitch PDF drawings only if multiple PDFs are present; skip for DWG/DXF. Uses feature points and edges (BoofCV), assuming same scale, to form a model-like format.
analyze-project --projectId <id>: Analyze the unified model (stitched PDFs or direct DWG/DXF) to decompose into groups by line properties (weight, color, thickness, linestyle), calculate quantities (metric units), store in database.
list-quantities --projectId <id>: Display quantities table with group properties, values, and units.
get-elements --quantityId <id>: Show elements (coordinates) for a specific quantity, for highlighting.
Additional commands as needed (e.g., view-project --id <id>, delete-project --id <id>).

3.2 File Upload and Management

Support PDF (Apache PDFBox for drawings), DWG (Teigha Java or alternative for models), DXF (Kabeja for models).
Files are organized by author and discipline.
Store files locally or in database blobs; metadata (including format) in PostgreSQL.
Detect format from extension/content; handle as drawing (PDF) or model (DWG/DXF).

3.3 Model Area Definition

CLI accepts coordinates; later front-end sends via API.
Primarily for PDFs to crop borders; optional or automatic for DWG/DXF.
User defines by clicking/dragging in front-end (future), snapping to vertices.

3.4 Drawing Overlap and Stitching

Applicable only to PDF drawings: Align and stitch multiple overlapping PDFs using BoofCV for feature detection (e.g., FAST corners) and alignment (homography with RANSAC).
Purpose: Combine PDF data into a unified format similar to a CAD model (DWG/DXF).
Assume same scale; handle overlaps by blending.
Skip stitching if project contains only DWG/DXF or single PDF.

3.5 Element Decomposition and Quantity Calculation

Process the unified model (stitched PDFs or direct DWG/DXF).
Group vectors by similar properties (exact or threshold match for weight, color, thickness, linestyle).
Calculate lengths/areas in meters/m² based on element type.
Support manual classification later.

3.6 Project Management

CRUD via CLI commands initially.
Projects can hold multiple files, grouped by author/discipline.

3.7 Visualization Interaction

CLI returns JSON-like text for elements; APIs will return JSON.
Highlight elements in model when quantity selected (future front-end).

4. Non-Functional Requirements
4.1 Technology Stack

CLI: Spring Shell for interactive commands.
Backend: Spring Boot (Java), Gradle.
Database: PostgreSQL.
Libraries: PDFBox, Kabeja (DXF), Teigha (DWG), BoofCV (stitching for PDFs).
Testing: JUnit 5, Spring Boot Test, Spring Shell Test.

4.2 Performance

CLI responses under 10 seconds for processing.
Handle up to 10 files/project.

4.3 Security

None in initial CLI phase.

5. CLI Implementation Details
5.1 Project Structure
construction-quantities-cli/
├── src/main/java/com/example/construction/
│   ├── Application.java      # Spring Boot entry point
│   ├── shell/                # Shell command classes (e.g., ProjectShell, FileShell)
│   ├── service/              # Business services (updated for file type handling)
│   ├── repository/           # JPA repositories
│   ├── model/                # Entities (File entity instead of Drawing, with format)
│   └── util/                 # CAD utilities
├── src/test/java/            # Tests
├── build.gradle              # Dependencies
└── application.properties    # Config (e.g., database URL)

5.2 Sample Code
Updated shell command for file upload.

package com.example.construction.shell;

import com.example.construction.model.ProjectFile;import com.example.construction.service.FileService;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.shell.standard.ShellComponent;import org.springframework.shell.standard.ShellMethod;import org.springframework.shell.standard.ShellOption;
import java.io.File;import java.io.IOException;import java.nio.file.Files;
@ShellComponentpublic class FileShell {    @Autowired    private FileService fileService;
@ShellMethod(key = "upload-file", value = "Upload a file (PDF, DWG, DXF) to a project")
public String uploadFile(
        @ShellOption(value = "--projectId") Long projectId,
        @ShellOption(value = "--file") String filePath,
        @ShellOption(value = "--author") String author,
        @ShellOption(value = "--discipline") String discipline) {
    try {
        File file = new File(filePath);
        byte[] content = Files.readAllBytes(file.toPath());
        ProjectFile projectFile = fileService.uploadFile(projectId, content, file.getName(), author, discipline);
        return "File uploaded: ID=" + projectFile.getId() + ", Format=" + projectFile.getFormat();
    } catch (IOException e) {
        return "Error uploading file: " + e.getMessage();
    }
}

// Other methods like define-model-area...

}
# Construction Quantities CLI

A Spring Boot-based command-line interface for managing construction project files and quantities.

## Quick Start

### Running the Interactive CLI

The easiest way to run the application is using the provided script:

```bash
./run-interactive-cli.sh
```

This script will:
1. 🔧 **Rebuild the project** to ensure all latest changes are included
2. ✅ **Verify the build** was successful
3. 🚀 **Start the interactive shell** with all features available

### Manual Build and Run

If you prefer to build manually:

```bash
# Clean and build
./gradlew clean build

# Run the application
java -cp build/classes/java/main com.akros.construction.InteractiveShell
```

## Available Commands

- `help` - Show available commands
- `create-project --name <name>` - Create a new project
- `list-projects` - List all projects
- `view-project --id <id>` - View project details
- `delete-project --id <id>` - Delete a project
- `upload-file --projectId <id> --file <path> --author <name> --discipline <type>` - Upload a file
- `list-files --projectId <id>` - List files in a project
- `quit` or `exit` - Exit the shell

## File Upload Examples

```bash
# Simple filename
upload-file --projectId 1 --file sample.dxf --author Cameron --discipline Architecture

# Complex filename with spaces (use quotes)
upload-file --projectId 1 --file "512173-2000-MDM-CC-0100-REV 3.dwg" --author Cameron --discipline Architecture

# Very complex filename
upload-file --projectId 1 --file "221019_JX_VSLP1_THN_BLOCK B_ELEMENTS - 3D MODEL_DXF_20240315.dxf" --author Cameron --discipline Architecture
```

## Supported File Formats

- **DXF** - AutoCAD Drawing Exchange Format
- **DWG** - AutoCAD Drawing Format  
- **PDF** - Portable Document Format

## Development Workflow

### After Making Changes

1. **Run the rebuild script** (recommended):
   ```bash
   ./run-interactive-cli.sh
   ```

2. **Or rebuild manually**:
   ```bash
   ./gradlew clean build
   java -cp build/classes/java/main com.akros.construction.InteractiveShell
   ```

### Key Features

- ✅ **Automatic rebuild** ensures latest changes are included
- ✅ **Robust command parsing** handles quoted filenames with spaces
- ✅ **In-memory storage** for quick development and testing
- ✅ **File format detection** based on file extensions
- ✅ **Comprehensive error handling** with helpful messages

## Project Structure

```
src/main/java/com/akros/construction/
├── Application.java              # Spring Boot main class
├── InteractiveShell.java         # CLI interface
├── model/
│   └── Project.java             # Project data model
├── service/
│   ├── ProjectService.java      # Project management
│   └── SimpleFileService.java   # File upload and management
└── repository/
    └── ProjectRepository.java   # Data persistence (JPA)
```

## Build Configuration

The project uses Gradle with the following key dependencies:
- Spring Boot 2.7.18
- H2 Database (in-memory)
- PDFBox for PDF processing
- Lombok for boilerplate reduction 
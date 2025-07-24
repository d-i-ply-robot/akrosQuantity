package com.akros.construction;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.akros.construction.model.Project;
import com.akros.construction.service.ProjectService;
import com.akros.construction.service.SimpleFileService;

public class InteractiveShell {

    private ProjectService projectService;
    private SimpleFileService fileService;

    public InteractiveShell() {
        this.projectService = new ProjectService();
        this.fileService = new SimpleFileService();
    }

    public static void main(String[] args) {
        InteractiveShell shell = new InteractiveShell();
        try {
            shell.run(args);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run(String... args) throws Exception {
        // Only run interactive shell if no command line arguments provided
        if (args.length == 0) {
            runInteractiveShell();
        }
    }

    private void runInteractiveShell() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n🏗️  Construction Quantities CLI");
        System.out.println("===============================");
        System.out.println("Welcome! Type 'help' to see available commands.");
        System.out.println("Type 'quit' or 'exit' to leave the shell.\n");

        while (true) {
            System.out.print("construction-shell:> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            
            if (input.equals("quit") || input.equals("exit")) {
                System.out.println("Goodbye!");
                System.exit(0);
            }
            
            try {
                processCommand(input);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void processCommand(String input) {
        String[] parts = parseCommand(input);
        String command = parts[0];
        
        switch (command) {
            case "help":
                showHelp();
                break;
            case "list-projects":
                listProjects();
                break;
            case "create-project":
                createProject(parts);
                break;
            case "delete-project":
                deleteProject(parts);
                break;
            case "view-project":
                viewProject(parts);
                break;
            case "upload-file":
                uploadFile(parts);
                break;
            case "list-files":
                listFiles(parts);
                break;
            default:
                System.out.println("Unknown command: " + command + ". Type 'help' for available commands.");
        }
    }
    
    private String[] parseCommand(String input) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        boolean escaped = false;
        
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            if (escaped) {
                current.append(c);
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ' ' && !inQuotes) {
                if (current.length() > 0) {
                    parts.add(current.toString());
                    current = new StringBuilder();
                }
            } else {
                current.append(c);
            }
        }
        
        if (current.length() > 0) {
            parts.add(current.toString());
        }
        
        return parts.toArray(new String[0]);
    }
    
    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  help                              - Show this help message");
        System.out.println("  list-projects                     - List all projects");
        System.out.println("  create-project --name <name>      - Create a new project");
        System.out.println("  view-project --id <id>           - View project details");
        System.out.println("  delete-project --id <id>          - Delete a project");
        System.out.println("  upload-file --projectId <id> --file <path> --author <name> --discipline <type>");
        System.out.println("  list-files --projectId <id>      - List files in a project");
        System.out.println("  quit, exit                        - Exit the shell");
    }
    
    private void listProjects() {
        System.out.println("Projects:");
        projectService.listProjects().forEach(project -> 
            System.out.println("  ID: " + project.getId() + ", Name: " + project.getName())
        );
    }
    
    private void createProject(String[] parts) {
        String name = null;
        for (int i = 0; i < parts.length - 1; i++) {
            if ("--name".equals(parts[i])) {
                name = parts[i + 1];
                break;
            }
        }
        
        if (name == null) {
            System.out.println("Error: Please provide a project name using --name <name>");
            return;
        }
        
        try {
            Project project = projectService.createProject(name);
            System.out.println("Project created successfully with ID: " + project.getId());
        } catch (Exception e) {
            System.out.println("Error creating project: " + e.getMessage());
        }
    }
    
    private void deleteProject(String[] parts) {
        String idStr = null;
        for (int i = 0; i < parts.length - 1; i++) {
            if ("--id".equals(parts[i])) {
                idStr = parts[i + 1];
                break;
            }
        }
        
        if (idStr == null) {
            System.out.println("Error: Please provide a project ID using --id <id>");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            projectService.deleteProject(id);
            System.out.println("Project deleted successfully");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid project ID. Please provide a valid number.");
        } catch (Exception e) {
            System.out.println("Error deleting project: " + e.getMessage());
        }
    }
    
    private void viewProject(String[] parts) {
        String idStr = null;
        for (int i = 0; i < parts.length - 1; i++) {
            if ("--id".equals(parts[i])) {
                idStr = parts[i + 1];
                break;
            }
        }
        
        if (idStr == null) {
            System.out.println("Error: Please provide a project ID using --id <id>");
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            Project project = projectService.getProject(id);
            System.out.println("Project Details:");
            System.out.println("  ID: " + project.getId());
            System.out.println("  Name: " + project.getName());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid project ID. Please provide a valid number.");
        } catch (Exception e) {
            System.out.println("Error retrieving project: " + e.getMessage());
        }
    }
    
    private void uploadFile(String[] parts) {
        String projectIdStr = null;
        String filePath = null;
        String author = null;
        String discipline = null;
        
        for (int i = 0; i < parts.length - 1; i++) {
            String param = parts[i].toLowerCase();
            if ("--projectid".equals(param) || "--project-id".equals(param)) {
                projectIdStr = parts[i + 1];
            } else if ("--file".equals(param)) {
                filePath = parts[i + 1];
            } else if ("--author".equals(param)) {
                author = parts[i + 1];
            } else if ("--discipline".equals(param) || "--discupline".equals(param)) {
                discipline = parts[i + 1];
            }
        }
        
        // Check for missing parameters and provide specific feedback
        StringBuilder missingParams = new StringBuilder();
        if (projectIdStr == null) missingParams.append("--projectId ");
        if (filePath == null) missingParams.append("--file ");
        if (author == null) missingParams.append("--author ");
        if (discipline == null) missingParams.append("--discipline ");
        
        if (missingParams.length() > 0) {
            System.out.println("Error: Missing required parameters: " + missingParams.toString().trim());
            System.out.println("Usage: upload-file --projectId <id> --file <path> --author <name> --discipline <type>");
            System.out.println("Note: Parameters are case-insensitive and common typos are handled.");
            return;
        }
        
        try {
            Long projectId = Long.parseLong(projectIdStr);
            SimpleFileService.FileInfo fileInfo = fileService.uploadFile(projectId, filePath, author, discipline);
            System.out.println("File uploaded successfully:");
            System.out.println("  ID: " + fileInfo.getId());
            System.out.println("  Name: " + fileInfo.getFileName());
            System.out.println("  Format: " + fileInfo.getFormat());
            System.out.println("  Author: " + fileInfo.getAuthor());
            System.out.println("  Discipline: " + fileInfo.getDiscipline());
            System.out.println("  Size: " + fileInfo.getFileSize() + " bytes");
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid project ID. Please provide a valid number.");
        } catch (Exception e) {
            System.out.println("Error uploading file: " + e.getMessage());
        }
    }
    
    private void listFiles(String[] parts) {
        String projectIdStr = null;
        for (int i = 0; i < parts.length - 1; i++) {
            if ("--projectId".equals(parts[i])) {
                projectIdStr = parts[i + 1];
                break;
            }
        }
        
        if (projectIdStr == null) {
            System.out.println("Error: Please provide a project ID using --projectId <id>");
            return;
        }
        
        try {
            Long projectId = Long.parseLong(projectIdStr);
            List<SimpleFileService.FileInfo> files = fileService.getProjectFiles(projectId);
            
            if (files.isEmpty()) {
                System.out.println("No files found for project ID: " + projectId);
            } else {
                System.out.println("Files in project ID " + projectId + ":");
                files.forEach(file -> {
                    System.out.println("  ID: " + file.getId() + 
                                     ", Name: " + file.getFileName() + 
                                     ", Format: " + file.getFormat() + 
                                     ", Author: " + file.getAuthor() + 
                                     ", Discipline: " + file.getDiscipline() + 
                                     ", Size: " + file.getFileSize() + " bytes");
                });
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid project ID. Please provide a valid number.");
        } catch (Exception e) {
            System.out.println("Error listing files: " + e.getMessage());
        }
    }
} 
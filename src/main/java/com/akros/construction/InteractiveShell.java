package com.akros.construction;

import com.akros.construction.model.Project;
import com.akros.construction.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class InteractiveShell implements CommandLineRunner {

    @Autowired
    private ProjectService projectService;

    @Override
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
        String[] parts = input.split("\\s+");
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
            default:
                System.out.println("Unknown command: " + command + ". Type 'help' for available commands.");
        }
    }
    
    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  help                              - Show this help message");
        System.out.println("  list-projects                     - List all projects");
        System.out.println("  create-project --name <name>      - Create a new project");
        System.out.println("  delete-project --id <id>          - Delete a project");
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
} 
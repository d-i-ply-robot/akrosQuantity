package com.akros.construction.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akros.construction.model.Project;

public class ProjectService {
    private final Map<Long, Project> projects = new HashMap<>();
    private long nextId = 1;

    public Project createProject(String name) {
        Project project = new Project(nextId++, name);
        projects.put(project.getId(), project);
        return project;
    }

    public List<Project> listProjects() {
        return new ArrayList<>(projects.values());
    }

    public Project getProject(Long id) {
        Project project = projects.get(id);
        if (project == null) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        return project;
    }

    public void deleteProject(Long id) {
        if (!projects.containsKey(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projects.remove(id);
    }
} 
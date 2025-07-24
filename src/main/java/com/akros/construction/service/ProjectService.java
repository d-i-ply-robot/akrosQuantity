package com.akros.construction.service;

import com.akros.construction.model.Project;
import com.akros.construction.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(String name) {
        Project project = new Project(name);
        return projectRepository.save(project);
    }

    public List<Project> listProjects() {
        return projectRepository.findAll();
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
} 
package com.akros.construction.model;

public class Project {
    private Long id;
    private String name;

    public Project(String name) {
        this.name = name;
    }
    
    public Project(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Project() {
        // Default constructor
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
} 
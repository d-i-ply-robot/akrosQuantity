package com.akros.construction.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimpleFileService {
    private final Map<Long, List<FileInfo>> projectFiles = new HashMap<>();
    private long nextFileId = 1;

    public static class FileInfo {
        private final Long id;
        private final String fileName;
        private final String format;
        private final String author;
        private final String discipline;
        private final Long fileSize;
        private final String filePath;

        public FileInfo(Long id, String fileName, String format, String author, 
                       String discipline, Long fileSize, String filePath) {
            this.id = id;
            this.fileName = fileName;
            this.format = format;
            this.author = author;
            this.discipline = discipline;
            this.fileSize = fileSize;
            this.filePath = filePath;
        }

        // Getters
        public Long getId() { return id; }
        public String getFileName() { return fileName; }
        public String getFormat() { return format; }
        public String getAuthor() { return author; }
        public String getDiscipline() { return discipline; }
        public Long getFileSize() { return fileSize; }
        public String getFilePath() { return filePath; }
    }

    public FileInfo uploadFile(Long projectId, String filePath, String author, String discipline) throws IOException {
        // Strip quotes from file path if present
        String cleanPath = filePath.replaceAll("^\"|\"$", "");
        File file = new File(cleanPath);
        
        // If file doesn't exist, try to find it in common locations
        if (!file.exists()) {
            // Try current directory
            File currentDirFile = new File("./" + cleanPath);
            if (currentDirFile.exists()) {
                file = currentDirFile;
            } else {
                // Try resources directory
                File resourcesFile = new File("./resources/" + cleanPath);
                if (resourcesFile.exists()) {
                    file = resourcesFile;
                } else {
                    // Try with absolute path
                    File absoluteFile = new File(cleanPath);
                    if (absoluteFile.exists()) {
                        file = absoluteFile;
                    } else {
                        throw new IOException("File not found: " + filePath + 
                                           "\nTried locations: " + cleanPath + 
                                           ", ./" + cleanPath + 
                                           ", ./resources/" + cleanPath);
                    }
                }
            }
        }
        
        String fileName = file.getName();
        String format = detectFileFormat(fileName);
        Long fileSize = file.length();
        
        FileInfo fileInfo = new FileInfo(nextFileId++, fileName, format, author, discipline, fileSize, file.getAbsolutePath());
        
        projectFiles.computeIfAbsent(projectId, k -> new ArrayList<>()).add(fileInfo);
        
        return fileInfo;
    }

    private String detectFileFormat(String fileName) {
        String extension = fileName.toLowerCase();
        if (extension.endsWith(".pdf")) {
            return "PDF";
        } else if (extension.endsWith(".dwg")) {
            return "DWG";
        } else if (extension.endsWith(".dxf")) {
            return "DXF";
        } else {
            throw new IllegalArgumentException("Unsupported file format. Supported formats: PDF, DWG, DXF");
        }
    }

    public List<FileInfo> getProjectFiles(Long projectId) {
        return projectFiles.getOrDefault(projectId, new ArrayList<>());
    }

    public FileInfo getProjectFile(Long fileId) {
        for (List<FileInfo> files : projectFiles.values()) {
            for (FileInfo file : files) {
                if (file.getId().equals(fileId)) {
                    return file;
                }
            }
        }
        throw new RuntimeException("File not found with id: " + fileId);
    }

    public void deleteProjectFile(Long fileId) {
        for (List<FileInfo> files : projectFiles.values()) {
            files.removeIf(file -> file.getId().equals(fileId));
        }
    }
} 
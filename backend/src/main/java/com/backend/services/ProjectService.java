package com.backend.services;

import com.backend.models.Project;
import com.backend.models.User;
import com.backend.repositories.ProjectRepository;
import com.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public ProjectService(ProjectRepository projectRepo, UserRepository userRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    public List<Project> getAllProjects() {
        return projectRepo.findAll();
    }

    public Project getProjectById(UUID id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    public Project createProject(String name, String description, UUID ownerId) {
        User owner = userRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + ownerId));

        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        project.setOwner(owner);
        return projectRepo.save(project);
    }

    public Project updateProject(UUID id, String name, String description, UUID ownerId) {
        Project project = getProjectById(id);

        if (name != null) project.setName(name);
        if (description != null) project.setDescription(description);
        if (ownerId != null) {
            User owner = userRepo.findById(ownerId)
                    .orElseThrow(() -> new RuntimeException("Owner not found with id: " + ownerId));
            project.setOwner(owner);
        }

        return projectRepo.save(project);
    }

    public boolean deleteProject(UUID id) {
        if (!projectRepo.existsById(id)) {
            throw new RuntimeException("Project not found with id: " + id);
        }
        projectRepo.deleteById(id);
        return true;
    }
}

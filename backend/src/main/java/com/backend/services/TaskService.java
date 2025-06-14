package com.backend.services;

import com.backend.models.Project;
import com.backend.models.Task;
import com.backend.models.User;
import com.backend.repositories.ProjectRepository;
import com.backend.repositories.TaskRepository;
import com.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository taskRepo, ProjectRepository projectRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public List<Task> getTasksByProject(UUID projectId) {
        return taskRepo.findByProjectId(projectId);
    }

    public Task getTaskById(UUID id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public Task createTask(String title, String description, LocalDate dueDate, UUID projectId, UUID assigneeId, UUID creatorId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        User creator = userRepo.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator not found with id: " + creatorId));

        User assignee = null;
        if (assigneeId != null) {
            assignee = userRepo.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + assigneeId));
        }

        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setCreator(creator);
        task.setAssignee(assignee);
        task.setProject(project);

        return taskRepo.save(task);
    }

    public Task updateTask(UUID id, String title, String description, LocalDate dueDate, UUID projectId, UUID assigneeId) {
        Task task = getTaskById(id);

        if (title != null) task.setTitle(title);
        if (description != null) task.setDescription(description);
        if (dueDate != null) task.setDueDate(dueDate);

        if (projectId != null) {
            Project project = projectRepo.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
            task.setProject(project);
        }

        if (assigneeId != null) {
            User assignee = userRepo.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found with id: " + assigneeId));
            task.setAssignee(assignee);
        }

        return taskRepo.save(task);
    }

    public boolean deleteTask(UUID id) {
        if (!taskRepo.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepo.deleteById(id);
        return true;
    }
}

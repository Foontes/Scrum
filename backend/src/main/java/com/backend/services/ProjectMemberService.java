package com.backend.services;

import com.backend.models.Project;
import com.backend.models.ProjectMember;
import com.backend.models.ProjectPermission;
import com.backend.models.User;
import com.backend.repositories.ProjectMemberRepository;
import com.backend.repositories.ProjectRepository;
import com.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectMemberService {

    private final ProjectMemberRepository memberRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public ProjectMemberService(ProjectMemberRepository memberRepo, ProjectRepository projectRepo, UserRepository userRepo) {
        this.memberRepo = memberRepo;
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
    }

    public List<ProjectMember> getAllMembers() {
        return memberRepo.findAll();
    }

    public ProjectMember getById(Long id) {
        return memberRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Project member not found with id: " + id));
    }

    public ProjectMember addProjectMember(UUID  projectId, UUID  userId, ProjectPermission permission) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setPermission(permission);
        return memberRepo.save(member);
    }

    public ProjectMember updateProjectMember(Long id, ProjectPermission permission) {
        ProjectMember member = getById(id);
        member.setPermission(permission);
        return memberRepo.save(member);
    }

    public boolean removeProjectMember(Long id) {
        if (!memberRepo.existsById(id)) {
            throw new RuntimeException("Project member not found with id: " + id);
        }
        memberRepo.deleteById(id);
        return true;
    }
}

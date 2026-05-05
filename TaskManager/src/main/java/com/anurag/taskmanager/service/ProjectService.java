package com.anurag.taskmanager.service;

import com.anurag.taskmanager.dto.ProjectDTO;
import com.anurag.taskmanager.entity.Project;
import com.anurag.taskmanager.entity.User;
import com.anurag.taskmanager.repository.ProjectRepository;
import com.anurag.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperService mapperService;

    public ProjectDTO createProject(ProjectDTO projectDTO, Long userId) {
        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(projectDTO.getName())
                .description(projectDTO.getDescription())
                .createdBy(creator)
                .build();

        project.getMembers().add(creator);

        Project savedProject = projectRepository.save(project);
        return mapperService.projectToDTO(savedProject);
    }

    public ProjectDTO getProjectById(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!hasAccessToProject(project, userId)) {
            throw new RuntimeException("Access denied");
        }

        return mapperService.projectToDTO(project);
    }

    public List<ProjectDTO> getUserProjects(Long userId) {
        List<Project> projects = projectRepository.findProjectsByUserIdOrMembership(userId);
        return projects.stream()
                .map(mapperService::projectToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Only project creator can update");
        }

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());

        Project updatedProject = projectRepository.save(project);
        return mapperService.projectToDTO(updatedProject);
    }

    public void deleteProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Only project creator can delete");
        }

        projectRepository.delete(project);
    }

    public ProjectDTO addMemberToProject(Long projectId, Long memberId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Only project creator can add members");
        }

        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.getMembers().add(member);
        Project updatedProject = projectRepository.save(project);
        return mapperService.projectToDTO(updatedProject);
    }

    public ProjectDTO removeMemberFromProject(Long projectId, Long memberId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getCreatedBy().getId().equals(userId)) {
            throw new RuntimeException("Only project creator can remove members");
        }

        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        project.getMembers().remove(member);
        Project updatedProject = projectRepository.save(project);
        return mapperService.projectToDTO(updatedProject);
    }

    private boolean hasAccessToProject(Project project, Long userId) {
        return project.getCreatedBy().getId().equals(userId) ||
               project.getMembers().stream().anyMatch(m -> m.getId().equals(userId));
    }
}

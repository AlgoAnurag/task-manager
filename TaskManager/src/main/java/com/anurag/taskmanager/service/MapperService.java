package com.anurag.taskmanager.service;

import com.anurag.taskmanager.dto.ProjectDTO;
import com.anurag.taskmanager.dto.TaskDTO;
import com.anurag.taskmanager.dto.UserDTO;
import com.anurag.taskmanager.entity.Project;
import com.anurag.taskmanager.entity.Task;
import com.anurag.taskmanager.entity.User;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class MapperService {

    public UserDTO userToDTO(User user) {
        if (user == null) return null;
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public TaskDTO taskToDTO(Task task) {
        if (task == null) return null;
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .projectId(task.getProject().getId())
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedTo(task.getAssignedTo() != null ? userToDTO(task.getAssignedTo()) : null)
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .completedAt(task.getCompletedAt())
                .build();
    }

    public ProjectDTO projectToDTO(Project project) {
        if (project == null) return null;
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdById(project.getCreatedBy().getId())
                .createdBy(userToDTO(project.getCreatedBy()))
                .members(project.getMembers().stream().map(this::userToDTO).collect(Collectors.toList()))
                .tasks(project.getTasks().stream().map(this::taskToDTO).collect(Collectors.toList()))
                .status(project.getStatus())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}

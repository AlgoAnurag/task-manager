package com.anurag.taskmanager.dto;

import com.anurag.taskmanager.entity.ProjectStatus;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;

    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    private Long createdById;
    private UserDTO createdBy;

    private List<UserDTO> members;

    private List<TaskDTO> tasks;

    private ProjectStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

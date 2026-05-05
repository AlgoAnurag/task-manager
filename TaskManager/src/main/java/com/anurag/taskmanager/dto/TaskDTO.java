package com.anurag.taskmanager.dto;

import com.anurag.taskmanager.entity.TaskPriority;
import com.anurag.taskmanager.entity.TaskStatus;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Long projectId;

    private Long assignedToId;
    private UserDTO assignedTo;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;
}

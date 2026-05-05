package com.anurag.taskmanager.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private Long totalProjects;
    private Long totalTasks;
    private Long completedTasks;
    private Long overdueTasks;
    private Long tasksInProgress;
    private List<TaskDTO> recentTasks;
    private List<ProjectDTO> recentProjects;
}

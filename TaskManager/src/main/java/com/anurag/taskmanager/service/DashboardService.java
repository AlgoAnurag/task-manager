package com.anurag.taskmanager.service;

import com.anurag.taskmanager.dto.DashboardDTO;
import com.anurag.taskmanager.dto.ProjectDTO;
import com.anurag.taskmanager.dto.TaskDTO;
import com.anurag.taskmanager.entity.Project;
import com.anurag.taskmanager.entity.Task;
import com.anurag.taskmanager.entity.TaskStatus;
import com.anurag.taskmanager.repository.ProjectRepository;
import com.anurag.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MapperService mapperService;

    public DashboardDTO getDashboard(Long userId) {
        List<Project> userProjects = projectRepository.findProjectsByUserIdOrMembership(userId);
        List<Long> projectIds = userProjects.stream().map(Project::getId).collect(Collectors.toList());

        long totalProjects = userProjects.size();
        long totalTasks = 0;
        long completedTasks = 0;
        long tasksInProgress = 0;
        long overdueTasks = 0;

        for (Project project : userProjects) {
            List<Task> projectTasks = taskRepository.findByProjectId(project.getId());
            totalTasks += projectTasks.size();

            for (Task task : projectTasks) {
                if (task.getStatus() == TaskStatus.COMPLETED) {
                    completedTasks++;
                } else if (task.getStatus() == TaskStatus.IN_PROGRESS) {
                    tasksInProgress++;
                }
            }
        }

        List<Task> overdueTasksForUser = taskRepository.findOverdueTasksForUser(userId, LocalDateTime.now());
        overdueTasks = overdueTasksForUser.size();

        List<Task> allUserTasks = taskRepository.findByAssignedToId(userId);
        List<TaskDTO> recentTasks = allUserTasks.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .map(mapperService::taskToDTO)
                .collect(Collectors.toList());

        List<ProjectDTO> recentProjects = userProjects.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .map(mapperService::projectToDTO)
                .collect(Collectors.toList());

        return DashboardDTO.builder()
                .totalProjects(totalProjects)
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .tasksInProgress(tasksInProgress)
                .overdueTasks(overdueTasks)
                .recentTasks(recentTasks)
                .recentProjects(recentProjects)
                .build();
    }
}

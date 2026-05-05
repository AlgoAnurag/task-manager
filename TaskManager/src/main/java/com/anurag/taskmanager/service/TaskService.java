package com.anurag.taskmanager.service;

import com.anurag.taskmanager.dto.TaskDTO;
import com.anurag.taskmanager.entity.Project;
import com.anurag.taskmanager.entity.Task;
import com.anurag.taskmanager.entity.User;
import com.anurag.taskmanager.repository.ProjectRepository;
import com.anurag.taskmanager.repository.TaskRepository;
import com.anurag.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperService mapperService;

    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!hasAccessToProject(project, userId)) {
            throw new RuntimeException("Access denied");
        }

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .project(project)
                .priority(taskDTO.getPriority())
                .dueDate(taskDTO.getDueDate())
                .build();

        if (taskDTO.getAssignedToId() != null) {
            User assignee = userRepository.findById(taskDTO.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(assignee);
        }

        Task savedTask = taskRepository.save(task);
        return mapperService.taskToDTO(savedTask);
    }

    public TaskDTO getTaskById(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!hasAccessToProject(task.getProject(), userId)) {
            throw new RuntimeException("Access denied");
        }

        return mapperService.taskToDTO(task);
    }

    public List<TaskDTO> getProjectTasks(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!hasAccessToProject(project, userId)) {
            throw new RuntimeException("Access denied");
        }

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream()
                .map(mapperService::taskToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getAssignedTasks(Long userId) {
        List<Task> tasks = taskRepository.findByAssignedToId(userId);
        return tasks.stream()
                .map(mapperService::taskToDTO)
                .collect(Collectors.toList());
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!hasAccessToProject(task.getProject(), userId)) {
            throw new RuntimeException("Access denied");
        }

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setDueDate(taskDTO.getDueDate());

        if (taskDTO.getAssignedToId() != null) {
            User assignee = userRepository.findById(taskDTO.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(assignee);
        }

        Task updatedTask = taskRepository.save(task);
        return mapperService.taskToDTO(updatedTask);
    }

    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!hasAccessToProject(task.getProject(), userId)) {
            throw new RuntimeException("Access denied");
        }

        taskRepository.delete(task);
    }

    public List<TaskDTO> getOverdueTasks(Long userId) {
        List<Task> tasks = taskRepository.findOverdueTasksForUser(userId, LocalDateTime.now());
        return tasks.stream()
                .map(mapperService::taskToDTO)
                .collect(Collectors.toList());
    }

    private boolean hasAccessToProject(Project project, Long userId) {
        return project.getCreatedBy().getId().equals(userId) ||
               project.getMembers().stream().anyMatch(m -> m.getId().equals(userId));
    }
}

package com.anurag.taskmanager.controller;

import com.anurag.taskmanager.dto.TaskDTO;
import com.anurag.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            TaskDTO response = taskService.createTask(taskDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long taskId, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            TaskDTO response = taskService.getTaskById(taskId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskDTO>> getProjectTasks(@PathVariable Long projectId, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            List<TaskDTO> response = taskService.getProjectTasks(projectId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/assigned/me")
    public ResponseEntity<List<TaskDTO>> getAssignedTasks(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            List<TaskDTO> response = taskService.getAssignedTasks(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/overdue/me")
    public ResponseEntity<List<TaskDTO>> getOverdueTasks(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            List<TaskDTO> response = taskService.getOverdueTasks(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId,
                                             @Valid @RequestBody TaskDTO taskDTO,
                                             Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            TaskDTO response = taskService.updateTask(taskId, taskDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            taskService.deleteTask(taskId, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

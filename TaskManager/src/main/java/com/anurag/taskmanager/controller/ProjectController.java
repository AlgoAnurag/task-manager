package com.anurag.taskmanager.controller;

import com.anurag.taskmanager.dto.ProjectDTO;
import com.anurag.taskmanager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            ProjectDTO response = projectService.createProject(projectDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long projectId, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            ProjectDTO response = projectService.getProjectById(projectId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getUserProjects(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            List<ProjectDTO> response = projectService.getUserProjects(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long projectId,
                                                     @Valid @RequestBody ProjectDTO projectDTO,
                                                     Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            ProjectDTO response = projectService.updateProject(projectId, projectDTO, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId, Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            projectService.deleteProject(projectId, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ProjectDTO> addMember(@PathVariable Long projectId,
                                               @PathVariable Long memberId,
                                               Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            ProjectDTO response = projectService.addMemberToProject(projectId, memberId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ProjectDTO> removeMember(@PathVariable Long projectId,
                                                  @PathVariable Long memberId,
                                                  Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            ProjectDTO response = projectService.removeMemberFromProject(projectId, memberId, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

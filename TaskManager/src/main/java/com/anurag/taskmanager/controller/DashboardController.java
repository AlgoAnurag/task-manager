package com.anurag.taskmanager.controller;

import com.anurag.taskmanager.dto.DashboardDTO;
import com.anurag.taskmanager.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard(Authentication authentication) {
        try {
            Long userId = (Long) authentication.getCredentials();
            DashboardDTO response = dashboardService.getDashboard(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

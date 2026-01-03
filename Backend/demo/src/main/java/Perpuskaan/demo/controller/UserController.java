package Perpuskaan.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Perpuskaan.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Get all PEMUSTAKA (ADMIN ONLY)
     * GET /api/users/pemustaka
     */
    @GetMapping("/pemustaka")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllPemustaka() {
        return ResponseEntity.ok(userService.getAllPemustaka());
    }
    

}

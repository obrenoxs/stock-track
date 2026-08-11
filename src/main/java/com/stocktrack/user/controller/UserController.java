package com.stocktrack.user.controller;

import com.stocktrack.user.dto.request.UserRequestDTO;
import com.stocktrack.user.dto.request.UserUpdateRequestDTO;
import com.stocktrack.user.dto.response.UserResponseDTO;
import com.stocktrack.user.security.CustomUserDetails;
import com.stocktrack.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public UserResponseDTO createWarehouseManager(@Valid @RequestBody UserRequestDTO dto) {
        return userService.createWarehouseManager(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('WAREHOUSE_MANAGER')")
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/me")
    public UserResponseDTO findAuthenticatedUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userService.findByRe(userDetails.getUser().getRe());
    }

    @PutMapping("/me")
    public UserResponseDTO updateAuthenticatedUser(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody UserUpdateRequestDTO dto) {
        return userService.updateSelf(userDetails.getUser().getRe(), dto);
    }
}

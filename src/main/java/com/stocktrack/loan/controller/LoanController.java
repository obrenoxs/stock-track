package com.stocktrack.loan.controller;

import com.stocktrack.loan.dto.request.LoanCreateRequestDTO;
import com.stocktrack.loan.dto.request.LoanReturnRequestDTO;
import com.stocktrack.loan.dto.response.LoanResponseDTO;
import com.stocktrack.loan.enums.LoanStatusFilter;
import com.stocktrack.loan.service.LoanService;

import com.stocktrack.user.enums.Role;
import com.stocktrack.user.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponseDTO create(
            @Valid @RequestBody LoanCreateRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return loanService.create(dto, principal.getUser().getId());
    }

    @PatchMapping("/{id}/return")
    public LoanResponseDTO returnLoan(
            @PathVariable Long id,
            @Valid @RequestBody LoanReturnRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return loanService.returnLoan(id, dto, principal.getUser().getId());
    }

    @GetMapping("/{id}")
    public LoanResponseDTO findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return loanService.findById(id, principal.getUser().getId(), isManager(principal));
    }

    @GetMapping
    public Page<LoanResponseDTO> findAll(
            @RequestParam(required = false) Long toolId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) LoanStatusFilter status,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return loanService.findAll(toolId, userId, status, pageable,
                principal.getUser().getId(), isManager(principal));
    }

    private boolean isManager(CustomUserDetails principal) {
        return principal.getUser().getRole() == Role.WAREHOUSE_MANAGER;
    }
}

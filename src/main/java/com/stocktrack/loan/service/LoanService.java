package com.stocktrack.loan.service;

import com.stocktrack.loan.dto.request.LoanCreateRequestDTO;
import com.stocktrack.loan.dto.request.LoanReturnRequestDTO;
import com.stocktrack.loan.dto.response.LoanResponseDTO;
import com.stocktrack.loan.entity.Loan;
import com.stocktrack.loan.event.ToolLoanedEvent;
import com.stocktrack.loan.event.ToolReturnedEvent;
import com.stocktrack.loan.mapper.LoanMapper;
import com.stocktrack.loan.repository.LoanRepository;
import com.stocktrack.shared.exception.BusinessRuleException;
import com.stocktrack.shared.exception.ResourceNotFoundException;
import com.stocktrack.tool.entity.Tool;
import com.stocktrack.tool.enums.ToolStatus;
import com.stocktrack.tool.service.ToolService;
import com.stocktrack.user.entity.User;
import com.stocktrack.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int DEFAULT_MINIMUM_LOAN_HOURS = 7;

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    private final ToolService toolService;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public LoanResponseDTO create(LoanCreateRequestDTO dto, Long borrowedByUserId) {
        Tool tool = toolService.findEntityById(dto.toolId());
        User borrowedByUser = userService.findEntityById(borrowedByUserId);

        validateToolAvailable(tool);

        Loan loan = loanMapper.toEntity(dto);
        loan.setTool(tool);
        loan.setBorrowedByUser(borrowedByUser);
        loan.setLoanDate(LocalDateTime.now());
        loan.setExpectedReturnDate(resolveExpectedReturnDate(dto.expectedReturnDate(), loan.getLoanDate()));

        Loan saved = loanRepository.save(loan);
        toolService.markAsInUse(tool.getId());

        eventPublisher.publishEvent(new ToolLoanedEvent(saved.getId(), tool.getId(), borrowedByUserId));

        return toResponseDTO(saved);
    }

    @Transactional
    public LoanResponseDTO returnLoan(Long id, LoanReturnRequestDTO dto, Long returnedByUserId) {
        Loan loan = getLoanOrThrow(id);

        if (loan.getReturnDate() != null) {
            throw new BusinessRuleException("Este empréstimo já foi devolvido");
        }

        User returnedByUser = userService.findEntityById(returnedByUserId);

        loan.setReturnDate(LocalDateTime.now());
        loan.setReturnedByUser(returnedByUser);
        loan.setObservation(dto.observation());

        Loan saved = loanRepository.save(loan);
        toolService.markAsAvailable(loan.getTool().getId());

        eventPublisher.publishEvent(new ToolReturnedEvent(loan.getId(), loan.getTool().getId(), returnedByUserId));

        return toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public LoanResponseDTO findById(Long id) {
        Loan loan = loanRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com id: " + id));
        return toResponseDTO(loan);
    }

    private void validateToolAvailable(Tool tool) {
        if (tool.getStatus() != ToolStatus.AVAILABLE) {
            throw new BusinessRuleException(
                    "Ferramenta não está disponível para empréstimo (status atual: " + tool.getStatus() + ")");
        }

        if (toolService.isCalibrationOverdue(tool.getId())) {
            throw new BusinessRuleException("Ferramenta com calibração vencida não pode ser emprestada");
        }

        if (loanRepository.existsByTool_IdAndReturnDateIsNull(tool.getId())) {
            throw new BusinessRuleException("Ferramenta já está emprestada");
        }
    }

    private LocalDateTime resolveExpectedReturnDate(LocalDateTime informed, LocalDateTime loanDate) {
        return informed != null ? informed : loanDate.plusHours(DEFAULT_MINIMUM_LOAN_HOURS);
    }

    private LoanResponseDTO toResponseDTO(Loan loan) {
        boolean late = isLate(loan);
        return loanMapper.toResponseDTO(loan, late);
    }

    private boolean isLate(Loan loan) {
        if (loan.getReturnDate() != null || loan.getExpectedReturnDate() == null) {
            return false;
        }
        return loan.getExpectedReturnDate().isBefore(LocalDateTime.now());
    }

    private Loan getLoanOrThrow(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado com id: " + id));
    }


}

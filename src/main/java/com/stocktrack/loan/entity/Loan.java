package com.stocktrack.loan.entity;

import com.stocktrack.shared.entity.BaseEntity;
import com.stocktrack.tool.entity.Tool;
import com.stocktrack.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loan extends BaseEntity {

    @Column(name = "reason", nullable = false, length = 255)
    private String reason;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "loan_date", nullable = false)
    private LocalDateTime loanDate;

    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrowed_by_user_id", nullable = false)
    private User borrowedByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "returned_by_user_id")
    private User returnedByUser;
}

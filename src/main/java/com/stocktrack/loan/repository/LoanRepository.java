package com.stocktrack.loan.repository;

import com.stocktrack.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    @Query("""
            SELECT l FROM Loan l
            JOIN FETCH l.tool t
            JOIN FETCH t.toolType
            JOIN FETCH l.borrowedByUser
            LEFT JOIN FETCH l.returnedByUser
            WHERE l.id = :id
            """)
    Optional<Loan> findByIdWithDetails(@Param("id") Long id);

    boolean existsByTool_IdAndReturnDateIsNull(Long toolId);
}

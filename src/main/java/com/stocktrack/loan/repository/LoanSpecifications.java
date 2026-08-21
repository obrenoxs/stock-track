package com.stocktrack.loan.repository;

import com.stocktrack.loan.entity.Loan;
import com.stocktrack.loan.enums.LoanStatusFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class LoanSpecifications {

    private LoanSpecifications() {
    }

    public static Specification<Loan> hasTool(Long toolId) {
        return (root, query, criteriaBuilder) -> {
            if (toolId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("tool").get("id"), toolId);
        };
    }

    public static Specification<Loan> hasBorrowedByUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("borrowedByUser").get("id"), userId);
        };
    }

    public static Specification<Loan> hasStatus(LoanStatusFilter status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }

            Predicate returnDateIsNull = criteriaBuilder.isNull(root.get("returnDate"));

            return switch (status) {
                case DEVOLVIDO -> criteriaBuilder.isNotNull(root.get("returnDate"));
                case ABERTO -> returnDateIsNull;
                case ATRASADO -> criteriaBuilder.and(
                        returnDateIsNull,
                        criteriaBuilder.lessThan(root.get("expectedReturnDate"), LocalDateTime.now())
                );
            };
        };
    }
}

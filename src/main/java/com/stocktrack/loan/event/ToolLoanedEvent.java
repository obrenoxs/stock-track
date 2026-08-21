package com.stocktrack.loan.event;

public record ToolLoanedEvent(Long loanId, Long toolId, Long performedId) {
}

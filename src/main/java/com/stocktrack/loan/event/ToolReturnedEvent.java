package com.stocktrack.loan.event;

public record ToolReturnedEvent(Long loanId, Long toolId, Long performedByUserId) {
}

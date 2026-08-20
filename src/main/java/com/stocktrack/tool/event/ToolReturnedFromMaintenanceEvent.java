package com.stocktrack.tool.event;

public record ToolReturnedFromMaintenanceEvent(Long toolId, Long performedByUserId) {
}

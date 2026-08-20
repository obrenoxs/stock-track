package com.stocktrack.tool.event;

public record ToolSentToMaintenanceEvent(Long toolId, Long performedByUserId) {
}

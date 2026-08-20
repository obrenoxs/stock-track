package com.stocktrack.tool.event;

public record ToolCreatedEvent(Long toolId, Long performedByUserId) {
}

package com.stocktrack.tool.event;

public record ToolDiscardedEvent(Long toolId, Long performedByUserId) {
}

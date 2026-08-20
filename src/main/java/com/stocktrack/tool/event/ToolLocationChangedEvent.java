package com.stocktrack.tool.event;

public record ToolLocationChangedEvent(Long toolId, Long performedByUserId) {
}

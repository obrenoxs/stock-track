package com.stocktrack.tool.event;

public record ToolCalibrationRegisteredEvent(Long toolId, Long performedByUserId) {
}

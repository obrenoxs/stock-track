package com.stocktrack.tooltype.service;

public interface ToolInventoryPort {

    boolean existsForToolType(Long toolTypeId);

    long countAvailableForToolType(Long toolTypeId);
}

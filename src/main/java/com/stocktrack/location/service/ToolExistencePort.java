package com.stocktrack.location.service;

public interface ToolExistencePort {

    boolean existsActiveToolForLocation(Long locationId);
}

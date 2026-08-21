package com.stocktrack.auditlog.listener;

import com.stocktrack.auditlog.enums.ActionType;
import com.stocktrack.auditlog.service.AuditLogService;
import com.stocktrack.loan.event.ToolLoanedEvent;
import com.stocktrack.loan.event.ToolReturnedEvent;
import com.stocktrack.tool.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogEventListener {

    private final AuditLogService auditLogService;

    @EventListener
    public void onToolCreated(ToolCreatedEvent event) {
        auditLogService.record(ActionType.CREATED, event.toolId(), event.performedByUserId(), null);
    }

    @EventListener
    public void onToolSentToMaintenance(ToolSentToMaintenanceEvent event) {
        auditLogService.record(ActionType.SENT_TO_MAINTENANCE, event.toolId(), event.performedByUserId(), null);
    }

    @EventListener
    public void onToolReturnedFromMaintenance(ToolReturnedFromMaintenanceEvent event) {
        auditLogService.record(ActionType.RETURNED_FROM_MAINTENANCE, event.toolId(), event.performedByUserId(), null);
    }

    @EventListener
    public void onToolDiscarded(ToolDiscardedEvent event) {
        auditLogService.record(ActionType.DISCARDED, event.toolId(), event.performedByUserId(), null);
    }

    @EventListener
    public void onToolLocationChanged(ToolLocationChangedEvent event) {
        auditLogService.record(ActionType.LOCATION_CHANGED, event.toolId(), event.performedByUserId(), null);
    }

    @EventListener
    public void onToolCalibrationRegistered(ToolCalibrationRegisteredEvent event) {
        auditLogService.record(ActionType.CALIBRATION_REGISTERED, event.toolId(), event.performedByUserId(), null);
    }

    @EventListener
    public void onToolLoaned(ToolLoanedEvent event) {
        auditLogService.record(ActionType.LOANED, event.toolId(), event.performedId(), null);
    }

    @EventListener
    public void onToolReturned(ToolReturnedEvent event) {
        auditLogService.record(ActionType.RETURNED, event.toolId(), event.performedByUserId(), null);
    }
}

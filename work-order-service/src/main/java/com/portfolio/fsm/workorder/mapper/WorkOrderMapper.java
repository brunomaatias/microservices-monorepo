package com.portfolio.fsm.workorder.mapper;

import com.portfolio.fsm.workorder.dto.WorkOrderDto;
import com.portfolio.fsm.workorder.models.WorkOrder;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderMapper {

    public WorkOrderDto toResponse(WorkOrder entity) {
        if (entity == null) {
            return null;
        }

        return new WorkOrderDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCustomerId(),
                entity.getTechnicianId(),
                entity.getScheduledDate()
        );
    }

    public WorkOrder toEntity(WorkOrderDto request) {
        if (request == null) {
            return null;
        }

        WorkOrder workOrder = new WorkOrder();
        workOrder.setTitle(request.title());
        workOrder.setDescription(request.description());
        
        if (request.status() != null) {
            workOrder.setStatus(request.status());
        }
        
        workOrder.setCustomerId(request.customerId());
        workOrder.setTechnicianId(request.technicianId());
        workOrder.setScheduledDate(request.scheduledDate());

        return workOrder;
    }

    public void updateEntityFromRequest(WorkOrderDto request, WorkOrder entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.title() != null) {
            entity.setTitle(request.title());
        }
        if (request.description() != null) {
            entity.setDescription(request.description());
        }
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
        if (request.customerId() != null) {
            entity.setCustomerId(request.customerId());
        }
        if (request.technicianId() != null) {
            entity.setTechnicianId(request.technicianId());
        }
        if (request.scheduledDate() != null) {
            entity.setScheduledDate(request.scheduledDate());
        }
    }
}

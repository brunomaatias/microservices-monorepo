package com.portfolio.fsm.workorder.services;

import com.portfolio.fsm.workorder.dto.WorkOrderDto;
import com.portfolio.fsm.workorder.events.EventPublisher;
import com.portfolio.fsm.workorder.mapper.WorkOrderMapper;
import com.portfolio.fsm.workorder.models.WorkOrder;
import com.portfolio.fsm.workorder.repositories.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    @Autowired
    private EventPublisher eventPublisher;

    public WorkOrderDto createWorkOrder(WorkOrderDto request) {
        WorkOrder workOrder = workOrderMapper.toEntity(request);
        WorkOrder saved = workOrderRepository.save(workOrder);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("workOrderId", saved.getId().toString());
        payload.put("title", saved.getTitle());
        payload.put("customerId", saved.getCustomerId().toString());
        
        eventPublisher.publishEvent("audit.event.workorder.created", "WORK_ORDER_CREATED", payload);
        
        return workOrderMapper.toResponse(saved);
    }

    public List<WorkOrderDto> getAllWorkOrders() {
        return workOrderRepository.findAll().stream()
                .map(workOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    public WorkOrderDto getWorkOrderById(UUID id) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new com.portfolio.fsm.workorder.exceptions.ResourceNotFoundException("WorkOrder not found"));
        return workOrderMapper.toResponse(workOrder);
    }

    public WorkOrderDto updateWorkOrder(UUID id, WorkOrderDto request) {
        WorkOrder workOrder = workOrderRepository.findById(id)
                .orElseThrow(() -> new com.portfolio.fsm.workorder.exceptions.ResourceNotFoundException("WorkOrder not found"));

        String oldStatus = workOrder.getStatus();

        workOrderMapper.updateEntityFromRequest(request, workOrder);
        WorkOrder updated = workOrderRepository.save(workOrder);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("workOrderId", updated.getId().toString());
        payload.put("title", updated.getTitle());
        payload.put("oldStatus", oldStatus);
        payload.put("newStatus", updated.getStatus());

        eventPublisher.publishEvent("audit.event.workorder.updated", "WORK_ORDER_UPDATED", payload);

        if ("COMPLETED".equals(updated.getStatus()) && !"COMPLETED".equals(oldStatus)) {
            eventPublisher.publishEvent("audit.event.workorder.completed", "WORK_ORDER_COMPLETED", payload);
        }

        return workOrderMapper.toResponse(updated);
    }
}

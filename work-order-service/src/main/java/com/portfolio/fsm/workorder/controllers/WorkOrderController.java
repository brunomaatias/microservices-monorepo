package com.portfolio.fsm.workorder.controllers;

import com.portfolio.fsm.workorder.dto.WorkOrderDto;
import com.portfolio.fsm.workorder.services.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/work-orders")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping
    public ResponseEntity<WorkOrderDto> createWorkOrder(@RequestBody WorkOrderDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workOrderService.createWorkOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<WorkOrderDto>> getAllWorkOrders() {
        return ResponseEntity.ok(workOrderService.getAllWorkOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderDto> getWorkOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(workOrderService.getWorkOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkOrderDto> updateWorkOrder(@PathVariable UUID id, @RequestBody WorkOrderDto request) {
        return ResponseEntity.ok(workOrderService.updateWorkOrder(id, request));
    }
}

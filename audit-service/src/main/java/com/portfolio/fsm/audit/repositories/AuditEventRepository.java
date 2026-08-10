package com.portfolio.fsm.audit.repositories;

import com.portfolio.fsm.audit.models.AuditEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEventRepository extends MongoRepository<AuditEvent, String> {
}

package com.anderfred.medical.clinic.service.impl;

import com.anderfred.medical.clinic.domain.audit.ActionType;
import com.anderfred.medical.clinic.domain.audit.Audit;
import com.anderfred.medical.clinic.domain.audit.EntityType;
import com.anderfred.medical.clinic.repository.jpa.AuditJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
  private static final Logger log = LoggerFactory.getLogger(AuditService.class);

  private final AuditJpaRepository repository;

  public AuditService(AuditJpaRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public void createAuditRecord(EntityType type, ActionType actionType, Long entityId) {
    Audit audit = Audit.of(actionType, type, entityId);
    repository.save(audit);
    log.info("Audit record created:[{}]", audit);
  }
}

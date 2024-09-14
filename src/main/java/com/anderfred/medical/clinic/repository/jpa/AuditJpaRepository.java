package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.audit.Audit;
import com.anderfred.medical.clinic.domain.audit.EntityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditJpaRepository extends JpaRepository<Audit, Long> {

  @Query(
      value =
          "select a from Audit a where a.actorId =:actorId and a.entityType =:type order by a.date asc")
  Page<Audit> findByActorType(
      @Param("actorId") Long actorId, Pageable pageable, @Param("type") EntityType type);
}

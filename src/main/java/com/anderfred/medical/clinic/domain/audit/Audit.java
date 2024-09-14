package com.anderfred.medical.clinic.domain.audit;

import com.anderfred.medical.clinic.domain.base.SequenceEntity;
import com.anderfred.medical.clinic.security.CustomAuthenticationToken;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Table(name = "audit")
public class Audit extends SequenceEntity<Long> {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;

  @Column(name = "action_type")
  @Enumerated(EnumType.STRING)
  private ActionType actionType;

  @Column(name = "entity_type")
  @Enumerated(EnumType.STRING)
  private EntityType entityType;

  @Column(name = "entity_id")
  private Long entityId;

  @Column(name = "actor_id")
  private Long actorId;

  @Override
  public Long getId() {
    return id;
  }

  public Audit setId(Long id) {
    this.id = id;
    return this;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public Audit setDate(LocalDateTime date) {
    this.date = date;
    return this;
  }

  public ActionType getActionType() {
    return actionType;
  }

  public Audit setActionType(ActionType actionType) {
    this.actionType = actionType;
    return this;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public Audit setEntityType(EntityType entityType) {
    this.entityType = entityType;
    return this;
  }

  public Long getEntityId() {
    return entityId;
  }

  public Audit setEntityId(Long entityId) {
    this.entityId = entityId;
    return this;
  }

  public Long getActorId() {
    return actorId;
  }

  public Audit setActorId(Long actorId) {
    this.actorId = actorId;
    return this;
  }

  public static Audit of(ActionType type, EntityType entityType, Long entityId) {
    return new Audit()
        .setActionType(type)
        .setEntityType(entityType)
        .setEntityId(entityId)
        .setActorId(
            ((CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getActorId())
        .setDate(LocalDateTime.now());
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("id", getId())
        .append("date", getDate())
        .append("actionType", getActionType())
        .append("entityType", getEntityType())
        .append("entityId", getEntityId())
        .append("actorId", getActorId())
        .toString();
  }
}

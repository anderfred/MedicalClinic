package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.Appointment;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentJpaRepository extends JpaRepository<Appointment, Long> {
  @Query(
      value =
          "select a from Appointment a left join fetch a.medicalExams me left join fetch me.examType " +
                  "left join fetch a.patient " +
                  "left join  fetch a.doctor " +
                  "left join fetch a.lastModifiedDoctor " +
                  "where a.id =:id")
  Optional<Appointment> findFullById(@Param("id") Long id);

  @Query(
      value =
          "select a.id from Appointment a where a.state !='DELETED'")
  Page<Long> findActivePageIds(Pageable pageable);

  @Query(
          value =
                  "select a from Appointment a left join fetch a.medicalExams me left join fetch me.examType " +
                          "left join fetch a.patient " +
                          "left join  fetch a.doctor " +
                          "left join fetch a.lastModifiedDoctor " +
                          "where a.id in:ids")
  List<Appointment> findFullByIds(@Param("ids") Set<Long> ids);

  @Query(value = "select a.id from Appointment a where a.patient.id =:id")
  List<Long> findIdsByPatientId(@Param("id") Long id);

  @Modifying
  @Query("Update Appointment a set a.state = 'CLOSED' where a.state != 'CLOSED'")
  void closeAppointments();
}

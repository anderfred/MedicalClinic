package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.MedicalExam;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicalExamJpaRepository extends JpaRepository<MedicalExam, Long> {
  @Query(
      value =
          "select exam from MedicalExam exam "
              + "join Appointment app on exam.appointment.id = app.id "
              + "where app.patient.id =:patientId "
              + "order by exam.date asc")
  List<MedicalExam> findAllByPatient(@Param("patientId") Long patientId);
}

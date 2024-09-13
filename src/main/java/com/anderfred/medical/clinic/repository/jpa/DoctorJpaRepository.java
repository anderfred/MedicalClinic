package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.Doctor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorJpaRepository extends JpaRepository<Doctor, Long> {
  @Query(value = "select d from Doctor d where d.email =:email")
  Optional<Doctor> findByEmail(@Param("email") String email);
}

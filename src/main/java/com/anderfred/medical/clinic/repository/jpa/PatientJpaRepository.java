package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.user.Patient;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientJpaRepository extends JpaRepository<Patient, Long> {
  @Query(value = "select p from Patient p where p.email =:email")
  Optional<Patient> findByEmail(@Param("email") String email);

  @Query(value = "select p from Patient p order by p.lastName,p.firstName asc")
  Page<Patient> findPageNameSorted(Pageable pageable);
}

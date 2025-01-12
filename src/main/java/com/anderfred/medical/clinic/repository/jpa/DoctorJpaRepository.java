package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.user.Doctor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorJpaRepository extends JpaRepository<Doctor, Long> {
  @Query(value = "select d from Doctor d where d.email =:email")
  Optional<Doctor> findByEmail(@Param("email") String email);

  @Query(value = "select d from Doctor d order by d.lastName,d.firstName asc")
  Page<Doctor> findPageNameSorted(Pageable pageable);
}

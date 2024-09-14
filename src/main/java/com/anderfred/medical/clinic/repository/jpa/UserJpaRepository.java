package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
  @Query(value = "select p from User p where p.email =:email")
  Optional<User> findByEmail(@Param("email") String email);
}

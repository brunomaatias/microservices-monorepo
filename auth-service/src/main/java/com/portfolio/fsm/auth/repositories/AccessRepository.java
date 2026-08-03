package com.portfolio.fsm.auth.repositories;

import com.portfolio.fsm.auth.models.Access;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    List<Access> findByUserId(Long userId);
}

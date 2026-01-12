package com.data.kanyh.repository;

import com.data.kanyh.model.CompteGuilde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompteGuildeRepository extends JpaRepository<CompteGuilde, Long> {
}

package com.data.kanyh.repository;

import com.data.kanyh.model.Aventurier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AventurierRepository extends JpaRepository<Aventurier, Long> {
}

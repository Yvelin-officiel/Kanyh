package com.data.kanyh.repository;

import com.data.kanyh.model.Quete;
import com.data.kanyh.model.StatutQuete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QueteRepository extends JpaRepository<Quete, Long> {
}
